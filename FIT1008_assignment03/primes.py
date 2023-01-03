""" 
Summary: Python file containing methods related to the generation of prime numbers
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 13/5/2022
"""

import math


def is_prime(num: int) -> bool:
        """ 
        Method which accepts an integer value as an input and returns True if a prime number and False otherwise
        :param: num: An integer number
        :pre: num has to be Integer
        :raises TypeError: If num is not an Integer
        :complexity: Best Case: O(1) where the input number is not a prime
                     Worst Case: O(sqrt(n)), where n is the input number
        """
        # If num is not an integer
        if type(num) != int:
            raise TypeError("num has to be an Integer")
        
        # For loop which checks whether the input number is a prime number or not
        for n in range(2, int(num ** 0.5) + 1):
            if num % n == 0:
                return False  # Not a prime number
        return True  # Is a prime number


def largest_prime(k: int) -> int:
    """
    Method which returns the largest prime number strictly less than k
    :param: k: An integer k which represents the upper limit in which the prime number have to be less than
    :pre: k has to be larger than 2 and smaller or equal than 100000.
          k has to be Integer
    :raises ValueError: k is smaller than or equal to 2 or larger than 100000
            TypeError: k is not an Integer
    :complexity: Best Case: O(sqrt(n)) when k-1 is the largest prime number
                 Worst Case: O(n * sqrt(m)) where n is the value of k and m is the input value for is_prime()
    """
    # If k is not within the acceptable range of 2 < k <= 100000
    if k <= 2 or k > 100000:
        raise ValueError("k is not within the acceptable range")
    # If k is not an Integer
    elif type(k) != int:
        raise TypeError("k has to be an Integer")
    
    # For loop which loops from k downwards and checks each number whether they are a prime number or not
    for x in reversed(range(k)):
        if is_prime(x):  # Calls the is_prime method which will return True if the number is a prime
            return x     # Returns the largest prime value from k



