from abc import ABC, abstractmethod

"""
Summary: This module contains an abstract class that has all the base attributes and methods that a pokemon can do
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""

class PokemonBase(ABC):
    """ Abstract class for the Charmander, Bulbasaur and Squirtle class"""

    # PokemonBase constructor but we dont instantiate PokemonBase
    def __init__(self, hp: int, poke_type: str) -> None:
        """
        PokemonBase class Constructor
        :param: hp(int), poke_type(str)
        :returns: None
        pre: hp has to be Integer, poke_type has to be String
        raises: TypeError if hp is not Integer, TypeError if poke_type is not String
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(hp) != int:
            raise TypeError("Hp value has to be Integer")
        elif type(poke_type) != str:
            raise TypeError("Pokemon type has to be String")
        # Set all of the instance variable hp, poke_type to the value in the argument and default sets level to 1 and has_fought to False
        self.set_hp(hp)  # Use the set_hp method to set the Hp
        self.poke_type = poke_type
        self.set_level(1)  # Use the set_level method to set the level
        self.set_has_fought(False)  # Use the set_has_fought method to set the has_fought instance variable

    def set_hp(self, hp: int) -> None:
        """
        Method to set the pokemon's Hp
        param: self = PokemonBase child's object
               hp = The new hp to set pokemon's hp with
        returns: None
        pre: hp has to be Integer
        raises: TypeError if hp is not Integer
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(hp) != int:
            raise TypeError("Hp value has to be Integer")
        else:
            # If hp is negative, sets hp to 0. Else sets hp to given hp
            if hp < 0:
                self.hp = 0
            else:
                self.hp = hp

    def get_hp(self) -> int:
        """
        Method to get the pokemon's Hp
        param: self = PokemonBase child's object
        returns: hp of the pokemon
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return self.hp # return the pokemon's hp

    def set_level(self, level: int) -> bool:
        """
        Method to set the pokemon's level
        param: self = PokemonBase child's object
               level  = The new level to set the pokemon's level with
        returns: True if successfully set the the level, False otherwise
        pre: level has to be Integer
        raises: TypeEror if level is not Integer
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        if type(level) != int:
            raise TypeError("Level has to be Integer")
        else:
            # If the argument level is more than equal to 1 then only we change it, otherwise don't and return False
            if level >= 1:
                self.level = level
                return True
            else:
                return False

    def is_fainted(self) -> bool:
        """
        Method to check if pokemon fainted or not
        param: self = PokemonBase child's obj
        return: a boolean value where True means the pokemon has fainted and returns False otherwise
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        # hp == 0 means that the pokemon cant fight anymore so it will faint(returns True)
        if self.hp == 0:
            return True
        # Else, the pokemon still can fight(return False)
        else:
            return False

    def get_level(self) -> int:
        """
        Method to get the pokemon's level
        param: self = PokemonBase child's obj
        returns: level of the pokemon
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return self.level # return the pokemon's level

    def get_poke_type(self) -> str:
        """
        Method to get the pokemon's poke_type
        param: self = PokemonBase child's obj
        returns poke_type of the pokemon
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return self.poke_type   # return the pokemon's poke_type

    def set_has_fought(self, has_fought: bool) -> None:
        """
        Method to set the pokemon's has_fought instance variable
        param: self: PokemonBase child's obj
               has_fought: True or False to signify whether pokemon has fought or not yet
        returns: None
        pre: has_fought has to be Boolean
        raises: TypeError if has_fought is not Boolean
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        # If has_fought is not boolean
        if type(has_fought) != bool:
            raise TypeError("Input of has_fought has to be Boolean")
        else:
            self.has_fought = has_fought  # Sets has_fought instance variable to has_fought input

    def get_has_fought(self) -> bool:
        """
        Method to get the pokemon's has_fought instance variable
        param: self: PokemonBase child's obj
        returns: returns True if pokemon has fought and False if not
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return self.has_fought  # Returns has_fought instance variable

    # These are all abstract method that will be implemented in the PokemonBase's child class
    @abstractmethod
    def get_name(self) -> str:
        pass

    @abstractmethod
    def get_speed(self) -> int:
        pass

    @abstractmethod
    def get_atk_dmg(self) -> int:
        pass

    @abstractmethod
    def get_defence(self) -> int:
        pass

    @abstractmethod
    def cal_dmg_taken(self, enemy) -> float:
        pass

    def __str__(self) -> str:
        """
        Method to print out the HP and level
        Format: "HP = <Pokemon's HP>'s and level = <Pokemon's level>"
        param: self = PokemonBase child's obj
        returns: the hp and level of the pokemon
        Complexity: Best: O(n), n = length of the output string
                    Worst: O(n), n = length of the output string
        """
        # to increase efficiency, we decided to use .join() to create the output string
        lst = []  # create an empty list
        lst.append("HP = ")  # append "HP = " into the list
        lst.append(str(self.get_hp()))  # append the hp of the pokemon(in str form) into the list
        lst.append(" and level = ")  # append " and level = " into the list
        lst.append(str(self.get_level()))  # append the level of the pokemon(in str form) into the list

        ret = "".join(lst)  # create the output string by using the .join() method
        return ret  # return the string