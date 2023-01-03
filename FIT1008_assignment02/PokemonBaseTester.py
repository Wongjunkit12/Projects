import unittest
from tester_base import TesterBase
from pokemon import Charmander
from Missingno import MissingNo

""" 
Summary: A testing file to test all methods inside the pokemon_base.py module
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class TestPokemonBase(TesterBase):
    def test_init__(self):
        """ Testing the init constructor function. Will count and return number of errors if fails to past test """
        # Try creating a pokemon.py's pokemon object
        # If exception raised means failed to instantised, and add an error
        try:
            c = Charmander()  # Create a Charmander object
        except Exception as e:
            self.verificationErrors.append(f"Charmander can't be instantiate: {str(e)}.")
        
        # Try creating a Missingno(glitchmon) object
        # If exception raised means failed to instantised, and add an error
        try:
            c = MissingNo()   # Create a MissingNo object
        except Exception as e:
            self.verificationErrors.append(f"MissingNo can't be instantiate: {str(e)}.")

    def test_get_hp(self):
        """ Testing the get_hp function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try get its hp stat
        # If exception raised, means get_hp failed and add an error
        try:
            c = Charmander()  # Create a Charmander object
            hp = c.get_hp()   # hp = Charmander's hp value
            # If hp from get_hp does not return 7. add an error
            if 7 != hp:
                self.verificationErrors.append(f"Charmander didn't get the correct Hp: {str(hp)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Hp failed: {str(e)}.")
        
        # Try create a MissingNo and try get its hp stat
        # If exception raised, means get_hp failed and add an error
        try:
            c = MissingNo()  # Create a MissingNo object
            hp = c.get_hp()  # hp = MissingNo's hp value
            # If hp from get_hp does not return 8. add an error
            if 8 != hp:
                self.verificationErrors.append(f"MissingNo didn't get the correct Hp: {str(hp)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get Hp failed: {str(e)}.")

    def test_set_hp(self):
        """ Testing the set_hp function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try set its hp to 6
        # If exception raised, means set_hp failed and add an error
        try:
            c = Charmander()   # Create a Charmander object
            t = c.set_hp(6)    # Sets the hp of the Charmander to 6
            # If Charmander hp is not set to 6, add an error
            if c.get_hp()!= 6:
                self.verificationErrors.append(f"Charmander didn't set the correct Hp: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Set Hp failed: {str(e)}.")
       
        # Try create a MissingNo and try set its hp to 6
        # If exception raised, means set_hp failed and add an error
        try:
            c = MissingNo()     # Create a MissingNo object
            t = c.set_hp(6)     # Sets the hp of the MissingNo to 6
            # If MissingNo hp is not set to 6, add an error
            if c.get_hp() != 6:
                self.verificationErrors.append(f"MissingNo didn't set the correct Hp: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Set Hp failed: {str(e)}.")

    def test_get_level(self):
        """ Testing the get_level function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try get its level
        # If exception raised, means get_level failed and add an error
        try:
            c = Charmander()    # Create a Charmander object
            t = c.get_level()   # Gets the level of the Charmander object
            # If the Charmander's level from get_level is not 1, add an error
            if t != 1:
                self.verificationErrors.append(f"Charmander is getting the wrong level: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get level failed: {str(e)}.")
        
        # Try create a MissingNo and try get its level
        # If exception raised, means get_level failed and add an error
        try:
            c = MissingNo()      # Create a MissingNo object
            t = c.get_level()    # Gets the level of the MissingNo object
            # If the MissingNo's level from get_level is not 1, add an error
            if t != 1:
                self.verificationErrors.append(f"MissingNo is getting the wrong level: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get level failed: {str(e)}.")

    def test_set_level(self):
        """ Testing the set_level function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try set its level to 2
        # If exception raised, means set_level failed and add an error
        try:
            c = Charmander()     # Create a Charmander object
            t = c.set_level(2)   # Sets the level of Charmander to 2
            # If Charmander's level is not set to 2, add an error
            if c.get_level() != 2:
                self.verificationErrors.append(f"Charmander didn't set the correct level: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Set level failed: {str(e)}.")
        
        # Try create a MissingNo and try set its level to 2
        # If exception raised, means set_level failed and add an error
        try:
            c = MissingNo()      # Create a MissingNo object
            t = c.set_level(2)   # Sets the level of MissingNo to 2
            # If MissingNo's level is not set to 2, add an error
            if c.get_level() != 2:
                self.verificationErrors.append(f"MissingNo didn't set the correct level: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Set level failed: {str(e)}.")

    def test_is_fainted(self):
        """ Testing the test_is_fainted function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try set its hp to 0
        # If exception raised, means is_fainted failed and add an error
        try:
            c = Charmander()  # Create a Charmander object
            c.set_hp(0)       # Sets the hp of the Charmander to 0
            # If is_fainted() does not return False, add an error as pokemon is suppose to be fainted with 0 hp
            t = c.is_fainted()
            if t == False:    # False means Charmander is not fainted
                self.verificationErrors.append(f"Charmander didn't define as fainted when it's hp==0: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f" Is fainted failed: {str(e)}.")
        
        # Try create a MissingNo and try set its hp to 0
        # If exception raised, means is_fainted failed and add an error
        try:
            c = MissingNo()  # Create a MissingNo object
            c.set_hp(0)      # Sets the hp of the MissingNo to 0
            # If is_fainted() does not return False, add an error as pokemon is suppose to be fainted with 0 hp
            t = c.is_fainted()
            if t == False:   # False means MissingNo is not fainted
                self.verificationErrors.append(f"MissingNo didn't define as fainted when it's hp==0: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f" Is fainted failed: {str(e)}.")

    def test_get_type(self):
        """ Testing the get_type function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and try to get its type
        # If exception raised, means get_type failed and add an error
        try:
            t = Charmander().get_poke_type()  # Create a Charmander object and get its poke_type
            # If the type returned by the get_type is not Fire, add an error
            if t != "Fire":
                self.verificationErrors.append(f"Charmander didn't get the correct type: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get type failed: {str(t)}.")
        
        # Try create a MissingNo and try to get its type
        # If exception raised, means get_type failed and add an error
        try:
            t = MissingNo().get_poke_type()  # Create a MissingNo object and get its poke_type
            # If the type returned by the get_type is not Normal, add an error
            if t != "Normal": #
                self.verificationErrors.append(f"MissingNo didn't get the correct type: {str(t)}.")
        except Exception as e:
            self.verificationErrors.append(f"Get type failed: {str(t)}.")

    def test_get_has_fought(self):
        """ Testing the get_has_fought function. Will count and return number of errors if fails to past test """
        # Try create a Charmander and check if it has fought at least once
        # If exception raised, the get_has_fought failed, add error
        try:
            c = Charmander()        # Create a Charmander object
            a = c.get_has_fought()  # Gets the has_fought boolean value
        except Exception as e:
            self.verificationErrors.append(f"get_has_fought has failed: {e}")
            return
       
        # If returned boolean value != False then the has fought had return the wrong result, add an error
        try:
            assert a == False       # Assert that has_fought is False
        except AssertionError:
            self.verificationErrors.append(f"get_has_fought has should be False but it returned {str(a)}")

    def test_set_has_fought(self):
        """ Testing the set_has_fought function. Will count and return number of errors if fails to past test """
        # Try create a charmander and set it had fought
        # If exception raised, get_has_fought failed and add an error
        try:
            c = Charmander()        # Create a Charmander object with has_fought = False
            c.set_has_fought(True)  # Sets Charmander's has_fought value to True
            a = c.get_has_fought()  # Gets the has_fought boolean value
        except Exception as e:
            self.verificationErrors.append(f" set_has_fought has failed: {e}")
            return
        
        # If returned boolean value != True then the has fought had return the wrong result, add an error
        try:
            assert a == True        # Assert that has_fought is True
        except AssertionError:
            self.verificationErrors.append(f" get_has_fought has should be False but it returned {str(a)}")


    def test_increase_hp(self):
        """ Testing the increase_hp function. Will count and return number of errors if fails to past test """
        #create a missingno and increase its hp by 10
        #if exception raised, the increase hp failed and add an error
        try:
            t = MissingNo() # Create a MissingNo object
            t.increase_hp(10) # Increase its hp by 10
            if t.get_hp() != 18: # if MissingNo's hp is not 18
                self.verificationErrors.append(f"MissingNo did not increase its hp to 18: {str(t)}")
                #if hp of missingno is not 18 means the increase hp add the wrong value of hp, add an error
        except Exception as e:
            self.verificationErrors.append(f"Increase hp failed: {str(t)}.")

    def test_superpower(self):
        # Create a MissingNo and check if its superpower help to increase it's level and hp
        # If exception raised, superpower failed and add an error
        try:
            t = MissingNo()             # Create a MissingNo object
            prev_hp = t.get_hp()        # Gets the hp of the MissingNo object
            prev_level = t.get_level()  # Gets the level of the MissingNo object
            check = t.superpower()      # Executes the superpower method
            aft_hp = t.get_hp()         # Gets the new hp of the MissingNo object
            aft_level = t.get_level()   # Gets the new level of the MissingNo object
        except Exception as e:
            self.verificationErrors.append(f"Superpower failed to execute: {e}")
        
        # Compares old stats of MissingNo to the new stats, if the new stats is not equal to old stats, add an error
        try:
            if check:  # If superpower successfully executed
                assert prev_hp != aft_hp or prev_level != aft_level  # Assert that the hp/level of MissingNo has changed
        except AssertionError:
            self.verificationErrors.append("Superpower failed at executing the effect, The hp and level of MissingNo is still the same")
            # If hp and lvl doesnt match the correct new lvl and hp , the super power is not operating at the way we wanted it to be ,add an error


if __name__ == '__main__':
    suite = unittest.TestLoader().loadTestsFromTestCase(TestPokemonBase)
    unittest.TextTestRunner(verbosity=0).run(suite)