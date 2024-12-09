from typing import List, Tuple, Union
import math


# Question 1
# Vertex class which will contain their ID and a list of edges, filled with Edge objects signifying all the 
# vertexes this vertex is connected to
class Vertex:
    def __init__(self, id: int) -> None:
        """
        Constructor to construct the Vertex object which has a unique ID instance variable and a list of 
        tuples which represents the edges. Will also contain instance variables representing the demand of the vertex
        and the availability of the person if the vertex id is 0 - 4.
        :Input:
            self: The Vertex class.
            id: The ID of the vertex. Numbers 0 - 4 will be people. 5 is Restaurant. 
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        self.vertex_id = id             # Create an instance variable to store the ID of the vertex.
        self.demand = 0                 # Create an instance variable to represent the demand (incoming - outgoing) of the vertex.
        self.total_incoming_flow = 0    # Instance variable representing the total flow incoming to the vertex.
        self.total_outgoing_flow = 0    # Instance variable signifying the total flow going out of the vertex.
        self.edges = []                 # Initially sets the edges to be an empty list. Will contain Edge objects.
        self.reverse_edges = []         # Will contain the Edge objects with their starting and end id reversed.
        self.visited = False            # An instance variable, when set to True will skip checking the edges.
        self.is_breakfast = False       # A boolean which will be False if the vertex is the breakfast.
        self.is_dinner = False          # A boolean which will be True if the vertex is the dinner.
        self.parent = None              # A parent vertex indicating the previous vertex this vertex is connected to.
        self.parent_edge = None         # A parent edge indicating the edge connecting the previous vertex and this vertex.
        self.parent_reverse_edge = None # The reverse edge of the parent edge.
        self.has_done = []              # A list which will contain booleans of length n days. If person has done a meal for
                                        # that day, it will be True.
    
    def append_edges(self, edge, is_reverse: bool = False) -> None:
        """
        Takes an edge object in the input and append them to the edges instance variable list. If the reverse edge is given,
        append it to the reverse_edges instance variable list instead.
        :Input:
            self: The Vertex class.
            edge: An Edge object representing the connections the vertex has.
            is_reverse: A boolean to determine whether to add edges to edges instance var or reverse_edges. Set to False by default.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        # If the edge is not reversed (normal edge), add it to edges list.
        if not is_reverse:
            self.edges.append(edge)         # Append the input edge into the edges instance variable list.
        # Else if the edge is reversed, add it to the reverse_edges list.
        else:
            self.reverse_edges.append(edge) # Append the input reverse_edge into the reverse_edges list.


# Edge class containing a tuple of the starting and end vertexes, their lower bound and capacity.
class Edge:
    def __init__(self, u: int, v: int, l: int, c: int) -> None:
        """
        Constructor to construct the Edge object connecting from one vertex to another, with their lower bound, flow and
        capacity.
        :Input:
            self: The Edge class.
            u: The starting ID of the edge.
            v: The ending ID of the edge.
            l: The lower bound of the directed edge.
            c: The capacity of the edge.
        :Output: None.
        :Time complexity: O(1)
        :Space complexity: O(1)
        """
        self.starting_id = u    # Create an instance variable to store the starting ID of the edge.
        self.ending_id = v      # Create an instance variable to store the ending ID of the edge.
        self.lower_bound = l    # Create an instance variable to store the minimum flow of the edge.
        self.flow = 0           # Create an instance variable representing the flow of the edge. Default sets to 0.
        self.capacity = c       # Create an instance variable representing the maximum flow of the edge.


# FlowGraph class which will create an network flow graph.
class FlowGraph:
    def __init__(self, availability: List[List[int]]) -> None:
        """
        Constructor to construct an adjacency list. Will utilise classes and objects to create a list of Vertex objects.
        Vertex id 0 - 4 representing each person and vertex 5 is the restaurant. Vertex 6 is the starting vertex and vertex
        7 is the ending vertex. Vertex 13 and 14 is the source and sink vertex respectively. Vertex 15 and above are all vertexes 
        for each meal with breakfast being first and then dinner in sequence. First the directed edges are added from the 
        Start to each person and the restaurant. Then, edges are added from each person to the meals they can cook based on 
        their availability. Then edges are added from the meals to the ending vertex with a lower bound of 1 and a capacity 
        of 1 as every day, every meal must be made.
        :Input:
            self: The FlowGraph class.
            availability: A nested list in which each list represents a person from 0 - 4 and each integer within the person's 
                          list represents the availability of the person to cook meals. 
                          0 means not free, 1 being able to cook for breakfast, 2 being for dinner and 3 for both.
        :Output: None
        :Time complexity: O(N) where n is the number of days
        :Aux space complexity: O(N) where n is the number of days
        """
        self.vertexes = []      # A list containing all the vertexes of the graph.

        number_of_days = len(availability)      # A variable signiyfying the number of days the meals have to be cooked for.
        amount_of_meals = number_of_days * 2    # A variable representing the amount of meals to cook. Days (n) * 2 as a day.
                                                # has 2 meals - breakfast and dinner.
        cooks = 6                               # There are 5 people tasked to cook meals and 1 restaurant.

        total_vertexes = amount_of_meals + cooks + 5 + 4   # The total amount of vertexes to create.
        
        # For loop to create all the cooks (people and restaurant) in the graph.
        # O(2N) time complexity
        for i in range(total_vertexes):
            self.vertexes.append(Vertex(i))
        
        # Create edges from Starting Vertex.
        start_vertex = self.vertexes[6]
        # Calculate the lower bound and upper bound for the people.
        lower_bound_people = math.floor(0.36 * number_of_days)
        capacity_people = math.ceil(0.44 * number_of_days)

        # For loop to set the directed edges of each person.
        for i in range(5):
            # Create the edge object from start to person and append it to the starting vertex edges list.
            each_person = Edge(6, i, lower_bound_people, capacity_people)
            reverse_each_person = Edge(i, 6, 0, 0)                  # Reverse the each_person Edge.
            start_vertex.append_edges(each_person)                  # Add the normal edge to the edges list.
            start_vertex.append_edges(reverse_each_person, True)    # Add the reverse edge to the reverse_edges lsit.
            # Set the total incoming flow of each person to the person lower bound.
            self.vertexes[i].total_incoming_flow += lower_bound_people
            # Set the total outgoing flow of the start vertex to the total lower bound for each vertex going out of the start.
            start_vertex.total_outgoing_flow += lower_bound_people
            # Sets the visited instance var to True so the edge checking for each people will be skipped.
            self.vertexes[i].visited = True
            
            self.vertexes[i].has_done = [False] * number_of_days    # Create a list of False of length n indicating if person
                                                                    # has made a meal that day.

        # Create the edges from start to restaurant and update demand.
        capacity_restaurant = math.ceil(number_of_days * 0.1)       # Calculate the upper bound for the restaurant.
        start_to_restaurant = Edge(6, 5, 0, capacity_restaurant)    # Create the edge object for the start to restaurant.
        reverse_restaurant = Edge(5, 6, 0, 0)                       # Create the reverse edge object from restaurant to start.
        start_vertex.append_edges(start_to_restaurant)              # Append the edge object to the starting vertex.
        start_vertex.append_edges(reverse_restaurant, True)         # Append the reverse edge object.
        start_vertex.demand = -2 * number_of_days                   # Change the demand of the start vertex to -2n.
                                                                    # where n is the number of days.
        
        restaurant = self.vertexes[5]                               # Get the restaurant vertex object.
        # For loop to loop through every meal to add an edge from restaurant to every meals.
        # O(2N) time complexity
        for meal in range(15, len(self.vertexes)):
            restaurant_to_meal = Edge(5, meal, 0, 1)                # Create the edge from restaurant to every meal.
            reverse_restaurant_meal = Edge(meal, 5, 0, 0)           # Create the reverse edge from eveery meal to restaurant.
            restaurant.append_edges(restaurant_to_meal)             # Append the edge object to the restaurant vertex.
            restaurant.append_edges(reverse_restaurant_meal, True)  # Append the reverse edge object.
        
        self.vertexes[5].visited = True     # Sets the resturant visited to True so it will be skipped during edge checking.
        k = 0                               # Set k to 0 which will be use to set vertex id for meals.
        
        # Create edges from people and restaurant to meals for each day. Will only create edges if the person can cook that
        # meal on that day.
        # O(N) time complexity
        for i in range(number_of_days):
            day = availability[i]       # Get the inner list which is the day.
            
            # For loop to each person availability for that day and set their edges to breakfast or dinner based on their 
            # availability for that day.
            for j in range(len(day)):
                person_availability = day[j]    # Get the availability of each person on that day.
                person = self.vertexes[j]       # Get the person Vertex Object based on their ID.       

                # Set the edges of the person based on their availability to cook certain meals.
                # If the person can cook breakfast.
                if person_availability == 1:
                    edge_to_breakfast = Edge(j, 15 + k, 0, 1)   # Create the edge object from every person to breakfast vertex.
                    reverse_breakfast = Edge(15 + k, j, 0, 0)   # Create the reverse edge object for every breakfast vertex.
                    # Append edges and reverse edges.
                    person.append_edges(edge_to_breakfast)
                    person.append_edges(reverse_breakfast, True)
                    self.vertexes[15 + k].is_breakfast = True   # Set is_breakfast to True to signify the vertex is a breakfast meal.
                
                # Else if the person can cook dinner.
                elif person_availability == 2:
                    edge_to_dinner = Edge(j, 16 + k, 0, 1)      # Create the edge object from every person to dinner vertex.
                    reverse_dinner = Edge(16 + k, j, 0, 0)      # Create the reverse object for every dinner vertex.
                    # Append edges and reverse edges.
                    person.append_edges(edge_to_dinner)
                    person.append_edges(reverse_dinner, True)
                    self.vertexes[16 + k].is_dinner = True      # Set is_dinner to True to signify the vertex is a dinner meal.
                
                # Else if the person can cook both.
                elif person_availability == 3:
                    edge_to_breakfast = Edge(j, 15 + k, 0, 1)   # Create the edge object from every person to breakfast vertex.
                    reverse_breakfast = Edge(15 + k, j, 0, 0)   # Create the reverse edge object for every breakfast vertex.
                    # Append edges and reverse edges.
                    person.append_edges(edge_to_breakfast)
                    person.append_edges(reverse_breakfast, True)
                    self.vertexes[15 + k].is_breakfast = True   # Set is_breakfast to True to signify the vertex is a breakfast meal.
                    
                    edge_to_dinner = Edge(j, 16 + k, 0, 1)      # Create the edge object from every person to dinner vertex.
                    reverse_dinner = Edge(16 + k, j, 0, 0)      # Create the reverse object for every dinner vertex.
                    # Append edges and reverse edges.
                    person.append_edges(edge_to_dinner)
                    person.append_edges(reverse_dinner, True)
                    self.vertexes[16 + k].is_dinner = True      # Set is_dinner to True to signify the vertex is a dinner meal.
            
            k += 2  # Increment k by 2 so that breakfast vertex id would skip the dinner id and vice versa.
        
        # For loop to add the edges from each breakfast and dinner to the end vertex.
        # O(2N) time complexity
        for meal in range(amount_of_meals):
            specific_meal = self.vertexes[15 + meal]    # Gets the Vertex object for the meals.
            meal_to_end = Edge(15 + meal, 7, 1, 1)      # Create the edge object from every meal to end vertex.
            reverse_end = Edge(7, 15 + meal, 0, 0)      # Create the reverse edge object frrom end to every meal vertex.
            
            # Append edges and reverse edge objects for every meal.
            specific_meal.append_edges(meal_to_end)
            specific_meal.append_edges(reverse_end, True)
            
            specific_meal.total_outgoing_flow += 1      # Decrement the total outgoing flow for each meal.
            
        # Change the demand and total incoming flow for the end vertex to be 2n where n is the number of days.
        end_vertex = self.vertexes[7]               # Get the end vertex object.
        end_vertex.demand = 2 * number_of_days      # Changes demand to 2n since total 2n flowing in.
        end_vertex.total_incoming_flow = 2 * number_of_days     # Changes total incoming flow to 2n also as each meal has a 
                                                                # lower bound of 1 so every meal needs to be cooked every day.
        end_vertex.visited = True                   # Skip checking edges of end vertex.
        
    def reduce_graph(self) -> None:
        """
        A function to reduce the Network Flow Graph with lower bounds into a circulation with demand problem. How it does this is
        by updating the demand for every vertex using the formula d(x)' = d(x) - (Lin - Lout) where d(x) is the original demand and
        Lin is the total lower bound flow going into the vertex whereas Lout is the total lower bound flow exiting the vertex.
        Change the demand to this newly calculated demand. Then every vertex edge capacity is updated to remove the lower bound.
        This is done by subtracting the capacity with the lower bound and setting that as the new capacity.
        :Input:
            self: The Network Flow Graph object.
        :Output: None
        :Time complexity: O(n^2) where n is the number of days.
        :Aux space complexity: O(1)
        """
        # For loop to loop through every vertex in the Flow Graph to update demand and capacities.
        # O(n) time complexity.
        for vertex in self.vertexes:
            # Update the new demand after removing the lower bound.
            vertex.demand -= (vertex.total_incoming_flow - vertex.total_outgoing_flow)
            # If the vertex has not been visited yet.
            if not vertex.visited:
                # Loop through every edge for each vertex to update capacity.
                for edge in vertex.edges:
                    edge.capacity -= edge.lower_bound   # Subtracts the lower bound from the capacity to reduce graph.
                vertex.visited = True       # Set the vertex to be visited so it will not be checked again.
                
    def add_source_and_sink(self) -> None:
        """
        A function to connect all vertexes with negative demand to the source so that their demand will be negated and turn to 0.
        All vertexes with positive demand is also connected to the sink so that their demand will turn to 0. This ensures that the
        law of flow conservation is maintained in which the total incoming flow == total outgoing flow.
        :Input:
            self: The Network Flow Graph object.
        :Output: None
        :Time complexity: O(n) where n is the number of days.
        :Aux space complexity: O(1)
        """
        # For loop to loop through every vertex in the flow graph to connect negative demands to source and positive demands to 
        # sink so that demands will all be 0.
        # O(n) time complexity.
        for vertex in self.vertexes:
            demand = vertex.demand          # Get the demand of the vertex.
            vertex_id = vertex.vertex_id    # Get the vertex id of the vertex object.
            source_vertex = self.vertexes[13]     # Get the vertex object for the source
            
            # If negative demand, create the Edge object and connect it to source
            if demand < 0:
                source_edge = Edge(13, vertex_id, 0, -demand)   # Change capacity to demand so new demand is 0.
                reverse_source = Edge(vertex_id, 13, 0, 0)      # Create a reverse edge from id current vertex to source vertex.
                source_vertex.append_edges(source_edge)         # Append normal edges to the vertex edges list.
                source_vertex.append_edges(reverse_source,True) # Append the reverse edges to the reverse_edges list.
                vertex.demand = 0                               # Update the demand to be 0 as incoming flow == outgoing flow.
                
            # Else if positive demand, create an Edge object and connect it to sink.
            elif demand > 0:
                sink_edge = Edge(vertex_id, 14, 0, demand)      # Change capacity to demand so new demand is 0.
                reverse_sink = Edge(14, vertex_id, 0, 0)        # Create a reverse edge from id sink vertex to current vertex.
                vertex.append_edges(sink_edge)                  # Append normal edges to the vertex edges list.
                vertex.append_edges(reverse_sink, True)         # Append the reverse edges to the reverse_edges list.
                vertex.demand = 0                               # Update the demand to be 0 as incoming flow == outgoing flow.

    def reset_instance(self) -> None:
        """
        A function to reset every single vertex in the graph 'visited' instance variable to False.
        :Input:
            self: The Network Flow Graph object.
        :Output: None
        :Time complexity: O(n) where n is the number of days.
        :Aux space complexity: O(1)
        """
        # Loops through every single vertex and reset the visited instance variable to False.
        for vertex in self.vertexes:
            vertex.visited = False
    
    def has_done_meal(self, person_vertex: Vertex, next_vertex: Vertex, set_day: bool = False) -> bool:
        """
        
        A function to reset every single vertex in the graph 'visited' instance variable to False.
        :Input:
            self: The Network Flow Graph object.
            person_vertex: The vertex object of the person.
            next_vertex: The vertex object of the meal. Should either be breakfast or dinner.
            set_day: A boolean on whether to set whether the person has done a meal on that day or not in the has_done list.
        :Output: 
            has_done_meal: A boolean to indicate if the person has done a meal for that day yet. True if has, False otherwise.
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        has_done_meal = False    # True if person have done a meal on that day, False otherwise.
        
        # Check whether the input vertex is a person, if not always return False as vertex is not a person.
        # Else check if the person have done a meal on that day.
        if person_vertex.vertex_id in [0, 1, 2, 3, 4]:
            # If the next vertex is breakfast, subtract id by 15 to find the day it's being done.
            if next_vertex.is_breakfast:
                day = (next_vertex.vertex_id - 15) // 2 # Get the index for the day.
                has_done_meal = person_vertex.has_done[day]  # Get the boolean on whether the person has down the meal on that day.
                # If to set the has_done to True on that specific day,
                if set_day:
                    person_vertex.has_done[day] = True  # Set to True as the person has done a meal on that day.

            # Else if it's dinner, subtract id by 16 to find the day it's being done.
            elif next_vertex.is_dinner:
                day = (next_vertex.vertex_id - 16) // 2 # Get the index for the day.
                has_done_meal = person_vertex.has_done[day]  # Get the boolean on whether the person has down the meal on that day.
                # If to set the has_done to True on that specific day,
                if set_day:
                    person_vertex.has_done[day] = True  # Set to True as the person has done a meal on that day.
                    
        return has_done_meal     # Return the boolean on whether the person has done the meal on that day.
    
    def DFS_recursive(self, start_vertex: Vertex, minn = math.inf) -> int:
        """
        A recursive function that will perform Depth-First Search from source to sink, finding the minimum capacity of a path
        and then updating all edges' capacities on that path with the minimum. If sink is not found, return 0 and terminate
        the DFS loop.
        :Input:
            self: The Network Flow Graph object.
            start_vertex: The starting vertex to start traversal at. Will be the source vertex.
            minn: The minimum capacity of a path. Default set to +infinite so all values will be lower than it.
        :Output: 
            minn: The minimum capacity of a path. Will be 0 if no existing path found.
        :Time complexity: O(n) where n is the number of days.
        :Aux space complexity: O(1)
        """
        start_vertex.visited = True     # Mark the vertex to be visited so it will not be visited again.      
        
        # Base case, if the current vertex is the sink, return the minimum capacity found.
        if start_vertex.vertex_id == 14:
            self.maximum_flow += minn
            return minn
        else:
            # Loop through the adjacent vertex and keep traversing down a path until reaching sink.
            for i in range(len(start_vertex.edges)):
                edge = start_vertex.edges[i]                    # Gets the edge object.
                reverse_edge = start_vertex.reverse_edges[i]    # Get the reverse edge object.
                next_vertex = self.vertexes[edge.ending_id]     # Get the next vertex.
                
                # If the vertex has not been visited yet and an edge exist, continue traversal until reaching sink.
                # Once sink is reached, get the minimum capacity and update capacity of the edges of that path with the minimum.
                if not next_vertex.visited and edge.capacity > 0 and not self.has_done_meal(start_vertex, next_vertex):                 
                    minimum = self.DFS_recursive(next_vertex, min(minn, edge.capacity))    # Call the DFS recursive to continue traversal.
                    # If path reach sink (minimum found), update edges capacity.
                    if minimum:
                        edge.capacity -= minimum            # Subtract the edge capacity with the minimum.
                        reverse_edge.capacity += minimum    # Add the reverse edge capacity with the minimum.
                        self.maximum_flow += minimum        # Add the maximum flow with the minimum.
                        
                        # Set has_done_meal to True as the person has done a meal for that day so he will nto do both meals
                        # on the same day.
                        self.has_done_meal(start_vertex, next_vertex, True)
                        
                        return minimum                      # Return the minimum capacity back to recursive caller.
                
        return 0    # Else if path does not reach sink, return 0.
    
    def DFS(self) -> int:
        """
        A Depth-First search algorithm that will continously call DFS_recursive to find all possible paths from source to sink.
        Will reset all vertexes visited instance variable before restarting the traversal from source. When a valid path is
        found, update the capacity of the edge and reverse edge with the minimum capacity of that path.
        :Input:
            self: The Network Flow Graph object.
        :Output: 
            minn: The minimum capacity of a path. Will be 0 if no existing path found.
        :Time complexity: O(n) where n is the number of days.
        :Aux space complexity: O(1)
        """
        self.reset_instance()                       # Reset all visited instance variables for every vertex back to False.
        
        source_vertex = self.vertexes[13]           # Index the source vertex object.
        
        return self.DFS_recursive(source_vertex)    # Start from the source vertex.
    
    def is_feasible(self) -> bool:
        """
        Checks the flow and capacity of the outgoing edges from source are equal. This is due to the Law of Flow Conservation 
        in which the total incoming flow should be equal to the total outgoing flow. If they are not equal, the problem 
        is not feasible to solve hence return False.
        :Input:
            self: The Network Flow Graph object.
        :Output:
            feasible: A boolean that represents whether the problem is feasible to solve or not. True if it is, False if not.
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        # Get the source vertex object from the vertexes adjacency list.
        source_vertex = self.vertexes[13]
        feasible = True     # The feasibility of the problem. True if it is solveable, False otherwise.
        
        # Loop through all the edges of the source, if the outgoing edges' flow is not equal to the capacity, the problem
        # is not feasible hence return False.
        for edge in source_vertex.edges:
            # If the flow is not equal to capacity.
            if edge.flow != edge.capacity:
                feasible = False    # Not feasible so return False.

        return feasible     # Return the boolean variable representing the feasibility of this problem.

    def ford_fulkerson(self) -> bool:
        """
        Perform Ford Fulkerson on the network flow graph. First, find all paths from source to sink using DFS. When a path is 
        found, modify the capacity with the minimum flow for each edge and reverse edge in a path. Once all paths from source
        to sink has been visited, return whether the problem is feasible or not.
        :Input:
            self: The Network Flow Graph object.
        :Output:
            feasible: A boolean that represents whether the problem is feasible to solve or not. True if it is, False if not.
        :Time complexity: O(n^2) where n is the number of days.
        :Aux space complexity: O(1)
        """
        self.maximum_flow = 0  # The maximum flow of the network flow graph. Should be equal to 2n where n is the number of days.
        # Call DFS repeatedly to find all possible paths.
        # O(n^2)
        while self.DFS():
            pass
        
        return self.is_feasible()   # Return on whether the problem is feasible to solve or not.
        
    def update_flow(self) -> None:
        """
        A function to loop through every edge in the Network Flow Graph to update the flow and capacity by augmenting the flow
        and removing the reverse edges. Once flow has been augmented, add the lower bound back to the flow and capacity of every
        edge.
        :Input:
            self: The Network Flow Graph object.
        :Output: None
        :Time complexity: O(n^2) where n is the number of days.
        :Aux space complexity: O(1)
        """
        # Loop through every vertex to update the flow of every edge.
        # O(n) time complexity ignoring cost within loop.
        # O(n^2) time complexity including cost within loop.
        for vertex in self.vertexes:
            # Loop through every edge of the vertex to update the flow to include lower bound.
            
            # O(n^2) time complexity
            for i in range(len(vertex.edges)):
                edge = vertex.edges[i]                  # Get the edge object.
                reverse_edge = vertex.reverse_edges[i]  # Get the reverse edge object.
                
                edge.flow = reverse_edge.capacity       # Augment the flow by setting it as the reverse edge capacity.
                edge.capacity += reverse_edge.capacity  # Update the capacity by adding it with the reverse edge capacity.
                reverse_edge.capacity = 0               # Set the reverse edge capacity to 0 to "remove" it.
                
                # Update the flow and capacity of the edge to include the lower bound.
                edge.flow += edge.lower_bound           # Add the lower bound to flow.
                edge.capacity += edge.lower_bound       # Add the lower bound to capacity.
            
    
def determine_meal(breakfast: List[Union[int, None]], dinner: List[Union[int, None]], next_vertex: Vertex, person_id: int) -> int:
    """
    Checks whether the vertex is a breakfast or dinner vertex. Arithmetically calculate the day from the breakfast or dinner
    id and set the person responsible for creating that meal into the breakfast or dinner list, returning it back to caller.
    :Input:
        breakfast: The list of length number of days which will contain people ID for each day they are responsible for making breakfast.
        dinner: The list of length number of days which will contain people ID for each day they are responsible for making dinner.
        next_vertex: The vertex object to check whether it's breakfast or dinner.
        person_id: The ID of the person responsible in making the meal.
    :Output: 
        breakfast: The list of people responsible in making breakfast. Will now have the person_id added to it at their respective day to cook meals.
        dinner: The list of people responsible in making dinner. Will now have the person_id added to it at their respective day to cook meals.
    :Time complexity: O(1)
    :Aux space complexity: O(1)
    """
    # If it's breakfast, subtract id by 15 to find the index for the breakfast list.
    if next_vertex.is_breakfast:
        meal_id = (next_vertex.vertex_id - 15) // 2
        breakfast[meal_id] = person_id  # Set the person for who cooks breakfast on that day.
    # Else if it's dinner, subtract id by 16 to find the index for the dinner list.
    elif next_vertex.is_dinner:
        meal_id = (next_vertex.vertex_id - 16) // 2
        dinner[meal_id] = person_id     # Set the person for who cooks dinner on that day.
    
    return breakfast, dinner            # Return the list with the index of the person cooking that meal now set.
        
def find_schedule(flow_graph: FlowGraph, num_of_days: int) -> Tuple[List[int], List[int]]:
    """
    Function used to find the schedule for every person on which day who is in charge of making breakfast or dinner.
    Will travel through edges with 1 flow until either breakfast or dinner is found. Finds out whether if the vertex is
    breakfast or dinner to allocate the responsibility of making the meal for the respective person.
    :Input:
        flow_graph: The Network Flow Graph object.
        num_of_days: THe number of days to find the schedule for each person for.
    :Output: 
        (breakfast, dinner): The breakfast and dinner lists paired together into a tuple.
    :Time complexity: O(n) where n is the number of days.
    :Aux space complexity: O(n) where n is the number of days.
    """
    breakfast = [None] * num_of_days    # Create a list filled with Nones with each index representing a single day.
    dinner = [None] * num_of_days       # Does the same as breakfast list but instead for dinner.
    
    # Loop through all 5 people + restaurant to find the days they can cook and what meal.
    for person in range(6):
        current_vertex = flow_graph.vertexes[person]                # Get the current vertex.
        # Loop through every edge connected to that person_vertex.
        # O(n) time complexity.
        for edge in current_vertex.edges:
            # If there's a flow, continue going through the edge until meal.
            if edge.flow == 1:
                next_vertex = flow_graph.vertexes[edge.ending_id]   # Get the next vertex it's connected to.
                # Call the determine_meal to find out which meal is the person doing and to set their schedule in the breakfast
                # and dinner graph.
                breakfast, dinner = determine_meal(breakfast, dinner, next_vertex, person)
                        
    return (breakfast, dinner)  # Pair the 2 list together into a tuple 
    
def allocate(availability: List[List[int]]) -> Union[Tuple[List[int], List[int]], None]:
    """
    This function will first create a Network Flow Graph for each person and meal. The starting vertex will have edges going
    to each person with a lower bound of 0.33n floor and capacity of 0.44n ceiling. This ensures that each person can do no
    less than 0.33n floor meals and no more than 0.44n ceiling meals. The start is also connected to the restaurant vertex
    with a capacity of 0.1n ceiling. Then, the flow network is reduced, removing all the lower bounds from the graph and 
    updating demand for each vertex accordingly. With that, we have transformed our problem into a circulation demands problem. 
    Then, add the source, with all vertexes with negative demand connecting to it and the sink with all vertexes with positive
    demand connecting to it. This is done to ensure that all vertexes' demands will be 0. Once this is done, perform Ford
    Fulkerson to find minimum cut and maximum flow. During Ford Fulkerson, augment the flow of each edge to the minimum capacity
    of the path. After that, update the flow and capacity of each edge by re-adding the lower bound back into the graph.
    With that, we can just traverse through the graph, going from person to meals by following edges with flow of 1 to determine
    the schedule of cooked meals for each person.
    :Input:
        availability: A nested list in which each list represents a person from 0 - 4 and each integer within the person's 
                      list represents the availability of the person to cook meals. 
                      0 means not free, 1 being able to cook for breakfast, 2 being for dinner and 3 for both.
    :Output: 
    :Time complexity: O(n^2) where n is the number of days.
    :Aux space complexity: O(n) where n is the number of days.
    """
    flow_graph = FlowGraph(availability)    # Create the flow graph based on the input availability list.
    
    # Reduce the graph by removing the lower bounds. Will also subtract the capacity with the lower bounds.
    # O(n^2) time complexity
    flow_graph.reduce_graph()
    
    # Add source and sink vertexes, connecting all negative demands to source and positive demands to sink to remove demands
    # so that the flow conversion is maintained. Demand for every vertex will now be 0.
    # O(n) time complexity
    flow_graph.add_source_and_sink()
    
    # Call the ford_fulkerson function to determine flow of each edge.
    # If the problem is not feasible to solve, return None.
    # O(n^2) time complexity
    if not flow_graph.ford_fulkerson():  
        # return None
        pass
    
    # O(n^2) time complexity
    flow_graph.update_flow()    # Call the update_flow function to reinclude the lower bound back into the graph by summing 
                                # it with the flow and capacity of every edge.
    
    # O(n^2) time complexity
    schedule = find_schedule(flow_graph, len(availability))     # Find the schedule to set the meals

    return schedule     # Return the tuple of breakfast and dinner paired together. 


# Question 2
# A Node class
class Node:
    def __init__(self, index: List[int] = [], is_sub1 = False, is_sub2 = False) -> None:
        """
        The Node constructor. Will take in a list of indexes [start, stop] which points to the substring from the root to that 
        node or from the branching node to that node. The Node object will have an linked list, pointing to all other children
        nodes based on their index.
        :Input:
            self: The Node object.
            index: index is a fixed list of length 2 which will contain the start and stop index of a substring. Default set to empty.
            is_sub1: A boolean to determine if the node's substring is a subset of submission1.
            is_sub2: A boolean to determine if the node's substring is a subset of submission2.
        :Output: None
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        self.index_range = index    # The start and ending index of a substring.
        self.link = [None] * 29     # A list pointing to all possible connections starting from '$', '%', ' ', 'a', 'b' ... 'z'.
                                    # Will contain 27 letters in the alphabet, one '$' terminal symbol, one '%' and one space.
        self.parent = None          # An instance variable which points to the previous parent Node of the current Node.
        self.child = None           # An instance variable which points to the next child Node of the current Node.
        self.is_sub1 = is_sub1      # An instance variable to indicate that the string/word is from submission1. Default False
        self.is_sub2 = is_sub2      # An instance variable to indicate that the string/word is from submission2. Default False
        
# A Suffix Tree class.
class SuffixTree:
    def __init__(self, combined_sub: str, len_sub1: int, len_sub2: int) -> None:
        """
        The Suffix Tree Constructor. Will take in the combined string of submission1 and submission2 with submission1 having a 
        '$' sign at the end and submission2 having a '%' symbol at the end. First, we will loop through each character in the
        combined string, with i being the first pointer. i will go from the start of the combined_str to the end. 
        Then we have our second pointer, j, which will point from the start of a submission to the end. To find the end of the 
        specific submission,the find_length_of_submissions method is used which will find the index of the '$'/'%'. 
       
        In the while loop, j will be used to create all possible substrings of submission1 first then submission2. During the 
        creation process, it will get the character and find their respective index in the linked list.
        Then, it will check if the current_node linked list already constain the node for that character/index. If not, create 
        a new Node and put it in the linked list. If found, go to create_branch() method which will create a branching node as 
        shared substrings are found. 
        
        The create_branch method will keep traversing through the nodes, until either the end symbol is reached or a mismatched 
        substring is found. Then, create a branching node which will split into multiple branches. One will go to the
        previous old node that we reached just now, the other will create the new branching substring.
        
        While the tree is being created, update the is_sub1/is_sub2 instance variable for the nodes based on which submission
        their respective substrings belong to. Then, call the len_common, which will find the longest common substring and
        store the indexes for it.
        
        Refered from: https://www.cs.jhu.edu/~langmea/resources/lecture_notes/suffix_trees.pdf from slide 10 onwards.
        :Input:
            self: The Suffix Tree object.
            combined_sub: A string with submission1 and submission2 concatenated together with '$' and '%' at the end of both strings respectively.
            len_sub1: The length of submission1 from 0 to the '$' symbol.
            len_sub2: The length of submission2 from 0 to the '%' symbol.
        :Output: None
        :Time complexity: O((N + M)^2) where N is the length of characters in submission1 and M is the length of characters in submission2.
        :Aux space complexity: O(N + M) where N is the length of characters in submission1 and M is the length of characters in submission2.
        """
        # Initialise the root node of the Suffix Trie. Will set is_sub1/sub2 to True as both submissions share the root.
        self.root = Node(is_sub1=True, is_sub2=True)   
        self.combined_str = combined_sub    # Initialise the Combined Submissions input as an instance variable.
        self.len_sub1 = len_sub1            # Set the length of submmision1 instance variable.
        self.len_sub2 = len_sub2            # Set the length of submmision2 instance variable.
        
        self.longest_len = 0                # The longest length of the largest common substring. Initially set to 0.
        self.largest_common = []            # The largest common substring Node object. Will contain a list of substring nodes.
        
        # Loop through each character in the combined_sub input.
        for i in range(len(combined_sub)):
            current_node = self.root    # current_node initially will be the root of the SuffixTree.
            
            # Find the length of the current submission the for loop is at.
            len_of_subs = self.find_length_of_submissions(i, len_sub1, len_sub2)
    
            j = i   # A pointer to find all the substrings from j to end of the submission string.
            # While the current pointer has not reached the end of the substring, '$' or '%'.
            while j < len_of_subs:
                char = combined_sub[j]                  # Get the character from the combined string.
                index = self.get_index(char)            # Get the index of the character for the Node's linked list.
                next_node = current_node.link[index]    # Get the next node according to the current node's linked list.
                
                # If the current node's link has a Node object in it, means node exist and thus create a branch in the Tree.
                if next_node is not None:
                    j, current_node = self.create_branch(current_node, next_node, i, j, len_of_subs, index)
                # Else if the next node does not exist, create it.
                else:
                    # Create the next node with the index to be True or False based on whether it exist in submission1 string
                    # or submission2 string.
                    current_node.link[index] = Node([j, len_of_subs])
                    current_node.link[index].parent = current_node  # Set the parent of the child to be the current node.
                    
                # If the current node is not the root.
                if current_node != self.root: 
                    # Update the is_sub1 or is_sub2 based on whether they are part of submission1 or submission2 substrings.
                    self.update_common(current_node, i, len_sub1)

                    # Find the length of the substring. If the length of the substring is larger, set the longest_len to the
                    # that and the largest_common to the node.
                    self.len_common(current_node)
               
    def find_length_of_submissions(self, current_len: int, len_sub1: int, len_sub2: int) -> int:
        """
        This method will compare the length of the current length with the length of submission 1 (0 to '$' symbol). If the
        current index/length is within the submission 1, return the length of submission1 to set that as the index for the j
        pointer to stop.
        Else, it's in submission 2 (> len_sub1), return the length from 0 to '%' symbol instead.
        :Input:
            self: The Suffix Tree object.
            current_len: The current length/current index of the i pointer.
            len_sub1: The length of submission1 from 0 to '$' symbol.
            len_sub2: The length of submission2 from 0 to '%' symbol.
        :Output: 
            len_sub: Will return either the length of submission1 or submission2 based on the current_len.
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        # If the current length of the string is less than the '$' sign, means it is within submission1. So return the
        # len_sub as the length of submission1 so that the suffix tree will no where one submission ends and another begins.
        if current_len < len_sub1:
            len_sub = len_sub1
        # Else, the len_sub is returned as the length of submission2 instead as it is currently in the submission2 string.
        else:
            len_sub = len_sub2
            
        return len_sub  # Return either the length of submission1 or submission2.
    
    def get_index(self, char: str) -> int:
        """
        A function that takes in a character and find their respective index in the Node's linked list. If it's the special
        character '$', index is 0. If special character is '%', the index is 1. If character is a space, index is 2. 
        If alphabets, convert it to ASCII and minus 94 to get the index. 'a' will be 3, 'b' will be 4 etc.
        :Input:
            self: The Suffix Trie object.
            char: A string to calculate the index for it in the link.
        :Output:
            char_index: The index pointing to where the character is stored within a Node's linked list.
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        # If character is the end symbol for submission1, index is 0.
        if char == "$":
            char_index = 0
        # Else if character is end symbol for submission2, index is 1.
        elif char == "%":
            char_index = 1
        # Else if character is space, index is 2.
        elif char == " ":
            char_index = 2
        # Else, get the ASCII of the alphabetical character and subtract it with 95 to get the index.
        else:
            char_index = ord(char) - 94
            
        return char_index   # Return the index in where the character is suppose to be stored in the link list.
    
    def update_common(self, current_node: Node, index: int, len_sub1: int) -> None:
        """
        A method to update the is_sub1/is_su2b instance variable. Will check the current index the i pointer is at and the
        the len_sub1. Similarly to the find_length_of_submissions method, it will compare the current index and the len_sub1.
        If the current index is within the submission1, set the is_sub1 to True to signify the node is inside the submission1
        string. Else, it's submission2 so set the is_sub2 instead. Common substrings will have both set.
        :Input:
            self: The Suffix Tree object.
            current_node: The current node the SuffixTree is at as it traverse downwards from root to children.
            index: The current index the i pointer is at.
            len_sub1: The length of submission1 from 0 to '$' symbol.
        :Output: None
        :Time complexity: O(1)
        :Aux space complexity: O(1)
        """
        # If the index currently is within the submission1 string, set the is_sub1 to True to signify its in sub1.
        if index < len_sub1:
            current_node.is_sub1 = True
        # Else if the index currently is within the submission2 string, set the is_sub2 to True to signify its in sub2.
        else:
            current_node.is_sub2 = True
        
    def len_common(self, current_node: Node) -> None:
        """
        Will find the length of any common substring and store the largest. First it will take the current node input and 
        check the difference in the their index ranges. Will continue traversing upwards via the .parent instance var until
        either the root is reached or a non-common substring is reached. 
        
        Store the largest length in a instance variable. If any of the common substring length is larger than that, set the
        largest length to be that instead and store the index ranges into the largest_common list.
        :Input:
            self: The Suffix Tree object.
            current_node: The current node the SuffixTree is at as it traverse downwards from root to children.
        :Output: None
        :Time complexity: O(N + M) where N is the length of characters in submission1 and M is the length of characters in submission2.
        :Aux space complexity: O(N + M) where N is the length of characters in submission1 and M is the length of characters in submission2.
        """
        length_of_string = 0    # Initially set the length of the string to zero.
        largest_common = []     # The largest common nodes list.
        
        # If the current_node substring is a common substring, calculate and compare the length of the substring.
        # O(N + M) time complexity.
        while current_node != self.root and current_node.is_sub1 and current_node.is_sub2:
            # Find the length of the substring, adding it continously until either a non-common substring is found or the root
            # has been reached.
            length_of_string += (current_node.index_range[1] - current_node.index_range[0])
            largest_common.append(current_node.index_range)
            current_node = current_node.parent      # Set the current_node to the parent node to continue backward traversal.

        # IF the length of the substring is longer than the current longest length, set it as the longest.
        if length_of_string > self.longest_len:
            self.longest_len = length_of_string     # Set the longest length to the length of the string to continue comparison/
            self.largest_common = largest_common    # Set the largest_common instance var with our found indexes of substring.
            
    def create_branch(self, current_node: Node, next_node: Node, i: int, j: int, len_of_subs: int, index: int) -> int:
        """
        A function to create a branching node and set the branching paths of the substring. Will first continue traversing until
        either the end symbol is found or if a mismatched substring is found. If a end symbol is found, just return the k and
        current node. 
        
        Else if mismatched (not shared) substring is found, create a branching node at that part. Set the earlier node to a child
        of the branching node. Create a new node for the new mismatched substring and set that as the child of the branching node
        also within the linked list. Once done, update on whether these 3 nodes are a subset of submission1 or submission2 and also
        check if they have the largest common substring.
        :Input:
            self: The Suffix Trie object.
            current_node: The current node the SuffixTree is at as it traverse downwards from root to children.
            next_node: The child of the current node based on their linked list and the substring we are at.
            i: The i pointer which is the length of the whole combined string.
            j: The j pointer which is the length of either submission.
            len_of_subs: The current length of which submission we are at. If submission1, length from 0 to submission1. Else if submission2, length from 0 to submission2.
            index: The current index of where in the substring the method is at right now.
        :Output: 
            k: Return k which will be used as the second pointer j in the caller.
            current_node: Return the current node back to the caller.
        :Time complexity: O(N + M) where N is the length of characters in submission1 and M is the length of characters in submission2.
        :Aux space complexity: O(1)
        """
        index_range = next_node.index_range             # Get the starting and ending index for the substring of the next node.
        len_of_substr = index_range[1] - index_range[0] # Find the length of the substring by subtracting the starting and
                                                        # ending index of the substring.
        start = index_range[0]                          # The starting index of the substring.
        end = index_range[1]                            # The ending index of the substring.
        combined_str = self.combined_str                # Get the combined string to add to the suffix tree.
                                   
        k = j + 1       # Set k as the third pointer to know where to branch when traversing through the substring.
        # Keep traversing through each node until no more existing node exists, then create a branch if not at '$' or '%'.
        # O(N + M) time complexity.
        while ((k - j) < len_of_substr) and (combined_str[k] == combined_str[start + (k - j)]):
            k += 1      # Increment k pointer by one to continue the traversal until a node to branch off is found.
        
        # If we hae reached the end of the submission string ('$' or '%' found), means no branch is needed.
        # Return k which will be used as the second pointer j in the caller.
        if (k - j) == len_of_substr:
            current_node = next_node    # Change the current node to the next node.
            return k, current_node      # Return the third pointer as the second pointer.
        
        # Else, a branching path is needed as a mismatched substring is found.
        else:
            start_end_range = [start, start + (k - j)]      # The list index [start, end] for the branch node.
            # Create the branch node with the range of index. Set True or False based on whether the branch node is within
            # the length of the first or second submission.
            branch_node = Node(start_end_range)
            branch_node.parent = current_node               # Set the branch node parent to be the current node.
        
            # Update the branching node to have the same is_sub1 and is_sub2 as the next_node.
            branch_node.is_sub1 = next_node.is_sub1
            branch_node.is_sub2 = next_node.is_sub2
            
            # Get the previous index before the branch node was created and the index of the newly created node after the branch.
            previous_index = self.get_index(combined_str[start + (k - j)])
            branching_index = self.get_index(combined_str[k])

            # Replaces the branching node and current node linked list childrens.
            branch_node.link[previous_index] = next_node    # Sets the next node as the children of the branch node.
            current_node.link[index] = branch_node          # Replaces the children of the current node to the branching node.
            # Change the index range pointer of the next node to start from the branching node to the end.
            next_node.index_range = [start + (k - j), end]

            # Create the child of the branching node with the newly created subtring node from k to the end of submission string.
            # Set to True or False if they either belong to submission1 or submission2.
            branch_node.link[branching_index] = Node([k, len_of_subs])

            # Update the child of the branching node on whether it's part of the submission1 or submission2 or both.
            self.update_common(branch_node.link[branching_index], i, self.len_sub1)
            
            # Compare all 3 nodes, branching next_node and the new node to see which has the larger common substring.
            # O(N + M) time complexity.
            self.len_common(branch_node)                                # Check for branching node.
            self.len_common(next_node)                                  # Check for next node.
            self.len_common(branch_node.link[branching_index])          # Check for the children of the other branch node.
            
            # Set the parent of both the previous nodes and the new branching node to the branch_node.
            branch_node.link[previous_index].parent = branch_node
            branch_node.link[branching_index].parent = branch_node
            
        return j, current_node      # Return j to caller.
      
      
def round_half_up(num: float) -> int:
    """
    A function that takes in a float and rounds it up/down and then converts it into an integer before returning it.
    :Input:
        num: A float number to be round up and down based on its decimal point
    :Output: 
        num: A rounded up/down number which has been converted to an integer.
    :Time complexity: O(1)
    :Aux space complexity: O(1)
    """
    # Calculate the new integer number after rounding up or down. <= 0.4 round down whereas >= 0.5 round up.
    num = int(math.floor(num * 1 + 0.5) / 1)
    return num      # Return the rounded integer.
          
def compare_subs(submission1: List[str], submission2: List[str]) -> List:
    """
    A function to compare the similarity between two submissions strings. Will first create a suffix tree for all possible
    substrings for submission1 and submission2. Then it will check for which substrings that are both shared by submission1
    and submission2. It will compare the length and find the substrings which has the largest/maximum length.
    
    Once the largest substring is found, reconstruct the substring using the stored index ranges. Start from the root or the 
    first node of the common substring then keep concatenating the characters into a string to build the final largest common
    substring.
    
    Once the longest common substring is build, add it to the similarity list and calculate the percentage of the common substring
    over the total length of submission1 and submission2.
    :Input:
        submission1: A string to compare with the other and to find the longest common substring.
        submission2: A string to compare with the other and to find the longest common substring.
    :Output: 
        similarity_lst: A list of fixed length 3, with the first index containing the largest common substring, the 
    :Time complexity: O((N + M)^2) where N is the length of characters in submission1 and M is the length of characters in submission2.
    :Aux space complexity: O(N + M) where N is the length of characters in submission1 and M is the length of characters in submission2.
    """
    # Combine the two strings together with each submission seperated by their respective special character, '$' for submission1
    # and '%' for submission2 so they can be differentiated.
    combine_sub = "{0}${1}%".format(submission1, submission2)
    
    # Calculate the length of submission1 from 0 to '$' and submission2 from 0 to '%'.
    len_of_sub1 = len(submission1) + 1      # Find the length of substring1 including the '$' symbol at the end.
    len_of_sub2 = len(combine_sub)          # Find the length of substring2 including the '%' symbol at the end.
    
    # Create the Suffix Tree for both submissions in one.
    # O((N + M)^2) time complexity.
    suffix_tree = SuffixTree(combine_sub, len_of_sub1, len_of_sub2)

    # Get the lists of the largest common substring nodes' indexes.
    largest_commons_range = suffix_tree.largest_common
    
    # Create an empty string which will hold the largest common substrings.
    largest_common_str = ""
    
    # Loop through the lists of indexes from the back to the front to construct the largest common substring.
    # O(N + M) time complexity.
    for i in range(len(largest_commons_range) - 1, -1, -1):
        # Get the starting and stopping index of the largest common substring.
        start = largest_commons_range[i][0]
        end = largest_commons_range[i][1]  
        
        # Build up the largest common substring from start to end using the range of indexes.
        for j in range(start, end):
            largest_common_str += combine_sub[j]    # Concat each character from start to end to build the final string.
    
    # Create the similarity list which will contain the largest common substring, the percentage of the common substring with
    # submission1 and submission2 respectively.
    similarity_lst = [largest_common_str, 0, 0]
    
    # Get the length of the largest common substring to compare with the overall length of both submissions.
    length_of_common = len(largest_common_str)

    # If the input string length is zero, dont divide by 0.
    if len(submission1) > 0:
        # Get the percentage of the longest common substring with the overall length of the submissions.
        similarity_lst[1] = round_half_up((length_of_common / len(submission1)) * 100)
    if len(submission2) > 0:
        similarity_lst[2] = round_half_up((length_of_common / len(submission2)) * 100)
    
    return similarity_lst   # Return the similarity report lst 