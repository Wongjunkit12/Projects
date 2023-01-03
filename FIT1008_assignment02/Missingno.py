from Glitchmon import GlitchMon
import random
from pokemon_base import PokemonBase

"""
Summary: This module contains the Mysterious pokemon class which inherits the GlitchMon class. This pokemon has no 
         poke_type and its stat increases every level
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""


class MissingNo(GlitchMon):
    """ Class for mysterious new pokemon MissingNo. Inherits from abstract class GlitchMon """
    # The attack, defence and speed stat of MissingNo  are the average of Charmander, Bulbasaur and Squirtle pokemon's stat
    NAME = "MissingNo"
    ATTACK = 5
    DEFENCE = 5
    SPEED = 7
    PRIORITY = 0  # Lowest priority when sorting pokemon if criterion attribute is same

    # MissingNO class constructor, the attack, defence and speed is not a class variable as it needs to be calculated
    # by getting the level of the MissingNo object
    def __init__(self) -> None:
        """
        MissingNo class Constructor
        returns: None
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        # Intialize all the instance variable: hp, poke_type,level, attack, defence and speed which are the average of
        # Charmander, Bulbasaur and Squirtle pokemon's stat
        hp = 8
        poke_type = "Normal"
        GlitchMon.__init__(self, hp, poke_type)  # Call GlitchMon init method to initialize the hp, poke_type and level


    def get_name(self) -> str:
        """
        Method to return the pokemon's NAME
        param: self: MissingNo object
        returns: The name of the pokemon : "MissingNo"
        Complexity: Best: O(1)
                    Worst: O(1)

        """
        return MissingNo.NAME

    def get_speed(self) -> int:
        """
        Method to return the pokemon's speed stat that scales with the pokemon's level
        param: self: MissingNo object
        returns: the speed of MissingNo according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return MissingNo.SPEED + self.get_level() - 1

    def get_atk_dmg(self) -> int:
        """
        Method to return the pokemon's attack stat that scales with the pokemon's level
        param: self: MissingNo object
        returns: the attack stat of MissingNo according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return MissingNo.ATTACK + self.get_level() - 1

    def get_defence(self) -> int:
        """
        Method to get the pokemon's defence stat that scales with the pokemon's level
        param: self: MissingNo object
        returns: the defence stat of MissingNo according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return MissingNo.DEFENCE + self.get_level() - 1

    def cal_dmg_taken(self, enemy: PokemonBase) -> int:
        """
        Method to calculate how much dmg will you take
        param: self: MissingNo object
               enemy: which is a pokemon either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: the damage of MissingNo received
        Complexity: Best: O(1)
                    Worst: O(1)

        """
        dmg = enemy.get_atk_dmg()  # Getting the enemy's attack stat
        effectiveness = 1  # Type effectiveness multiplier
        defend_stat = self.get_defence()  # Getting ur DEFENCE stat

        hit_or_miss = self.superpower()  # Call superpower function to determine whether takes damage or not

        if hit_or_miss:
            return 0  # Return 0 damage if superpower activates since it will negate all damage done
        else:
            # Since MissingNo has no type , all dmg taken is neutral
            effective_dmg = dmg * effectiveness

            chance = [0, 1, 2]  # A list to represent 3 values that represent each defend condition
            # 0 is defend_stat, 1 is defend_stat+5 and 2 is defend_stat * 2

            def_condition = random.choice(chance)  # Randomly choose a value in the chance list to satisfy the 1/3 chance
            if def_condition == 0:  # Defend_stat
                if effective_dmg < defend_stat:  # If its less than defend_stat cut the dmg by half
                    effective_dmg = effective_dmg // 2

            elif def_condition == 1:  # Defend_stat +5
                if effective_dmg < defend_stat + 5:  # If its less than defend_stat + 5 cut the dmg by half
                    effective_dmg = effective_dmg // 2

            else:  # Defend_stat * 2
                if effective_dmg < defend_stat * 2:  # If its less than defend_stat * 2 cut the dmg by half
                    effective_dmg = effective_dmg // 2

            return int(effective_dmg)  # Making sure its integer

    def get_priority(self) -> int:
        """
        Method to return the class variable PRIORITY
        param: self: MissingNo object
        returns: MissingNo's priority
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return MissingNo.PRIORITY  # Return the class variable PRIORITY

    def __str__(self) -> str:
        """ 
        method to display the name, hp and level of the pokemon
        Format:  “<Pokemon_Name>’s HP = <hp> and level = <level>”
        param: self: MissingNo object
        returns: The name, Hp and level of the pokemon
        Complexity: Best: O(n) where n is the length of the output string
                    Worst: O(n) where n is the length of the output string
        """
        # To increase efficiency, we decided to use .join() to create the output string

        lst = [str(self.get_name()), "'s "]  # Create a list that contains “<Pokemon_Name>" and ’s
        lst.append(GlitchMon.__str__(self))  # Append with the GlitchMon's str method
        ret = "".join(lst)  # Use .join() to create the output string
        return ret  # Return the output string


