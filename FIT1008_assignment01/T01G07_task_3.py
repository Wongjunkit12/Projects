"""
This file contains python code for calculating the
number of multiples of a given number from a given
list of integers
"""
__author__ = "Bryan Wong Jun Kit"
__date__ = "19.03.2022"


from typing import List         # import list


def get_multiples(the_list: List[int], n: int) -> int:
    """
    Computes the number of multiples of n

    :param the_list: The list of integers being passed
    :param n: The number to check for multiples

    :pre: n > 0
    :post: count = number of integers in number of multiples in the_list[0:len(the_list)+1] that is divisible by n and
            its value is not equal to n

    Input: A list of integers or an empty list
    Output: The count of multiples from the list
    Complexity: Worst Case: O(n) where n is the length of the array
                Best Case : O(1) where the list contains either only n or its an empty list
    Invariant_: count = number of multiples in the_list[0:i+1]
    """
    count = 0                                               # let count be 0
    for i in range(len(the_list)):                          # for loop: when i is within the length of the_list
        if the_list[i] % n == 0 and the_list[i] != n:       # If condition: if the remainder of ith element of the_list divide n equals 0 and the ith element of the_list isn't equal to n
            count += 1                                      # increment count by 1
    return count                                            # returns the value of count


def main() -> None:
    """ Calls all functions with some inputs and prints the result."""
    my_list = [2, 4, 6]                                     # let my_list be an array of numbers input by the user
    n = 3                                                   # let n be an integer input by the user
    print("The number of multiples of " + str(n) + " is: " + str(get_multiples(my_list, n)))  #prints the value of n and value of count


main()