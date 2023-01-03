"""
    Array-based implementation of SortedList ADT.
    Items to store should be of time ListItem.
"""

from referential_array import ArrayR
from sorted_list import *

__author__ = 'Maria Garcia de la Banda and Brendon Taylor. Modified by Alexey Ignatiev and Graeme Gange'
__docformat__ = 'reStructuredText'

class ArraySortedList(SortedList[T]):
    """ SortedList ADT implemented with arrays. """
    MIN_CAPACITY = 1

    def __init__(self, max_capacity: int) -> None:
        """ ArraySortedList object initialiser. """

        # first, calling the basic initialiser
        SortedList.__init__(self)

        # initialising the internal array
        size = max(self.MIN_CAPACITY, max_capacity)
        self.array = ArrayR(size)

    def reset(self):
        """ Reset the list. """
        SortedList.__init__(self)

    def __getitem__(self, index: int) -> T:
        """ Magic method. Return the element at a given position. """
        return self.array[index]

    def __setitem__(self, index: int, item: ListItem) -> None:
        """ Magic method. Insert the item at a given position,
            if possible (!). Shift the following elements to the right.
        """
        if self.is_empty() or \
                (index == 0 and item.key <= self[index].key) or \
                (index == len(self) and self[index - 1].key <= item.key) or \
                (index > 0 and self[index - 1].key <= item.key <= self[index].key):

            if self.is_full():
                self._resize()

            self._shuffle_right(index)
            self.array[index] = item
        else:
            # the list isn't empty and the item's position is wrong wrt. its neighbourghs
            raise IndexError('Element should be inserted in sorted order')

    def __contains__(self, item: ListItem):
        """ Checks if value is in the list. """
        for i in range(len(self)):
            if self.array[i] == item:
                return True
        return False

    def _shuffle_right(self, index: int) -> None:
        """ Shuffle items to the right up to a given position. """
        for i in range(len(self), index, -1):
            self.array[i] = self.array[i - 1]

    def _shuffle_left(self, index: int) -> None:
        """ Shuffle items starting at a given position to the left. """
        for i in range(index, len(self)):
            self.array[i] = self.array[i + 1]

    def _resize(self) -> None:
        """ Resize the list. """
        # doubling the size of our list
        new_array = ArrayR(2 * len(self.array))

        # copying the contents
        for i in range(self.length):
            new_array[i] = self.array[i]

        # referring to the new array
        self.array = new_array

    def delete_at_index(self, index: int) -> ListItem:
        """ Delete item at a given position. """
        if index >= len(self):
            raise IndexError('No such index in the list')
        item = self.array[index]
        self.length -= 1
        self._shuffle_left(index)
        return item
    
    def index(self, item: ListItem) -> int:
        """ Find the position of a given item in the list. """
        pos = self._index_to_add(item)
        if pos < len(self) and self[pos] == item:
            return pos
        raise ValueError('item not in list')

    def is_full(self):
        """ Check if the list is full. """
        return len(self) >= len(self.array)

    def add(self, item: ListItem) -> None:
        """ Add new element to the list. """
        if self.is_full():
            self._resize()

        # find where to place it
        position = self._index_to_add(item)

        self[position] = item
        self.length += 1

    def _index_to_add(self, item: ListItem) -> int:
        """ Find the position where the new item should be placed. """
        low = 0
        high = len(self) - 1

        while low <= high:
            mid = (low + high) // 2
            if self[mid].key < item.key:
                low = mid + 1
            elif self[mid].key > item.key:
                high = mid - 1
            else:
                return mid

        return low

    def add_tie(self, item: ListItem) -> None:
        """
        Add new element to the list and checks for tie between keys.
        param: item which is a ListItem object
        returns: None
        pre: item has to be a ListeItem object
        raises: TypeError if item is not a ListItem object
        Complexity: Best Case: O(log n) where the item should be at the end
                    Worst Case: O(n) where the item should be at the front
        """
        if not isinstance(item, ListItem):
            raise TypeError("Item has to be a ListItem object")
        else:
            # Find where to place it
            position = self._index_to_add_tie(item)  # Calls index_to_add_tie method to determine where to add
            self[position] = item  # Sets the item at the new position
            self.length += 1  # Increment length by 1

    def _index_to_add_tie(self, item: ListItem) -> int:
        """
        Find the position where the new item should be placed and check for tie between keys.
        param: item which is a ListItem object
        returns: int which represents the position to add the item into the array
        pre: item has to be a ListeItem object
        raises: TypeError if item is not a ListItem object
        Complexity: Best Case: O(1) where index is at the front
                    Worst Case: O(log n) where index is at the end
        """
        if not isinstance(item, ListItem):
            raise TypeError("Item has to be a ListItem object")
        else:
            low = 0
            high = len(self) - 1

            # Binary search to determine where to place new item
            while low <= high:
                mid = (low + high) // 2
                # If both the keys are the same, call check_for_tie method
                if self[mid].key == item.key:
                    return self._check_for_tie(item)
                # If item key is more than the keys in the array
                elif self[mid].key < item.key:
                    low = mid + 1
                # If item key is less than the keys in the array
                elif self[mid].key > item.key:
                    high = mid - 1
        return low

    def _check_for_tie(self, item: ListItem) -> int:
        """
        Decides where to put an item based on priority order of pokemon.
        param: self: ArraySortedList object
               item: ListItem object 
        returns  index of the position where the item should be placed based on priority
        pre: item has to be a ListeItem object
        raises: TypeError if item is not a ListItem object
        Complexity: Best Case: O(n), n = len(ArraySortedList object)
                    Worst Case: O(n), n = len(ArraySortedList object)
        """
        if not isinstance(item, ListItem):
            raise TypeError("Item has to be a ListItem object")
        else:
            same_key = []  # New list containing all the ListItems of the array with the same key
            item_priority = item.value.get_priority()
            # Adds every pokemon with same key into a list
            for i in range(len(self) - 1, -1, -1): #O(n)
                if self[i].key == item.key:
                    same_key.append(i)
            # Checks through every pokemon with same key and compare priorities to determine where to add
            for index in same_key: # O(m)
                index_priority = self[index].value.get_priority()  # Get the priority value of pokemons with same keys as item
                # If same priority value, put in front of it
                if index_priority == item_priority:
                    return index + 1
                # If the priority of the pokemon being added is more, put it in front of it
                elif index_priority < item_priority:
                    return index + 1
            same_key.reverse()  # Reverse the order of same key so that it will loop from the back of the team instead of the front
            # Loops through the same key again but in reverse
            for index in same_key: # O(m)
                index_priority = self[index].value.get_priority()
                # If the priority of the item is less than the other pokemon's with same key, place behind it
                if index_priority > item_priority:
                    return index
                
    def __str__(self) -> str:
        """
        Print out all the elements in the array sorted list.
        param: self: ArraySortedList object
        returns: A string that displays all the elements in the list
        Complexity: Best Case: O(n)
                    Worst Case: O(n), where n = len(ArraySortedList object)
        """
        if len(self)!= 0: # If the arraysortedlist is not empty
            lst = [] # Create an empty list
            for i in range(len(self)): # Loop through array sorted list
                item = self[((len(self) - 1) - i)] # Gets the ListItem element in the array at given index
                lst.append(item.value) # Append it to the lst
            ret = ", ".join(list(map(str,lst))) # Create the output string by using the .join() method but first
                                                # Map every LinkList item to str to call the LinkList str method
            ret= ret.replace("(","") # Then replace the ( to "" because of the str method of the ListItem
            ret = ret.replace(")", "") # Then replace the ) to "" because of the str method of the ListItem
            return ret # Return the output string
        else:
            return ""  # Wlse return an empty string