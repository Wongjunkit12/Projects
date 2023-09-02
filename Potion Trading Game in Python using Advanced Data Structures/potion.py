""" 
Summary: Python module containing the Potion class and all its methods
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 13/5/2022
"""

from typing import TypeVar

R = TypeVar('R', int, float)  # R represents int and float numbers


class Potion:

    def __init__(self, potion_type: str, name: str, buy_price: R, quantity: R) -> None:
        """
        Potion class constructor
        :params: self: A potion class object
                 potion_type: A string that represents the effect of the potion
                 name: A string that represents the potion's name
                 buy_price: A float that represents the price of the potion
                 quantity: A float that represents how many potions are there
        :pre: potion_type has to be String
              name has to be String
              buy_price has to be a Float value
              quantity has to be a Float value
        :raises TypeError: if potion_type is not String
                         : if name is not String
                         : if buy_price is not Float
                         : if quantity is not Float
        """
        # Preconditions to check whether the input arguements matches the typehints provided
        if type(potion_type) != str:
            raise TypeError("potion_type has to be String")
        elif type(name) != str:
            raise TypeError("name has to be String")
        elif not (type(buy_price) == float or type(buy_price) == int):
            raise TypeError("buy_price has to be a Float or Integer")
        elif not (type(quantity) == float or type(quantity) == int):
            raise TypeError("quantity has to be Float or Integer")

        # Initialize the instance variables potion_type, name, buy_price and quantity according to the values of the
        # input parameter
        self.set_potion_type(potion_type)
        self.set_name(name)
        self.set_price(buy_price * 1.0)
        self.set_quantity(quantity * 1.0)
        self.set_valuation(0)  # Sets valuation to 0 first when potion objecti s being created

    @classmethod
    def create_empty(cls, potion_type: str, name: str, buy_price: R) -> 'Potion':
        """
        Method to create a potion object with 0 quantity
        :param: cls: Class Name
                potion_type: A string that represents the effect of the potion
                name: A string that represents the potion's name
                buy_price: A float that represents the price of the potion
        :pre: potion_type has to be String
              name has to be String
              buy_price has to be a Float value
        :raises TypeError: if potion_type is not String
                         : if name is not String
                         : if buy_price is not Float
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Preconditions to check whether the input arguements matches the typehints provided
        if type(potion_type) != str:
            raise TypeError("potion_type has to be String")
        elif type(name) != str:
            raise TypeError("name has to be String")
        elif not (type(buy_price) == float or type(buy_price) == int):                      
            raise TypeError("buy_price has to be a Float")  

        return cls(potion_type, name, buy_price,0)  # Calls the Potion class init method to create the object

    @classmethod
    def good_hash(cls, potion_name: str, tablesize: int) -> int:
        """
        Method to hash a key to return a position in an array
        :params: cls: Class Name
                 potion_name: A string that represents a type of potion
                 tablesize: size of the hash table
        :pre: potion_name has to be String
              tablesize has to be Integer
        :raises TypeError: if potion_name is not a String
                           if tablesize is not an Integer
        :complexity: Best Case: O(1)
                     Worst Case: O(n) where n is the number of characters in the key
        """
        # Preconditions to ensure the input arguements matches the typehints provided
        if type(potion_name) != str:
            raise TypeError("potion_name has to be String")
        elif type(tablesize) != int:
            raise TypeError("tablesize has to be Integer")

        value = 0

        a = 31415
        b = 27183
        for char in potion_name:
            value = (ord(char) + a * value) % tablesize  # using ord() returns ASCII integer value
            a = a * b % (tablesize - 1)
        return value



    @classmethod
    def bad_hash(cls, potion_name: str, tablesize: int) -> int:
        """
        Method to hash a key to return a position in an array
        :params: cls: Class Name
                 potion_name: A string that represents a type of potion
                 tablesize: size of the hash table
        :pre: potion_name has to be String
              tablesize has to be Integer
        :raises TypeError: if potion_name is not a String
                           if tablesize is not an Integer
        :complexity: Best Case: O(1)
                     Worst Case: O(n) where n is the number of characters in the key
        """
        # Preconditions to ensure the input arguements matches the typehints provided
        if type(potion_name) != str:
            raise TypeError("potion_name has to be String")
        elif type(tablesize) != int:
            raise TypeError("tablesize has to be Integer")

        sums = 0

        # Add up all the ascii value in the potion name
        for char in potion_name:
            sums += ord(char)

        # The position of the key will be sums % tablesize
        return sums % tablesize

    def set_potion_type(self, potion_type: str) -> None:
        """
        A method that sets the potion type of the potion object based on the input potion_type
        :param: potion_type: A string representing the potion type
        :pre: potion_type has to be String
              potion_type String cannot be an empty string
        :raises: TypeError: if potion_type is not a String
                 ValueError: if length of potion_type is 0
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Enforce preconditions 
        if type(potion_type) != str:
            raise TypeError("potion_type is not a String")
        elif len(potion_type) <= 0:
            raise ValueError("potion_type must not be an empty String")
        
        self.potion_type = potion_type  # Sets the instance variable to the input potion_type
    
    def set_name(self, name: str) -> None:
        """
        A method that sets the potion type of the potion object based on the input potion_type
        :param: name: A string representing the potion name
        :pre: name has to be String
              name String cannot be an empty string
        :raises: TypeError: if name is not a String
                 ValueError: if length of name is 0 
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Enforce preconditions 
        if type(name) != str:
            raise TypeError("name is not a String")
        elif len(name) <= 0:
            raise ValueError("name must not be an empty String")
        
        self.name = name  # Sets the instance variable to the input name
    
    def set_price(self, buy_price: R) -> bool:
        """
        A method that sets the buy_price of the potion object based on the input buy_price
        :param: buy_price: A float or integer to set the new price of the potion to
        :pre: buy_price has to be Float or Integer
        :raises: TypeError: if buy_price is not a Float or Integer
        :return: bool to represent whether it successfully set the quantity value. True if yes, False otherwise
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Enforce precondition that buy_price is to be float or integer
        if not (type(buy_price) == float or type(buy_price) == int):
            raise TypeError("buy_price has to be Float or Integer")
        # If buy_price is a positive value, then set it 
        elif buy_price >= 0:
            self.buy_price = buy_price
            return True  # Return True to signify that quantity has been successfully set
        else:
            return False
    
    def set_quantity(self, quantity: R) -> bool:
        """
        A method that sets the quantity of the potion object based on the input quantity
        :param: quantity: A float or integer to set the new quantity of the potion to
        :pre: quantity has to be Float or Integer
        :raises: TypeError: if quantity is not a Float or Integer
        :return: bool to represent whether it successfully set the quantity value. True if yes, False otherwise
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Enforce precondition that quantity is to be float or integer
        if not (type(quantity) == float or type(quantity) == int):
            raise TypeError("quantity has to be Float or Integer")
        # If quantity is a positive value, then set it 
        elif quantity >= 0:
            self.quantity = quantity
            return True  # Return True to signify that quantity has been successfully set
        else:
            return False
        
    def set_valuation(self, valuation: R) -> bool:
        """
        A method that sets the valuation of the potion object based on the input valuation
        :param: valuation: A float or integer that represents how much an adventurer is willing to pay for one litre
        :pre: valuation has to be Float or Integer
        :raises: TypeError: if valuation is not a Float or Integer
        :return: bool to represent whether it successfully set the quantity value. True if yes, False otherwise
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Enforce precondition that valuation is to be float or integer
        if not (type(valuation) == float or type(valuation) == int):
            raise TypeError("valuation has to be Float or Integer")
        # If valuation is a positive value, then set it 
        elif valuation >= 0:
            self.valuation = valuation
            return True  # Return True to signify that quantity has been successfully set
        else:
            return False

    def get_potion_type(self) -> str:
        """
        Method to return the instance variable potion_type
        :param: self: Potion object
        :return: A string representing the type of potion
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        return self.potion_type
    
    def get_name(self) -> str:
        """
        Method to return the instance variable name
        :param: self: Potion object
        :return: A string representing the name of the potion
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        return self.name
    
    def get_price(self) -> float:
        """
        Method to return the instance variable buy_price
        :param: self: Potion object
        :return: A float which is the value for the buy_price instance variable
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        return self.buy_price

    def get_quantity(self) -> float:
        """
        Method to return the instance variable quantity
        :param: self: Potion object
        :return: A float which is the value for the quantity instance variable
        :complexity: Best Case: O(1)
                    Worst Case: O(1)
        """
        return self.quantity
    
    def get_valuation(self) -> float:
        """
        Method to return the instance variable valuation
        :param: self: Potion object
        :return: A float which is the value for the valuation instance variable
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        return self.valuation
