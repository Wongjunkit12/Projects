from typing import List, Tuple, Union
import heapq as hq
import math


# Question 1
# Vertex class which will contain their ID and a list of edges, filled with Edge objects signifying all the 
# vertexes this vertex is connected to
class Vertex:
    def __init__(self, id: int) -> None:
        """
        Constructor to construct the Vertex object which has a unique location ID instance variable and a list of 
        tuples which represents the edges (roads). Will also set some instance variables used later to their default values.
        :Input:
            self: The Vertex class.
            id: The Location ID of the vertex.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        self.location_id = id               # Create an instance variable to store the Location ID.
        self.edges = []                     # Initially sets the edges to be an empty list. Will contain Edge objects.
        self.cafe_waiting_time = None       # Initially set all cafe_waiting_time to None to signify no cafes in location ID.
        self.is_discovered = False          # is_discovered representing whether this vertex is visible or not.
        self.is_visited = False             # is_visited to signify if the vertex has been visited inside by the Dijkstra.
        self.previous = None                # The optimal previous vertex connected to it, None by default.
        self.time_taken = None              # Initial time taken to travel to this vertex from source set to None.
        self.accumulated_score = -math.inf  # This is used for Q2. Accumulated score is the total score from all slopes traversed before.
                                            # Sets it to negative infinity so that it can be compared with negative numbers too.
        self.been_visited = []              # Used for Q2. Will append True for every edge visited.
        
    def append_edges(self, road: Tuple) -> None:
        """
        Takes an edge object in the input and append them to the edges instance variable list.
        :Input:
            self: The Vertex class.
            road: A tuple (u, v, w) which represents a single directed road. This will be considered as an edge for the vertex.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        self.edges.append(road)     # Append the input road into the edges instance variable list.
        
    def add_cafe_time(self, waiting_time: int) -> None:
        """
        Adds waiting time to the vertex for a coffee. Will only add if a cafe is present, else the waiting time remains None.
        to signify no cafe at location ID.
        :Input:
            self: The Vertex class.
            waiting_time: How long a person has to wait before receiving his coffee at the cafe in this location.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        self.cafe_waiting_time = waiting_time   # Changes the instance variable to the waiting time input.
        
               
# Edge class containing a tuple of the starting and end vertexes and their weight.
class Edge:
    def __init__(self, u: Vertex, v: Vertex, w: int) -> None:
        """
        Constructor for the edge object which will have 3 instance variables based on its input.
        :Input:
            self: The Edge class.
            u: u is the starting ID of the road.
            v: v is the end ID of the road.
            w: w is the time taken to traverse the road from starting ID to end ID.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        # Sets instance variable of u, v and w respectively.
        self.starting_vertex = u    # Starting Vertex.
        self.ending_vertex = v      # Ending Vertex.
        self.weight = w             # The weight, this is the time taken.
    

# RoadGraph class which will create an adjacency list of vertexes.
class RoadGraph:
    def __init__(self, roads: List[Tuple[int, int, int]], cafes: Union[List[Tuple[int, int]], None] = None) -> None:
        """
        Constructor to construct a weighted adjacency list. Will utilise classes and objects to create a list of Vertex objects.
        Will first find the maximum location ID within the roads list. Then it will add each ID as a Vertex object. It will
        then add the edges of the Vertex into it which is taken from the roads input list. If cafes list is provided, add
        cafe waiting time for each Vertex in which a cafe is present.
        :Input:
            self: The RoadGraph class.
            roads: A list of tuples where (u, v, w) represent a road. u is the starting ID of the road, v is the end ID and v is the time taken to traverse the road.
            cafes: A list of tuples where (location, waiting_time represents a cafe. location is the location ID and waiting_time is the time taken to grab a coffee. If none are provided, default set to None.
        :Output: None.
        :Time complexity: O(|V| + |E|) where V is the amount of unique location IDs and E is the amount of roads.
        :Space complexity: O(|V| + |E|) where V is the amount of unique location IDs and E is the amount of roads.
        """
        self.vertexes = []          # Will contain the vertexes from source to cafes.
        self.reverse_vertexes = []  # Will contain the vertexes from cafes to end.
        self.cafes = cafes          # List containing all the cafe tuples.
        
        # For loop to loop through the roads and find the maximum starting ID.
        # O(|E|) time complexity where E is the amount of roads.
        # O(1) space complexity.
        maximum = 0                 # Assigns the maximum initially to 0 as minimum ID is 0.
        for road in roads:
            # If the starting ID is more than the current maximum, set it as the new maximum.
            if (road[0] > maximum):
                maximum = road[0]
            # If the end ID is more than the current maximum, set it as the new maximum.
            elif (road[1] > maximum):
                maximum = road[1]
                
        # For loop to loop through the IDs and create Vertexes for each of them. Will add it to both vertexes list.
        # O(|V|) time complexity where V is the amount of Unique IDs.
        # O(|V|) space complexity.
        for id in range(maximum + 1):
            self.vertexes.append(Vertex(id))
            self.reverse_vertexes.append(Vertex(id))
        
        # For loop to loop through the roads again and add edges into the vertexes objects.
        # O(|E|) time complexity.
        # O(|E|) space complexity.
        for road in roads:
            # Take u, v, w from the road tuple and grab the vertex object based on ID for the start and ending IDs.
            starting_vertex = self.vertexes[road[0]]
            ending_vertex = self.vertexes[road[1]]
            time_taken = road[2]
            
            edge = Edge(starting_vertex, ending_vertex, time_taken)     # Create an edge classes using the road tuple.
            starting_vertex.append_edges(edge)      # Add the edge object into the starting vertex.
            
            # Same as above except reverse the starting and ending vertexes for the reverse vertex used to go from end to cafes.
            reverse_starting = self.reverse_vertexes[road[1]]
            reverse_ending = self.reverse_vertexes[road[0]]
            time_taken = road[2]
            
            edge = Edge(reverse_starting, reverse_ending, time_taken)   # Create an edge classes using the road tuple.
            reverse_starting.append_edges(edge)     # Add the edge object into the starting vertex.
        
        # If cafes are provided, set for cafe.
        if cafes != None:
            # Loop through the cafe list to add the waiting time to the vertex a cafe is present.
            # Worst case O(|V|) time complexity.
            # O(1) space complexity
            for cafe in cafes:
                # For self.vertex
                vertex_with_cafe = self.vertexes[cafe[0]]   # Get the vertex based on the cafe location ID.
                vertex_with_cafe.add_cafe_time(cafe[1])     # Add the waiting time in minutes for that specific vertex.
                
                # For self.reverse_vertex
                vertex_with_cafe = self.reverse_vertexes[cafe[0]]   # Get the vertex based on the cafe location ID.
                vertex_with_cafe.add_cafe_time(cafe[1])             # Add the waiting time in minutes for that specific vertex.
        
    
    def dijkstra_algo(self, adjacency_lst: List[Vertex], beginning_vertex: Vertex) -> None:
        """
        A function to find the fastest path from source using Dijkstra Algorithm. Will use the inbuilt priority queue/heap
        to push the vertexes in with their key being the time taken to traverse to that location from source. Will find
        optimal route taken by mutating the Vertex .previous instance variable. The .previous instance variable will
        contain the previous Vertex connected to it with the shortest time taken.
        :Input:
            self: The RoadGraph class.
            adjacency_lst: An adjacency list for the dijkstra_algo to use to traverse through to find fastest path.
            beginning_vertex: The beginning vertex that the dijkstra will start at.
        :Output: None.
        :Time complexity: O(|E| log |V|) where E is the number of roads and V is the number of unique location IDs.
        :Aux Space complexity: O(|V| + |E|) where V is the number of unique location IDs and E is the number of roads.
        """
        beginning_vertex.time_taken = 0 # Sets the time taken to travel the route to 0 if no cafe. 0 + cafe waiting time if there is one.
        visible_vertexes = []           # Create the heap for all visible vertexes.
        # Pushes the starting vertex into the heap as a tuple of (key, vertex).
        # O(log |V|) time complexity.
        hq.heappush(visible_vertexes, (beginning_vertex.time_taken, beginning_vertex.location_id))
        
        # While the heap is not empty and has still some vertexes in it.
        # O(|E| log |V|) time complexity.
        while len(visible_vertexes) > 0:
            # Pops the smallest vertex location ID from the heap. Indexes it with the vertexes adjacency list to get the vertex object.
            # O(log |V|) time complexity.
            starting_id = adjacency_lst[hq.heappop(visible_vertexes)[1]]
            # Set starting vertex to both discovered and visited.
            starting_id.is_visited = True
            starting_id.is_discovered = True
            
            # Loop through all vertexes the starting vertex has a directed connection to.
            for edge in starting_id.edges:
                ending_vertex = edge.ending_vertex      # Gets the ending vertex of the edge connection.
                total_time_taken = starting_id.time_taken + edge.weight # Total time taken to go from source to this ending vertex.
                
                # If the ending vertex is still invisible/not discovered yet, update it.
                if ending_vertex.is_discovered == False:
                    ending_vertex.is_discovered = True       # Has been discovered, make it visible.
                    # Gets the total time taken from source to this vertex
                    ending_vertex.time_taken = total_time_taken
                    # Updates the previous vertex to be the starting vertex. Makes the previous vertex visible to the ending vertex.
                    ending_vertex.previous = starting_id
                    # Pushes the ending_vertex into heap to signify it has been discovered.
                    # O(log |V|) time complexity.
                    hq.heappush(visible_vertexes, (total_time_taken, ending_vertex.location_id))
                    
                # Once all neighbouring vertexes are discovered, check if any of them have been visited before.
                elif ending_vertex.is_visited == False:
                    # If the new route time taken is less than the old route time taken for the ending vertex,
                    # update the time taken to the new route.
                    if ending_vertex.time_taken > total_time_taken:
                        ending_vertex.time_taken = total_time_taken    # Update the time taken to follow the new faster route.
                        ending_vertex.previous = starting_id           # Adds the previous vertex to the ending vertex.
             
    def get_fastest_cafe(self) -> Union[Tuple[int, int], None]:
        """
        A function to get the cafe that takes the shortest time to travel there, get a coffee then reach the end. If no cafe
        exists, return None. Loops through all possible cafes in existance and then calculate the total time taken to journey 
        there, wait for coffee and then go to the end. Returns the cafe with the shortest total time taken. If no cafe exists,
        return None.
        :Input:
            self: The RoadGraph class.
        :Output:
            fastest_cafe: The cafe tuple that has the shortest time taken to travel there, get a coffee and then traverse to the end. Will return None if none exists.
        :Time complexity: O(|V|) where V is the number of unique location IDs.
        :Space complexity: O(1)
        """
        fastest_time = math.inf # Initially set the fastest_time to infinity so every single value will be smaller than it.
        fastest_cafe = None     # Initial set fastest_cafe is None as no fastest cafe is found yet.
        # For loop to loop through each cafe and find the cafe with the fastest time taken.
        # Worst case O(|V|) time complexity.
        for cafe in self.cafes:
            # Gets the travel time to and from cafe.
            travel_time_to_cafe = self.vertexes[cafe[0]].time_taken
            travel_time_from_cafe = self.reverse_vertexes[cafe[0]].time_taken
            is_cafe_connected = travel_time_to_cafe != None and travel_time_from_cafe != None
            
            # If the cafe is reachable from start and end vertex.
            if is_cafe_connected:
                # Finds the total time taken to move to that location, get a coffee, then move to the end location.
                total_time_taken = self.vertexes[cafe[0]].time_taken + self.reverse_vertexes[cafe[0]].time_taken + self.vertexes[cafe[0]].cafe_waiting_time
                # If the total_time_taken for this cafe is lesser than the current fastest time, set the fastest cafe to this cafe
                # and the fastest_time to the new total_time_taken.
                if total_time_taken < fastest_time:
                    fastest_cafe = cafe
                    fastest_time = total_time_taken
        
        return fastest_cafe # Return fastest cafe if found, else return None.
            
    def get_optimal_route(self, adjacency_lst: List[Vertex], stopping_id: int, source_id: int) -> List[int]:
        """
        A function to find the fastest/quickest route from start to the end, including one cafe stop to get some coffee. Will
        return a list containing all the location ID traversed. Will backtrack from source_id to stopping_id by appending the
        .previous instance variable in the Vertex object into the list.
        :Input:
            self: The RoadGraph class.
            adjacency_lst: An adjacency list which will be traversed in order to get the fastest path from source to end.
            stopping_id: The location ID of the where to stop the traversal.
            source_id: The location ID of the source vertex to begin the traversal.
        :Output:
            fastest_route: A list containing all the location IDs from beginning to end, including a cafe stop for coffee. Will be the quickest route.
        :Time complexity: O(|V|) where V is the number of unique location IDs.
        :Space complexity: O(|V|) where V is the number of unique location IDs.
        """
        current_vertex = adjacency_lst[source_id]     # Gets the vertex of the end_id.
        fastest_route = [current_vertex.location_id]  # The fastest route from beginning vertex to ending vertex, including a cafe stop in between.
    
        # While the current vertex ID is not the beginning vertex ID.
        # O(|V|) time complexity.
        # O(|V|) space complexity.
        while current_vertex.location_id != stopping_id:
            previous_vertex = current_vertex.previous           # Gets the fastest previous vertex of the current vertex.
            fastest_route.append(previous_vertex.location_id)   # Append the location ID of the previous vertex.
            current_vertex = previous_vertex    # Changes the new current vertex to the previous one to continue traversing backwards.
            
        return fastest_route    # Returns the list now filled with location IDs of the fastest path/route.
                            
    def routing(self, start: int, end: int) -> Union[List[int], None]:
        """
        The routing function will find the quickest path through the roads, while stopping by to one cafe anywhere during the 
        journey to get a cup of coffee. It will first use the constructed RoadGraph from 1.1 and then use a Dijkstra Algorithm
        to compute the quickest time from start to end. It will do the same but for the reverse, from end to start. Then
        it will loop through all the cafes list and then calculate the quickest time from start to cafe and then from that 
        cafe to end. It will select the cafe in which the total time taken (time taken traversing + waiting time) is the shortest.
        Then it will loop through the vertexes from cafe to start, to construct the route taken (location IDs) from start to cafe.
        It will do the same but from end to cafe instead to construct the route taken from cafe to end. Combining this two lists
        will return the full quickest route taken from start to end while also grabbing coffee from one cafe.
        :Input:
            self: The RoadGraph class.
            start: A location ID that represents the starting location of your journey. Route must begin on this ID.
            end: A location ID representing the ending location of your journey. Route must end on this ID.
        :Output:
            fastest_route: A list containing all the location IDs from beginning to end, including a cafe stop for coffee. Will be the quickest route. Will return None if no route/cafe exist.
        :Time complexity: O(|E| log |V|) where E is the amount of roads and V is the amount of unique location IDs
        :Aux space complexity: O(|V| + |E|) where V is the amount of unique location IDs and E is the amount of roads.
        """
        # O(|E| log |V|) time complexity.
        self.dijkstra_algo(self.vertexes, self.vertexes[start]) # Call the dijkstra to go from starting to all cafes.
        self.dijkstra_algo(self.reverse_vertexes, self.reverse_vertexes[end])  # Call the dijkstra to go from ending to all cafes.
        
        # O(|V|) time complexity.
        fastest_cafe = self.get_fastest_cafe()   # Gets all possible routes from start to all cafes.
        # If there are no cafes, return None.
        if fastest_cafe == None:
            return None

        # Gets the fastest route from cafe to the start ID.
        # O(|V|) time complexity.
        fastest_route = self.get_optimal_route(self.vertexes, start, fastest_cafe[0])
        # O(|V|) time complexity to reverse.
        fastest_route.reverse()     # Reverse it so it's in the correct order.
        fastest_route.pop()         # Deletes the last index to remove the cafe from the list.
        # Gets the fastest route from end ID to cafe.
        # O(|V|) time complexity.
        fastest_route += self.get_optimal_route(self.reverse_vertexes, end, fastest_cafe[0])

        # If the fastest_route is not empty and the last location ID is the ending ID, return it.
        if len(fastest_route) > 0 and fastest_route[-1] == end:
            return fastest_route
        # Else it means route doesn't exist, return None.
        else:
            return None
    
    
# Question 2
def recursive_backtrack(uphill_adjacency: List[Vertex], starting_p: int, current_p: int) -> Union[Vertex, None]:
    """
    A function to recursively backtrack through the adjacency list inputted. Will start from finishing point to starting point,
    checking through every possible path and returning the path which generates the most score. The base case will be the 
    starting point, which will return the starting vertex itself. Will traverse from the finishing point to the starting point,
    finding all possible paths on the way. It will the pick the most optimal path (highest score gain) as the path to traverse.
    Vertexes that already have the optimal path from starting to that vertex will not be traversed again as the optimal path 
    from starting point to that vertex is already saved. This ensures that visited edges will not be revisited again, making it
    more efficient.
    :Input:
        uphill_adjacency: An adjacency list of Vertexes where the edges are reversed. The edges are going "uphill".
        starting_p: The starting point of the route.
        current_p: The current point the backtrack function is currently at.
    :Output:
        current_v: The final vertex of where it tops. May also return None if the current vertex has no connection to start.
    :Time complexity: O(|D|) where D is the number of downhill segments.
    :Space complexity: O(|D|) where D is the number of downhill segments.
    """
    # Base case. If the current point the backtrack is at is the starting point.
    if current_p == starting_p:
        starting_v = uphill_adjacency[starting_p]   # Gets the starting vertex by indexing it using start point.
        return starting_v   # Will return the starting vertex itself.
    
    # Else if the current point does not connect to the starting point,
    elif len(uphill_adjacency[current_p].edges) == 0:
        return None   # Return None if current point has no edges, meaning it's disconnected from the starting point.
    
    # Else, continue to call itself recursively.
    else:
        current_v = uphill_adjacency[current_p] # Indexes the current vertex using the current point.
        possible_routes = current_v.edges       # Get the list of all edges in the current vertex.
        
        # If all the edges has been visited before, return the current vertex as the previous vertex. Do not traverse the edges again.
        if len(current_v.been_visited) == len(current_v.edges):
            return current_v
        
        # For loop to loop through every edge of the current point to find all possible routes.
        # In total will run D times. So O(|D|) time complexity ignoring costs within loop.
        # Including costs will be O(|D| + |P|). Since P <= D, O(|D|) time complexity.
        for possible_route in possible_routes:
            connected_p = possible_route.ending_vertex.location_id   # Gets the point P of the new slope of all connected edges of current_p.
            # Recursive will traverse through every point so will run a max of P times. O(|P|) time complexity.
            previous_v = recursive_backtrack(uphill_adjacency, starting_p, connected_p)  # Calls itself but will go uphill from current_p to connected_p.
            
            # If the current vertex is connected to the starting vertex and has not been visited before.
            if previous_v != None:
                # If the previous point is the starting point/base case, set the total accumulated score to be 0 + weight.
                if previous_v.location_id == starting_p:
                    # Gets the new total score (0 + weight).
                    total_accumulated_score = 0 + possible_route.weight
                # Else the total accumulated score is the previous score + new weight.
                else:
                    # Gets the new total score (total previous accumulated score + weight).
                    total_accumulated_score = previous_v.accumulated_score + possible_route.weight
                
                # If the new score in the new path is more than the accumulated score of an older alternative path, update it.
                if total_accumulated_score > current_v.accumulated_score:
                    current_v.accumulated_score = total_accumulated_score   # Update accumulated score.
                    current_v.previous = previous_v # Points the previous vertex to be previous_v in the current_v.
                
            current_v.been_visited.append(True) # Appends True to the list to signify the edge has been already visited.
        
        return current_v    # Return the current vertex it's at.
    

def optimalRoute(downhillScores: Tuple[int, int, int], start: int, finish: int) -> Union[List[int], None]:
    """
    This function will find the most optimal route with the highest score from starting point to finishing point while using
    recursive backtracking. The recursive backtracking will save the previous optimal vertex from end to start. Then, the
    get_optimal_route method is called to traverse from finish to start in order to get all the points on the optimal route.
    Once the optimal route is created, it will reverse it so it goes from start to finish.
    :Input:
        downhillScores: A tuple (a, b, c) where a is the start point and b is the end point of a downhill segment and c is the score gain for using this downhill segment.
        start: The starting point of the route.
        finish: The finishing point of the route.
    :Output:
        max_score_route: The most optimal path going downhill from start to finish while obtaining the highest score possible. Will return None if no route is found.
    :Time complexity: O(|D|) where D is the amount of  downhill segments.
    :Space complexity: O(|D|) where D is the amount of  downhill segments.
    """
    # O(|P| + |D|) time complexity.
    # Since P <= D, O(|D|) time complexity.
    downhill_adjacency = RoadGraph(downhillScores)   # Reuse the Graph constructor in Q1. Will create an adjacency list for the downhill segments as edges.
    
    # Calls the recursive backtrack to brute force the optimal route. Will put in the reverse adjacency list so that it begins
    # from finish to start. Will also input the start as the base case and the finish as the current vertex to start the backtrack.
    # O(|D|) time complexity.
    ending_v = recursive_backtrack(downhill_adjacency.reverse_vertexes, start, finish)

    # If no possible route found to finish from start, return None.
    if ending_v == None or (ending_v.location_id != start and ending_v.previous == None):
        return None
    
    # Reuse the get_optimal_route from the RoadGraph class. Will start from finish point and keep appending previous 
    # points until reaching the start point.
    # O(|D|) time complexity.
    max_score_route = downhill_adjacency.get_optimal_route(downhill_adjacency.reverse_vertexes, start, finish)
    # O(|D|) time complexity.
    max_score_route.reverse()   # Reverses the list so that it goes from start to finish instead of finish to start.
    
    return max_score_route  # Return the path taken from starting point to finishing point.