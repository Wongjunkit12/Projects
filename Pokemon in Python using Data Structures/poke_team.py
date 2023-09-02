from pokemon import Charmander, Bulbasaur, Squirtle
from Missingno import MissingNo
from queue_adt import CircularQueue
from array_sorted_list import ArraySortedList
from sorted_list import ListItem
from stack_adt import ArrayStack
from typing import TypeVar

A = TypeVar('A', Charmander, Bulbasaur, Squirtle, MissingNo, ListItem)  # A represents all Pokemons and ListItem objects
P = TypeVar('P', Charmander, Bulbasaur, Squirtle, MissingNo)  # P represents only Pokemons

"""
Summary: This module contains the class which inherits the 3 Pokemon classes to create different types of team of 
         pokemon based on the battle_mode 
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""

class PokeTeam(Charmander, Bulbasaur, Squirtle, MissingNo):
    """ PokeTeam class which triggers the creation of two teams and assigns pokemon in the teams based on user input """
    # Class variable
    BATTLE_MODE_RANGE = [0, 1, 2]
    MAXMISSINGNO = 1

    # PokeTeam class constructor
    def __init__(self, team_name: str) -> None:
        """
        PokeTeam class Constructor
        param: team_name: a string that represents the team's name
        returns: None
        pre: team_name has to be String
        raises: TypeError if team_name is not String
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(team_name) != str:
            raise TypeError("Team name has to be String")
        else:
            self.team_name = team_name  # Initialize the instance variable team_name according to the argument
            self.team_has_MsgNo = False  # A boolean instance variable on whether the team has MissingNo
            self.has_MsgNo_appeared = False  # A boolean instance variable on whether MissingNo have come in or not

    def key_criterion(self, pokemon: P, criterion: str, multiplied: int = 1) -> int:
        """
        Method to return the specified stat based on criterion inputted
        param self: Poketeam object
              pokemon: Charmander, Bulbasaur, Squirtle or MissingNo object
              criterion: a string that represents one of the stat of the pokemon
              multiplied: an int that is used to multiply the criterion attribute. Default set it to 1
        returns: stat of the pokemon according to the criterion, either the hp, level, attack, defence or speed
        pre: criterion has to be String, multiplied has to be int, pokemon has to be a pokemon object,
             criterion has to be hp, lvl, atk, def or speed
        raises: TypeError if criterion is not String, TypeError if multiplied is not Integer,
                TypeError if pokemon is not a pokemon object, ValueError if criterion is not any of the 5 allowed
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(criterion) != str:
            raise TypeError("Criterion has to be String")
        elif type(multiplied) != int:
            raise TypeError("Multiplied arguement has to be Integer")
        elif not isinstance(pokemon, Charmander) and not isinstance(pokemon, Bulbasaur) and not isinstance(pokemon, Squirtle) and not isinstance(pokemon, MissingNo):
            raise TypeError("Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            # Checks criterion and returns pokemon attribute value based on it
            if criterion == "hp":
                return pokemon.get_hp() * multiplied  # Multiplied to multiply the given attribute by the variable. Done so to negate the key in MissingNo
            elif criterion == "lvl":
                return pokemon.get_level() * multiplied
            elif criterion == "atk":
                return pokemon.get_atk_dmg() * multiplied
            elif criterion == "def":
                return pokemon.get_defence() * multiplied
            elif criterion == "spd":
                return pokemon.get_speed() * multiplied
            elif criterion == None:  # Skip if criterion is None
                pass
            else:
                raise ValueError("Invalid criterion. Criterion has to be hp, lvl, atk, def, spd")  # Criterion does not match the format of criterion allowed.

    def assign_team(self, charm: int, bulb: int, squir: int, criterion: str = None, MsgNo: int = 0) -> None:
        """
        Method to populate the team based on the ADT chosen according to the battle_mode value
        param: self: PokeTeam object
               charm: an integer that represents how many Charmander objects
               bulb: an integer that represents how many Bulbasaur objects
               squir: an integer that represetns how many Squirtle objects
               criterion: a string that represents criteria used to sort the order of the Pokemons
               MsgNo: an integer that represents how many MissingNo objects
        return: None
        pre: Number of pokemons to add has to be Integer
        raises: TypeError if number of pokemons are not Integer
        Complexity: Best: O(1) since the for loop can only run up to maximum 6 times
                    Worst: O(1)

        """
        if type(charm) != int or type(bulb) != int or type(squir) != int or type(MsgNo) != int:
            raise TypeError("Number of pokemons to add has to be Integer")
        
        # If the team is a stack
        if isinstance(self.team, ArrayStack):
            for i in range(MsgNo):
                self.team.push(MissingNo())  # Push the MissingNo object into the stack
                self.team_has_MsgNo = True  # Sets this to True to signify team has MissingNo pokemon
            for i in range(squir):
                self.team.push(Squirtle())  # Push Squirtle object squir times into the stack
            for i in range(bulb):
                self.team.push(Bulbasaur())  # Push Bulbasaur object bulb times into the stack
            for i in range(charm):
                self.team.push(Charmander())  # Push Charmander object charm times into the stack

        # If the team is a Circular queue
        elif isinstance(self.team, CircularQueue):
            for i in range(charm):
                self.team.append(Charmander())  # Append Charmander object charm times into the queue)
            for i in range(bulb):
                self.team.append(Bulbasaur())  # Append Bulbasaur object bulb times into the queue
            for i in range(squir):
                self.team.append(Squirtle())  # Append Squirtle object squir times into the queue
            for i in range(MsgNo):
                self.team.append(MissingNo())  # Append the MissingNo object into the queue
                self.team_has_MsgNo = True  # Sets this to True to signify team has MissingNo pokemon

        # Else it is an array sorted list
        else:
            for i in range(charm):
                poke = Charmander()  # Creates Charmander object and assign it to variable poke
                poke_criterion = ListItem(poke, self.key_criterion(poke,criterion))  # Creates ListItem object with value Charmander object and key based on criterion
                self.team.add_tie(poke_criterion)  # Add Charmander ListItem char times into the array
            for i in range(bulb):
                poke = Bulbasaur()  # Creates Bulbosaur object and assign it to variable poke
                poke_criterion = ListItem(poke, self.key_criterion(poke, criterion))  # Creates ListItem object with value Bulbasaur object and key based on criterion
                self.team.add_tie(poke_criterion)  # Add Bulbasaur ListItem bulb times into the array
            for i in range(squir):
                poke = Squirtle()  # Creates Squirtle object and assign it to variable poke
                poke_criterion = ListItem(poke, self.key_criterion(poke,criterion))  # Creates ListItem object with value Squirtle object and key based on criterion
                self.team.add_tie(poke_criterion)  # Add Squirtle ListItem squir times into the array
            for i in range(MsgNo):
                poke = MissingNo()  # Creates MissingNo object and assign it to variable poke
                poke_criterion = ListItem(poke, self.key_criterion(poke, criterion, -1))  # Creates ListItem object with value MissingNo object and key based on criterion. Negate key so MissingNo is last in team
                self.team.add_tie(poke_criterion)  # Add MissingNo ListItem MsgNo times into the array
                self.team_has_MsgNo = True  # Sets this to True to signify team has MissingNo pokemon

    def create_adt(self, size: int, battle_mode: int) -> None:
        """
        Method to create team based on ADT
        param: self: PokeTeam object
               size: an integer that represents the size of the team(ADT)
               battle_mode: an integer that represents the type of battle_mode(type of ADT used)
        return: None
        pre: size has to be Integer, battle_mode has to be Integer, battle_mode has to be 0, 1 or 2
        raises: TypeError if size is not Integer, TypeError it battle_mode is not Integer,
                ValueError if battle_mode is not 0, 1 or 2
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(size) != int:
            raise TypeError("Size has to be Integer")
        elif type(battle_mode) != int:
            raise TypeError("Battle mode has to be Integer")
        else:
            if battle_mode == 0: # Create a stack if battle_mode = 0
                self.team = ArrayStack(size)
            elif battle_mode == 1: # Create a CircularQueue if battle_mode = 1
                self.team = CircularQueue(size)
            elif battle_mode == 2: # Create an ArraySortedList if battle_mode = 2
                self.team = ArraySortedList(size)
            else:
                raise ValueError(f"Invalid battle mode: {battle_mode}. Battle mode has to be 0, 1 or 2.")

    def choose_team(self, battle_mode: int, criterion: str = None) -> None:
        """
        Method to make a team of pokemons
        params: self: PokeTeam object
                battle_mode: an integer that represents the type of battle_mode(type of ADT used)
                criterion: a string that represents criteria used to sort the order of the Pokemons
        returns: None
        pre: battle_mode has to be Integer, criterion has to be String, battle_mode has to be 0, 1 or 2
        raises: TypeError if battle_mode is not Integer, TypeError if criterion is not String,
                ValueError if battle_mode is not 0, 1 or 2
        Complexity: Best: O(k) where k is the cost of comparison of the while loop
                    Worst: O(k) where k is the cost of comparison of the while loop
        """
        if type(battle_mode) != int:
            raise TypeError("Battle mode has to be Integer")
        elif battle_mode == 2 and type(criterion) != str:
            raise TypeError("Criterion has to be String")
        else:
            # Check if battle mode is int then check if its in range of 0,1,2
            if battle_mode not in PokeTeam.BATTLE_MODE_RANGE:
                raise ValueError(f"Invalid battle mode: {battle_mode}. Battle mode has to be 0, 1 or 2.")

            # Print the team input prompt
            prompt = "Howdy Trainer! Choose your team as C B S or C B S M \n" \
                     "where C is the number of Charmanders \n" \
                     "      B is the number of Bulbasaurs \n" \
                     "      S is the number of Squirtles \n" \
                     "      M is the number of MissingNo \n"

            print(prompt)

            sentinel = False  # This sentinel used is to stop the while loop once a valid team can be created

            # This while loop is created so that the input prompt will keep repeating until they create a valid team
            while not sentinel:  # If sentinel == False meaning if no valid team has yet to be created
                lst = input().split()  # Call the input and then split it to create a list of string of number of each pokemon
                if len(lst) == 3:      # If the trainer input for Charmander, Bulbasaur, Squirtle
                    C, B, S = map(int,lst)  # Map each value in lst to integer then assign to C,B,S to represent the pokemon
                    number = C + B + S  # Number = number of charmander + bulbasaur + squirtle
                    if number < 7 and number > 0:  # If number is less than 7(within 1-6) and not a negative value:
                        sentinel = True  # A valid team can be created so sentinel = True to break the while loop
                        self.create_adt(number, battle_mode)  # Create the adt based on the size(number)
                        self.assign_team(C, B, S,criterion)   # Populate the team based on the ADT chosen by calling the assign_team method
                    else:
                        print("Please input a valid team \n" + prompt)  # Else: tell the user to input a valid team

                elif len(lst) == 4:
                    C, B, S, M = map(int,lst)  # Map each value in lst to integer then assign to C,B,S,M to represent the pokemon
                    number = C + B + S + M  # Number = number of charmander + bulbasaur + squirtle + MissingNo
                    if number < 7 and number > 0 and M == PokeTeam.MAXMISSINGNO:  # If number is less than 7(within 1-6) and not < 0 and
                        sentinel = True  # M is == 1, sentinel = True so a valid team can be created
                        self.create_adt(number, battle_mode)    # Create the adt based on the size(number)
                        self.assign_team(C, B, S, criterion,M)  # Populate the team based on the ADT chosen by calling the assign_team method
                    else:
                        print("Please input a valid team\n" + prompt)  # Else tell the user to input a valid team

                else: # If input pokemons is 0
                    print("Please input the correct amount of Pokemon, 3 or 4 Pokemons ")  # if user did not input 3/4 pokemons
                    # Ask the user to reinput

    def who_has_fought(self) -> bool:
        """
        Function that checks which pokemon had fought and will return True if all other pokemon in the team has fought
        so that MissingNo will be made avalaible to fight.
        param: self: Battle object
        return: boolean value on whether all pokemon has fought yet
        Complexity Best:  O(1)
                   Worst: O(n)
        """
        # If team 1, loops through team 1 and get each pokemon object to check whether all pokemon have fought yet
        for i in range(len(self.team) - 1, 0, -1):
            pokemon = self.team[i].value  # Gets the pokemon object from the ArraySortedList
            # If any pokemon have not fought yet, return False
            if not pokemon.get_has_fought():
                return False  # Return False to signify not all pokemon in the team has fought yet
        return True  # Else return True if all pokemon has fought

    def get_pokemon(self, battle_mode: int, criterion: str = None) -> A:
        """
        Method to return the first pokemon in the team
        param: self: PokeTeam object
               battle_mode: an integer that represents the type of battle_mode(type of ADT used)
               criterion: a string representing the criterion to sort the team by
        returns: A pokemon object which is the first pokemon in the team or a ListItem object if the battle mode is 2
        pre: battle_mode has to be Integer, criterion has to be String, battle_mode has to be 0, 1 or 2
        raises: TypeError if battle_mdoe is not Integer, TypeError if criterion is not String
                ValueError if battle_mode is not 0, 1 or 2
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(battle_mode) != int:
            raise TypeError("Battle mode has to be Integer")
        elif criterion is not None and type(criterion) != str:
            raise TypeError("Criterion should be String")
        else:
            if battle_mode == 0:    # If Stack ADT
                return self.team.pop()
            elif battle_mode == 1:  # If Queue ADT
                return self.team.serve()
            elif battle_mode == 2:  # If ArraySortedList ADT
                # If team has no MissingNo
                if not self.team_has_MsgNo or self.has_MsgNo_appeared:
                    return self.team.delete_at_index(len(self.team) - 1)  # Returns first instance of pokemon found

                # Else if team has a MissingNo and not all pokemon in the team have fought at least once yet
                elif self.team_has_MsgNo and not self.who_has_fought():
                    pokemon = self.team.delete_at_index(len(self.team) - 1)  # Pokemon is the pokemon at the front of the team
                    pokemon.value.set_has_fought(True)  # Set the pokemon has_fought instance variable to True to signify it has fought
                    return pokemon

                # Else if team has a MissingNo, all the pokemon in the team have fought once and MissingNo have not appeared yet
                elif self.team_has_MsgNo and self.who_has_fought() and not self.has_MsgNo_appeared:
                    missing_no = self.team.delete_at_index(0)  # Gets MissingNo ListItem object
                    missing_no.key = self.key_criterion(missing_no.value, criterion)  # Updates MissingNo key by negating it
                    self.team.add_tie(missing_no)   # Add MissingNo back to team
                    self.has_MsgNo_appeared = True  # Set True to signify MissingNo has appeared
                    pokemon = self.team.delete_at_index(len(self.team) - 1) # Get the pokemon in front of the team
                    return pokemon
            else:
                raise ValueError(f"Invalid battle mode: {battle_mode}. Battle mode has to be 0, 1 or 2.")

    def check_if_team_empty(self) -> bool:
        """
        Method to check if a team's pokemon has all fainted
        param: self: PokeTeam object
        returns: a boolean value, True indicates the team is empty and False otherwise
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return self.team.is_empty() # Check if the team is empty

    def __str__(self) -> str:
        """
        Method to print out the pokemon in the teams
        param: self: PokeTeam object
        returns: The attributes(Name, hp and level) of the pokemons in the team
        Complexity: Best: O(n)
                    Worst: O(n)
        """
        return str(self.team) # Print out the pokemons in the team