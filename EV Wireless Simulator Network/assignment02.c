#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <mpi.h>
#include <time.h>
#include <unistd.h>
#include <pthread.h>

#define MAX_ITERATIONS 10           // The maximum number of iterations to run.
#define MAX_REPORTS 4               // The maximum number of port reports to hold in the array.
#define MAX_ALERTS 20               // The maximum number of alerts the alert queue can hold.
#define NUM_PORTS 5                 // Number of charging ports per charging node.
#define FULL_USED 2                 // The number of ports available to be considered "fully" used.
#define CYCLE_TIME 10               // The time taken for one cycle to complete.
#define ELAPSED_TIME 5              // If no reports for the last 5 seconds, node is available.
#define NODE_INFO_SIZE 7            // The size of the NodeInfo structure.
#define NODE_ALERT_SIZE 5           // The size of the NodeAlert structure.
#define SLEEP_MICRO_SEC	1000000     // Microsecond to sleep.
#define TERMINATE_TAG 0             // 0 is the tag to be used to terminate the nodes.
#define NEIGHBOUR_REQUEST_TAG 1     // 1 is the tag use to request for neighbouring nodes report.
#define NEIGHBOUR_REPLY_TAG 2       // 2 is the tag use to reply to the request for neighbouring nodes report.
#define NODE_ALERT_TAG 3            // 3 is the tag for alerting the base station.
#define ALERT_REPLY_TAG 4           // 4 is the tag for the base station replying to the alert with the available nodes.

int baseStationIo(MPI_Comm, MPI_Comm);
int chargingNodeIo(MPI_Comm, MPI_Comm);
void* runChargingPort(void *pArg);
void* receiveAlerts(void *pArg);

// Structure that contains various information and attributes of each charging node. Contains its rank, coordinates, latestAvailability, number of neighbours, 
// the array of neighouring node ranks and the array of neighbouring node coordinates.
typedef struct {
    int nodeRank;
    int nodeCoords[2];
    int latestAvailability;
    int numNeighbours;
    int neighbourRanks[4];
    int neighbourCoords[4][2];
    double relativeTime;
} NodeInfo;

// Representing an Alert that will be sent to the base station. Containing the current timestamp of the alert, the relative time of the reporting node sending the alert,
// the total number of messages exchanged between the neighbouring nodes and the NodeInfo of the reporting node itself and an array of neighbouring NodeInfos.
typedef struct {
    time_t timestamp;
    double relativeTime;
    int nMessagesExchanged;
    NodeInfo reportingNode;
    NodeInfo neighbouringNodes[4];
} NodeAlert;

// Structure containing the information needed to log the report by the base station, containing the NodeAlert object, the total messages exchanged between base station
// and charging node, the number of available nearby nodes and the array of available nearby nodes.
typedef struct {
    NodeAlert nodeAlert;
    double commTime;
    int baseTotalMessages;
    int nMessagesExchanged;
    int numAvailableNearbyNodes;
    int availableNearbyNodes[10];
} BaseLog;

int nChargingNodes;                                     // The number of charging nodes.
int keepRunning = 1;                                    // Global flag to determine whether to continue running the simulation or not.
int availabilitySet = 0;                                // Global flag to signify if the availability of the node has been set or not.
int nPortsRan = 0;                                      // The number of ports that have ran.
int nAvailability = 0;                                  // The number of available ports for that cycle.
int *portReports;                                       // Initialise global shared array for Port Reports.
BaseLog *baseLogs;                                      // Initialise global queue for BaseLog structures.
int  tailReport = 0;                                    // The head, tail and size of the queue shared array.
int headAlert = 0, tailAlert = 0, sizeAlert = 0;        // The head, tail and size of the alert queue.
int iteration = 0;                                      // Global flag to determine the iteration the base station is at. Each charging cycle is one iteration.                                  
double *chargingNodeLastReportTimeElapsed;              // An array of time elapsed since last report of each charging node.
pthread_mutex_t slaveLock = PTHREAD_MUTEX_INITIALIZER;  // The mutex_t lock for the slave.
pthread_mutex_t masterLock = PTHREAD_MUTEX_INITIALIZER; // The mutex_t lock for the master.
//The Comm for the EV Charging Nodes.
MPI_Comm chargingNodeComm;

// Initialise the custom MPI_Datatype and their required variables.
MPI_Datatype MPI_NODE_DATATYPE;         // The NodeInfo Datatype to create.
MPI_Datatype MPI_ALERT_DATATYPE;        // The NodeAlert Datatype to create.

void enqueueReport(int item) {
    // Add the new item to the queue at the tail position.
    portReports[tailReport] = item;

    // Move the tail index in a circular manner to the next position.
    // If the tail is at the end of the array, it wraps around to the beginning.
    tailReport = (tailReport + 1) % MAX_REPORTS;
}

int getLatestAvailability() {
    // Return the latest entry in the portReports queue which is the availability.
    return portReports[(tailReport == 0) ? MAX_REPORTS - 1 : tailReport - 1];
}

void dequeueAlert(BaseLog *baseLogOut) {
    int i, j, k;

    // Check if the queue is not empty (size is greater than 0).
    if (sizeAlert > 0) {        
        // Copy over the NodeAlert in the array to the nodeAlert output.
        baseLogOut->nodeAlert.timestamp = baseLogs[headAlert].nodeAlert.timestamp;
        // Copy over the reporting NodeInfo.
        memcpy(&baseLogOut->nodeAlert.reportingNode, &baseLogs[headAlert].nodeAlert.reportingNode, sizeof(NodeInfo));

        // Copy over the coordinates of the reporting NodeInfo.
        for (i = 0; i < 2; i++) {
            baseLogOut->nodeAlert.reportingNode.nodeCoords[i] = baseLogs[headAlert].nodeAlert.reportingNode.nodeCoords[i];
        }
        // Copy over the neighbours of the reporting NodeInfo.
        for (i = 0; i < 4; i++) {
            baseLogOut->nodeAlert.reportingNode.neighbourRanks[i] = baseLogs[headAlert].nodeAlert.reportingNode.neighbourRanks[i];
            baseLogOut->nodeAlert.reportingNode.neighbourCoords[i][0] = baseLogs[headAlert].nodeAlert.reportingNode.neighbourCoords[i][0];
            baseLogOut->nodeAlert.reportingNode.neighbourCoords[i][1] = baseLogs[headAlert].nodeAlert.reportingNode.neighbourCoords[i][1];
        }

        // Copy over the NodeAlert neighbouringNodes array.
        for (i = 0; i < baseLogs[headAlert].nodeAlert.reportingNode.numNeighbours; i++) {
            // Copy over the neighbourNodeInfo values.
            baseLogOut->nodeAlert.neighbouringNodes[i].nodeRank = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].nodeRank;
            baseLogOut->nodeAlert.neighbouringNodes[i].numNeighbours = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].numNeighbours;
            baseLogOut->nodeAlert.neighbouringNodes[i].latestAvailability = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].latestAvailability;
            // Copy over the coords array.
            for (j = 0; j < 2; j++) {
                baseLogOut->nodeAlert.neighbouringNodes[i].nodeCoords[j] = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].nodeCoords[j];
            }
            // Copy over the neighbours array.
            for (k = 0; k < 4; k++) {
                baseLogOut->nodeAlert.neighbouringNodes[i].neighbourRanks[k] = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].neighbourRanks[k];
                baseLogOut->nodeAlert.neighbouringNodes[i].neighbourCoords[k][0] = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].neighbourCoords[k][0];
                baseLogOut->nodeAlert.neighbouringNodes[i].neighbourCoords[k][1] = baseLogs[headAlert].nodeAlert.neighbouringNodes[i].neighbourCoords[k][1];
            }      
        }

        // Copy over the availableNearbyNodes array.
        for (i = 1; i < 9; i++) {
            // Copy over each element to the availableNearbyNodes array.
            baseLogOut->availableNearbyNodes[i] = baseLogs[headAlert].availableNearbyNodes[i];

        }

        // Copy over the BaseLog object attributes.
        baseLogOut->numAvailableNearbyNodes = baseLogs[headAlert].numAvailableNearbyNodes;
        baseLogOut->baseTotalMessages = baseLogs[headAlert].baseTotalMessages;
        baseLogOut->nMessagesExchanged = baseLogs[headAlert].nMessagesExchanged;
        baseLogOut->commTime = baseLogs[headAlert].commTime;

        // Move the head index in a circular manner to the next position.
        headAlert = (headAlert + 1) % MAX_ALERTS;

        // Decrease the size of the queue.
        sizeAlert--;
    }
}

void enqueueAlert(NodeAlert *nodeAlert, int *availableNearbyNodes, int numAvailableNearbyNodes, int baseTotalMessages, int nMessagesExchanged, double commTime) {
    int i, j, k;

    // Enqueue if the queue still has space.
    if (sizeAlert != MAX_ALERTS) {
        // Copy over the item and its attributes to the array.
        baseLogs[tailAlert].nodeAlert.timestamp = nodeAlert->timestamp;
        // Copy over the reporting NodeInfo.
        memcpy(&baseLogs[tailAlert].nodeAlert.reportingNode, &nodeAlert->reportingNode, sizeof(NodeInfo));

        // Copy over the coordinates of the reporting NodeInfo.
        for (i = 0; i < 2; i++) {
            baseLogs[tailAlert].nodeAlert.reportingNode.nodeCoords[i] = nodeAlert->reportingNode.nodeCoords[i];
        }
        // Copy over the neighbours' info of the reporting NodeInfo.
        for (i = 0; i < 4; i++) {
            baseLogs[tailAlert].nodeAlert.reportingNode.neighbourRanks[i] = nodeAlert->reportingNode.neighbourRanks[i];
            baseLogs[tailAlert].nodeAlert.reportingNode.neighbourCoords[i][0] = nodeAlert->reportingNode.neighbourCoords[i][0];
            baseLogs[tailAlert].nodeAlert.reportingNode.neighbourCoords[i][1] = nodeAlert->reportingNode.neighbourCoords[i][1];
        }

        // Copy over the NodeAlert neighbouringNodes array.
        for (i = 0; i < nodeAlert->reportingNode.numNeighbours; i++) {
            // Copy over the neighbourNodeInfo values to the neighbourNodeInfos array.
            baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].nodeRank = nodeAlert->neighbouringNodes[i].nodeRank;
            baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].numNeighbours = nodeAlert->neighbouringNodes[i].numNeighbours;
            baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].latestAvailability = nodeAlert->neighbouringNodes[i].latestAvailability;
            // Copy over the coords array.
            for (j = 0; j < 2; j++) {
                baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].nodeCoords[j] = nodeAlert->neighbouringNodes[i].nodeCoords[j];
            }
            // Copy over the neighbours array.
            for (k = 0; k < 4; k++) {
                baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].neighbourRanks[k] = nodeAlert->neighbouringNodes[i].neighbourRanks[k];
                baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].neighbourCoords[k][0] = nodeAlert->neighbouringNodes[i].neighbourCoords[k][0];
                baseLogs[tailAlert].nodeAlert.neighbouringNodes[i].neighbourCoords[k][1] = nodeAlert->neighbouringNodes[i].neighbourCoords[k][1];
            }      
        }

        // Copy over the availableNearbyNodes array.
        for (i = 1; i < 9; i++) {
            // Copy over each element to the availableNearbyNodes array.
            baseLogs[tailAlert].availableNearbyNodes[i] = availableNearbyNodes[i];
        }

        // Set the number of available nearby nodes.
        baseLogs[tailAlert].numAvailableNearbyNodes = numAvailableNearbyNodes;
        // Set the number of messages exchanged between base station and charging node.
        baseLogs[tailAlert].baseTotalMessages = baseTotalMessages;
        // Set the number of messages exchanged between reporting node and its neighbours.
        baseLogs[tailAlert].nMessagesExchanged = nMessagesExchanged;
        // Set the communication time of the charging nodes and base stations.
        baseLogs[tailAlert].commTime = commTime;

        // Move the tail index in a circular manner to the next position.
        // If the tail is at the end of the array, it wraps around to the beginning.
        tailAlert = (tailAlert + 1) % MAX_ALERTS;

        // Increase the size of the queue.
        sizeAlert++;
    }
}

void createCustomDatatype() {
    // Set the respective NodeInfo datatype type and length of block
	MPI_Datatype nodeTypes[NODE_INFO_SIZE] = {MPI_INT, MPI_INT, MPI_INT, MPI_INT, MPI_INT, MPI_INT, MPI_DOUBLE};
	int nodeBlockLens[NODE_INFO_SIZE] = {1, 2, 1, 1, 4, 8, 1};
    MPI_Aint nodeDisp[NODE_INFO_SIZE];      // The amount of bytes each NodeInfo variable will require.

    // Set the offsets/displacements of each NodeInfo variable.
    nodeDisp[0] = offsetof(NodeInfo, nodeRank);
    nodeDisp[1] = offsetof(NodeInfo, nodeCoords);
    nodeDisp[2] = offsetof(NodeInfo, latestAvailability);
    nodeDisp[3] = offsetof(NodeInfo, numNeighbours);
    nodeDisp[4] = offsetof(NodeInfo, neighbourRanks);
    nodeDisp[5] = offsetof(NodeInfo, neighbourCoords);
    nodeDisp[6] = offsetof(NodeInfo, relativeTime);

    // Create the MPI data structure for the NodeInfo.
    MPI_Type_create_struct(NODE_INFO_SIZE, nodeBlockLens, nodeDisp, nodeTypes, &MPI_NODE_DATATYPE);
    MPI_Type_commit(&MPI_NODE_DATATYPE);

    // Set the respective NodeAlert datatype type and length of block
	MPI_Datatype alertTypes[NODE_ALERT_SIZE] = {MPI_LONG, MPI_DOUBLE, MPI_INT, MPI_NODE_DATATYPE, MPI_NODE_DATATYPE};
    int alertBlockLens[NODE_ALERT_SIZE] = {1, 1, 1, 1, 4};
    MPI_Aint alertDisp[NODE_ALERT_SIZE];    // The amount of bytes each NodeAlert variable will require.

    // Set the offsets/displacements of each NodeAlert variable.
    alertDisp[0] = offsetof(NodeAlert, timestamp);
    alertDisp[1] = offsetof(NodeAlert, relativeTime);
    alertDisp[2] = offsetof(NodeAlert, nMessagesExchanged);
    alertDisp[3] = offsetof(NodeAlert, reportingNode);
    alertDisp[4] = offsetof(NodeAlert, neighbouringNodes);

    // Create the MPI data structure for the NodeAlert.
    MPI_Type_create_struct(NODE_ALERT_SIZE, alertBlockLens, alertDisp, alertTypes, &MPI_ALERT_DATATYPE);
    MPI_Type_commit(&MPI_ALERT_DATATYPE);
}

void *runChargingPort(void *pArg) {
    // Get the seed value from the pArg arguement.
    unsigned int seed = *(unsigned int*)pArg;
    unsigned int newSeed, localSeed = seed;
    // Initalise variables to be used in the while loop.
    int availability;
    time_t currentTime;

    // While no termination signal has been received yet, continue simulating the charging port.
    while (1) {
        // Exit the loop when termination signal is received.
        pthread_mutex_lock(&slaveLock);
		if(keepRunning == 0){
			pthread_mutex_unlock(&slaveLock);
			break;      // Terminate loop.
		}
		pthread_mutex_unlock(&slaveLock);

        availability = 0;                   // Reset the availability of the port to 0.
        currentTime = time(NULL);           // Get the current time.

        newSeed = currentTime * localSeed;  // Multiply the current seed with the current time.

        // Generate a random number representing availability. Get the remainder which is always 0 or 1.
        availability = rand_r(&newSeed) % 2;

        // Lock the mutex to prevent race conditions.
        pthread_mutex_lock(&slaveLock);
        nPortsRan++;        // Increment the number of ports that have ran for this cycle.

        // If the charging port is available, increment it the number of available ports.
        if (availability == 1) {
          nAvailability++;  // Increment the number of available charging ports for this cycle.
        }

        // If all threads ran (this is the last thread), calculate the total availability and enqueue the report.
        if (nPortsRan % NUM_PORTS == 0) {
            // Push the nAvailability into the portsArray.
            enqueueReport(nAvailability);

            availabilitySet = 1;    // Availability has been set

            // Rest the availability to 0.
            nAvailability = 0;
        }

        // Unlock the mutex once done.
        pthread_mutex_unlock(&slaveLock);
        
        // Simulate a charging cycle.
        sleep(CYCLE_TIME);
    }
    // printf("Charging Node Thread Finished\n");

    // Exit the function once processing thread is done.
    return 0;
}

int chargingNodeIo(MPI_Comm worldComm, MPI_Comm newComm) {
    // The start time of the process in seconds. Also declare the communication time between nodes, and variables
    double startTime = MPI_Wtime(), commTime, totalCommTime, requestingNodeRelativeTime, sentNodeRelativeTime;
    // Initalise the default values currently with number of dimensions being 2 for a 2D Cartesian Grid. Also initialise other variables used.
    int ndims = 2, commSize, currentRank, reorder, ierr, i, portsFilled = 0, countRecv = 0, vacantNeighbours = 0, receivedReports = 0, nMessagesExchanged = 0;
    // Initialise the seed to be used to randomly generate availability.
    unsigned int seed;
    // Also initialise flag and signals for termination and report requests.
    int terminateFlag = 0, terminationSignal = 0, neighbourRequestFlag = 0, neighbourReplyFlag = 0, alertReplyFlag = 0, neighbourSendSignal = 0;
    // Initailise up, down, left and right neighbours
    int up, down, left, right;
    
    // MPI_Requests required in the function.
    MPI_Request terminationRequest, neighbourRecvRequest, neighbourSendRequest, alertSendRequest;
    // The status of the probe.
    MPI_Status requestProbeStatus, replyProbeStatus, alertProbeStatus;

    // Initalise the dimensions.
	int dims[ndims];
    // Initalise the period of length ndims.
	int periods[ndims];

    // Array of thread ids. Will by default create NUM_PORTS charging ports for each charging node.
    pthread_t threadID[NUM_PORTS];

    // Initialise the NodeInfo structure.
    NodeInfo nodeInfo;

    // Get the size of the slave communicator and current rank.
  	MPI_Comm_size(newComm, &commSize);
	MPI_Comm_rank(newComm, &currentRank);

    // Set the dimensions by default.
	dims[0] = 0;
    dims[1] = 0;

    // Create the division of processors in the Cartesian Grid.
	MPI_Dims_create(commSize, ndims, dims);

    if (currentRank == 0) {
        printf("Comm Size: %d: Grid Dimension = [%d x %d] \n", commSize, dims[0], dims[1]);
    }

    // Set periods to false as there's no wrap around for any layer.
	periods[0] = 0;
	periods[1] = 0;
    // Set reoreder to 0 to prevent reordering of ranks and set ierr to catch any error during creation.
	reorder = 0;
	ierr = 0;

    // Call the MPI_Cart_create and create the Comm with the Cartesian Grid. 
	ierr = MPI_Cart_create(newComm, ndims, dims, periods, reorder, &chargingNodeComm);

    // Print error if encountered.
	if(ierr != 0) {
        printf("ERROR[%d] creating CART\n", ierr);
    }

    // Get the neighbouring nodes of the charging node.
    MPI_Cart_shift(chargingNodeComm, 0, 1, &up, &down);
	MPI_Cart_shift(chargingNodeComm, 1, 1, &left, &right);
    
    // Set the NodeInfo variables.
    nodeInfo.nodeRank = currentRank;    // Set the Node rank.
    nodeInfo.numNeighbours = 0;         // Initially set the number of neighbours.
    // Get the coordinates of the node.
    MPI_Cart_coords(chargingNodeComm, currentRank, ndims, nodeInfo.nodeCoords);
    // Set the neighbours of the node to be up, down, left and right respectively for each index of the array.
    nodeInfo.neighbourRanks[0] = (up != MPI_PROC_NULL) ? up : -1;
    nodeInfo.neighbourRanks[1] = (down != MPI_PROC_NULL) ? down : -1;
    nodeInfo.neighbourRanks[2] = (left != MPI_PROC_NULL) ? left : -1;
    nodeInfo.neighbourRanks[3] = (right != MPI_PROC_NULL) ? right : -1;
    
    // Count the number of neighbours the Node has. Also fill up the neighbourCoords array.
    for (i = 0; i < 4; i++) {
        // If the neighbour node exists, increment the num neighours,
        if (nodeInfo.neighbourRanks[i] >= 0) {
            // Get the coordinates of the neighbour and store it within the neighbourCoords array.
            MPI_Cart_coords(chargingNodeComm, nodeInfo.neighbourRanks[i], ndims, nodeInfo.neighbourCoords[i]);

            nodeInfo.numNeighbours++;
        }
    }

    // Allocate sufficient memory for the shared port report array.
    portReports = (int *) calloc(MAX_REPORTS, sizeof(int));

    // Initialise the pthread mutex.
    pthread_mutex_init(&slaveLock, NULL);

    // Create the ports in each node.
    for (i = 0; i < NUM_PORTS; i++) {
        // Calculate the seed to randomly generate the availability of the port.
        seed = i * currentRank;
        
        // Create the POSIX threads and run the runChargingPort method for each thread.
		pthread_create(&threadID[i], NULL, runChargingPort, &seed);
	}

    // Non blocking receiver, receiving termination signal from base station master.
    MPI_Irecv(&terminationSignal, 1, MPI_INT, commSize, TERMINATE_TAG, worldComm, &terminationRequest);

    // While no termination signal has been received yet, continue running.
    while (1) {
        // Test whether the termination signal has been received from the base station.
        MPI_Test(&terminationRequest, &terminateFlag, MPI_STATUS_IGNORE);

        // If termination signal has been received and its True, terminate the program by making keepRunning False.
        if (terminateFlag && terminationSignal) {            
            // Lock thread and set keepRunning to false to terminate program.
            pthread_mutex_lock(&slaveLock);
            keepRunning = 0;
            pthread_mutex_unlock(&slaveLock);

            // printf("Node %d Received Termination Signal\n", nodeInfo.nodeRank);

            break;      // Terminate loop.
        }
        // Otherwise continue the simulation of nodes.
        else {
            // Create a NodeAlert object to be sent to the base station and set its attributes.
            NodeAlert nodeAlert;

            // Lock the thread and get the latest number of available charging ports.
            // Will only run once at the start of each charging cycle.
            pthread_mutex_lock(&slaveLock);
            if (availabilitySet) {
                availabilitySet = 0;        // Reset the flag.
                portsFilled = 0;            // Reset portsFilled flag to 0 to start a new cycle.
                vacantNeighbours = 0;       // Set the vacantNeighbours boolean to False initially.
                neighbourSendSignal = 0;    // Reset neighbourSendSignal to 0 at the start of a new cycle.
                receivedReports = 0;        // Reset the receivedReports flag to 0 at the start of a new cycle.
                countRecv = 0;              // Reset the countRecv to 0.
                nMessagesExchanged = 0;     // Reset the nMessagesExchanged to 0.
                totalCommTime = 0;          // Print out the total communication time between node and all its neighbours.

                // Get the latest number of available charging ports.
                nodeInfo.latestAvailability = getLatestAvailability();

                printf("Node Rank: %d, Availability: %d, Iteration: %d\n", nodeInfo.nodeRank, nodeInfo.latestAvailability, nPortsRan / NUM_PORTS);  
            }
            pthread_mutex_unlock(&slaveLock);

            // Probe to detect reply from base station.
            MPI_Iprobe(commSize, ALERT_REPLY_TAG, worldComm, &alertReplyFlag, &alertProbeStatus);

            // If incoming reply from base station, receive it.
            if (alertReplyFlag) {
                alertReplyFlag = 0;     // Reset the flag.
                
                // Allocate enough memory to store the available nearby nodes.
                int *availableNearbyNodes = (int *)malloc(10 * sizeof(int));
                // Blocking receive to get nearby available nodes fron the base station.
                MPI_Recv(availableNearbyNodes, 10, MPI_INT, alertProbeStatus.MPI_SOURCE, alertProbeStatus.MPI_TAG, worldComm, MPI_STATUS_IGNORE);
                
                // If there exist available nearby nodes.
                if (availableNearbyNodes[0] == 1) {
                    int count = 0;      // Reset the count to 0.
                    // Print out the available nearby nodes
                    printf("Node %d has Nearby Available Nodes ", nodeInfo.nodeRank);
                    // Print out each available nearby nodes.
                    for (i = 1; i < 9; i++) {
                        // If there exist an available nearby nodes, write it with a comma.
                        if (count < availableNearbyNodes[9] - 1 && availableNearbyNodes[i] >= 0) {
                            printf("%d, ", availableNearbyNodes[i]);
                            count++;        // Increment the count representing the number of availableNearbyNodes.
                        }
                        // Else if on the last available nearby node, write it out without a comma.
                        else if (count == availableNearbyNodes[9] - 1 && availableNearbyNodes[i] >= 0) {
                            printf("%d\n", availableNearbyNodes[i]);
                            count++;        // Increment the count representing the number of availableNearbyNodes.
                        }
                    }
                }
                // Else if there isn't any available nearby nodes.
                else {
                    printf("Node %d has No Available Nearby Node\n", nodeInfo.nodeRank);
                }

                // Free memory once done.
                free(availableNearbyNodes);
            }

            // Do-while loop to receive request for neighbour nodes reports.
            do {
                // Non-blocking probe. Detect incoming requests from neighbouring node.
                MPI_Iprobe(MPI_ANY_SOURCE, NEIGHBOUR_REQUEST_TAG, chargingNodeComm, &neighbourRequestFlag, &requestProbeStatus);

                // If there exist an incoming request from neighbouring node, receive it.
                if (neighbourRequestFlag) {
                    // Blocking receive to get request from neighbours for the report. Will receive the relative time of the sending node.
                    MPI_Recv(&requestingNodeRelativeTime, 1, MPI_DOUBLE, requestProbeStatus.MPI_SOURCE, requestProbeStatus.MPI_TAG, chargingNodeComm, MPI_STATUS_IGNORE);

                    // Calculate the communication time for the node sending a request signal to its neighbours.
                    commTime = ((MPI_Wtime() - startTime) - requestingNodeRelativeTime) > 0 ? ((MPI_Wtime() - startTime) - requestingNodeRelativeTime) : ((MPI_Wtime() - startTime) - requestingNodeRelativeTime) * -1 ;
                    totalCommTime += commTime;      // Add the communication time to the total.

                    // printf("Communication Time (seconds) for Request from Node %d to Neighbouring Node %d: %.4f seconds\n", requestProbeStatus.MPI_SOURCE, nodeInfo.nodeRank, commTime);

                    // printf("Node %d rceived Signal from Node %d\n", nodeInfo.nodeRank, requestProbeStatus.MPI_SOURCE);

                    // Calculate the relative time of the neigbouring node when it sends the reply.
                    nodeInfo.relativeTime = MPI_Wtime() - startTime;

                    // Send the NodeInfo structure to the neighbour node that is requesting it.
                    MPI_Isend(&nodeInfo, 1, MPI_NODE_DATATYPE, requestProbeStatus.MPI_SOURCE, NEIGHBOUR_REPLY_TAG, chargingNodeComm, &neighbourRecvRequest);
                    
                    nMessagesExchanged += 2;    // Increment the total number of messages exchanged between neighbours by 2.
                }
            }
            // Continue looping until no more messages can be received.
            while(neighbourRequestFlag);

            // Do-while loop to receive reply from request for neighbour nodes reports.
            do {
                // Non-blocking probe. Detect incoming replies from neighbouring node.
                MPI_Iprobe(MPI_ANY_SOURCE, NEIGHBOUR_REPLY_TAG, chargingNodeComm, &neighbourReplyFlag, &replyProbeStatus);

                // If there exist an incoming reply from neighbouring node, receive it.
                if (neighbourReplyFlag) {
                    NodeInfo neighbourNodeInfo;
                    // Blocking receive to get reply from neighbours for the report.
                    MPI_Recv(&neighbourNodeInfo, 1, MPI_NODE_DATATYPE, replyProbeStatus.MPI_SOURCE, replyProbeStatus.MPI_TAG, chargingNodeComm, MPI_STATUS_IGNORE);

                    // Calculate the communication time for the neighbours replying to the reporting node request.
                    commTime = ((MPI_Wtime() - startTime) - neighbourNodeInfo.relativeTime) > 0 ? ((MPI_Wtime() - startTime) - neighbourNodeInfo.relativeTime) : ((MPI_Wtime() - startTime) - neighbourNodeInfo.relativeTime) * -1;
                    totalCommTime += commTime;      // Add the communication time to the total.

                    // printf("Communication Time (seconds) for Reply to Node %d from Neighbouring Node %d: %.4f seconds\n", nodeInfo.nodeRank, neighbourNodeInfo.nodeRank, commTime);

                    nMessagesExchanged++;       // Increment the total number of messages exchanged between neighbours by 1.

                    // printf("Node %d Received Reports From Neighbouring Nodes.\n", nodeInfo.nodeRank);
                    
                    // Copy over the neighbourNodeInfo values to the neighbourNodeInfos array.
                    memcpy(&nodeAlert.neighbouringNodes[countRecv], &neighbourNodeInfo, sizeof(NodeInfo));

                    // Copy over the coords array.
                    for (int j = 0; j < 2; j++) {
                        nodeAlert.neighbouringNodes[countRecv].nodeCoords[j] = neighbourNodeInfo.nodeCoords[j];
                    }
                    // Copy over the neighbours rank and coord array.
                    for (int k = 0; k < 4; k++) {
                        nodeAlert.neighbouringNodes[countRecv].neighbourRanks[k] = neighbourNodeInfo.neighbourRanks[k];
                        nodeAlert.neighbouringNodes[countRecv].neighbourCoords[k][0] = neighbourNodeInfo.neighbourCoords[k][0];
                        nodeAlert.neighbouringNodes[countRecv].neighbourCoords[k][1] = neighbourNodeInfo.neighbourCoords[k][1];
                    }        

                    receivedReports = 1;    // Succeessfully received report.
                    
                    // Print out the nearest available neighbour nodes.
                    if (nodeAlert.neighbouringNodes[countRecv].latestAvailability > FULL_USED) {
                        vacantNeighbours = 1;       // There are vacancies in neighbouring charging nodes.
                        neighbourSendSignal = 0;    // Reset the send signal to 0 as nearest available node found.
                        receivedReports = 0;        // Reset the receivedReports to 0 once found available nodes.
                        // printf("Reporting Node: %d, Nearest Available Node: %d with Availability: %d\n", nodeInfo.nodeRank, nodeAlert.neighbouringNodes[countRecv].nodeRank, nodeAlert.neighbouringNodes[countRecv].latestAvailability);
                    }
                    countRecv++;    // Increment the counter by one.
                }
            }
            // Continue looping until no more messages can be received.
            while(neighbourReplyFlag);

            // If requested for report from neighbouring nodes and no vacant neighbouring node, alert the base station.
            if (nodeInfo.latestAvailability <= FULL_USED && neighbourSendSignal && receivedReports && countRecv == nodeInfo.numNeighbours && vacantNeighbours != 1) {
                // Reset the signals to 0.
                neighbourSendSignal = 0;
                receivedReports = 0;

                // Copy the NodeInfo for the reporting node.
                memcpy(&nodeAlert.reportingNode, &nodeInfo, sizeof(NodeInfo));
                nodeAlert.timestamp = time(NULL);
                
                // Copy over the coords array.
                for (int j = 0; j < 2; j++) {
                    nodeAlert.reportingNode.nodeCoords[j] = nodeInfo.nodeCoords[j];
                }
                // Copy over the neighbours rank and coords array.
                for (int k = 0; k < 4; k++) {
                    nodeAlert.reportingNode.neighbourRanks[k] = nodeInfo.neighbourRanks[k];
                    nodeAlert.reportingNode.neighbourCoords[k][0] = nodeInfo.neighbourCoords[k][0];
                    nodeAlert.reportingNode.neighbourCoords[k][1] = nodeInfo.neighbourCoords[k][1];
                }

                // Set the total number of messages exchanged between reporting node and its neighbours.
                nodeAlert.nMessagesExchanged = nMessagesExchanged;

                // Calculate the relative time at the sending of the alert.
                nodeAlert.relativeTime = MPI_Wtime() - startTime;

                printf("Node %d Total Communication Time (seconds) between Itself and its Neighbours: %.4f\n", nodeInfo.nodeRank, totalCommTime);

                // Send the NodeAlert object to the base station.
                MPI_Isend(&nodeAlert, 1, MPI_ALERT_DATATYPE, commSize, NODE_ALERT_TAG, worldComm, &alertSendRequest);

                // printf("Sent Alert from Node %d. Availability: %d.\n", nodeAlert.reportingNode.nodeRank, nodeAlert.reportingNode.latestAvailability);
            }
            
            // Check in the current cycle whether the port has sufficient available ports. 
            // If less than or equal to FULL_USED, send request for reports from neighbours,
            if (portsFilled == 0 && nodeInfo.latestAvailability <= FULL_USED) {
                portsFilled = 1;    // Set true if ports are almost full.
                // Loop through all neighbouring nodes and send request for report if neighbours exist.
                for (i = 0; i < 4; i++) {
                    if (nodeInfo.neighbourRanks[i] >= 0) {
                        // Set the neighbourSendSignal to one to signify request has been sent to neighbours.
                        neighbourSendSignal = 1;
                        // Calculate the relative time of the reporting/sending node.
                        sentNodeRelativeTime = MPI_Wtime() - startTime;

                        // Send the node's relative time to the neighbours along with the NEIGHBOUR_REQUEST_TAG to ask for the neighbours' availability.
                        MPI_Isend(&sentNodeRelativeTime, 1, MPI_DOUBLE, nodeInfo.neighbourRanks[i], NEIGHBOUR_REQUEST_TAG, chargingNodeComm, &neighbourSendRequest);
                        nMessagesExchanged++;       // Increment the total number of messages exchanged between `neighbours by 1.
                    }
                }
            }
        }
        // Pause for one second in between iterations.
        usleep(SLEEP_MICRO_SEC);
    }

    // Wait for all threads to finished and rejoined them back.
    // Also clean memory allocated for the PortInfo structures.
    for(i = 0; i < NUM_PORTS; i++) {
        pthread_join(threadID[i], NULL);
	}

    // Destroy lock and free allocated memory.
    free(portReports);
    pthread_mutex_destroy(&slaveLock);
    MPI_Comm_free(&chargingNodeComm);

    // printf("Charging Node Finished\n");
    
	return 0;
}

int processAlert(int *availableNearbyNodes, NodeAlert *receivedNodeAlert) {
    // Initialise all variables, flags and signals.
    int i, j, k, index, nearbyNodeRank, isUnique = 0;

    index = 1;      // Set index of availableNearbyNodes to 1.
    // Loop through the neighbouring nodes of the reporting node.
    for (i = 0; i < receivedNodeAlert->reportingNode.numNeighbours; i++) {
        // Loop through the adjacent nodes of the neighbouring nodes.
        for (j = 0; j < 4; j++) {
            nearbyNodeRank = receivedNodeAlert->neighbouringNodes[i].neighbourRanks[j];      // Get the nearby node rank.
            isUnique = 1;           // Reset flag to 0.
            
            // Don't add the reporting node itself.
            if ((nearbyNodeRank >= 0) && (nearbyNodeRank != receivedNodeAlert->reportingNode.nodeRank)) {
                // Ensure the rank has not been added to the availableNearbyNodes array before.
                for (k = 1; k < 9; k++) {
                    // Check if the rank is unique.
                    if (availableNearbyNodes[k] == nearbyNodeRank) {
                        isUnique = 0;
                    }
                }
                // If node has not been added into the array yet, add it.
                // If the time elapsed since the last report surpass the predefined ELAPSED_TIME, it's available hence add it to the availableNearbyNodes list.
                if (isUnique && (chargingNodeLastReportTimeElapsed[nearbyNodeRank] >= ELAPSED_TIME)) {
                    // Set the flag to true to signify that the reporting node has available nearby nodes.
                    availableNearbyNodes[0] = 1;
                    availableNearbyNodes[index] = nearbyNodeRank;   // Store the vacant node ranks into the array.
                    index++;            // Increment the index to store the neighbouring nodes.
                }
            }
        }
    }

    return index - 1;
}

void *receiveAlerts(void *pArg) {
    double startTime = *(double *) pArg;            // Get the start time of the base station process.
    int numAvailableNearbyNodes;                    // The number of available nearby nodes.
    double elapsedTime, commTime;                   // Time elapsed since last report and communication time.
    struct timespec clockTime;                      // Time Spec structure.
	clock_gettime(CLOCK_MONOTONIC, &clockTime);     // Get the current clock time.

    // Initialise all flags and signals.
    int i, nodeAlertFlag = 0;
    // The status of the probe.
    MPI_Status alertProbeStatus;
    // Request to reply to the charging node alerts.
    MPI_Request alertSendRequest;
    
    // Allocate memory for the chargingNodeLastReportTime structure. Saves current clock time of each charging node.
    struct timespec *chargingNodeLastReportTime = (struct timespec*)malloc(nChargingNodes * sizeof(struct timespec));
    // Allocate memory for the chargingNodeLastReportTimeElapsed array. Save time elapsed since last report.
    chargingNodeLastReportTimeElapsed = (double*)calloc(nChargingNodes, sizeof(double));

    // Loop through each slave and update their time to the current clock time.
    for(i = 0; i < nChargingNodes; i++) {
        chargingNodeLastReportTime[i].tv_sec = clockTime.tv_sec;
        chargingNodeLastReportTime[i].tv_nsec =  clockTime.tv_nsec;
    }

    // Continue running until base station terminates the nodes.
    while (1) {
        // Exit the loop when termination signal is received.
        pthread_mutex_lock(&masterLock);
		if(iteration > MAX_ITERATIONS){
			pthread_mutex_unlock(&masterLock);
			break;      // Terminate Loop
		}
		pthread_mutex_unlock(&masterLock);

        do {
            // Non-blocking probe. Detect incoming alerts from reporting charging nodes.
            MPI_Iprobe(MPI_ANY_SOURCE, NODE_ALERT_TAG, MPI_COMM_WORLD, &nodeAlertFlag, &alertProbeStatus);
            
            // If there exist an incoming alert from charging nodes, receive it.
            if (nodeAlertFlag) {       
                int baseTotalMessages = 0;      // Set baseTotalMessages to 0 initially.

                // Create the NodeAlert object to receive.
                NodeAlert receivedNodeAlert;

                // Blocking receive to get alert fron reporting charging nodes.
                MPI_Recv(&receivedNodeAlert, 1, MPI_ALERT_DATATYPE, alertProbeStatus.MPI_SOURCE, alertProbeStatus.MPI_TAG, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

                // Calculate the communication time for the alert between the charging node and base station.
                commTime = ((MPI_Wtime() - startTime) - receivedNodeAlert.relativeTime) > 0 ? ((MPI_Wtime() - startTime) - receivedNodeAlert.relativeTime) : ((MPI_Wtime() - startTime) - receivedNodeAlert.relativeTime) * -1 ;

                // printf("Received Alert from Node %d. Availability: %d.\n", receivedNodeAlert.reportingNode.nodeRank, receivedNodeAlert.reportingNode.latestAvailability);

                baseTotalMessages++;            // Increment the baseTotalMessages by one when receiving message from charging node.

                // Get the current clocktime.
                clock_gettime(CLOCK_MONOTONIC, &clockTime);
                // Set the seconds of the corresponding reporting node.
                chargingNodeLastReportTime[alertProbeStatus.MPI_SOURCE].tv_sec =  clockTime.tv_sec;
                chargingNodeLastReportTime[alertProbeStatus.MPI_SOURCE].tv_nsec =  clockTime.tv_nsec;

                // Calculate the time elapsed for each charging node.
                for(i = 0; i < nChargingNodes; i++) {
                    // Calculate the elapsed time since the last report.
                    elapsedTime = (clockTime.tv_sec - chargingNodeLastReportTime[i].tv_sec) * 1e9; 
                    elapsedTime = (elapsedTime + (clockTime.tv_nsec - chargingNodeLastReportTime[i].tv_nsec)) * 1e-9; 
                    // Update the array with the elapsed time.
                    chargingNodeLastReportTimeElapsed[i] = elapsedTime;
                }

                // Allocate sufficient memory for the availableNearbyNodes dynamic array. Will hold maximum 8 nearby nodes with the first index being a boolean to indicate.
                // if there exist an available nearby nodes. Will only hold rank of available nearby nodes.
                int *availableNearbyNodes =  (int *)malloc(10 * sizeof(int));

                // Set all values to -1 for availableNearbyNodes array.
                memset(availableNearbyNodes, -1, 10 * sizeof(int));

                // Set first index to 0 (false) to indicate no available nearby nodes. Will be 1 (true) if such exists.
                availableNearbyNodes[0] = 0;

                // Process the alert and check for nearby available nodes.
                numAvailableNearbyNodes = processAlert(availableNearbyNodes, &receivedNodeAlert);
                
                // Set last index to the number of available nearby nodes..
                availableNearbyNodes[9] = numAvailableNearbyNodes;
                
                // Send the availableNearbyNodes array once alert has been processed.
                MPI_Isend(availableNearbyNodes, 10, MPI_INT, receivedNodeAlert.reportingNode.nodeRank, ALERT_REPLY_TAG, MPI_COMM_WORLD, &alertSendRequest);
                
                baseTotalMessages++;            // Increment the baseTotalMessages by one when sending messages to charging node.
                
                pthread_mutex_lock(&masterLock);
                enqueueAlert(&receivedNodeAlert, availableNearbyNodes, numAvailableNearbyNodes, baseTotalMessages, receivedNodeAlert.nMessagesExchanged, commTime);
                pthread_mutex_unlock(&masterLock);

                // printf("Sent Reply to Node %d\n", receivedNodeAlert.reportingNode.nodeRank);

                // Free allocated memory.
                free(availableNearbyNodes);
            }

            clock_gettime(CLOCK_MONOTONIC, &clockTime);     // Get the current clock time.
        }
        // Continue looping until no more messages can be received.
        while(nodeAlertFlag);

        // Calculate the time elapsed for each charging node.
        for(i = 0; i < nChargingNodes; i++) {
            // Calculate the elapsed time since the last report.
            elapsedTime = (clockTime.tv_sec - chargingNodeLastReportTime[i].tv_sec) * 1e9; 
            elapsedTime = (elapsedTime + (clockTime.tv_nsec - chargingNodeLastReportTime[i].tv_nsec)) * 1e-9; 
            // Update the array with the elapsed time.
            chargingNodeLastReportTimeElapsed[i] = elapsedTime;
        }

        // Sleep for one second.
        usleep(SLEEP_MICRO_SEC);
    }

    // Clean memory.
    free(chargingNodeLastReportTime);
    free(chargingNodeLastReportTimeElapsed);

    // printf("Base Station Thread Finished\n");
    
    return 0;
}

int baseStationIo(MPI_Comm worldComm, MPI_Comm newComm) {
    // The start time of the process in seconds.
    double startTime = MPI_Wtime();
    // Initialise all variables, flags and signals.
    int i, j, count, nearbyNodeRank, worldSize, terminationSignal = 1;

    // Get the ThreadID for the master.
	pthread_t threadID;

    FILE *file = fopen("log.txt", "w");     // Open the file to write logs.

    // Get the size of the worldComm.
	MPI_Comm_size(worldComm, &worldSize);

    // The number of slaves.
    nChargingNodes = worldSize - 1;

    // Allocate sufficient memory for the queue of alerts.
    baseLogs = (BaseLog *) calloc(MAX_ALERTS, sizeof(BaseLog));

    // Initialise the mutex lock and create a thread to send and receive messages.
    pthread_mutex_init(&masterLock, NULL);
    pthread_create(&threadID, NULL, receiveAlerts, &startTime);     // Create the thread and pass in the number of slaves to it.

    // Simulate the base station MAX_ITERATIONS times.
    while (1) {
        // Exit the loop when MAX_ITERATIONS have ran.
        pthread_mutex_lock(&masterLock);
		if(iteration > MAX_ITERATIONS){
			pthread_mutex_unlock(&masterLock);
			break;      // Terminate Loop
		}
        
        // Increment the iteration at the start of each iteration.
        iteration++;

		pthread_mutex_unlock(&masterLock);

        // printf("Iteration: %d\n", iteration);

        // Log the alert.
        while (1) {
            // While there are still unlogged alerts in the queue, continue.
            // Else break while loop and exit.
            pthread_mutex_lock(&masterLock);
            if (sizeAlert <= 0) {
                pthread_mutex_unlock(&masterLock);
                break;
            }

            count = 0;      // Reset the count to zero.

            // Dequeue the NodeAlert from the baseLogs queue.
            BaseLog baseLog;
            dequeueAlert(&baseLog);

            pthread_mutex_unlock(&masterLock);

            // Get the current time for logging.
            time_t currentTime = time(NULL);

            struct tm * currentTimestamp;
            char currentTimeBuffer[80];

            currentTimestamp = localtime(&currentTime);

            strftime(currentTimeBuffer, sizeof(currentTimeBuffer), "%a %Y-%m-%d %H:%M:%S", currentTimestamp);
            
            // Get the time the alert was sent.
            struct tm * alertTimestamp;
            char alertTimeBuffer[80];

            alertTimestamp = localtime(&baseLog.nodeAlert.timestamp);

            strftime(alertTimeBuffer, sizeof(alertTimeBuffer), "%a %Y-%m-%d %H:%M:%S", alertTimestamp);

            // Begin the log for the alert.
            fprintf(file, "------------------------------------------------------------------------------------------------------------\n");
            fprintf(file, "Iteration : \t\t\t\t\t\t%d\n", iteration - 1);
            fprintf(file, "Logged time : \t\t\t\t\t\t%s\n", currentTimeBuffer);
            fprintf(file, "Alert reported time : \t\t\t\t%s\n", alertTimeBuffer);
            fprintf(file, "Number of adjacent node(s): \t\t%d\n", baseLog.nodeAlert.reportingNode.numNeighbours);
            fprintf(file, "Availability to be considered full: %d\n\n", FULL_USED);

            // Write reporting node.
            fprintf(file, "Reporting Node \t\tCoord\t\t\tPort Value\t\tAvailable Port\n");
            fprintf(file, "%d\t\t\t\t\t(%d,%d)\t\t\t%d\t\t\t\t%d\n\n", baseLog.nodeAlert.reportingNode.nodeRank, baseLog.nodeAlert.reportingNode.nodeCoords[0], baseLog.nodeAlert.reportingNode.nodeCoords[1], NUM_PORTS, baseLog.nodeAlert.reportingNode.latestAvailability);

            // Write adjacent nodes.
            fprintf(file, "Adjacent Nodes\t\tCoord\t\t\tPort Value\t\tAvailable Port\n");
            // Loop through each adjacent neighbouring node and write their respective attributes and information.
            for (i = 0; i < baseLog.nodeAlert.reportingNode.numNeighbours; i++) {
                fprintf(file, "%d\t\t\t\t\t(%d,%d)\t\t\t%d\t\t\t\t%d\n", baseLog.nodeAlert.neighbouringNodes[i].nodeRank, baseLog.nodeAlert.neighbouringNodes[i].nodeCoords[0], baseLog.nodeAlert.neighbouringNodes[i].nodeCoords[1], NUM_PORTS, baseLog.nodeAlert.neighbouringNodes[i].latestAvailability); 
            }

            // Write the nearby nodes.
            fprintf(file, "\nNearby Nodes\t\tCoord\n");

            // Array of booleans to signify if the node rank has been written already.
            int *nearbyNodeAdded = (int *) calloc(worldSize - 1, sizeof(int));

            for (i = 0; i < baseLog.nodeAlert.reportingNode.numNeighbours; i++) {
                for (j = 0; j < 4; j++) {
                    nearbyNodeRank = baseLog.nodeAlert.neighbouringNodes[i].neighbourRanks[j];

                    // If the nearby node rank has not been added yet and it's a valid rank, write it to the log file.
                    if (nearbyNodeRank >= 0 && nearbyNodeRank != baseLog.nodeAlert.reportingNode.nodeRank && nearbyNodeAdded[nearbyNodeRank] == 0) {
                        nearbyNodeAdded[nearbyNodeRank] = 1;                        // Make the corresponding node rank index to true if it has been added.
                        // Write the nearby nodes' rank and coords to the log.
                        fprintf(file, "%d\t\t\t\t\t(%d,%d)\n", nearbyNodeRank, baseLog.nodeAlert.neighbouringNodes[i].neighbourCoords[j][0], baseLog.nodeAlert.neighbouringNodes[i].neighbourCoords[j][1]);
                    }
                }
            }

            // Print out the nearby available nodes.
            fprintf(file, "\nAvailable station nearby (no report received in last %d seconds): ", ELAPSED_TIME);
            
            // Print out each nearby available node.
            for (i = 1; i < 9; i++) {
                nearbyNodeRank = baseLog.availableNearbyNodes[i];
                
                // If there still exist an available nearby node to write, write it out including a comma on the end.
                if (count < baseLog.numAvailableNearbyNodes - 1 && nearbyNodeRank >= 0) {
                    fprintf(file, "%d, ", nearbyNodeRank);
                    count++;        // Increment the count representing the number of availableNearbyNodes.
                }
                // Else if on the last available nearby node, write it out without a comma.
                else if (count == baseLog.numAvailableNearbyNodes - 1 && nearbyNodeRank >= 0) {
                    fprintf(file, "%d", nearbyNodeRank);
                    count++;        // Increment the count representing the number of availableNearbyNodes.
                }
            }

            // The communication time between base station and repoting node in seconds.
            fprintf(file, "\nCommunication Time (seconds) between reporting node and base station : %.4f\n", baseLog.commTime);
            // The total number of messages between the reporting node and base station.
            fprintf(file, "Total Messages exchanged between reporting node and base station: %d\n", baseLog.baseTotalMessages);
            // The total number of messages exchanged between reporting node and neighbouring nodes.
            fprintf(file, "Total Messages exchanged between reporting node and its neighbours: %d\n\n", baseLog.nMessagesExchanged);

            // Free memory once done.
            free(nearbyNodeAdded);
        }

        // Sleep for CYCLE_TIME to simulate one cycle/iteration.
        sleep(CYCLE_TIME);
    }

    // Send termination signal to charging stations to shut down EV network.
    for (i = 0; i < worldSize - 1; i++) {
        MPI_Send(&terminationSignal, 1, MPI_INT, i, TERMINATE_TAG, MPI_COMM_WORLD);

        // printf("Termination Signal Sent to Node %d\n", i);
    }

    // Wait for the thread to complete.
	pthread_join(threadID, NULL);
    
    // Clean and free memory.
    free(baseLogs);
    pthread_mutex_destroy(&masterLock);
    
    fclose(file);       // CLose the file.

    // printf("Base Station Node Finished\n");

    return 0;
}

int main(int argc, char **argv) {
    // Initisalise the rank, worldSize and provided values and initalise MPI_Comm newComm.
    int currentRank, worldSize, provided, masterRank;
    MPI_Comm newComm;

    // Call the MPI_Init_thread function and get the rank and size of the World Comm.
	MPI_Init_thread(&argc, &argv, MPI_THREAD_MULTIPLE, &provided);
    MPI_Comm_rank(MPI_COMM_WORLD, &currentRank);
    MPI_Comm_size(MPI_COMM_WORLD, &worldSize); 
    
    // Master Rank will be the last processor.
    masterRank = worldSize - 1;

    // Split the communicator into master and slave (base station and charging node).
    // Master will be last node.
    MPI_Comm_split(MPI_COMM_WORLD, currentRank == masterRank, 0, &newComm);
    
    // Create the custom MPI Datatypes.
    createCustomDatatype();

    // Call the baseStationIo for the master processor.
    if (currentRank == masterRank) {
	    baseStationIo(MPI_COMM_WORLD, newComm);
    }
    // Call the chargingNodeIo for the slave processors.
    else {
	    chargingNodeIo(MPI_COMM_WORLD, newComm);
    }

    // Free Datatypes created.
    MPI_Type_free(&MPI_NODE_DATATYPE);
    MPI_Type_free(&MPI_ALERT_DATATYPE);
    // Terminate the MPI program once done and clean up processes.
    MPI_Finalize();

    // Terminate the program.
    return 0;
}