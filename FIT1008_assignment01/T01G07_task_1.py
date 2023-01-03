"""
This file contains python code for calculating the
number of divisors of an input integer
"""

__author__ = "Teoh Tian Zhi"
__date__ = "19.03.2022"

"""
Complexity: O(1) because this code uses arithmetic and selection operations
"""

number = int(input("Enter the number: "))                       # let number be an integer input by the user
first_divisor = int(input("Enter the first divisor: "))         # let first_divisor be the first divisor input by the user
second_divisor = int(input("Enter the second divisor: "))       # let second_divisor be the 2nd divisor input by the user
divisors = 0                                                    # let divisors equals to 0

if number % first_divisor == 0 and number % second_divisor == 0:       # If Condition: if the remainder of number divide first_divsior and second_divsior equals to 0
    divisors = 2                                                       # divisors will be equals to 2
elif number % first_divisor == 0 or number % second_divisor == 0:      # Elif Condition: if either of the remainder of number divide first_divisor and second divisor equals to 0
    divisors = 1                                                       # divisors will be equals to 1
else:                                                                  # Else Condition: if the conditions above isn't fulfilled
    divisors = 0                                                       # divisors will be equals to 0

print("\nDivisors: " + str(divisors))       # prints out the value of divisors