import unittest
from array_sorted_list import ArraySortedList
from poke_team import PokeTeam
from queue_adt import CircularQueue
from sorted_list import ListItem
from tester_base import TesterBase, captured_output
from pokemon import Charmander, Bulbasaur, Squirtle
from stack_adt import ArrayStack
from Missingno import MissingNo

"""
Summary: A testing file to test all methods inside the pokemon_team.py module
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class TestPokeTeam(TesterBase):
    def test_key_criterion(self):
        """ Testing the key_criterion function. Will count and return number of errors if fails to past test """
        a = Charmander()     # Create a Charmander object
        b = PokeTeam("Ash")  # Create a PokeTeam object

        # Inputs hp as the criterion and compares output, if output is not the correct hp value then add an error
        try:
            c = b.key_criterion(a, "hp")   # Instantiate c by executing key_criterion with a, "hp" parameters
            if c != a.get_hp():            # If c is not equal to the Charmander's hp value, it adds an error
                self.verificationErrors.append(f"Key criterion should return hp: {c}")
        except Exception as e:
            self.verificationErrors.append(f"Key_criterion failed for hp. {e}") # If an Exception is raised, it adds an error.

        # Inputs lvl as the criterion and compares output, if output is not the correct lvl value then add an error
        try:
            c = b.key_criterion(a, "lvl")  # Instantiate c by executing key_criterion with a, "lvl" parameters
            if c != a.get_level():         # If c is not equal to the Charmander's level value, it adds an error
                self.verificationErrors.append(f"Key criterion should return level: {c}")
        except Exception as e:
            self.verificationErrors.append(f"Key_criterion failed for level. {e}") # If an Exception is raised, it adds an error.

        # Inputs atk as the criterion and compares output, if output is not the correct atk value then add an error
        try:
            c = b.key_criterion(a, "atk")  # Instantiate c by executing key_criterion with a, "atk" parameters
            if c != a.get_atk_dmg():       # If c is not equal to the Charmander's attack damage value, it adds an error
                self.verificationErrors.append(f"Key criterion should return atk: {c}")
        except Exception as e:
            self.verificationErrors.append(f"Key_criterion failed for attack. {e}") # If an Exception is raised, it adds an error.

        # Inputs def as the criterion and compares output, if output is not the correct def value then add an error
        try:
            c = b.key_criterion(a, "def")  # Instantiate c by executing key_criterion with a, "def" parameters
            if c != a.get_defence():       # If c is not equal to the Charmander's defence value, it adds an error
                self.verificationErrors.append(f"Key criterion should return defence: {c}")
        except Exception as e:
            self.verificationErrors.append(f"Key_criterion failed for defense. {e}") # If an Exception is raised, it adds an error.

        # Inputs spd as the criterion and compares output, if output is not the correct spd value then add an error
        try:
            c = b.key_criterion(a, "spd")  # Instantiate c by executing key_criterion with a, "spd" parameters
            if c != a.get_speed():         # If c is not equal to the Charmander's speed value, it adds an error
                self.verificationErrors.append(f"Key criterion should return speed: {c}")
        except Exception as e:
            self.verificationErrors.append(f"Key_criterion failed for speed. {e}") # If an Exception is raised, it adds an error.

    def test_choose_team(self):
        """ Testing the choose_team function. Will count and return number of errors if fails to past test """
        # Executes choose_team method using battle mode 0 and no criterion. Checks user input is valid, if not add error
        try:
            team = PokeTeam("Ash")  # Creates a PokeTeam object with trainer name "Ash"
            with captured_output("4 4 1\n1 1 1") as (inp, out, err):
                # 4 4 1 should fail, since it is too many pokemon.
                # So 1 1 1 should be the correct team.
                team.choose_team(0, None)  # Execute choose_team to create a team of pokemon
        except Exception as e:
            self.verificationErrors.append(f"Choose team method failed for Array stack battle mode: {str(e)}.") # If an Exception is raised, it adds an error.
            return
        output = out.getvalue().strip()

        # Check whether the prompt is being printed correctly and assert the output to the expected strings
        try:
            assert "is the number of Charmanders" in output
            assert "is the number of Bulbasaurs" in output
            assert "is the number of Squirtles" in output
        except AssertionError:
            self.verificationErrors.append(f"PokeTeam does not print prompt correctly.") # If an Exception is raised, it adds an error.

        # Executes choose_team method using battle mode 1 and no criterion. Checks user input is valid, if not add error
        try:
            team = PokeTeam("Ash")  # Creates a PokeTeam object with trainer name "Ash"
            with captured_output("4 4 1\n1 1 1") as (inp, out, err):
                # 4 4 1 should fail, since it is too many pokemon.
                # So 1 1 1 should be the correct team.
                team.choose_team(1, None) # Execute choose team to create a team of pokemon
        except Exception as e:
            self.verificationErrors.append(f"Choose team method failed for Circular queue battle mode: {str(e)}.") # If an Exception is raised, it adds an error.
            return
        output = out.getvalue().strip()

        # Check whether the prompt is being printed correctly and assert the output to the expected strings
        try:
            assert "is the number of Charmanders" in output
            assert "is the number of Bulbasaurs" in output
            assert "is the number of Squirtles" in output
        except AssertionError:
            self.verificationErrors.append(f"PokeTeam does not print prompt correctly.") # If an Exception is raised, it adds an error.

        # Executes choose_team method using battle mode 2 and criterion "hp". Checks user input is valid, if not add error
        try:
            team = PokeTeam("Ash")  # Create a PokeTeam object
            with captured_output("4 4 1\n1 1 1") as (inp, out, err):
                # 4 4 1 should fail, since it is too many pokemon.
                # So 1 1 1 should be the correct team.
                team.choose_team(2, "hp")  # Execute choose team to create a team of pokemon
        except Exception as e:
            self.verificationErrors.append(f"Choose team method failed for Array sorted list battle mode: {str(e)}.") # If an Exception is raised, it adds an error.
            return
        output = out.getvalue().strip()

        # Check whether the prompt is being printed correctly and assert the output to the expected strings
        try:
            assert "is the number of Charmanders" in output
            assert "is the number of Bulbasaurs" in output
            assert "is the number of Squirtles" in output
        except AssertionError:
            self.verificationErrors.append(f"PokeTeam does not print prompt correctly.") # If an Exception is raised, it adds an error.

    def test_create_adt(self):
        """ Testing the create_adt function. Will count and return number of errors if fails to past test """
        a = PokeTeam("Ash") # Creates a PokeTeam object
        # Creates the ArrayStack object for battle mode 0 and checks whether it's created correctly
        try:
            battlemode = 0
            a.create_adt(1, battlemode)  # Creates an empty team
            # Checking the type of team, if the team is not an ArrayStack, add error
            if isinstance(a, ArrayStack):
                self.verificationErrors.append(f"ADT created should be ArrayStack: {battlemode}")
        except Exception as e:
            self.verificationErrors.append(f"Create adt failed for array stack: {e}") # If an Exception is raised, it adds an error.

        # Creates the CircularQueue object for battle mode 1 and checks whether it's created correctly
        try:
            battlemode = 1
            a.create_adt(1,battlemode)   # Creates an empty team
            # Checking the type of team, if the team is not a CircularQueue, add error
            if isinstance(a, CircularQueue):
                self.verificationErrors.append(f"ADT created should be Circular Queue: {battlemode}")
        except Exception as e:
            self.verificationErrors.append(f"Create adt failed circular queue: {e}") # If an Exception is raised, it adds an error.

        # Creates the ArraySortedList object for battle mode 2 and checks whether it's created correctly
        try:
            battlemode = 2
            a.create_adt(1,battlemode)   # Creates an empty team
            # Checking the type of team, if the team is not a ArraySortedList, add error
            if isinstance(a, ArraySortedList):
                self.verificationErrors.append(f"ADT created should be ArraySortedList: {battlemode}")
        except Exception as e:
            self.verificationErrors.append(f"Create adt failed array sorted list: {e}") # If an Exception is raised, it adds an error.

    def test_who_has_fought(self):
        """ Testing the who_has_fought function. Will count and return number of errors if fails to past test """
        a = PokeTeam("Ash")  # Create a PokeTeam object
        # Creates a team, if choose_Team fail add an error
        try:
            with captured_output("1 1 1") as (inp, out, err):
                a.choose_team(2, "hp")  # Create a team of pokemon
        except Exception as e:
            self.verificationErrors.append(f"Creating the team has failed: {e}") # If an Exception is raised, it adds an error.
        
        # Assert who_has_fought result to False as not all the pokemons has fought once yet
        try:
            assert a.who_has_fought() == False # assert that not all pokemon has fought, so it returns False
        except Exception as e:
            self.verificationErrors.append(f" Not all the pokemon has fought, but it returned {str(a.who_has_fought())}") # If an Exception is raised, it adds an error.

        # Changes all the has_fought instance variable for all pokeons to True then Assert again.
        try:
            # set all the has_fought value of each pokemon in the team to True
            for i in range(len(a.team)):   # Loop through the team
                poke = a.get_pokemon(2, "hp").value  # Get the pokemon object from the team
                poke.set_has_fought(True)  # Set its has_fought to True

            assert a.who_has_fought() == True  # Assert that all pokemon has fought, so it returns True
        except AssertionError:
            self.verificationErrors.append(f"All the pokemon have fought, but it returned {str(a.who_has_fought())}") # If an Exception is raised, it adds an error.

    def test_get_pokemon(self):
        """ Testing the test_get_pokemon function. Will count and return number of errors if fails to past test """
        try:
            a=PokeTeam("Ash") # Create a PokeTeam object
            with captured_output("1 1 1") as (inp, out, err):
                a.choose_team(0, None) # Create a team of pokemon
            b=a.get_pokemon(0) # get the first pokemon
            if not isinstance(b,Charmander): # if the first pokemon is not Charmander
                self.verificationErrors.append(f"Pokemon get from ash team is wrong for Arraystack battlemode: {b.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Get pokemon had failed: {e}") # If an Exception is raised, it adds an error.

        try:
            a=PokeTeam("Ash") # Create a PokeTeam object
            with captured_output("1 1 1") as (inp, out, err):
                a.choose_team(1, None) # Create a team of pokemon
            b=a.get_pokemon(1) # get the first pokemon
            if not isinstance(b, Charmander): # if the first pokemon is not Charmander
                self.verificationErrors.append(f"Pokemon get from ash team is wrong for Circular Queue battlemode: {b.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Get pokemon had failed: {e}") # If an Exception is raised, it adds an error.

        try:
            a=PokeTeam("Ash") # Create a PokeTeam object
            with captured_output("1 1 1") as (inp, out, err):
                a.choose_team(2, "hp") # Create a team of pokemon
            b = a.get_pokemon(2,"hp").value # get the first pokemon
            if not isinstance(b, Bulbasaur): # if the first pokemon is not Bulbasaur
                self.verificationErrors.append(f"Pokemon get from ash team is wrong for ArraySortedList battlemode: {b.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Get pokemon had failed: {e}") # If an Exception is raised, it adds an error.

        try:
            t = PokeTeam("Ash")  # Create a PokeTeam object
            with captured_output("1 0 0 1") as (inp, out, err):
                t.choose_team(2, "hp")  # Create a team of pokemon

            # set all the has_fought value of each pokemon in the team to True
            for i in range(len(t.team)):  # Loop through the team
                poke = t.get_pokemon(2, "hp").value  # get the pokemon object
                poke.set_has_fought(True)  # set its has_fought to True
                poke_criterion = ListItem(poke, t.key_criterion(poke,"hp"))  # Creates ListItem object with value Squirtle object and key based on criterion
                t.team.add_tie(poke_criterion) # add back the pokemon into the team

            b = t.get_pokemon(2,"hp").value  # get the first pokemon
            if not isinstance(b, MissingNo):  # if the first pokemon is not MissingNo
                self.verificationErrors.append(f"Pokemon get from Ash team is wrong for ArraySortedList battlemode with MissingNo: {b.get_name()}")
        except Exception as e:
            self.verificationErrors.append(f"Get pokemon had failed: {e}") # If an Exception is raised, it adds an error.

    def test_check_if_team_empty(self):
        """ Testing the check_if_team_empty function. Will count and return number of errors if fails to past test """
        a = PokeTeam("Misty")  # Create a PokeTeam object
        with captured_output("1 1 1") as (inp, out, err):
            a.choose_team(0, None)  # Create a team of pokemon
        # Calls the check_if_team_empty and asserts it to False as team is created with pokemon in it so it's not empty
        try:
            b = a.check_if_team_empty()  # Check if the team is empty
            # If returned result is not False, add an error
            if b != False:
                self.verificationErrors.append(f"Misty team is not empty but it return {b} when check if team's empty")
        except Exception as e:
            self.verificationErrors.append(f"Check if team empty had failed: {e}") # If an Exception is raised, it adds an error.

        # Calls the check_if_team_empty after clearing it. Assert it to True as team is now empty
        try:
            a.team.clear()   # Clear the team so its empty
            b = a.check_if_team_empty()  # Check if the team is empty
            # If returned result is not True, add an error
            if b != True:
                self.verificationErrors.append(f"Misty team is empty but it return {b} when check if team's empty")
        except Exception as e:
            self.verificationErrors.append(f"Check if team empty had failed: {e}") # If an Exception is raised, it adds an error.

    def test_str_(self):
        """ Testing the __str__ function. Will count and return number of errors if fails to past test """
        a = PokeTeam("Misty")       # Create a PokeTeam object
        with captured_output("1 1 1") as (inp, out, err):
            a.choose_team(0, None)  # Create a team of pokemon
        # Compares output of the __str__ function to the expected string, if not the same add an error
        try:
            a = str(a)  # a = the attributes of the team
            if a != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1": # if wrong attributes
                self.verificationErrors.append(f"Wrong string is being printed for Misty: {a}")
        except Exception as e:
            self.verificationErrors.append(f"String method had failed: {e}") # If an Exception is raised, it adds an error.

    def test_assign_team(self):
        """ Testing the assign_team function. Will count and return number of errors if fails to past test """
        # Calls the assign_team function with battle mode 0. If it fails to execute, add an error
        try:
            a = PokeTeam("Misty")   # Creates a PokeTeam object
            a.create_adt(3, 0)      # Creates an empty team
        except Exception as e:
            self.verificationErrors.append(f"Array stack for misty team can't be instantiated: {e}") # If an Exception is raised, it adds an error.
            return
        # Compares output of the team to the expected string to check if pokemon was added properly, if not the same add an error
        try:
            a.assign_team(1, 1, 1)  # Try to fill in the team with 1 Charmander 1 Bulbasaur and 1 Squirtle
            a = str(a)              # a = the attributes of the team
            # Checks whether pokemon is added to the team correctly, if not add error
            if a != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1": # If wrong attributes
                self.verificationErrors.append(f"Wrong team is being assigned for Misty: {a}")
        except Exception as e:
            self.verificationErrors.append(f"Assign team had failed for Array stack : {e}") # If an Exception is raised, it adds an error.

        # Calls the assign_team function with battle mode 1. If it fails to execute, add an error
        try:
            a = PokeTeam("Misty") #Create a PokeTeam object
            a.create_adt(3, 1) #Create an empty team
        except Exception as e:
            self.verificationErrors.append(f"Circular Queue for misty team can't be instantiated: {e}") # If an Exception is raised, it adds an error.
            return
        # Compares output of the team to the expected string to check if pokemon was added properly, if not the same add an error
        try:
            a.assign_team(1, 1, 1)  # Try to fill in the team with 1 Charmander 1 Bulbasaur and 1 Squirtle
            a = str(a)              # a = the attributes of the team
            # Checks whether pokemon is added to the team correctly, if not add error
            if a != "Charmander's HP = 7 and level = 1, Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1": # If wrong attributes
                self.verificationErrors.append(f"Wrong team is being assigned for Misty: {a}")
        except Exception as e:
            self.verificationErrors.append(f"Assign team had failed for circular queue: {e}") # If an Exception is raised, it adds an error.

        # Calls the assign_team function with battle mode 2. If it fails to execute, add an error
        try:
            a = PokeTeam("Misty")   # Create a PokeTeam object
            a.create_adt(3,2)       # Create an empty team
        except Exception as e:
            self.verificationErrors.append(f"Array sorted list for misty team can't be instantiated: {e}") # If an Exception is raised, it adds an error.
            return
        # Compares output of the team to the expected string to check if pokemon was added properly, if not the same add an error
        try:
            a.assign_team(1, 1, 1, "hp")  # Try to fill in the team with 1 Charmander 1 Bulbasaur and 1 Squirtle with criterion "hp"
            a = str(a)                  # a = the attributes of the team
            # Checks whether pokemon is added to the team correctly in sorted order based on criterion, if not add error
            if a != "Bulbasaur's HP = 9 and level = 1, Squirtle's HP = 8 and level = 1, Charmander's HP = 7 and level = 1": # If wrong attributes
                self.verificationErrors.append(f"Wrong team is being assigned for Misty: {a}")
        except Exception as e:
            self.verificationErrors.append(f"Assign team had failed for Array sorted list: {e}") # If an Exception is raised, it adds an error.


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(TestPokeTeam)
    unittest.TextTestRunner(verbosity=0).run(suite)