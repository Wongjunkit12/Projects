from abc import ABC, abstractmethod
from pokemon_base import PokemonBase
import random

"""
Summary: This module contains an abstract base class for a mysterious new pokemon which inherits the PokemonBase abstract 
         class and has 2 more methods to increase its hp and execute a move called superpower, 
         which has a random chance to choose one of three effects:
          - Gain 1 level
          - Gain 1 HP
          - Gain 1 HP and 1 level
            and has a 25% chance of calling it
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class GlitchMon(PokemonBase, ABC):
    """ Abstract class for the MissingNo pokemon child class """

    # GlitchMon class constructor but we dont instantiate GlitchMon
    def __init__(self, hp: int, poke_type: str)-> None:
        """
        GlitchMon class Constructor
        param: hp(int), poke_type(str)
        returns: None
        pre: hp has to be Integer and poke_type has to be String
        raises: TypeError hp is not Integer, TypeError is poke_type is not String
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(hp) != int:
            raise TypeError("Hp value has to be Integer")
        elif type(poke_type) != str:
            raise TypeError("Pokemon type has to be String")
        else:
            PokemonBase.__init__(self, hp, poke_type) # Calling PokemonBase constructor to initialize the hp
                                                      # Poketype and level instance var

    def increase_hp(self, hp: int) -> bool:
        """
        Method to increase the pokemon's hp according to the input value
        params: self: A GlitchMon child class object
                hp : an integer value to represent how much should the hp value increase
        returns: A boolean value, True to indicate if its increased, False if not
        Complexity: Best: O(1)
                    Wosrt: O(1)
        """
        if type(hp) != int:
            raise TypeError("Hp value has to be Integer")
        else:
            if hp > 0: # Making sure that increase_hp really increase the hp value and return True to indicate that it has done it
                self.hp = self.hp + hp
                return True
            else:
                return False # It returns False if its not increased as the hp parameter is <= 0

    
    def superpower(self) -> bool:
        """
        Method which has a random chance to choose one of three effects
        - Gain 1 level
        - Gain 1 hp
        - Gain 1 hp and 1 level
        params: self: A GlitchMon child class object
        returns: A boolean value, if it successfully executed superpower(), False otherwise
        Complexity : Best: O(1)
                     Worst: O(1)
        """
        if random.randint(1,100) <= 25: # 25% chance to be called
            chance = [0,1,2] # A list to represent 3 values that represent each effect
                             # 0 is Gain 1 level, 1 is Gain 1 hp and 2 is Gain 1 hp and 1 level

            power_up = random.choice(chance) # Randomly choose a value in the chance list to satisfy the 1/3 chance
            if power_up == 0: # Gain 1 level
                increase = self.get_level()+1
                self.set_level(increase)
            elif power_up == 1: # Gain 1 hp
                self.increase_hp(1)
            elif power_up == 2: # Gain 1 hp and 1 level
                self.increase_hp(1)
                self.set_level((self.get_level()+1))

            return True # Indicate the user that the move has succeeded

        else:
            return False # Else tell the user that the move missed



    # These here are abstract methods that will be implemented in the GlitchMon's child classes
    @abstractmethod
    def get_name(self) -> str:
        """ Abstract method for get_name """
        pass

    @abstractmethod
    def get_speed(self) -> int:
        """ Abstract method for get_speed """
        pass

    @abstractmethod
    def get_atk_dmg(self) -> int:
        """ Abstract method for get_atk_dmg """
        pass

    @abstractmethod
    def get_defence(self) -> int:
        """ Abstract method for get_defence """
        pass

    @abstractmethod
    def cal_dmg_taken(self, enemy) -> int:
        """ Abstract method for cal_dmg_taken """
        pass