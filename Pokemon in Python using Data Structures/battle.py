from Missingno import MissingNo
from poke_team import PokeTeam
from pokemon import Bulbasaur, Charmander, Squirtle
from sorted_list import ListItem
from typing import TypeVar

A = TypeVar('A', Charmander, Bulbasaur, Squirtle, MissingNo, ListItem)  # A represents all Pokemons and ListItem objects
P = TypeVar('P', Charmander, Bulbasaur, Squirtle, MissingNo)  # P represents only Pokemons

""" 
Summary: This module commences the battle phase of two trainer teams and has different methods based on different battle modes
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class Battle:
    """ Battle class which initiates and runs battling of two different teams based on chosen battle mode """
    def __init__(self, trainer_one_name: str, trainer_two_name: str) -> None:
        """
        Battle class Constructor
        param: name of trainer one and two
        returns: None
        pre: trainer_one_name and trainer_two_name has to be string
        raises: TypeError if the trainer names are not String
        Complexity: Best case: O(1)
                    Worst case: O(1)
        """
        if type(trainer_one_name) != str or type(trainer_two_name) != str:
            raise TypeError("Trainer Names have to be String")  # Raise TypeError if trainer names not string
        else:
            self.team1 = PokeTeam(trainer_one_name)
            self.team2 = PokeTeam(trainer_two_name)
            self.battle_mode = None  # Sets battle_mode to None
            self.criterion1 = None   # Sets criterion for team 1 and team 2 to None also
            self.criterion2 = None

    def set_mode_battle(self) -> str:
        """
        Normal battle mode utilising Stack ADT
        Method for battle_mode 0
        param: self
        return: the winner
        Complexity: Best case: O(k+n^2) where k is the from while loop comparison in choose_team,
                                n^2 is from the string concatenation
                    Worst case: O(k+n^2) where k is the from while loop comparison in choose_team,
                                n^2 is from the string concatenation
        """
        self.battle_mode = 0  # Sets battle_mode to 0

        self.pick_team()  # Calls pick_team function to choose pokemon teams

        winner = self.battle()  # Calls battle function and assign winner to variable winner

        return self.print_winner(winner)  # Return the winner

    def rotating_mode_battle(self) -> str:
        """
        Rotating battle mode utilising Circular Queue ADT
        Method for battle_mode 1
        param: self
        return: the winner
        Complexity: Best case: O(k+n^2) where k is the from while loop comparison in choose_team,
                                n^2 is from the string concatenation
                    Worst case: O(k+n^2) where k is the from while loop comparison in choose_team,
                                n^2 is from the string concatenation
        """
        self.battle_mode = 1

        self.pick_team()

        winner = self.battle()

        return self.print_winner(winner)

    def optimised_mode_battle(self, criterion_team1: str, criterion_team2: str) -> str:
        """
        Optimised battle mode utilising ArraySortedList ADT
        Method for battle_mode 2
        param: criterion team 1 and 2
        return: the winner
        pre: criterion_team1 and criterion_team2 has to be String, Criteiron has to be hp, lvl, atk, def, spd
        raises: TypeError if criterions are not String, ValueErrpr of criterion is neither of these 5
        Complexity: Best case: O(k+ n^2) where k is the cost of comparison in the choose_team method in pick_team
                               and n^2 os from the string concatenation

                    Worst case: O(k+ n^2) where k is the cost of comparison in the choose_team method in pick_team
                               and n^2 os from the string concatenation
        """
        if type(criterion_team1) != str or type(criterion_team2) != str:
            raise TypeError("Criterion has to be String")
        else:
            self.battle_mode = 2  # Sets battle_mode to 2
            if not self.check_criterion(criterion_team1) and not self.check_criterion(criterion_team2):
                raise ValueError("Invalid criterion. Criterion has to be hp, lvl, atk, def, spd")  # Criterion does not match the format of criterion allowed.
            else:
                # Creates instance variables criterion
                self.criterion1 = criterion_team1
                self.criterion2 = criterion_team2

                self.pick_team(criterion_team1, criterion_team2)  # Calls pick_team function to choose pokemon teams with criterion

                winner = self.battle()

                return self.print_winner(winner)

    def pick_team(self, team1_criterion: str = None, team2_criterion: str = None) -> None:
        """
        Method to initiate user input prompt to pick team
        param: criterion team 1 and 2
        return: None
        Complexity: Best case: O(k+n^2) where k is the from while loop comparison in choose_team and
                                n^2 is from the string concatenation
                    Worst case: O(k+n^2) where k is the from while loop comparison in choose_team and
                                n^2 is from the string concatenation
        """
        print("\nChoose team for " + self.team1.team_name)
        self.team1.choose_team(self.battle_mode, team1_criterion)  # Call choose_team function in poke_team module to
        # create team based on criterion selected
        print("\nChoose team for " + self.team2.team_name)
        self.team2.choose_team(self.battle_mode, team2_criterion)  # Call choose_team function in poke_team module to
        # create team based on criterion selected

        # Prints out both teams
        print("\n" + self.team1.team_name + "'s team: ")
        print(self.team1)
        print("\n" + self.team2.team_name + "'s team: ")
        print(self.team2)

    def check_winner(self) -> int:
        """
        Method to determine who is winner and returns the number of the team who won
        param: self
        return: the number of the team who won, 0 if its a draw, raises error if both teams aren't empty
        pre: One team has to be empty
        raises: Exception if both teams are not empty and have pokemons in them
        Complexity: Best case: O(1)
                    Worst case: O(1)
        """
        if self.team1.check_if_team_empty() & self.team2.check_if_team_empty():  # If both teams are empty, it's a draw
            return 0  # 0 to signify it's a draw
        elif self.team1.check_if_team_empty():  # If Team 1 is empty, Team 2 wins
            return 2
        elif self.team2.check_if_team_empty():  # Team 1 wins if Team 2 is empty
            return 1
        else:
            raise Exception("Both teams are not empty")  # If neither team are empty, raise error

    def print_winner(self, winner: int) -> str:
        """
        Method to print out and return the winner's team
        param: winner, which is one of the team, an integer
        return: a sentence for the winning team, raises error otherwise
        pre: winner has to be Integer, winning team has to either be team 1 or 2
        raises: TypeError if winner is not Integer, ValueError if winning team is neither team 1 or 2
        Complexity: Best case: O(n^2) from string concatenation
                    Worst case: O(n^2) from string concatenation
        """
        if type(winner) != int:
            raise TypeError("Winner input has to be Integer")
        else:
            # Checks who is winner
            if winner == 0:  # 0 means draw
                print("\nIt's a draw")
                res = "Draw"
            # 1 means Team 1 is winner
            elif winner == 1:
                print("\n" + self.team1.team_name + " is the winner")
                res = self.team1.team_name  # sets res as Team 1's name to be returned as the winner
            # 2 means Team 2 is the winner
            elif winner == 2:
                print("\n" + self.team2.team_name + " is the winner")
                res = self.team2.team_name
            else:
                raise ValueError("Invalid winning team. Team either has to be 1 or 2")
            return res

    def check_criterion(self, criterion: str) -> bool:
        """
        Method to check if criterion inputted is any of the 5 criterion allowed
        param: self
               criterion
        pre: criterion has to be String
        raises: TypeError if criterion is not String
        return: a boolean value if criterion equals to either hp,lvl,atk,def or spd
        Complexity: Best case: O(1)
                    Worst case: O(1)
        """
        if type(criterion) != str:
            raise TypeError("Criterion has to be String")
        else:
            return criterion == "hp" or criterion == "lvl" or criterion == "atk" or criterion == "def" or criterion == "spd"

    def battle(self) -> int:
        """
        Method to start battle between teams
        param: self
        return: the team which won, which is an integer
        Complexity: Best case: O(1) if battle_mode = 0/1
                    Worst case: O(n) if battle_mode = 2, where n is the length of the team
        """
        # Will continue looping until one team is empty
        while not self.team1.check_if_team_empty() and not self.team2.check_if_team_empty():
            self.battle_to_death()
        return self.check_winner()  # Return team which won

    def battle_to_death(self) -> None:
        """
        Method for 2 Pokemons battling each other
        param: self
        return: None
        pre: battle_mode has to be either 0, 1 or 2
        raises: ValueError if battle_mode is neither 0, 1 or 2
        Complexity: Best case: O(1) if battle_mode = 0/1
                    Worst case: O(n) if battle_mode = 2, where n is the length of the team
        """
        to_exit = False  # Loop condition
        while not to_exit:
            # Gets pokemon from their teams and assign it to instance variable
            self.pokemon1 = self.team1.get_pokemon(self.battle_mode, self.criterion1)  # Gets pokemon from team 1
            self.pokemon2 = self.team2.get_pokemon(self.battle_mode, self.criterion2)  # Gets pokemon from team 2
            
            # If either Stack or Circular Queue
            if self.battle_mode == 0 or self.battle_mode == 1:
                # If statements to determine who attacks first and who defends
                # If pokemon1 is faster, attack first while pokemon2 defends
                if self.pokemon1.get_speed() > self.pokemon2.get_speed():
                    if self.round(self.pokemon1, self.pokemon2, 1, 2):
                        to_exit = True  # If any pokemon faints, exit while loop
                # If pokemon2 is faster, attack first while pokemon1 defends
                elif self.pokemon1.get_speed() < self.pokemon2.get_speed():
                    if self.round(self.pokemon2, self.pokemon1, 2, 1):
                        to_exit = True
                # Else if both same speed, they both attack and defend simulataneously
                elif self.pokemon1.get_speed() == self.pokemon2.get_speed():
                    if self.round_together(self.pokemon1, self.pokemon2):
                        to_exit = True

            # If ArraySortedList, so if optimised battle
            elif self.battle_mode == 2:
                # If statements to determine who attacks first and who defends
                # If pokemon1 is faster, attack first while pokemon2 defends
                if self.pokemon1.value.get_speed() > self.pokemon2.value.get_speed():
                    if self.round(self.pokemon1.value, self.pokemon2.value, 1, 2):
                        to_exit = True  # If any pokemon faints, exit while loop
                # If pokemon2 is faster, attack first while pokemon1 defends
                elif self.pokemon1.value.get_speed() < self.pokemon2.value.get_speed():
                    if self.round(self.pokemon2.value, self.pokemon1.value, 2, 1):
                        to_exit = True
                # Else if both same speed, they both attack and defend simulataneously
                elif self.pokemon1.value.get_speed() == self.pokemon2.value.get_speed():
                    if self.round_together(self.pokemon1.value, self.pokemon2.value):
                        to_exit = True

            # If battle mode is not 0, 1 or 2, raise error
            else:
                raise ValueError(f"Invalid battle mode: {self.battle_mode}. Battle mode has to be 0, 1 or 2.")

    def place_back(self, pokemon: P, team: int) -> None:
        """
        Method to place pokemon back into specified team
        param: self: Battle object
               pokemon: A pokemon object either Charmander, Bulbasaur, Squirtle or MissingNo
               team: An integer that represents which team, team1 or team2
        return: None
        pre: team has to be Integer, pokemon has to be either a Charmander, Bulbasaur, Squirtle or MissingNo object
             team has to be 1 or 2, battle_mode has to be 0, 1 or 2
        raises: TypeError if team is not Integer, TypeError if team is not a pokemon object,
                ValueError if team is not 1 or 2, ValueError if battle_mode is not 0, 1 or 2
        Complexity: Best case: O(1) if battle_mode = 0/1
                    Worst case: O(n) if battle_mode = 2, where n is the length of the team
        """
        if type(team) != int:
            raise TypeError("Team must be Integer")
        elif not isinstance(pokemon, Charmander) and not isinstance(pokemon, Bulbasaur) and not isinstance(pokemon, Squirtle) and not isinstance(pokemon, MissingNo):
            raise TypeError("Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            # For stack, push pokemon back into stack
            if self.battle_mode == 0:
                if team == 1:
                    self.team1.team.push(pokemon)  # Push pokemon back into team1
                elif team == 2:
                    self.team2.team.push(pokemon)  # Push pokemon back into team2
                else:
                    raise ValueError(f"Invalid team number: {team}. Team has to be 1 or 2")  # If team is neither of the 2, raise error.

            # For Circular Queue
            elif self.battle_mode == 1:
                if team == 1:
                    self.team1.team.append(pokemon)
                elif team == 2:
                    self.team2.team.append(pokemon)
                else:
                    raise ValueError(f"Invalid team number: {team}. Team has to be 1 or 2")

            # For ArraySortedList
            elif self.battle_mode == 2:
                if team == 1:
                    self.team1.team.add_tie(self.pokemon1)  # Add pokemon back into team based on criterion
                elif team == 2:
                    self.team2.team.add_tie(self.pokemon2)
                else:
                    raise ValueError(f"Invalid team number: {team}. Team has to be 1 or 2")

            # Else raise error in battle_mode
            else:
                raise ValueError(f"Invalid battle mode: {self.battle_mode}. Battle mode has to be 0, 1 or 2.")

    def update_key(self, team_to_update: int) -> None:
        """
        Method that updates key in ListItem object based on criterion
        param: self: Battle object
               team_to_update: An integer that represents which team to update the key, team1 or team2
        returns: None
        pre: team_to_update has to be Integer, team_to_updateh has to be 1 or 2
        raises: TypeError if team_to_update is not Integer, ValueError if team_to_updateh is not 1 or 2
        Complexity: Best case: O(1)
                    Worst case: O(1)
        """
        if type(team_to_update) != int:
            raise TypeError("Team has to be Integer")

        # If battle mode 2, update key to new criterion attribute values
        elif self.battle_mode == 2:
            if team_to_update == 1:
                self.pokemon1.key = self.team1.key_criterion(self.pokemon1.value, self.criterion1)  # Update criterion of pokemon1 based on new attribute values
            elif team_to_update == 2:
                self.pokemon2.key = self.team2.key_criterion(self.pokemon2.value, self.criterion2)  # Update criterion of pokemon2 based on new attribute values
            else:
                raise ValueError(f"Invalid team number: {team_to_update}. Team has to be 1 or 2")

    def level_up(self, pokemon: P, team: int) -> None:
        """
        Method to level up pokemon and place back into specified team
        param: self: Battle object
               pokemon: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               team : an integer that represents which team, team1 or team2
        returns: None
        pre: team has to be Integer, pokemon has to be a pokemon object
        raises: TypeError if team is not Integer, TypeError if pokemon is not pokemon object
        Complexity: Best case: O(1) if battle_mode = 0/1
                    Worst case: O(n) if battle_mode = 2, where n is the length of the team
        """
        if type(team) != int:
            raise TypeError("Team has to be Integer")
        elif not isinstance(pokemon, Charmander) and not isinstance(pokemon, Bulbasaur) and not isinstance(pokemon, Squirtle) and not isinstance(pokemon, MissingNo):
            raise TypeError("Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            # If pokemon levelling up is MissingNo, increase its hp by 1
            if isinstance(pokemon, MissingNo):
                pokemon.increase_hp(1)  # Increase hp by 1
            # Increment pokemon level by one
            pokemon.set_level(pokemon.get_level() + 1)  # Level up pokemon by one
            self.update_key(team)
            self.place_back(pokemon, team)  # Place pokemon back into their team

    def attacking(self, attacker: P, defender: P) -> bool:
        """
        Method for the attacking phase between two pokemon
        param: self: Battle object
               attacker: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               defender: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: A boolean value, True if the defender fainted, False otherwise
        pre: attacker has to be a pokemon object and defender has to be a pokemon object
        raises: TypeError if attacker is not pokemon object, TypeError if defender is nto pokemon object
        Complexity: Best case: O(1)
                    Worst case: O(1)
        """
        if not isinstance(attacker, Charmander) and not isinstance(attacker, Bulbasaur) and not isinstance(attacker, Squirtle) and not isinstance(attacker, MissingNo):
            raise TypeError("Attacking Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        if not isinstance(defender, Charmander) and not isinstance(defender, Bulbasaur) and not isinstance(defender, Squirtle) and not isinstance(defender, MissingNo):
            raise TypeError("Defending Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            damage = defender.cal_dmg_taken(attacker)  # Calculate damage defender will take from attacker
            defender.set_hp(defender.get_hp() - damage)  # Set defender new Hp after taking damage
            return defender.is_fainted()  # Will return True if defender faints, False otherwise.

    def round(self, faster_poke: P, slower_poke: P, team_faster: int, team_slower: int) -> bool:
        """
        Method for the Round phase
        param: self: Battle object
               faster_poke: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               slower_poke: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               team_faster: An integer that represents the team of the faster_poke, team1 or team2
               team_slower: An integer that represents the team of the slower_poke, team1 or team2
        returns: A boolean value, True if one of the pokemon fainted, False if no pokemon fainted
        pre: team_faster and team_slower has to be Integer, faster_poke and slower_poke has to be a pokemon object
        raises: TypeError if team_faster and team_slower is not Integer
                ValueError if faster_poke and slower_poke is not a pokemon object
        Complexity: Best case: O(1) if battle_mode = 0/1
                    Worst case: O(n) if its ArraySortedList, where n is the length of the team
        """
        if type(team_faster) != int or type(team_slower) != int:
            raise TypeError("Team has to be Integer")
        elif not isinstance(faster_poke, Charmander) and not isinstance(faster_poke, Bulbasaur) and not isinstance(faster_poke, Squirtle) and not isinstance(faster_poke, MissingNo) and not isinstance(slower_poke, Charmander) and not isinstance(slower_poke, Bulbasaur) and not isinstance(slower_poke, Squirtle) and not isinstance(slower_poke, MissingNo):
            raise TypeError("Pokemon has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            if self.attacking(faster_poke, slower_poke):  # Faster pokemon will attack the slower pokemon
                self.level_up(faster_poke, team_faster)   # If the defender faints, level up the attacker
                return True                               # and is placed back into its team

            # If defender does not faint, it will retaliate and attack
            else:
                if self.attacking(slower_poke, faster_poke):  # The slower pokemon will now attack the faster pokemon
                    self.level_up(slower_poke, team_slower)   # If the faster pokemon faints, the slower pokemon
                    return True                               # will level up and is place backed into its team

                # Else if neither faints, both lose 1 hp.
                else:
                    faster_poke.set_hp(faster_poke.get_hp() - 1)  # Pokemon loses 1 hp
                    slower_poke.set_hp(slower_poke.get_hp() - 1)

                    # If both pokemon fainted, exit function
                    if faster_poke.is_fainted() and slower_poke.is_fainted():
                        return True  # Return True to signify round is over as at least one pokemon fainted

                    # If either one fainted, the last one standing levels up and gets placed back into the team
                    elif faster_poke.is_fainted():
                        self.level_up(slower_poke, team_slower)
                        return True
                    elif slower_poke.is_fainted():
                        self.level_up(faster_poke, team_faster)
                        return True

                    # If neither faints, sent back into their own teams
                    else:
                        self.update_key(team_faster)  # Updates both pokemon's key in the array
                        self.update_key(team_slower)
                        self.place_back(faster_poke, team_faster)
                        self.place_back(slower_poke, team_slower)
                        return False  # Return False to signify none of the pokemon fainted

    def check_who_fainted(self, poke_1: P, poke_2: P) -> bool:
        """
        Method to check which pokemon fainted
        param: self: Battle object
               poke_1: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               poke_2: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: A boolean value, True if poke_1 or poke_2 or both fainted, False if neither one of the pokemon fainted
        pre: poke_1 and poke_2 has to be a pokemon object
        raises: TypeError if poke_1 and poke_2 is not a pokemon object
        Complexity: Best case: O(1) if its stack and queue
                    Worst case: O(n) if its ArraySortedList, where n is the length of the team
        """
        if not isinstance(poke_1, Charmander) and not isinstance(poke_1, Bulbasaur) and not isinstance(poke_1, Squirtle) and not isinstance(poke_1, MissingNo):
            raise TypeError("Pokemon1 has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        elif not isinstance(poke_2, Charmander) and not isinstance(poke_2, Bulbasaur) and not isinstance(poke_2, Squirtle) and not isinstance(poke_2, MissingNo):
            raise TypeError("Pokemon2 has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            # If both fainted, exit function
            if poke_1.is_fainted() and poke_2.is_fainted():
                return True  # Return True if any pokemon fainted

            # If either one fainted, level up the remaining pokemon and return it back into its team
            elif poke_1.is_fainted():
                self.level_up(poke_2, 2)
                return True
            elif poke_2.is_fainted():
                self.level_up(poke_1, 1)
                return True

            # Else if neither fainted, return False
            else:
                return False

    def round_together(self, pokemon_1: P, pokemon_2: P) -> bool:
        """
        Method in which both pokemons attack and defend together simulataneously
        param: self: Battle object
               pokemon_1: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
               pokemon_2: A pokemon object either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: A boolean value, True if one of the pokemon fainted, False if none of the pokemon fainted
        pre: pokemon_1 and pokemon_2 has to be a pokemon object
        raises: TypeError if pokemon_1 and pokemon_2 is not a pokemon object
        Complexity: Best case: O(1) if its stack and queue
                    Worst case: O(n) if its ArraySortedList, where n is the length of the team
        """
        if not isinstance(pokemon_1, Charmander) and not isinstance(pokemon_1, Bulbasaur) and not isinstance(pokemon_1, Squirtle) and not isinstance(pokemon_1, MissingNo):
            raise TypeError("Pokemon1 has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        elif not isinstance(pokemon_2, Charmander) and not isinstance(pokemon_2, Bulbasaur) and not isinstance(pokemon_2, Squirtle) and not isinstance(pokemon_2, MissingNo):
            raise TypeError("Pokemon2 has to either be a Charmander, Bulbasaur, Squirtle or MissingNo class")
        else:
            # Both pokemon attack each other at the same time
            self.attacking(pokemon_1, pokemon_2)
            self.attacking(pokemon_2, pokemon_1)

            # If one faints, return True and exit function
            if self.check_who_fainted(pokemon_1, pokemon_2):
                return True  # Returns True to signify round ending as one pokemon faints
            # Else if neither pokemon fainted, both lose 1 hp
            else:
                pokemon_1.set_hp(pokemon_1.get_hp() - 1)  # Pokemon loses 1 hp
                pokemon_2.set_hp(pokemon_2.get_hp() - 1)

                # Check which one faints after losing 1 hp
                if self.check_who_fainted(pokemon_1, pokemon_2):
                    return True
                # If none of them faints, return both back to their respective teams
                else:
                    self.update_key(1)
                    self.update_key(2)
                    self.place_back(pokemon_1, 1)
                    self.place_back(pokemon_2, 2)
                    return False  # Return False to signify none of the pokemon fainted