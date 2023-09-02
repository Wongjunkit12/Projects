"""
This file contains python code for sorting an list
through insertion sort
"""

__author__ = "Teoh Tian Zhi"
__date__ = "19.03.2022"


from typing import List, TypeVar    # import list and typevar

T = TypeVar('T')


def insertion_sort(the_list: List[T]):
    """
    :param the_list: An unsorted list
    :return: A sorted list

    pre: the_list[:] is sorted
    post: the_list[ : length(the_list)+1] is sorted
    Time Complexity: Worst case: O(n^2) where the_list is in descending order, n is the length of the_list
                     Best case : O(1) where the_list is sorted

    Invariant: the_list[ : i+1] is sorted
    """
    length = len(the_list)                  # let length equals to the length of the the_list
    for i in range(1, length):              # for loop: when i is within the length
        key = the_list[i]                   # let key equals to ith element of the_list
        j = i-1                             # let j equals to i decrementation of 1
        while j >= 0 and key < the_list[j]: # while loop: when j is more than 0 and key is less than the_list[j]
            the_list[j + 1] = the_list[j]   # let the jth + 1  element of the_list equals to the jth element of the_list
            j -= 1                          # j decrement by 1
        the_list[j + 1] = key               # let jth + 1 element of the_list equals to key


def main() -> None:
    """ Calls all functions with some inputs and prints the result."""
    arr = [6, -2, 7, 4, -10]                # let arr be an array of numbers
    insertion_sort(arr)                     # jumps to insertion_sort function with the input arr
    for i in range(len(arr)):               # for loop: when i is within the length of arr
        print(arr[i], end=" ")              # prints the ith of arr
    print()                                 # prints nothing


main()