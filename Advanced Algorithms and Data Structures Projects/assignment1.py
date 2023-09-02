from typing import List, TypeVar
T = TypeVar('T')  # Generic TypeHint variable. T can be for anything.
    

def create_count_list(size: int) -> List[T]:
    """
    A function which will accept an integer input size and will create an empty list of empty lists with length == size. 
    The teams would be stored within the nested lists based on their sorting order. For example, size = 3 will create a list
    [[], [], []].
    :Input:
        size: An integer which represents how many nested lists will be created/index or the outer list.
    :Output:
        count_lst: Returns an empty list of size size with each index representing one letter in the alphabet.
    :Time complexity: O(R) where r is roster since a list of length R is created. 
                      In this case, it will be O(1) as roster is treated as a constant.
    :Space compelxity: O(R) where r is roster since a list of length R is created. 
                       In this case, it will be O(1) as roster is treated as a constant.
    """
    # Creates an empty nested list with same length as size using list comprehension.
    # O(R) where R is roster/size. Since roster is constant - O(1) time and space complexity.
    count_lst = [[] for i in range(size)]
    
    return count_lst    # Returns newly created list with length roster.


def char_radix_sort(team_str: str, roster: int) -> str:
    """
    A function to sort the characters of the team in lexicographically order. Radix sort is used.
    :Input:
        team_str: An unsorted string representing the team's composition of characters.
        roster: An integer that indicates the maximum amount of characters a team can have. Eg: roster = 5 means each team can choose a character from the set {A, B, C, D, E}
    :Output:
        results: The input list is modified in which the team characters are sorted and returned to the caller.
    :Time complexity: O(M) where M is the number of characters in a team.
    :Space complexity: O(M) where M is the number of characters in a team
    """
    count_lst = create_count_list(roster)  # Call the create_count_list to initialize an empty nested list of length roster.
    
    # Update count list with the frequency a letter appears.
    # Loops through all the characters in a string of length M hence O(M) time complexity.
    for index in range(len(team_str) - 1, -1, -1):
        letter = team_str[index]                # Gets the character of the string from the desired index.
        letter_index = ord(letter) - 65         # Index for the given letter. Eg: A = 0, B = 1, C = 2 etc.
        count_lst[letter_index].append(letter)  # Add the characters into count_lst according to their sorted order.
        
    team_str = ""    # Clears the team_str to be an empty string.

    # For loop to loop through the count_lst and update original list with the new characters.
    # count_lst of length roster thus O(R) where R is roster. Since roster is constant - O(1) time complexity.
    for alphabets in count_lst:
        # For loop to update original list with said repeated string. 
        # In total will loop through M times so O(M) time complexity.
        for char in alphabets:
            team_str += char    # Adds the characters into team_str string in sorted order while keeping stability.
        
    return team_str  # Sorted string will be returned


def opposite_match(results: List[T]) -> List[T]:
    """
    A function that will loop through the results list, get the score and 100 - score is the team2 vs team1 score. For example
    a match of ['AAB', 'ABB', 30] will create a seperate match ['ABB', 'AAB', 70] and is appended to the results list.
    :Input: 
        results: A list of matches with team 1 vs team 2 and their respective score
    :Output:
        results: The orginal list modified with additional matches and their respective new scores.
    :Time complexity: O(N) where N is the number of matches.
    :Space complexity: O(N) where N is the number of matches.
    """
    new_match = []  # Creates an empty list.
    
    # For loop to loop through the results list and add new matches for team2 vs team1 with their score being (100 - team's 1 score)
    # Will loop through N times so O(N) time complexity. Will append a list of N length so O(N) space complexity.
    for match in results:
        new_match.append([match[1], match[0], (100 - match[2])])  # [Team 2, Team 1, score].
    
    return new_match    # Return a list with all the matches with their team order flipped with their respective scores.


def counting_sort(results: List[T], roster: int, index: int, column: int) -> List[T]:
    """
    A function that will sort an unsorted list in order using counting sort.
    :Input:
        results: A list of lists containing all the matches between 2 teams and their respective scores.
        roster: An integer that indicates the maximum amount of characters a team can have. Eg: roster = 5 means each team can choose a character from the set {A, B, C, D, E}.
        index: The index of the nested list in which the algorithm will sort.
        column: An integer specifying which column the radix sort will sort by.
    :Output:
        results: the original list, modified so that the matches are now sorted according to specifications.
    :Time complexity: O(N) where N is the length of the results list.
    :Space complexity: O(N) where N is the length of the results list. count_lst is treated as constant since it's length roster.
    """
    count_lst = create_count_list(roster)  # Call the create_count_list to initialize an empty nested list of length roster.
    
    # Update count list with the frequency an team appears. 
    # Loops through results of length N hence O(N) time complexity.
    for i in range(len(results)):
        match = results[i]      # Gets each match list.
        team = match[index]     # Gets the team from the results list based on input index.
        
        letter = team[column]                   # Gets the letter based on given column to sort.
        letter_index = ord(letter) - 65         # Index for the given letter. Eg: A = 0, B = 1, C = 2 etc.
        count_lst[letter_index].append(match)   # Add their match into count_lst according to the sorted order of the teams.
        
    # Initialise starter count_index to 0.
    count_index = 0

    # Loops through the count_lst and update original list with the new team positioned in sorted order.
    # Outer loop loops through count_lst which is O(1) time complexity since its bound by a constant <= 26.
    for i in range(len(count_lst)):
        count = len(count_lst[i])   # Frequency of team repeated in the original list.
            
        # For loop to update original list with said repeated string. 
        # Inner loop will loop in total N times hence O(N) time complexity.
        for j in range(count):
            results[count_index] = count_lst[i][j]    # Updates results list.
            count_index += 1                          # Increment count_index by 1 to add i to the next index of results
                                                      # in the next iteration.
                                                      
    return results  # Return sorted list based on desired index and column.
    

def radix_sort_list(results: List[T], roster: int, index: int) -> List[T]:
    """
    A function that will sort an unsorted nested list using radix sort. The algorithm will sort based on given input index.
    :Input:
        results: An unsorted list to be sorted via radix sort.
        roster: An integer that indicates the maximum amount of characters a team can have. Eg: roster = 5 means each team can choose a character from the set {A, B, C, D, E}.
        index: An integer specifying which index to sort the list with.
    :Outpu:
        results: The now sorted list based on input index.
    :Time complexity: O(NM) where N is the length of ther results list and M is the number of characters in a team
    :Space complexity: O(NM) where N is the length of ther results list and M is the number of characters in a team
    """
    # Initialise columns based on the length of the teams. Since teams are all same length, first team's length should be 
    # equal to all the other teams' length.
    column = len(results[0][0]) - 1
    
    # While loop to loop through each column of a team/score. 
    # Will loop M times, hence O(M) time complexity if ignoring cost within loop.
    # Total cost will be O(NM) since loop M times and call a function with a complexity of O(N).
    while column >= 0:
        results = counting_sort(results, roster, index, column)  # Sort the list of the specified index based on the specified column.
        column -= 1    # Decrement column by 1 to move towards the left bit. 

    return results  # Return sorted matches based on index input.


def count_sort_num(results: List[T]) -> List[T]:
    """
    A function that will sort an unsorted list of numbers in descending order using counting sort.
    :Input:
        results: An unsorted list to be sorted via counting sort
    :Output:
        results: The now sorted list of score in descending order
    :Time complexity: O(N) where N is the length of the results list
    :Space complexity: O(N) where N is the length of the results list. count_lst is treated as constant since it's lenght roster.
    """
    count_lst = create_count_list(101)  # Call the create_count_list to initialize an empty nested list of length roster.
                                        # O(1) since length of the list is bound by an upper constant <= 100.
    # Update count list with the frequency an team appears. 
    # Loops through results of length N hence O(N) time complexity.
    for i in range(len(results)):
        match = results[i]  # Gets each match list.
        score = match[2]    # Gets the score from each match
        
        count_lst[score].append(match)    # Add their match into count_lst according to the sorted order of the teams.
        
    # Initialise starter count_index to 0.
    count_index = 0

    # Loops through the count_lst backwards and update original list with the new team positioned in descending order.
    # Outer loop loops through count_lst which is O(1) time complexity since its bound by a constant <= 26.
    for i in range(len(count_lst) - 1, -1, -1):
        count = len(count_lst[i])   # Frequency of team repeated in the original list.
            
        # For loop to update original list with said repeated string. 
        # Inner loop will loop in total N times hence O(N) time complexity.
        for j in range(count):
            results[count_index] = count_lst[i][j]  # Updates results list.
            count_index += 1                        # Increment count_index by 1 to add i to the next index of results
                                                    # in the next iteration.
    
    return results      # Returns the matches list with their score sorted in descending order.


def remove_duplicates(results: List[T]) -> List[T]:
    """
    A function which will loop through the input results list and detect all unique matches, adding them into a new list.
    For example, an input list of [['ABB', 'AAB', 70], ['ABB', 'AAB', 70], ['ABB', 'BBB', 68], ['AAB', 'BBB', 67]] will
    first check the first match. The first match is the same as the second match, so it's skipped for now and in the next 
    iteration, the second match is checked against the third match. The second match is not the same as the third, so the third
    match will be added into the list since it's unique. Lastly, the third match is not the same as the last match so the last
    match is added.
    :Input:
        results: A list containing matches with potential duplicates of them. If found, they are removed.
    :Output:
        results: The now modified input list with all duplicate matches removed.
    :Time complexity: O(NM) where N is the number of matches and M is the number of characters in a team.
    :Space complexity: O(N) where N is the length of the results list.
    """
    res_lst = [results[0]]      # Create list to store the unique matches in. Add the first match inside.
    j = 0
    
    # While loop to loop through the results list and if no duplicates are found in the next match, append the unique matches into the res lst.
    # Will loop through a list of length N so O(N) time complexity without including the cost of string comparisons.
    # O(NM) in total if including the cost of string comparisons which is O(M) where M is the number of characters in a team.
    while j < (len(results)):
        # Does not check the last index to avoid index error.
        if j != len(results) - 1:
            match = results[j]
            next_match = results[j + 1]
            
            # If the match not equaled to the next match (unique), append them into the res_lst
            if match != next_match:
                res_lst.append(next_match)
            
        j += 1
        
    return res_lst  # Return the original list now modified to have the duplicates removed.
        

def search_score(results: List[T], score: int, find_closest_higher: bool = False) -> List[T]:
    """
    A function which will search through the input results list and return a list containing all the matches that has the 
    same score or the closest higher score. Sorry for the brute force method :( I had no choice due to time constraints.
    :Input:
        results: A list of matches for the function to find the matches which meet the score criteria.
        score: An integer in which the function will find the matches with the exact score or the closest higher equivalent.
        find_closest_higher: A boolean variable which specifies whether to find the exact score or the closest higher score. Set to false as default
    :Output:
        searchedmatches: A list containing all the matches that has the exact score as the score input or closest higher score.
    :Time complexity: O(N) where N is the length of the results list.
    :Space complexity: O(N) where N is the length of the results list.
    """
    searchedmatches = []    # Create an empty list to store all matches that fit the score criteria.
    has_found = False       # Create a boolean variable has_found to signify whether closest higher score has been found.
    i = 0

    # Loop through the results list to find the exact score or the closest highest score.
    # Will loop through the results list of length N hence O(N) time complexity.
    while i < len(results) and not has_found:
        # If finding exact score, find the match and append it to the searchedmatches.
        if not find_closest_higher:
            # If the match's score is equaled to the desired score, add it to the searchedmatches list.
            if results[i][2] == score:
                searchedmatches.append(results[i])
            
        # Else, will find the closest score that is higher than the input score.
        elif find_closest_higher:
            # If the highest score is less than target score, return an empty list.
            if results[0][2] < score:
                return searchedmatches
            # Else if haven't found the closest higher score yet, use a greedy method to take the first score that is higher.
            elif results[i][2] < score:
                searchedmatches.append(results[i - 1])  # Adds the match before the score that is lower so that the added score is
                                                        # higher.
                has_found = True                        # Exit the while loop once closest higher score has been found
            # If the match is the last match and has a higher score than the target score, add that in instead.
            elif i == len(results) - 1 and results[i][2] > score:
                searchedmatches.append(results[i])
                
        i += 1

    # Terminate the function early if score is found.
    if not find_closest_higher:
        return searchedmatches  # Return the list containing all the matches that have the same score as the score input.
        
    # In case of multiple similar scores which are the closest higher, loop through again to find all equivalent.
    desired_score = searchedmatches[0][2]
    # Clear the searchedmatches list to re-add all the matches with the desired_score into it.
    searchedmatches = []
    
    # For loop to loop through the results list to find all the score which is similar to the desired_score.
    # Will loop through the results list of length N thus O(N) time complexity.
    for match in results:
        # If the matches' score are the desired_score, add the match to the searchedmatches.
        if match[2] == desired_score:
            searchedmatches.append(match)
            
    return searchedmatches  # Return the list containing all the matches that has the closest highest score.


def analyze(results: List[T], roster: int, score: int) -> List[T]:
    """
    A function which will accept a list of matches, perform radix sort on each individual teams within each match so that they are
    sorted lexicographically, perform multiple radix sorts on each team before performing a final counting sort on the scores so
    that they are sorted in descending order. The Top 10 matches are added into a seperate list. It will then search through the 
    results list to find which match(es) has the same score as the input score or is the closest higher score, adding them into a
    another list. These two list are nested into a list and is returned back to the user in the format 
    of [[top10matches], [searchedmatches]].
    :Input:
        results: A list of lists containing matches between two teams and their resulting score (third index) obtained by Team1 (first index) and Team 2 (second index).
        roster: An integer that indicates the maximum amount of characters a team can have. Eg: roster = 5 means each team can choose a character from the set {A, B, C, D, E}.
        score: An integer ranging from 0 to 100 inclusive. It denotes a score obtained in a matchup in which the analyze function will have to find the match with the same score or the closest higher score.
    :Output:
        res: A list of lists containing top10matches - the top 10 matches sorted in descending order based on their score and searchedmatches - a list containing all the matches that have equal score or is the closest higher equivalent.
    :Time complexity: O(NM) where N is the number of matches and M is the number of characters in a team.
    :Space complexity: O(NM) where N is the number of matches and M is the number of characters in a team.
    """
    # If the results list is not empty.
    if len(results) > 0:        
        # Initialise the output lists.
        res = []
        top10matches = []
        searchedmatches = []
        
        # For loop to loop through each match in order to sort the teams' string lexicographically.
        # Will loop through N times hence O(N) time complexity ignoring costs within the loop.
        # Total cost is O(NM) as it loops through N times and each loop will call a function of complexity O(M)
        for match in results:
            match[0] = char_radix_sort(match[0], roster)    # Sorts team 1 string.
            match[1] = char_radix_sort(match[1], roster)    # Sorts team 2 string.
        
        # Call opposite_match to add the opposing matches of team 2 vs team 1 into the results list.
        results += opposite_match(results)
        
        # Sorting the whole results list so that the score will be in descending order.
        results = radix_sort_list(results, roster, 1)   # Call radix_sort_list with index 1 to sort list via team 2
        results = radix_sort_list(results, roster, 0)   # Call radix_sort_list with index 0 to sort list via team 1
        results = count_sort_num(results)               # Sort the results in descending order via score.
        
        # Remove the duplicate matches within the results list.
        results = remove_duplicates(results)
        
        # Adds the top 10 matches into the top10matches.
        # Loops constant 10 times always hence O(1) time complexity
        i = 0
        while i < len(results) and i < 10:
            top10matches.append(results[i])
            i += 1
            
        searchedmatches = search_score(results, score)
        
        # If searchedmatches is empty, means no equivalent scores are found.
        if len(searchedmatches) == 0:
            searchedmatches = search_score(results, score, True)    # Call the search function again but search for closest
                                                                    # highest score instead.
        
        # Append the top10matches and searchedmatches to the res list to be returned
        res.append(top10matches)
        res.append(searchedmatches) 
        return res      # Returns [[top10matches], [searchedmatches]]