"""
Assignment 4

Student name: Yeoh Li Jun
Student ID: 31862586
Last modified: 27/5/2022

References:
    1. https://en.wikipedia.org/wiki/Ford%E2%80%93Fulkerson_algorithm

"""
# Question 1
def allocate(preferences, sysadmins_per_night, max_unwanted_shifts, min_shifts):
    """
    The purpose of this function is to given the preferences of each system administrators on
    which night shifts they are interested and not interested in working and given the three
    constraints being given, the function will find the optimal allocation for all of the n
    system administrators if it exists and return None otherwise.

    Precondition: min_shift cannot be negative
    Postcondition: The optimal allocations for all the n system administrators (list of lists)

    Input:
        preferences: A list of lists. Each interior list represents a different day.
                     preferences[i][j] is equal to 1 if the sysadmin numbered j is interested in working in the
                     shift of the night numbered i, and preferences[i][j] is equal to 0 otherwise.
        sysadmins_per_night: An integer that specifies the exact number of
                             sysadmins that should be on duty each night (the same number of sysadmins will be
                             needed on each night).
        max_unwanted_shifts: An integer that specifies each of the n sysadmins should be allocated to at most
                             max_unwanted_shifts night shifts for which s/he was not interested.
        min_shifts: An integer that specifies each of the n sysadmins should be allocated to at least
                    min_shifts night shifts

    Return: The appropriate allocations (list of lists) for all the n system administrators for the next 30 nights

    Time complexity:
        Worst: O(n^2), where n is the number of system administrators

    Space complexity:
        Input: O(n^2), where n is the number of system administrators
    """
    number_of_admins = len(preferences[0])
    graph = NetworkFlowGraph(preferences, number_of_admins, sysadmins_per_night, max_unwanted_shifts, min_shifts)
    graph.ford_fulkerson(0, 1)
    return None

class NetworkFlowGraph:
    """
    This class creates an object of type "NetworkFlowGraph" which is used to represent
    a network flow graph (or more specifically bipartite graph).

    NOTE: This is supposed to be a generic Network Flow Graph class that are able to extend to other
          graph applications like bipartite graph using minor modifications.
    """
    def __init__(self, preferences, number_of_admins, sysadmins_per_night, max_unwanted_shifts, min_shifts):
        """
        Constructor for the class, binds instance variables to
        given values.

        The function will have 0 for source and 1 for sink and initialise the necessary nodes.
        The function then will call the function add_edges to add all the required edges in the
        bipartite graph.

        Precondition: sysadmins_per_night cannot be larger than number_of_admins
        Postcondition: The initialisation of the bipartite graph

        Input:
            preferences: A list of lists. Each interior list represents a different day.
                         preferences[i][j] is equal to 1 if the sysadmin numbered j is interested in working in the
                         shift of the night numbered i, and preferences[i][j] is equal to 0 otherwise.
            number_of_admins: The number of system administrators that are available for taking some night shifts
            sysadmins_per_night: An integer that specifies the exact number of
                                 sysadmins that should be on duty each night (the same number of sysadmins will be
                                 needed on each night).
            max_unwanted_shifts: An integer that specifies each of the n sysadmins should be allocated to at most
                                 max_unwanted_shifts night shifts for which s/he was not interested.
            min_shifts: An integer that specifies each of the n sysadmins should be allocated to at least
                        min_shifts night shifts

        Return: None

        Time complexity:
            Worst: O(n), where n is the number of system administrators

        Space complexity:
            Input: O(n), where n is the number of system administrators
        """
        self.preferences = preferences
        self.number_of_admins = number_of_admins
        self.sysadmins_per_night = sysadmins_per_night
        self.max_unwanted_shifts = max_unwanted_shifts
        self.min_shifts = min_shifts
        # source = 0, sink = 1
        self.vertices = [None] * (32 + number_of_admins + (number_of_admins * 2))
        for i in range(32 + number_of_admins + (number_of_admins * 2)):
            self.vertices[i] = Vertex(i)

        self.add_edges(number_of_admins)

    def add_edges(self, number_of_admins):
        """
        The purpose of this function is to add all the necessary edges in the bipartite graph.

        Precondition: number_of_admins cannot be negative
        Postcondition: The initialisation of all edges in the bipartite graph

        Input:
            number_of_admins: The number of system administrators that are available for taking some night shifts

        Return: None

        Time complexity:
            Worst: O(n), where n is the number of system administrators

        Space complexity:
            Input: O(n), where n is the number of system administrators
        """
        # Connect sink back to source
        edge = Edge(1, 0, (32 + number_of_admins + (number_of_admins * 2)))
        self.vertices[1].add_edge(edge)

        # Connect source to all n sysadmins
        for sysadmin_index in range(2, 2 + number_of_admins):
            interested_shifts = 0
            for i in range(len(self.preferences)):
                interested_shifts += self.preferences[i][sysadmin_index - 2]
            edge = Edge(0, sysadmin_index, self.max_unwanted_shifts + interested_shifts)
            self.vertices[0].add_edge(edge)

        # Connect each sysadmins to an two intermediate node (one is where the sysadmins is
        # interested in working night shifts and the other is where the sysadmins is working on the
        # night shifts that they are not interested to work in)
        sysadmin_index = 2
        for index in range(2 + number_of_admins, 2 + number_of_admins + (number_of_admins * 2), 2):
            interested_shifts = 0
            for i in range(len(self.preferences)):
                interested_shifts += self.preferences[i][sysadmin_index - 2]
            edge = Edge(sysadmin_index, index, interested_shifts)
            self.vertices[sysadmin_index].add_edge(edge)
            for i in range(len(self.preferences)):
                if self.preferences[i][sysadmin_index - 2] == 1:
                    edge = Edge(index, 2 + number_of_admins + (number_of_admins * 2) + i, 1)
                    self.vertices[index].add_edge(edge)
            edge = Edge(sysadmin_index, index + 1, self.max_unwanted_shifts)
            self.vertices[sysadmin_index].add_edge(edge)
            for i in range(len(self.preferences)):
                if self.preferences[i][sysadmin_index - 2] == 0:
                    edge = Edge(index + 1, 2 + number_of_admins + (number_of_admins * 2) + i, 1)
                    self.vertices[index + 1].add_edge(edge)

        # Connect all the nights with the sink
        for i in range(2 + number_of_admins + (number_of_admins * 2), 2 + number_of_admins + (number_of_admins * 2) + 30):
            edge = Edge(i, 1, self.sysadmins_per_night)
            self.vertices[i].add_edge(edge)

    def reset(self, list):
        """
        The purpose of this function is to reset the values (instance variables)
        inside all the vertices (these vertices are retrieve from an adjacency list).

        Precondition: An adjacency list of vertices and edges
        Postcondition: The values of the vertices are being reset to their default values

        Input:
            list: An adjacency list that contains n number of vertices to be reset

        Return: None

        Time complexity:
            Worst: O(n), where n is the number of vertices inside the adjacency list

        Space complexity:
            Input: O(n), where n is the number of vertices inside the adjacency list
        """
        for vertex in list:
            vertex.discovered = False
            vertex.visited = False

    def has_augmenting_path(self, source, t, parent):
        """
        The purpose of this function is to check if there is still any path between the source and the sink in the
        bipartite graph.

        Precondition: source must be 0 and t (sink) must be 1
        Postcondition: A boolean indicating whether there is a path between the source and the sink

        Input:
            source: An integer indicating the source
            t: An integer indicating the sink
            parent: A list indicating the parent for every vertices

        Return: None

        Time complexity:
            Worst: O(n), where n is the number of edges

        Space complexity:
            Input: O(n), where n is the number of edges
        """
        discovered = []
        discovered.append(self.vertices[source])
        while len(discovered) > 0:
            u = discovered.pop(0)
            u.visited = True
            if u.id == self.vertices[t].id:
                self.reset(self.vertices)
                return True
            for edge in u.edges:
                v = self.vertices[edge.ending]
                if not v.discovered and edge.capacity > 0:
                    discovered.append(v)
                    v.discovered = True
                    parent[v.id] = u.id
        self.reset(self.vertices)
        return False

    def ford_fulkerson(self, source, sink):
        """
        The purpose of this function is to run ford fulkerson on the network flow in order to
        return the maximum flow from the source to the sink based on the graph the function created

        Precondition: source must be 0 and t (sink) must be 1
        Postcondition: Returns the maximum flow from the source to the sink based on the graph the function created

        Input:
            source: An integer indicating the source
            sink: An integer indicating the sink

        Return: None

        Time complexity:
            Worst: O(n^2), where n is the number of system administrators

        Space complexity:
            Input: O(n^2), where n is the number of system administrators
        """
        # This array is filled by has_augmenting_path and is used to store path
        parent = [-1] * len(self.vertices)

        flow = 0

        # Augment the flow while there is path from source to sink
        while self.has_augmenting_path(source, sink, parent):

            # Find minimum residual capacity of the edges along the
            # path filled by has_augmenting_path
            path_flow = float("Inf")
            s = sink
            while (s != source):
                path_flow = min(path_flow, self.vertices[parent[s]].get_edge(s).capacity)
                s = parent[s]

            # Add path flow to overall flow
            flow += path_flow

            # update residual capacities of the edges and reverse edges
            # along the path
            v = sink
            while (v != source):
                u = parent[v]
                self.vertices[u].get_edge(v).capacity -= path_flow
                if self.vertices[v].has_edge(u):
                    self.vertices[v].get_edge(u).capacity += path_flow
                else:
                    edge = Edge(v, u, 0)
                    self.vertices[v].add_edge(edge)
                    self.vertices[v].get_edge(u).capacity += path_flow
                v = parent[v]

        return flow

class Vertex:
    """
    This class creates an object of type "Vertex" which is used to represent
    a vertex

    NOTE: This is a generic Vertex class that are able to support other
          graph applications and not just for our graph used here.
    """
    def __init__(self, id):
        """
        Constructor for the class, binds instance variables to
        given values (which include default values)

        Precondition: The vertex id must be in an integer form
        Postcondition: The initialisation of a Vertex object

        Input:
            id: An integer representing the vertex's id

        Return: None

        Time complexity:
            Worst: O(1) as initialising the instance variables are constant operations

        Space complexity:
            Input: O(1)
        """
        self.id = id
        self.edges = []
        self.discovered = False
        self.visited = False

    def add_edge(self, edge):
        """
         The purpose of this function is to add an edge (Edge object) to this
         vertex (Vertex object)

         Precondition: An Edge object where its instance variable starting (in the form
                       of an integer) is this vertex's id
         Postcondition: This particular vertex's edges has increased by 1

         Input:
             edge: An Edge object representing a connection between this vertex and
                   another vertex

         Return: None

         Time complexity:
             Worst: O(1)

         Space complexity:
             Input: O(1)
         """
        self.edges.append(edge)

    def get_edge(self, v):
        """
         The purpose of this function is to retrieve an edge instance if it exists

         Precondition: v must be in an integer form
         Postcondition: An edge instance where this current vertex is the starting and v
                        is the ending

         Input:
             v: An integer (vertex id) representing the end of an edge

         Return: An edge instance where this current vertex is the starting and v
                 is the ending

         Time complexity:
             Worst: O(1)

         Space complexity:
             Input: O(1)
         """
        for edge in self.edges:
            if edge.ending == v:
                return edge
        return 0

    def has_edge(self, v):
        """
         The purpose of this function is to check whether an edge instance with ending as v exists or not

         Precondition: v must be in an integer form
         Postcondition: A boolean indicating whether the edge instance exists or not

         Input:
             v: An integer (vertex id) representing the end of an edge

         Return: A boolean indicating whether an edge instance with the ending set as v exists or not

         Time complexity:
             Worst: O(1)

         Space complexity:
             Input: O(1)
         """
        for edge in self.edges:
            if edge.ending == v:
                return True
        return False

class Edge:
    """
    This class creates an object of type "Edge" which is used to form
    an edge between two vertices

    NOTE: This is a generic Edge class that are able to support other
          graph applications and not just for the graph used here.
    """
    def __init__(self, starting, ending, capacity):
        """
        Constructor for the class, binds instance variables to
        given values

        Precondition: The vertices and capacity given must be in an integer form
        Postcondition: The initialisation of an Edge object (starting, ending, capacity)

        Input:
            starting: An integer representing a vertex
            ending: An integer representing a vertex
            capacity: An integer representing the capacity of the edge between the two vertices (starting and ending)

        Return: None

        Time complexity:
            Worst: O(1) as initialising the instance variables are constant operations

        Space complexity:
            Input: O(1)
        """
        self.starting = starting
        self.ending = ending
        self.capacity = capacity

# Question 2
class EventsTrie:
    """
    This class creates an object of type "EventsTrie" which is used to represent
    am events trie and it contains all the functionalities that can be performed
    on that events trie.
    """
    def __init__(self, timelines):
        """
        Constructor for the class, binds instance variables to
        given values and perform the initialisation process of a
        events trie class.

        Firstly, the constructor will initialise a root node for the generalised suffix trie.
        The function then proceeds to create a list that will store information for all possible occurrences
        (from 1 occurrence up to n occurrences). Inside the list, each element will contain a list that stores
        3 information about the longest chain in a particular number of occurrences.
        [The level of the longest chain (the number of characters in the chain),
        the index of a particular timeline in timelines to retrieve the longest chain from,
        the ending position of the longest chain in a particular timeline].

        Next, the function will start to form the generalised suffix trie structure. The function will iterate over all
        the timelines and for each timeline, the function will insert the full timeline and all of its suffixes into the
        trie.

        Precondition: The timelines should be represented in terms of a string
        Postcondition: A generalised suffix trie will be formed that encapsulates all of the timelines and their
                       corresponding events.

        Input:
            timelines: A list of strings representing a list of timelines

        Return: None

        Time complexity:
            Worst: O(nm^2), where n is the number of timelines in timelines.
                   and m is the number of events in the longest timeline.

        Space complexity:
            Input: O(nm^2), where n is the number of timelines in timelines.
                   and m is the number of events in the longest timeline.
        """
        self.timelines = timelines
        # Create a root node
        self.root = Node()

        self.longest_chain_upto_noccurrences = [None] * (len(timelines) + 1)
        for i in range(1, len(timelines) + 1):
            self.longest_chain_upto_noccurrences[i] = ([None] * 3) # [level, key, end]

        for i in range(len(timelines)):
            timeline = timelines[i]
            for j in range(len(timeline)):
                self.insert(self.root, timeline, j, len(timeline), 1, i)

    def insert(self, current, timeline, start, end, level, timeline_index):
        """
        The purpose of this function is to insert a suffix into a generalised suffix trie.

        Firstly, the function will check if the start is equal to the end. If the start is not equal to the end, the
        function will then proceed to insert a character into the trie. If from the current node, there does not exist a
        node for the current character that the function is inserting, then it will create a new node for this
        character, set its appropriate level and check with the centralise array to see if its level is higher than the
        level for the current number of occurrences (store in the centralise array) for the current node.
        If yes, then all previous characters up to this
        character will form the longest chain for the current number of occurrences held by this character.

        On the other hand, if from the current node, there exist a
        node for the current character that the function is inserting then the function will move to that node and
        increase its number of occurrences by 1 if the current timeline has not visited it before and check with the
        centralise array to see if its level is higher than the
        level for the current number of occurrences (store in the centralise array) for that current node.
        If yes, then all previous characters up to this
        character will form the longest chain for the current number of occurrences held by this character.

        The function will then proceed to do this recursively until it reaches the end of the timeline (start == end)
        then navigate to the terminal node or create one if it does not exist. The function will then return back up
        all the to its caller.

         Precondition: The start (index) and end (index) must be between 0 and the length of the timeline inclusive.
                       Furthermore, the timeline_index must be between 0 and the length of timelines.
         Postcondition: A suffix has been stored into the generalised suffix trie

         Input:
             current: The Node that the function will be working on
             timeline: A string representing the timeline the function will be working on
             start: An integer (index) representing the beginning of a suffix in a timeline
             end: An integer (index) representing the end a timeline
             level: An integer representing how deep is the character in the trie (the position of the character
                    in its suffix)
             timeline_index: The position (index) of this particular timeline in timelines

         Return: None

         Time complexity:
             Worst: O(m), where m is the number of events in the longest timeline.

         Space complexity:
             Input: O(m), where m is the number of events in the longest timeline.
         """
        if start == end:
            index = 0
            if current.link[index] is not None:
                current = current.link[index]
            else:
                current.link[index] = Node()
                current = current.link[index]
                current.level = level
            current.latest_visited_timeline = timeline_index
            return None
        else:
            char = timeline[start]
            index = ord(char) - 97 + 1
            if current.link[index] is not None:
                current = current.link[index]
                if current.latest_visited_timeline != timeline_index:
                    current.noccurrence += 1
                    if self.longest_chain_upto_noccurrences[current.noccurrence][0] is None or current.level > self.longest_chain_upto_noccurrences[current.noccurrence][0]:
                        self.longest_chain_upto_noccurrences[current.noccurrence][0] = current.level
                        self.longest_chain_upto_noccurrences[current.noccurrence][1] = timeline_index
                        self.longest_chain_upto_noccurrences[current.noccurrence][2] = start
            else:
                current.link[index] = Node()
                current = current.link[index]
                current.level = level
                if self.longest_chain_upto_noccurrences[current.noccurrence][0] is None or current.level > \
                        self.longest_chain_upto_noccurrences[current.noccurrence][0]:
                    self.longest_chain_upto_noccurrences[current.noccurrence][0] = current.level
                    self.longest_chain_upto_noccurrences[current.noccurrence][1] = timeline_index
                    self.longest_chain_upto_noccurrences[current.noccurrence][2] = start
            current.latest_visited_timeline = timeline_index
            self.insert(current, timeline, start + 1, end, level + 1, timeline_index)

    def getLongestChain(self, noccurence):
        """
        The purpose of this function is to return a string that represents the longest chain of events that occurs in
        at least noccurence timelines or return None if it such a chain does not exist.

        Firstly, the function will check if the longest chain of events that occurs in
        at least noccurence timelines is None or not. If its not None, then the function will
        retrieve the timeline where the longest chain resides. The function also retrieves the start index in the
        timeline where the longest chain will begin from. The function will then use a for loop to iterate from the
        start position of the longest chain in the particular timeline up to the end of the longest chain in the
        particular timeline. The function then returns the longest chain.

        Precondition: noccurence must be a positive integer in the range 1 to n (number of timelines)
        Postcondition: Returns a string that represents the longest chain of events that occurs in at least
                       noccurence timelines or return None if it such a chain does not exist.

        Input:
            noccurence: A positive integer in the range 1 to n (number of timelines)

        Return: A string that represents the longest chain of events that occurs in at least
                noccurence timelines or return None if it such a chain does not exist.

        Time complexity:
            Worst: O(k), where k is the length of the longest event chain that occur at least in noccurence number of
                   timelines

        Space complexity:
            Input: O(k), where k is the length of the longest event chain that occur at least in noccurence number of
                   timelines
        """
        if self.longest_chain_upto_noccurrences[noccurence][0] is not None:
            timeline = self.timelines[self.longest_chain_upto_noccurrences[noccurence][1]]
            index = self.longest_chain_upto_noccurrences[noccurence][2] + 1 - self.longest_chain_upto_noccurrences[noccurence][0]
            longest_chain = ""
            for _ in range(self.longest_chain_upto_noccurrences[noccurence][0]):
                longest_chain += timeline[index]
                index += 1
            return longest_chain
        else:
            return None

class Node:
    """
    This class creates an object of type "Node" which is used to represent
    a character or more specifically a particular level in a particular chain
    and it contains all the information needed to retrieve the longest chain.
    """
    def __init__(self):
        """
        Constructor for the class, binds instance variables to
        given values and perform the initialisation process of a
        Node class.

        Precondition: None
        Postcondition: Initialises all the instance variables inside the Node class to its
                       default values

        Return: None

        Time complexity:
            Worst: O(1)

        Space complexity:
            Input: O(1)
        """
        self.noccurrence = 1
        # The latest timeline that has visited this Node
        self.latest_visited_timeline = None
        # How deep the Node is in the chain
        self.level = 0
        # An array list representing 27 possible directions for the chain to continue.
        # Starting from $, a, b.... z
        self.link = [None] * 27

if __name__ == '__main__':
    timelines = ["abc", "dbcef", "gdbc"]