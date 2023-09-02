import unittest
from tester_base import TesterBase
from pokemon import Charmander, Squirtle, Bulbasaur
from Missingno import MissingNo

"""
Summary: A testing file to test all the methods in the pokemon.py module
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class TestPokemon(TesterBase):
    def test_init_(self):
        """ Testing the init constructor function. Will count and return number of errors if fails to past test """
        # Try to create a Charmander object
        # If exception raised, init failed and add an error
        try:
            c = Charmander()  # Create a Chamander object
        except Exception as e:
            self.verificationErrors.append(f"Charmander could not be instantiated: {str(e)}.")

        # Try create a Squirtle object
        # If exception raised, init failed and add an error
        try:
            c = Squirtle()    # Create a Squirtle object
        except Exception as e:
            self.verificationErrors.append(f"Squirtle could not be instantiated: {str(e)}.")

        # Try create a Bulbasaur object
        # If exception raised, init failed and add an error
        try:
            c = Bulbasaur()   # Create a Bulbasaur object
        except Exception as e:
            self.verificationErrors.append(f"Bulbasaur could not be instantiated: {str(e)}.")

        # Try create a Missingno onject
        # If exception raised, init failed and add an error
        try:
            c = MissingNo()   # Create a MissingNo object
        except Exception as e:
            self.verificationErrors.append(f"MissingNo could not be instantiated: {str(e)}.")

    def test_get_name(self):
        """ Testing the get_name function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and get its name
        # If exception raised, get_name failed and add an error
        try:
            c = Charmander().get_name() # Create a Chamander object and get its name
            if c != "Charmander":       # If the name is not Charmander, it's the wrong name so add an error
                self.verificationErrors.append(f"Charmander is getting the wrong name: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Name failed: {str(e)}.")
        
        # Try create a Squirtle and get its name
        # If exception raised, get_name failed and add an error
        try:
            c = Squirtle().get_name()   # Create a Squirtle object and get its name
            if c != "Squirtle":         # If the name is not Squirtle, it's the wrong name so add an error
                self.verificationErrors.append(f"Squirtle is getting the wrong name: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Name failed: {str(e)}.")
        
        # Try create a Bulbasaur and get its name
        # If exception raised, get_name failed and add an error
        try:
            c = Bulbasaur().get_name()  # Create a Bulbasaur object and get its name
            if c != "Bulbasaur":        # If the name is not Bulbasaur, it's the wrong name so add an error
                self.verificationErrors.append(f"Bulbasaur is getting the wrong name: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Name failed: {str(e)}.")
        
        # Try create a MissingNo and get its name
        # If exception raised, get_name failed and add an error
        try:
            c = MissingNo().get_name()  # Create a MissingNo object and get its name
            if c != "MissingNo":        # If the name is not MissingNo, it's the wrong name so add an error
                self.verificationErrors.append(f"MissingNo is getting the wrong name: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Name failed: {str(e)}.")

    def test_get_speed(self):
        """ Testing the get_speed function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and get its speed stat
        # If exception raised, get_speed failed and add an error
        try:
            c = Charmander().get_speed()
            if c != 8:  # If the speed is not 8, it's the wrong speed so add an error
                self.verificationErrors.append(f"Charmander is getting the wrong speed: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get speed failed: {str(e)}.")
       
        # Try create a Squirtle and get its speed stat
        # If exception raised, get_speed failed and add an error
        try:
            c = Squirtle().get_speed()
            if c != 7:  # If the speed is not 7, it's the wrong speed so add an error
                self.verificationErrors.append(f"Squirtle is getting the wrong speed: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get speed failed: {str(e)}.")
        
        # Try create a Bulbasaur and get its speed stat
        # If exception raised, get_speed failed and add an error
        try:
            c = Bulbasaur().get_speed()
            if c != 7: # If the speed if not 7, it's the wrong speed so add an error
                self.verificationErrors.append(f"Bulbasaur is getting the wrong speed: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get speed failed: {str(e)}.")
        
        # Try create a MissingNo and get its speed stat
        # If exception raised, get_speed failed and add an error
        try:
            c = MissingNo().get_speed()
            if c != 7:  # If the speed is not 7, it's the wrong speed so add an error
                self.verificationErrors.append(f"MissingNo is getting the wrong speed: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get speed failed: {str(e)}.")

    def test_get_atk_dmg(self):
        """ Testing the get_atk_dmg function. Will count and return number of errors if fails to past test """
        # Try create a Charmander object and get its attack stat
        # If exception raised, get_atk_dmg failed and add an error
        try:
            c = Charmander().get_atk_dmg()  # Create a Charmander object and get its attack
            if c != 7:                      # If the attack is not 7, it's the wrong atk dmg so add an error
                self.verificationErrors.append(f"Charmander is getting the wrong attack dmg: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get atk dmg failed: {str(e)}.")
        
        # Try create a Squirtle object and get its attack stat
        # If exception raised, get_atk_dmg failed and add an error
        try:
            c = Squirtle().get_atk_dmg()    # Create a Squirtle object and get its attack
            if c != 4:                      # If the attack is not 4, it's the wrong atk dmg so add an error
                self.verificationErrors.append(f"Squirtle is getting the wrong attack dmg: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get atk dmg failed: {str(e)}.")

        # Try create a Bulbasaur object and get its attack stat
        # If exception raised, get_atk_dmg failed and add an error
        try:
            c = Bulbasaur().get_atk_dmg()   # Create a Bulbasaur object and get its attack
            if c != 5:                      # If the attack is not 5, it's the wrong atk dmg so add an error
                self.verificationErrors.append(f"Bulbasaur is getting the wrong attack dmg: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get atk dmg failed: {str(e)}.")

        # Try create a Squirtle object and get its attack stat
        # If exception raised, get_atk_dmg failed and add an error
        try:
            c = MissingNo().get_atk_dmg()   # Create a MissingNo object and get its attack
            if c != 5:                      # If the attack is not 5, it's the wrong atk dmg so add an error
                self.verificationErrors.append(f"MissingNo is getting the wrong attack dmg: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get atk dmg failed: {str(e)}.")

    def test_get_defence(self):
        """ Testing the get_defence function. Will count and return number of errors if fails to past test """
        # Try create a Charmander object and get its defence stat
        # If exception raised, get_defence failed and add an error
        try:
            c = Charmander().get_defence()  # Create a Charmander object and get its defence
            if c != 4:                      # If the defence is not 4, it's the wrong defence so add an error
                self.verificationErrors.append(f"Charmander is getting the wrong defence: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get defence failed: {str(e)}.")

        # Try create a Squirtle object and get its defence stat
        # If exception raised, get_defence failed and add an error
        try:
            c = Squirtle().get_defence()    # Create a Squirtle object and get its defence
            if c != 7:                      # If the defence is not 7, it's the wrong defence so add an error
                self.verificationErrors.append(f"Squirtle is getting the wrong defence: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get defence failed: {str(e)}.")

        # Try create a Bulbasaur object and get its defence stat
        # If exception raised, get_defence failed and add an error
        try:
            c = Bulbasaur().get_defence()   # Create a Bulbasaur object and get its defence
            if c != 5:                      # If the defence is not 5, it's the wrong defence so add an error
                self.verificationErrors.append(f"Bulbasaur is getting the wrong defence: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get defence failed: {str(e)}.")

        try:
            c = MissingNo().get_defence()   # Create a MissingNo object and get its defence
            if c != 5:                      # If the defence is not 5, it's the wrong defence so add an error
                self.verificationErrors.append(f"MissingNo is getting the wrong defence: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get defence failed: {str(e)}.")

    def test_cal_dmg_taken(self):
        """ Testing the cal_dmg_taken function. Will count and return number of errors if fails to past test """
        p1 = Charmander()  # Create a Chamander object that acts as the enemy
        
        # Try create a Charmander object and p1 attacks it
        # If exception raised, cal_dmg_taken failed and add an error
        try:
            c = Charmander().cal_dmg_taken(p1)  # Create a Charmander object and fight with p1
            if c != 7:                          # If the dmg taken is not 7, it's the wrong damage returned so add an error
                self.verificationErrors.append(f"Charmander is getting the wrong damage taken against another Charmander: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Cal damage taken failed: {str(e)}.")

        # Try create a Squirtle object and p1 attacks it
        # If exception raised, cal_dmg_taken failed and add an error
        try:
            c = Squirtle().cal_dmg_taken(p1)  # Create a Squirtle object and fight with p1
            if c != 1:                        # If the dmg taken is not 1, it's the wrong damage returned so add an error
                self.verificationErrors.append(f"Squirtle is getting the wrong damage taken against another Charmander: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Cal damage taken failed: {str(e)}.")

        # Try create a Bulbasaur object and p1 attacks it
        # If exception raised, cal_dmg_taken failed and add an error
        try:
            c = Bulbasaur().cal_dmg_taken(p1)  # Create a Bulbasaur object and fight with p1
            if c != 14:                        # If the dmg taken is not 14, it's the wrong damage returned so add an error
                self.verificationErrors.append(f"Bulbasaur is getting the wrong damage taken against another Charmander: {str(c)}.")
        except Exception as e:
            self.verificationErrors.append(f"Cal damage taken failed: {str(e)}.")

        # Try create a MissingNo object and p1 attacks it
        # If exception raised, cal_dmg_taken failed and add an error
        # No testing for whether damage is a fix number as superpower could result in zero damage taken
        try:
            c = MissingNo().cal_dmg_taken(p1)  # Create a MissingNo object and fight with p1
        except Exception as e:
            self.verificationErrors.append(f"Cal damage taken failed for MissingNo: {str(e)}.")

    def test_str_(self):
        """ Testing the __str__ function. Will count and return number of errors if fails to past test """
        # Try create a Charmander object and then call the __str__ method to print it out
        # If exception raised, __str__ failed and add an error
        try:
            c = Charmander()  # Create a Chamander object
        except Exception as e:
            self.verificationErrors.append(f"Charmander could not be instantiated: {str(e)}.")
            return
        # Compares the result from the __str__ method to the expected string output. If not equal, add an eror
        try:
            s = str(c)  # s = Charmander object(c) attributes
            if s != "Charmander's HP = 7 and level = 1":  # If s doesnt contain the right string, add an error
                self.verificationErrors.append(f"String method did not return correct string: {s}")
        except Exception as e:
            self.verificationErrors.append(f"String method failed. {e}")
        
        # Try create a Squirtle object and then call the __str__ method to print it out
        # If exception raised, __str__ failed and add an error
        try:
            c = Squirtle()    # Create a Squirtle object
        except Exception as e:
            self.verificationErrors.append(f"Squirtle could not be instantiated: {str(e)}.")
            return
        # Compares the result from the __str__ method to the expected string output. If not equal, add an eror
        try:
            s = str(c)  # s = Squirtle object(c) attributes
            if s != "Squirtle's HP = 8 and level = 1":  # If s doesnt contain the right string, add an error
                self.verificationErrors.append(f"String method did not return correct string: {s}")
        except Exception as e:
            self.verificationErrors.append(f"String method failed. {e}")
           
        # Try create a Bulbasaur object and then call the __str__ method to print it out
        # If exception raised, __str__ failed and add an error   
        try:
            c = Bulbasaur()    # Create a Bulbasaur object
        except Exception as e:
            self.verificationErrors.append(f"Bulbasaur could not be instantiated: {str(e)}.")
            return
        # Compares the result from the __str__ method to the expected string output. If not equal, add an eror
        try:
            s = str(c)  # s = Bulbasaur object(c) attributes
            if s != "Bulbasaur's HP = 9 and level = 1":  # If s doesnt contain the right string, add an error
                self.verificationErrors.append(f"String method did not return correct string: {s}")
        except Exception as e:
            self.verificationErrors.append(f"String method failed. {e}")

        try:
            c = MissingNo()     # Create a MissingNo object
        except Exception as e:
            self.verificationErrors.append(f"MissingNo could not be instantiated: {str(e)}.")
            return
        try:
            s = str(c)  # s = MissingNo object(c) attributes
            if s != "MissingNo's HP = 8 and level = 1":  # if s doesnt contain the right string, add an error
                self.verificationErrors.append(f"String method did not return correct string: {s}")
        except Exception as e:
            self.verificationErrors.append(f"String method failed. {e}")


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(TestPokemon)
    unittest.TextTestRunner(verbosity=0).run(suite)