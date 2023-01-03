from pokemon_base import PokemonBase

"""
Summary: This module contains the 3 pokemon classes that inherit from PokemonBase that can be in the team 
         which are Charmander, Bulbasaur and Squirtle and each of them has different attributes based on its level 
         or not and different ways to defend
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 30/4/2022
"""

class Charmander(PokemonBase):
    """ Charmander class which consists of the attributes and stats of Charmander pokemon. Inherits PokemonBase """
    # Class variable
    NAME = "Charmander"
    ATTACK = 6
    DEFENCE = 4
    SPEED = 7
    PRIORITY = 3  # Highest priority when sorting pokemon if criterion attribute is same

    # Charmander class constructor
    def __init__(self) -> None:
        """
        Charmander class Constructor
        returns: None
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        hp = 7
        poke_type = "Fire"
        PokemonBase.__init__(self, hp, poke_type) # Calling PokemonBase constructor to initialize the hp
                                                  # Poketype and level instance var

    def get_name(self) -> str:
        """
        Method to return the pokemon's NAME
        param: self: Charmander object
        returns: The name of the pokemon : "Charmander"
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Charmander.NAME

    def get_speed(self) -> int:
        """
        Method to return the pokemon's SPEED stat that scales with level
        param: self: Charmander object
        returns: the speed of Charmander according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)

        """
        return Charmander.SPEED + self.get_level()

    def get_atk_dmg(self) -> int:
        """
        Method to return the pokemon's ATTACK stat that scales with level
        param: self: Charmander object
        returns: the attack stat of Charmander according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Charmander.ATTACK + self.get_level()

    def get_defence(self) -> int:
        """
        Method to return the pokemon's DEFENCE stat
        param: self: Charmander object
        returns: the defence of Charmander according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Charmander.DEFENCE

    def cal_dmg_taken(self, enemy: PokemonBase) -> int:
        """
        Method to calculate how much dmg will you take
        param: self: Charmander object
               enemy: which is a pokemon which is a pokemon either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: the damage of Charmander received
        Complexity: Best: O(1)
                    Worst: O(1)
        """

        dmg = enemy.get_atk_dmg()           # Getting the enemy's attack stat
        effectiveness = 1                   # The effectiveness of the dmg according to the enemy's poke_type
        enemy_type = enemy.get_poke_type()  # Getting the enemy's poke_type
        defend_stat = self.get_defence()    # Getting ur defence stat

        if enemy_type == "Grass":           # IF enemy is a grass type then the type effectiveness is 0.5 times
            effectiveness = 0.5
        elif enemy_type == "Water":         # IF enemy is a water type then the type effectiveness is 2.0 times
            effectiveness = 2.0

        effective_dmg = dmg*effectiveness   # Calculate the effective dmg due to type advantage/disadvantage

        if effective_dmg > defend_stat:     # If the dmg done is larger than ur defence than it will deal that dmg
            return int(effective_dmg)       # Otherwise the dmg is halved.
        else:
            return int(effective_dmg//2)


    def get_priority(self)-> int:
        """
        Method to return the class variable PRIORITY
        param: self: Charmander object
        returns: Charmander's priority
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Charmander.PRIORITY # return the class variable PRIORITY

    def __str__(self) -> str:
        """
        Method to display the name, hp and level of the pokemon
        Format:  “<Pokemon_Name>'s HP = <hp> and level = <level>”
        param: self: Charmander object
        returns: The name, Hp and level of the pokemon
        Complexity: Best: O(n), n = length of the output string
                    Worst: O(n), n = length of the output string
        """
        # to increase efficiency, we decided to use .join() to create the output string
        lst =[] # create an empty list
        lst.append(self.get_name()) # append the name of the pokemon into the list
        lst.append("\'s ") # append "'s " into the list
        lst.append(PokemonBase.__str__(self)) # append the hp and level of the pokemon via the PokemonBase str method

        ret = "".join(lst) # use .join() to create the output string
        return ret # return the output string



class Bulbasaur(PokemonBase):
    """ Bulbasaur class which consists of the attributes and stats of Bulbasaur pokemon. Inherits PokemonBase """
    # Class variable
    NAME = "Bulbasaur"
    ATTACK = 5
    DEFENCE = 5
    SPEED = 7
    PRIORITY = 2  # Priority 2 when sorting pokemon if criterion attribute is same

    # Bulbasaur class constructor
    def __init__(self)-> None:
        """
        Bulbasaur class Constructor
        returns: None
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        hp = 9
        poke_type = "Grass"
        PokemonBase.__init__(self, hp, poke_type) # Calling PokemonBase constructor to initialize the hp
                                                  # Poketype and level instance var

    def get_name(self) -> str:
        """
        Method to return the pokemon's NAME
        param: self: Bulbasaur object
        returns: The name of the pokemon : "Bulbasaur"
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Bulbasaur.NAME

    def get_speed(self) -> int:
        """
        Method to return the pokemon's SPEED stat that scales integer division 1//2 of the pokemon's level
        param: self: Bulbasaur object
        returns: the speed of Bulbasaur according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Bulbasaur.SPEED + (self.get_level()//2)

    def get_atk_dmg(self) -> int:
        """
        Method to return the pokemon's ATTACK stat
        param: self: Bulbasaur object
        returns: the attack of Bulbasaur according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Bulbasaur.ATTACK

    def get_defence(self) -> int:
        """
        Method to return the pokemon's DEFENCE stat
        param: self: Bulbasaur object
        returns: the defence of Bulbasaur according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Bulbasaur.DEFENCE

    def cal_dmg_taken(self, enemy: PokemonBase) -> int:
        """
        Method to calculate how much dmg will you take
        param: self: Bulbasaur object
               enemy which is a pokemon which is a pokemon either Charmander, Squirtle, Bulbasaur or MissingNo
        returns: the damage received by Bulbasaur
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        dmg = enemy.get_atk_dmg()           # Getting the enemy's ATTACK stat
        effectiveness = 1                   # Type effectiveness factor
        enemy_type = enemy.get_poke_type()  # Getting the enemies poke_type
        defend_stat = self.get_defence()    # Getting ur DEFENCE stat

        if enemy_type == "Fire":            # If the enemy is a fire type, then the type effectiveness will be 2.0 times
            effectiveness = 2.0
        elif enemy_type == "Water":         # If the enemy is a water type, then the type effectiveness will be 0.5 times
            effectiveness = 0.5

        effective_dmg = dmg * effectiveness # Calculate the effective_dmg due to type advantage/disadvantage

        if effective_dmg > defend_stat+5:   # If effective_dmg larger than ur DEFENCE than the dmg taken = effective_dmg
            return int(effective_dmg)
        else:
            return int(effective_dmg//2)    # Otherwise the dmg taken = effectiev_dmg // 2

    def get_priority(self)-> int:
        """
        Method to return the class variable PRIORITY
        param: self: Bulbasaur object
        returns: Bulbasaur's priority
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Bulbasaur.PRIORITY # return class variable PRIORITY

    def __str__(self) -> str:
        """
        Method to display the name, hp and level of the pokemon
        Format:  “<Pokemon_Name>'s HP = <hp> and level = <level>”
        param: self: Bulbasaur object
        returns: The name, Hp and level of the pokemon
        Complexity: Best: O(n), n = length of the output string
                    Worst: O(n), n = length of the output string
        """
        # To increase efficiency, we decided to use .join() to create the output string
        lst = []                     # Create an empty list
        lst.append(self.get_name())  # Append the name of the pokemon into the list
        lst.append("\'s ")           # Append "'s " into the list
        lst.append(PokemonBase.__str__(self))  # Append the hp and level of the pokemon via the PokemonBase str method

        ret = "".join(lst)  # Use .join() to create the output string
        return ret          # Return the output string


class Squirtle(PokemonBase):
    """ Squirtle class which consists of the attributes and stats of Squirtle pokemon. Inherits PokemonBase """
    # Class variable
    NAME = "Squirtle"
    ATTACK = 4
    DEFENCE = 6
    SPEED = 7
    PRIORITY = 1  # Priority 1 when sorting pokemon if criterion attribute is same

    # Squirtle class constructor
    def __init__(self) -> None:
        """
        Squirtle class Constructor
        returns: None
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        hp = 8
        poke_type = "Water"
        PokemonBase.__init__(self, hp, poke_type) # Calling PokemonBase constructor to initialize the hp
                                                  # Poketype and level instance var

    def get_name(self) -> str:
        """
        Method to return the pokemon's NAME
        param: self: Squirtle object
        returns: The name of the pokemon : "Squirtle"
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Squirtle.NAME

    def get_speed(self) -> int:
        """
        Method to return the pokemon's SPEED stat
        param: self: Squirtle object
        returns: the speed of squirtle according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Squirtle.SPEED

    def get_atk_dmg(self) -> int:
        """
        Method to return the pokemon's ATTACK stat that scales with the integer division 1/2 of its level
        param: self: Squirtle object
        returns: the attack of squirtle according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Squirtle.ATTACK + (self.get_level()//2)


    def get_defence(self) -> int:
        """
        Method to return the pokemon's DEFENCE stat that scales with its level
        param: self: Squirtle object
        returns: the defence of squirtle according to it's level
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Squirtle.DEFENCE + self.get_level()

    def cal_dmg_taken(self, enemy: PokemonBase) -> int:
        """
        Method to calculate how much dmg will you take
        param: self: Squirtle object
               enemy which is a which is a pokemon either Charmander, Squirtle, Bulbasaur or MissingNo pokemon in PokemonBase
        returns: the damage received by squirtle
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        dmg = enemy.get_atk_dmg()           # Getting the enemy's ATTACK stat
        effectiveness = 1                   # Type effectiveness factor
        enemy_type = enemy.get_poke_type()  # Getting the enemy's poke_type
        defend_stat = self.get_defence()    # Getting ur DEFENCE stat

        if enemy_type == "Fire":            # If the enemy is a fire type then the type effectiveness will be 0.5 times
            effectiveness = 0.5
        elif enemy_type == "Grass":         # If the enemy is a grass type then the type effectiveness will be 2.0 times
            effectiveness = 2.0

        effective_dmg = dmg * effectiveness # Calculate the effective_dmg due to type advantage/disadvantage

        if effective_dmg > defend_stat*2:   # If the effective_dmg is larger than 2 times of its DEFENCE stat
            return int(effective_dmg)       # Then the dmg taken = effective_dmg, otherwise it will be
        else:                               # effective_dmg //2
            return int(effective_dmg // 2)


    def get_priority(self) -> int:
        """
        Method to return the class variable PRIORITY
        param: self: Squirtle object
        returns: Squirtle's priority
        Complexity: Best: O(1)
                    Worst: O(1)
        """
        return Squirtle.PRIORITY

    def __str__(self)-> str:
        """
        method to display the name, hp and level of the pokemon
        Format:  “<Pokemon_Name>'s HP = <hp> and level = <level>”
        param: self: Squirtle object
        returns: The name, Hp and level of the pokemon
        Complexity: Best: O(n), n = length of the output string
                    Worst: O(n), n = length of the output string
        """
        # To increase efficiency, we decided to use .join() to create the output string
        lst = []                     # Create an empty list
        lst.append(self.get_name())  # Append the name of the pokemon into the list
        lst.append("\'s ")           # Append "'s " into the list
        lst.append(PokemonBase.__str__(self))  # Append the hp and level of the pokemon via the PokemonBase str method

        ret = "".join(lst)  # Use .join() to create the output string
        return ret          # Return the output string