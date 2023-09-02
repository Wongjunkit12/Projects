import unittest
from sorted_list import ListItem
from battle import Battle
from tester_base import TesterBase, captured_output
from pokemon import Charmander

""" 
Summary: A testing file to test all methods inside the battle.py module
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class TestBattle(TesterBase):
    def test_set_mode_battle(self):
        """ Testing the set_mode_battle function. Will count and return number of errors if fails to past test """
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names.
        # If an Exception is raised, instantiation fails and add an error.
        try:
            b = Battle("Ash", "Misty")
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be instantiated: {str(e)}.")
            return
        # Creates team of pokemons for Ash and Misty and calls the set_mode_battle() function to start a battle
        # If an Exception is raise, the battle failed to execute or an error was encountered midway
        try:
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                result = b.set_mode_battle()
        except Exception as e:
            self.verificationErrors.append(f"Battle failed to execute: {str(e)}.")
            return
        # Checks the results of the battle executed above and assert the winner to be "Ash"
        # If an AssertionError is raised, means the wrong person won the battle and Ash should have won.
        try:
            assert result == "Ash"
        except AssertionError:
            self.verificationErrors.append(f"Ash should win: {result}.")

    def test_rotate_mode_battle(self):
        """ Testing the rotating_mode_battle function. Will count and return number of errors if fails to past test """
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names.
        # If an Exception is raised, instantiation fails and add an error.
        try:
            b = Battle("Ash", "Misty")
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be instantiated: {str(e)}.")
            return
        # Creates team of pokemons for Ash and Misty and calls the rotating_mode_battle() function to start a battle
        # If an Exception is raise, the battle failed to execute or an error was encountered midway
        try:
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                result = b.rotating_mode_battle()
        except Exception as e:
            self.verificationErrors.append(f"Rotate mode battle failed to execute: {str(e)}.")
            return
        # Checks the results of the battle executed above and assert the winner to be "Ash"
        # If an AssertionError is raised, means the wrong person won the battle and Ash should have won.
        try:
            assert result == "Ash"
        except AssertionError:
            self.verificationErrors.append(f"Ash should win in rotate mode battle: {result}.")

    def test_optimised_mode_battle(self):
        """ Testing the optimised_mode_battle function. Will count and return number of errors if fails to past test """
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names.
        # If an Exception is raised, instantiation fails and add an error.
        try:
            b = Battle("Ash", "Misty")
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be instantiated: {str(e)}.")
            return
        # Creates team of pokemons for Ash and Misty and calls the optimised_mode_battle() function to start a battle with criterion "hp" and "hp" for both teams
        # If an Exception is raise, the battle failed to execute or an error was encountered midway
        try:
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                result = b.optimised_mode_battle('hp', 'hp')
        except Exception as e:
            self.verificationErrors.append(f"Optimised mode Battle failed to execute: {str(e)}.")
            return
        # Checks the results of the battle executed above and assert the winner to be "Ash"
        # If an AssertionError is raised, means the wrong person won the battle and Ash should have won.
        try:
            assert result == "Ash"
        except AssertionError:
            self.verificationErrors.append(f"Ash should win: {result}.")

    def test_pick_team(self):
        """ Testing the pick_team function. Will count and return number of errors if fails to past test """
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names for battle mode 0.
        try:
            a = Battle("Ash", "Misty")
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                a.battle_mode = 0 # Sets instance variable battlemdoe to 0
                a.pick_team()     # Pick team with Ash 111 and misty 110
                b = str(a.team1)
                c = str(a.team2)  # Uses tostring method to check if the pickteam method operate correctly
                # If team is not the same as the expected team, add error
                if b != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1" or c != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1":
                    self.verificationErrors.append(f"Wrong pokemon is picked for both team: {b} and {c}")
        # If an Exception is raised, pickteam fails and add an error.
        except Exception as e:
            self.verificationErrors.append(f"Pick team had failed for battle mode 0 : {e}")
        
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names for battle mode 1.
        try:
            a = Battle("Ash", "Misty")
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                a.battle_mode = 1  # Sets instance variable battlemdoe to 1
                a.pick_team()      # Pick team with Ash 111 and misty 110
                b = str(a.team1)
                c = str(a.team2)   # Uses tostring method to check if the pickteam method operate correctly
                # If team is not the same as the expected team, add error
                if b != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1" or c != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1":
                    self.verificationErrors.append(f"Wrong pokemon is picked for both team: {b} and {c}")
        # If an Exception is raised, pickteam fails and add an error.
        except Exception as e:
            self.verificationErrors.append(f"Pick team had failed for battle mode 1 : {e}")
       
        # Attempts to instantiate a Battle object using "Ash" and "Misty" as the two trainer names for battle mode 2.
        try:
            a = Battle("Ash", "Misty")
            with captured_output("1 1 1\n1 1 0") as (inp, out, err):
                # Here, Ash gets a Bulbasaur a Chamander and a Squirtle, and Misty gets a Charmander and a Bulbasaur.
                a.battle_mode = 2        # Sets instance variable battlemdoe to 2
                a.pick_team('hp', 'hp')  # Pick team with Ash 111 and misty 110, and use criterion 'hp'
                b = str(a.team1)
                c = str(a.team2)         # Uses tostring method to check if the pickteam method operate correctly
                # If team is not the same as the expected team, add error
                if b != "Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1, Charmander's HP = 7 and level = 1" or c != "Bulbasaur's HP = 9 and level = 1, Charmander's HP = 7 and level = 1":
                    self.verificationErrors.append(f"Wrong pokemon is picked for both team: {b} and {c}")
        # If an Exception is raised, pickteam fails and add an error.
        except Exception as e:
            self.verificationErrors.append(f"Pick team had failed for battle mode 2 : {e}")
    
    def test_check_winner(self):
        """ Testing the check_winner function. Will count and return number of errors if fails to past test """
        # Create a battle object and two empty teams. Then call the check winner function. If results is not draw (0), add an error
        try:
            a = Battle("Ash", "Misty") # Create Battle object
            a.team1.create_adt(0, 0)   # Create an empty team for Ash
            a.team2.create_adt(0, 0)   # Create an empty team for Misty
            b = a.check_winner()       # Check who's the winner
            # Should be a draw(0) since both team is empty
            if b != 0:
                self.verificationErrors.append(f"Should be a draw: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Check winner for battle mode 0 had fail: {e}")

        # Create a battle object and two teams. Then call the check winner function. If results is not 1, add an error
        try:
            a = Battle("Ash", "Misty") # Create Battle object
            a.team1.create_adt(1, 0)   # Create an empty team for Ash
            a.team2.create_adt(0, 0)   # Create an empty team for Misty
            with captured_output("1 1 0") as (inp, out, err):
                a.team1.choose_team(0, None) # Fill Ash's team with pokemon
            b = a.check_winner()       # Check who's the winner
            # Should be Ash(1) since he has a full team
            if b != 1:
                self.verificationErrors.append(f"Ash is the winner: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Check winner for battle mode 0 had fail: {e}")

        # Create a battle object and two teams. Then call the check winner function. If results is not 2, add an error
        try:
            a = Battle("Ash", "Misty") # Create Battle object
            a.team1.create_adt(0, 0)   # Create an empty team for Ash
            a.team2.create_adt(1, 0)   # Create an empty team for Misty
            with captured_output("1 1 0") as (inp, out, err):
                a.team2.choose_team(0, None) # Fill Misty's team with pokemon
            b = a.check_winner()       # Check who's the winner
            # Should be Misty(2) since she has a full team
            if b != 2:
                self.verificationErrors.append(f"Misty is the winner: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Check winner for battle mode 0 had fail: {e}")

    def test_print_winner(self):
        """ Testing the print_winner function. Will count and return number of errors if fails to past test """
        # Creates two empty teams and fill one team with one pokemon. Then remove the pokemon and check who's winner
        # If results is not draw, add an error
        try:
            a = Battle("Ash", "Misty") # Create Battle object
            a.team1.create_adt(6, 0)   # Create an empty team for Ash
            a.team2.create_adt(6, 0)   # Create an empty team for Misty
            b = ''
            with captured_output("1 0 0") as (inp, out, err):
                a.team1.choose_team(0, None) # fill Ash's team with pokemon
                a.team1.get_pokemon(0) # Get Ash's pokemon and then discard it to make Ash team empty
                b = a.print_winner(0)  # Check who's the winner
            # Should be a draw(0) since both team is empty
            if b != "Draw":
                self.verificationErrors.append(f"Should print Draw: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Print winner had fail: {e}")

        # Creates two empty teams and fill one team with one pokemon. Then check who is winner
        # If results is not Ash (1), add an error
        try:
            a = Battle("Ash", "Misty") # Create Battle object
            a.team1.create_adt(6, 0)   # Create an empty team for Ash
            a.team2.create_adt(6, 0)   # Create an empty team for Misty
            b = ''
            with captured_output("1 1 0") as (inp, out, err):
                a.team1.choose_team(0, None) # Fill Ash's team with pokemon
                b = a.print_winner(1)        # Check who's the winner
            # Should be Ash since she has a full team
            if b != a.team1.team_name:
                self.verificationErrors.append(f"Should print Ash's name: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Print winner had fail: {e}")

        # Creates two empty teams and fill one team with one pokemon. Then check who is winner
        # If results is not Misty (2), add an error
        try:
            a = Battle("Ash", "Misty")  # Create Battle object
            a.team1.create_adt(6, 0)    # Create an empty team for Ash
            a.team2.create_adt(6, 0)    # Create an empty team for Misty
            b = ''
            with captured_output("1 1 0") as (inp, out, err):
                a.team2.choose_team(0, None)  # Fill Misty's team with pokemon
                b = a.print_winner(2)         # Check who's the winner
            # Should be Misty since she has a full team
            if b != a.team2.team_name:
                self.verificationErrors.append(f"Should print Misty's name: {b}")
        except Exception as e:
            self.verificationErrors.append(f"Print winner had fail: {e}")

    def test_check_criterion(self):
        """ Testing the check_criterion function. Will count and return number of errors if fails to past test """
        # Tester for check_criterion for battle_mode 2 by inputting two example criterion strings into the method
        # If check_criterion fail to execute, add an error
        try:
            a = Battle("Ash", "Misty") # Create a Battle object
            a.criterion1 = "hp"        # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"       # Set its criterion2 instance var to lvl
            C1 = a.check_criterion(a.criterion1)  # Check if criterion1 is either hp, lvl, atk, def or spd
            C2 = a.check_criterion(a.criterion2)  # Check if criterion2 is either hp, lvl, atk, def or spd
        except Exception as e:
            self.verificationErrors.append(f"check_criterion cannot execute: {e}")
            return
        
        # Results return have to be True since criterion1 is valid, if False add error
        try:
            assert C1 != False
            # if criterion1 is not either hp, lvl, atk, def or spd raise AssertionError
        except AssertionError:
            self.verificationErrors.append(f"Criterion of the team1 should be hp but its: {a.criterion1}")
        
        # Results return have to be True since criterion2 is valid, if False add error
        try:
            assert C2 != False
             # if criterion2 is not either hp, lvl, atk, def or spd raise AssertionError
        except AssertionError:
            self.verificationErrors.append(f"Criterion of team2 should be lvl but its: {a.criterion2}")

    def test_place_back(self):
        """ Testing the place_back function. Will count and return number of errors if fails to past test """
        # Creates an empty team with battle mode 0 and attempt to place the pokemon back into the team
        # Then checks if the team has the added pokemon which was placed correctly. If not add error
        try:
            a = Battle("Ash", "Misty") # Create a Battle object
            a.battle_mode = 0          # Sets the Battle_mode to 0
            a.team1.create_adt(6, a.battle_mode)  # Create an empty team for Ash
            c = Charmander()           # Create a Charmander object
            a.place_back(c, 1)         # Place Charmander object into Ash's team
            # Checks if Ash's team has this pokemon, if not add an error
            if len(a.team1.team) != 1 and not isinstance(a.team1.get_pokemon(0), Charmander):
                self.verificationErrors.append(
                    f"The place back method didn't place back pokemon correctly {c.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Place back method for Array stack had failed: {e}")

        # Creates an empty team with battle mode 1 and attempt to place the pokemon back into the team
        # Then checks if the team has the added pokemon which was placed correctly. If not add error
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 1           # Set the Battle_mode to 1
            a.team1.create_adt(6, a.battle_mode)  # Create an empty team for Ash
            c = Charmander()            # Create a Charmander object
            a.place_back(c, 1)          # Place Charmander object into Ash's team
            # Checks if Ash's team has this pokemon, if not add an error
            if len(a.team1.team) != 1 and not isinstance(a.team1.get_pokemon(1), Charmander):
                self.verificationErrors.append(
                    f"The place back method didn't place back pokemon correctly {c.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Place back method for Circular queue had failed: {e}")

        # Creates an empty team with battle mode 2 and criterion "hp" and then attempt to place the pokemon back into the team
        # Then checks if the team has the added pokemon which was placed correctly. If not add error
        try:
            a = Battle("Ash", "Misty") # Create a Battle object
            a.battle_mode = 2          # Set the Battle_mode to 2
            a.team1.create_adt(6, a.battle_mode) # Create an empty team for Ash
            a.criterion1 = "hp"        # Set criterion1 to hp
            c = Charmander()           # Create a Charmander object
            a.pokemon1 = ListItem(c, c.get_hp())  # Create a ListItem object that stores a Charmander object
            a.place_back(a.pokemon1.value, 1)     # Place Charmander object into Ash's team
            # Checks if Ash's team has this pokemon, if not add an error
            if len(a.team1.team) != 1 and not isinstance(a.team1.get_pokemon(2,"hp").value, Charmander):
                self.verificationErrors.append(f"The place back method didn't place back pokemon correctly {c.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Place back method for Sorted array list had failed: {e}")

    def test_battle(self):
        """ Testing the battle function. Will count and return number of errors if fails to past test """
        # Tests battle method using battle mode 0 which is ArrayStack
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 0           # Set the battle_mode to 0
            with captured_output("1 1 1\n0 0 1") as (inp, out, err):
                # Create the teams so such that Ash wins and Misty loses
                a.pick_team()
                winner = a.battle()     # Execute the battle
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be executed for battle_mode 0: {e}")
            return
        try:
            assert winner == 1  # Assert the winner as team1, if not add an error
        except AssertionError:
            self.verificationErrors.append(f"It should have been Ash that won but it shows that team{a.team2.team_name} won")

        # Tests battle method using battle mode 1 which is CircularQueue
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("0 0 1\n1 1 1") as (inp, out, err):
                # Create the teams so that Misty wins and Ash loses
                a.pick_team()
                winner = a.battle()     # Execute the battle
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be executed for battle_mode 1: {e}")
            return
        try:
            assert winner == 2  # Assert the winner as team2, if not add an error
        except AssertionError:
            self.verificationErrors.append(f"It should have been Ash that won but it shows that team{a.team2.team_name} won")

        # Tests battle method using battle mode 2 which is ArraySortedList
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 2           # Set the battle_mode to 2
            a.criterion1 = "hp"         # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"        # Set its criterion2 instance var to lvl
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create the team so both Ash and Misty ended the battle as draw
                a.pick_team("hp","lvl")
                winner = a.battle()     # Execute the battle
        except Exception as e:
            self.verificationErrors.append(f"Battle could not be executed for battle_mode 2: {e}")
            return
        try:
            assert winner == 0  # Assert the results as a draw, if not add an error
        except AssertionError:

            if winner == 1:
                name = a.team1.team_name
            else:
                name = a.team2.team_name

            self.verificationErrors.append(f"It should have been a draw but it shows that {name} won")

    def test_battle_to_death(self):
        """ Testing the battle_to_death function. Will count and return number of errors if fails to past test """
        # Tests battle_to_Death method using battle mode 0 which is ArrayStack
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 0           # Set the battle_mode to 0
            with captured_output("1 0 0\n0 1 0") as (inp, out, err):
                # Create the teams so that Ash's Charmander should kill Misty's Bulbasaur
                a.pick_team()
            pokemonteam2 = str(a.team2.team) # pokemonteam2 = Misty's team attribute
            a.battle_to_death()  # Execute 1 round of battle
        except Exception as e:
            self.verificationErrors.append(f"Battle_to_death could not be executed for battle_mode 0: {e}")
            return
        # Compares the aftermath of the battle to the team before the round and then add error if they are the same
        try:
            check = str(a.team2.team) == pokemonteam2  # Check if Misty's team is the same
            assert check == False  # Assert that the team should be different, if not different add an error
        except AssertionError:
            self.verificationErrors(f"Misty's team should have been dealt some dmg but it did not")

        # Tests battle_to_Death method using battle mode 1 which is CircularQueue
        try:
            a = Battle("Ash", "Misty")  # Create a Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("1 0 0\n0 1 0") as (inp, out, err):
                # Create the teams so that Ash's Charmander should kill Misty's Bulbasaur
                a.pick_team()
            pokemonteam2 = str(a.team2.team) # pokemonteam2 = Misty's team attribute
            a.battle_to_death()  # Execute 1 round of battle
        except Exception as e:
            self.verificationErrors.append(f"Battle_to_death could not be executed for battle_mode 1: {e}")
            return
        # Compares the aftermath of the battle to the team before the round and then add error if they are the same
        try:
            check = str(a.team2.team) == pokemonteam2  # Check if Misty's team is the same
            assert check == False  # Assert that the team should be different, if not different add an error
        except AssertionError:
            self.verificationErrors(f"Misty's team should have been dealt some dmg but it did not")

        # Tests battle_to_Death method using battle mode 2 which is ArraySortedList
        try:
            a = Battle("Ash", "Misty") # Create the Battle object
            a.battle_mode = 2     # Set the battle_mode to 2
            a.criterion1 = "hp"   # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"  # Set its criterion2 instance var to lvl
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create a team so that Misty's Charmander should kill Ash's Bulbasaur so check if the Ash's team is still the same after battle
                a.pick_team("hp", "lvl")
            pokemonteam1 = str(a.team1.team) # pokemonteam1 = Ash's team attribute
            a.battle_to_death()  # Execute 1 round of battle
        except Exception as e:
            self.verificationErrors.append(f"Battle_to_death could not be executed for battle_mode 2: {e}")
            return
        # Compares the aftermath of the battle to the team before the round and then add error if they are the same
        try:
            check = str(a.team1.team) == pokemonteam1 # Check if Ash's team is still the same
            assert check == False  # Assert that the team should be different
        except AssertionError:
            self.verificationErrors(f"Misty's team should have been dealt some dmg but it did not")

    def test_update_key(self):
        """ Testing the update_key function. Will count and return number of errors if fails to past test """
        # Tests the update_key method using battle mode 2 to ensure the key of the ListItem objects in the ArraySortedList are updated
        try:
            a = Battle("Ash", "Misty") # Create the Battle object
            a.battle_mode = 2     # Set the battle_mode to 2
            a.criterion1 = "hp"   # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"  # Set its criterion2 instance var to lvl
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create the teams with criterion "hp" and "lvl" respectively
                a.pick_team("hp", "lvl")
            a.pokemon1 = a.team1.get_pokemon(2,"hp")  # Get the ListItem element that contains a Pokemon object in Ash's team
            prev_hp = a.pokemon1.key    # Get the key of the pokemon which is its hp value
            a.pokemon1.value.set_hp(2)  # Change the value of the pokemon's hp to 2
            a.update_key(1)          # Update the pokemon1's key for team 1
            aft_hp = a.pokemon1.key  # Get the key of the pokemon which is its hp value
        except Exception as e:
            self.verificationErrors.append(f"update_key() for battle_mode 2 has failed: {e}")
            return
        # If the new hp received from the key is the same as before, add an error
        try:
            assert aft_hp != prev_hp  # Assert that the pokemon's key value before and after are different
        except AssertionError:
            self.verificationErrors.append(f"Ash's Bulbsaur's hp should be 2 but its {aft_hp}")

    def test_level_up(self):
        """ Testing the level_up function. Will count and return number of errors if fails to past test """
        # Test the level_up method for battle mode 0 which is ArrayStack
        try:
            a = Battle("Ash", "Misty") # Create the Battle object
            a.battle_mode = 0          # Set the battle_mode to 0
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create the teams
                a.pick_team()
            a.pokemon2 = a.team2.get_pokemon(0)  # Get Misty's Charmander
            prev_team = a.team2.team             # prev_team = Misty's team attibutes
            prev_level = a.pokemon2.get_level()  # prev_level = Misty's Charmander level
            a.level_up(a.pokemon2, 2)            # Execute the level up method
            aft_team = a.team2.team              # aft_team = Misty's team attibutes
            aft_level = a.pokemon2.get_level()   # aft_level = Misty's Charmander level
        except Exception as e:
            self.verificationErrors.append(f"update_key() for battle_mode 0 has failed: {e}")
            return
        # If the new level after level up is the same as previous level, add an error
        try:
            assert aft_level != prev_level and aft_team == prev_team # Assert that Charmanders level before and after are different and the teams are the same
        except AssertionError:
                self.verificationErrors.append(f"Misty's Charmander's level should be 2 but its {aft_level} and Charmander is not put back into the team")

        # Test the level_up method for battle mode 1 which is CirculaQueue
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # create the teams
                a.pick_team()
            a.pokemon2 = a.team2.get_pokemon(1)  # Get Misty's Charmander
            prev_team = a.team2.team             # prev_team = Misty's team attibutes
            prev_level = a.pokemon2.get_level()  # prev_level = Misty's Charmander level
            a.level_up(a.pokemon2, 2)            # Execute the level up method
            aft_team = a.team2.team              # aft_team = Misty's team attibutes
            aft_level = a.pokemon2.get_level()   # aft_level = Misty's Charmander level
        except Exception as e:
            self.verificationErrors.append(f"update_key() for battle_mode 2 has failed: {e}")
            return
        # If the new level after level up is the same as previous level, add an error
        try:
            assert aft_level != prev_level and aft_team == prev_team # Assert that Charmanders level before and after are different and the teams are the same
        except AssertionError:
                self.verificationErrors.append(f"Misty's Charmander's level should be 2 but its {aft_level} and Charmander is not put back into the team")

        # Test the level_up method for battle mode 2 which is ArraySortedList
        try:
            a = Battle("Ash", "Misty") # Create the Battle object
            a.battle_mode = 2          # Set the battle_mode to 2
            a.criterion1 = "hp"        # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"       # Set its criterion2 instance var to lvl
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create the teams
                a.pick_team("hp", "lvl")
            a.pokemon2 = a.team2.get_pokemon(2, "hp")  # Get Misty's Charmander
            prev_team = a.team2.team                   # prev_team = Misty's team attibutes
            prev_level = a.pokemon2.key                # prev_level = Misty's Charmander level
            a.level_up(a.pokemon2.value,2)             # Execute the level up method
            aft_team = a.team2.team                    # aft_team = Misty's team attibutes
            aft_level = a.pokemon2.key                 # aft_level = Misty's Charmander level
        except Exception as e:
            self.verificationErrors.append(f"level_up() for battle_mode 2 has failed: {e}")
            return
        # If the new level after level up is the same as previous level, add an error
        try:
            assert aft_level != prev_level and aft_team == prev_team  # Assert that Charmanders level before and after are different and the teams are the same
        except AssertionError:
            self.verificationErrors.append(f"Misty's Bulbasaur's level should be 2 but its {aft_level} and Bulbsaur is not put back into the team")

    def test_attacking(self):
        """ Testing the attacking function. Will count and return number of errors if fails to past test """
        # Test attacking method for battle mode 0 which is ArrayStack and initiate a pokemon attacking another
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 0           # Set the battle_mode to 0
            with captured_output("1 0 0\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.attacking(a.pokemon1, a.pokemon2)          # Charmander attacks Squirtle
            # A healthy Squirtle can't be fainted by Charmander in one round, so if Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(f"Squirtle shouldn't be fainted : {b}")
        except Exception as e:
            self.verificationErrors.append(f"attacking method for battle mode 0 had failed: {e}")
        
        # Test attacking method for battle mode 1 which is CircularQueue and initiate a pokemon attacking another
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("1 0 0\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.attacking(a.pokemon1, a.pokemon2)          # Charmander attacks Squirtle
            # A healthy Squirtle can't be fainted by Charmander in one round, so if Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(f"Squirtle shouldn't be fainted : {b}")
        except Exception as e:
            self.verificationErrors.append(f"attacking method for battle mode 1 had failed: {e}")
        
        # Test attacking method for battle mode 2 which is ArraySortedList and initiate a pokemon attacking another
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 2           # Set the battle_mode to 2
            a.criterion1 = "hp"         # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"        # Set its criterion2 instance var to lvl
            with captured_output("1 0 0\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team('hp', 'lvl')  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode,"hp")  # Get Ash Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode,"hp")  # Get Misty's Squirtle
            b = a.attacking(a.pokemon1.value, a.pokemon2.value)   # Charmander attack Squirtle
            # A healthy Squirtle can't be fainted by Charmander in one round, so if Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(f"Squirtle shouldn't be fainted : {b}")
        except Exception as e:
            self.verificationErrors.append(f"attacking method for battle mode 2 had failed: {e}")

    def test_round(self):
        """ Testing the round function. Will count and return number of errors if fails to past test """
        # Test round method which is one pokemon attacking the other with battle mode 0 which is ArrayStack
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 0           # Set the battle_mode to 0
            with captured_output("1 0 0 \n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round(a.pokemon1, a.pokemon2, 1, 2)        # Charmander is faster so Charmander attack first
            # Charmander can't faint a healthy Squirtle in one round but Squirtle can faint Charmander in one round
            # If Charmader not fainted, add an error
            if b != True:
                self.verificationErrors.append(f"Charmander should be fainted in the round(True): {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round method for battle mode 0 had failed: {e}")

        # Test round method which is one pokemon attacking the other with battle mode 1 which is CircularQueue
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("1 0 0 \n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round(a.pokemon1, a.pokemon2, 1, 2)  # Charmander is faster so Charmander attack first
            # Charmander can't faint a healthy Squirtle in one round but Squirtle can faint Charmander in one round
            # If Charmader not fainted, add an error
            if b != True:
                self.verificationErrors.append(f"Charmander should be fainted in the round(True): {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round method for battle mode 1 had failed: {e}")

        # Test round method which is one pokemon attacking the other with battle mode 2 which is ArraySortedList
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 2           # Set the battle_mode to 2
            a.criterion1 = "hp"         # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"        # Set its criterion2 instance var to lvl
            with captured_output("1 0 0 \n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team('hp', 'lvl')  # Ash get a Charmander and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Charmander
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round(a.pokemon1.value, a.pokemon2.value, 1, 2)  # Charmander is faster so Charmander attack first
            # Charmander can't faint a healthy Squirtle in one round but Squirtle can faint Charmander in one round
            # If Charmader not fainted, add an error
            if b != True:
                self.verificationErrors.append(f"Charmander should be fainted in the round(True): {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round method for battle mode 2 had failed: {e}")

    def test_round_together(self):
        """ Testing the round_together function. Will count and return number of errors if fails to past test """
        # Test round_together method which is when two pokemons attack and defend simultaneously with battle mode 0
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 0           # Set the battle_mode to 0
            with captured_output("0 0 1\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Squirtle and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Squirtle
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round_together(a.pokemon1,
                                 a.pokemon2)  # Both attack together Squirtle can't fainted each other in one round
            # If Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(
                    f"A round together between two healthy Squirtle can't had one fainted(True) : {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round together method for battle mode 0 had failed: {e}")
        
        # Test round_together method which is when two pokemons attack and defend simultaneously with battle mode 1
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 1           # Set the battle_mode to 1
            with captured_output("0 0 1\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team()  # Ash get a Squirtle and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Squirtle
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round_together(a.pokemon1,
                                 a.pokemon2)  # Both attack together Squirtle can't fainted each other in one round
            # If Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(
                    f"A round together between two healthy Squirtle can't had one fainted(True) : {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round together method for battle mode 1 had failed: {e}")
        
        # Test round_together method which is when two pokemons attack and defend simultaneously with battle mode 2
        try:
            a = Battle("Ash", "Misty")  # Create the Battle object
            a.battle_mode = 2           # Set the battle_mode to 2
            a.criterion1 = "hp"         # Set its criterion1 instance var to hp
            a.criterion2 = "lvl"        # Set its criterion2 instance var to lvl
            with captured_output("0 0 1\n0 0 1") as (inp, out, err):
                # Create the team
                a.pick_team('hp', 'lvl')  # Ash get a Squirtle and Misty get a Squirtle
            a.pokemon1 = a.team1.get_pokemon(a.battle_mode)  # Get Ash's Squirtle
            a.pokemon2 = a.team2.get_pokemon(a.battle_mode)  # Get Misty's Squirtle
            b = a.round_together(a.pokemon1.value,
                                 a.pokemon2.value)  # Both attack together Squirtle can't fainted each other in one round
            # If Squirtle fainted, add an error
            if b != False:
                self.verificationErrors.append(
                    f"A round together between two healthy Squirtle can't had one fainted(True) : {b}")
        except Exception as e:
            self.verificationErrors.append(f"Round together method for battle mode 2 had failed: {e}")

    def test_check_who_fainted(self):
        """ Testing the check_who_fainted function. Will count and return number of errors if fails to past test """
        # Create two teams and set one pokemon hp to 0 so it faints then check if any pokemon has fainted
        try:
            a = Battle("Ash", "Misty") # Create the Battle object
            a.battle_mode = 0          # Set the battle_mode to 0
            with captured_output("1 1 1\n1 1 1") as (inp, out, err):
                # Create the teams
                a.pick_team()
            a.pokemon1 = a.team1.get_pokemon(0)  # Get Ash's Charmander
            a.pokemon2 = a.team2.get_pokemon(0)  # Get Misty's Charmander
            a.pokemon1.set_hp(0)                 # Set Ash's Charmander's hp value to 0
            check = a.check_who_fainted(a.pokemon1,a.pokemon2) # Check if a.pokemon1 or a.pokemon2 fainted
        except Exception as e:
            self.verificationErrors.append(f"check_who_fainted method has failed: {e}")
            return
        
        # If no pokemon has fainted, add an error as pokemon1 should have fainted
        try:
            assert check == True # assert that one pokemon has fainted
        except AssertionError:
                self.verificationErrors.append(f"Ash's pokemon should have fainted but its hp is {a.pokemon1.get_hp()}")


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(TestBattle)
    unittest.TextTestRunner(verbosity=0).run(suite)