"""
This file contains python code for calculating the
number of multiples of an input integer(excluding itself)
from a given list of integers
"""

__author__ = "Toh Thien Yew"
__date__ = "19.03.2022"

"""
 Complexity: Worst Case: O(n) where n is the length of the array
             Best Case : O(1) where the list contains either only n or its an empty list
 pre: count = 0
 post : count = number of multiples in the_list[0:len(the_list)+1]
 Invariant_: count = number of multiples in the_list[0:i+1]
"""

size = int(input("Enter array length: "))       # let size be an integer input by the user
the_list = [None]*size                          # let the_list be the size of the array
n = int(input("Enter n: "))                     # let n be an integer input by the user
count = 0                                       # let count be 0

for i in range(len(the_list)):                      # for loop: when i is within the length of the_list
    the_list[i] = int(input("Enter the value: "))    # let the ith element of the_list be an integer input by the user
    if the_list[i] % n == 0 and the_list[i] != n:   # If condition: if the remainder of ith element of the_list divide n equals 0 and the ith element of the_list isn't equal to n
        count += 1                                  # increment count by 1

print("\nThe number of multiples (excluding itself) = " + str(count))       # prints the value of count