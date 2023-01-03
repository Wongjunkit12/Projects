"""
This file contains python code for finding the index of a target
in a sorted list
"""

__author__ = "Tong Jet Kit"
__date__ = "19.03.2022"

def binary_search(the_list: list, target: int, low: int, high: int) -> int:  # this function assumes the array is sorted
    """
    :param the_list: An array of sorted elements that is orderable
    :param target: The targeted element that we are searching
    :param low: The smallest index of the section of array that we are searching
    :param high: The highest index of the section of array that we are searching

    pre: the_list is sorted
    post: mid = -1 and target is not in the_list or a[mid] = target

    Input: A sorted list containing elements that is orderable
    Output: The index of the target element
    Complexity:  Worst Case: O(log n) where n is the length of the array
                 Best Case : O(1) where the target is found during the first instant call
    Invariant_: The target is in my_list[low:high+1]
    """
    if low > high:      # if low > high it means the whole array has been searched and the target cant be found, so we return
        return -1       # -1 as an indication that the target is not in the array
    else:
        mid = (high + low) // 2                                     # find the middle index of the array

        if the_list[mid] == target:                                 # if list[mid] == target, the mid will be the index of the array so we return it
            return mid

        elif the_list[mid] > target:                                # if list[mid]> target, the target is in the lower half of the array
            return binary_search(the_list, target, low, mid - 1)    # we search the lower half of the array

        else:                                                       # else if list[mid]> target, the target is in the upper half of the array
            return binary_search(the_list, target, mid + 1, high)   # so we search the upper half of the array


def main() -> None:
    """ Calls all functions with some inputs and prints the result."""
    arr = [1, 5, 10, 11, 12]                            # Declare and initialize a sorted array called arr
    index = binary_search(arr, 11, 0, len(arr) - 1)     # call the function binary search to find the index of the target(11)
    print(index)                                        # print the index


main() # call the main function()

