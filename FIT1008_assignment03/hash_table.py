""" Hash Table ADT

Defines a Hash Table using Linear Probing for conflict resolution.
It currently rehashes the primary cluster to handle deletion.
"""
__author__ = 'Brendon Taylor, modified by Jackson Goerner'
__docformat__ = 'reStructuredText'
__modified__ = '21/05/2020'
__since__ = '14/05/2020'

from referential_array import ArrayR
from typing import TypeVar, Generic
from potion import Potion

T = TypeVar('T')


class LinearProbePotionTable(Generic[T]):
    """
    Linear Probe Potion Table

    This potion table does not support deletion.

    attributes:
        count: number of elements in the hash table
        table: used to represent our internal array
        table_size: current size of the hash table
    """

    def __init__(self, max_potions: int, good_hash: bool = True, tablesize_override: int = -1) -> None:
        """
        LinearProbePotionTable class constructor
        :params: self: A LinearProbePotionTable class object
                 max_potions: An Integer representing the maximum amount of potions to be added to the potion table
                 good_hash: A boolean to signify whether to use good_hash or bad_hash method to hash keys. Default is True
                 tablesize_override: An Integer which overrides the value of max_potions if != -1. Default is -1
        :pre: max_potions has to be Integer
              good_hash has to be Boolean
              tablesize_override has to be Integer
        :raises TypeError: if max_potions is not an Integer
                           if good_hash is not Boolean
                           if tablesize_override is not an Integer
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # Preconditions to make sure data type of input arguements matches the typehints provided
        if type(max_potions) != int:
            raise TypeError("max_potions has to be an Integer")
        elif type(good_hash) != bool:
            raise TypeError("good_hash has to be Boolean")
        elif type(tablesize_override) != int:
            raise TypeError("tablesize_override has to be an Integer")

        # Statistic setting
        self.conflict_count = 0
        self.probe_max = 0
        self.probe_total = 0

        if good_hash:
            self.hashing = Potion.good_hash
        else:
            self.hashing = Potion.bad_hash

        if tablesize_override == -1:
            self.initalise_with_tablesize(max_potions)
        else:
            self.initalise_with_tablesize(tablesize_override)

    def hash(self, potion_name: str) -> int:
        """
        Method which hash the input and find the location for it in the hash table
        :params: self: A LinearProbePotionTable class object
                 potion_name: A string representing the name of the potion
        :pre: potion_name has to be String
        :raises TypeError: if potion_name is not String
        :complexity: Best Case: O(1)
                     Worst Case: O(n) where n is number of character in the input string
        """
        # potion_name has to be String, else raise TypeError if not
        if type(potion_name) != str:
            raise TypeError("potion_name has to be String")

        size = len(self.table)
        return self.hashing(potion_name, size)

    def statistics(self) -> tuple:
        """
        Method which do analysis on the hash table, which return the returns a tuple of 3 values(num_conflict,probe_total and probe_max)
        :params: self: A LinearProbePotionTable class object
        :complexity: Best Case: O(1)
                    Worst Case: O(1)
        """
        return (self.conflict_count, self.probe_total, self.probe_max)

    def __len__(self) -> int:
        """
        Returns number of elements in the hash table
        :complexity: O(1)
        """
        return self.count

    def __linear_probe(self, key: str, is_insert: bool) -> int:
        """
        Find the correct position for this key in the hash table using linear probing
        :complexity best: O(K) first position is empty
                          where K is the size of the key
        :complexity worst: O(K + N) when we've searched the entire table
                           where N is the table_size
        :raises KeyError: When a position can't be found
        """
        position = self.hash(key)  # get the position using hash
        probe = 0  # probe = the probelength of a key

        if is_insert and self.is_full():  # if user is trying to insert into a full hash table raise an Error
            raise KeyError(key)

        for _ in range(len(self.table)):  # start traversing
            if self.table[position] is None:  # found empty slot
                if is_insert:  # if user is inserting an element return the position
                    self.set_probeMax(probe) # if found a spot check if the probe_length is the maximum probe length
                                             # throughout the execution of the code
                    return position

                else:
                    raise KeyError(key)  # so the key is not in
            elif self.table[position][0] == key:  # found key
                return position
            else:  # there is something but not the key, try next
                self.probe_total += 1  # increase the total number of probes done
                probe += 1  # increase the probe length of that key
                position = (position + 1) % len(self.table)  # go to the next position

        raise KeyError(key)

    def __contains__(self, key: str) -> bool:
        """
        Checks to see if the given key is in the Hash Table
        :see: #self.__getitem__(self, key: str)
        """
        try:
            _ = self[key]
        except KeyError:
            return False
        else:
            return True

    def __getitem__(self, key: str) -> T:
        """
        Get the item at a certain key
        :see: #self.__linear_probe(key: str, is_insert: bool)
        :raises KeyError: when the item doesn't exist
        """
        position = self.__linear_probe(key, False)
        return self.table[position][1]

    def __setitem__(self, key: str, data: T) -> None:
        """
        Set an (key, data) pair in our hash table
        :see: #self.__linear_probe(key: str, is_insert: bool)
        :see: #self.__contains__(key: str)
        """
        if len(self) == len(self.table) and key not in self:
            raise ValueError("Cannot insert into a full table.")
        position = self.__linear_probe(key, True)

        if position != self.hash(key):  # if the linear_probe position is not the hashed position that means theres a conflict
            self.conflict_count += 1  # so increase conflict_count by 1

        if self.table[position] is None:
            self.count += 1
        self.table[position] = (key, data)

    def initalise_with_tablesize(self, tablesize: int) -> None:
        """
        Initialise a new array, with table size given by tablesize.
        :complexity: O(n), where n is len(tablesize)
        """

        self.count = 0
        self.table = ArrayR(tablesize)

    def is_empty(self):
        """
        Returns whether the hash table is empty
        :complexity: O(1)
        """
        return self.count == 0

    def is_full(self):
        """
        Returns whether the hash table is full
        :complexity: O(1)
        """
        return self.count == len(self.table)

    def insert(self, key: str, data: T) -> None:
        """
        Utility method to call our setitem method
        :see: #__setitem__(self, key: str, data: T)
        """
        self[key] = data

    def set_probeMax(self, probe_length: int) -> None:
        """
        Helper method to set the probe_max instance variable
        :param: self referring to the hash_table object itself
                probe_length: An integer to signify the probe_length of the linear probing
        :pre: probe_length has to be Integer
        :raises TypeError: if probe_length is not Integer
        :complexity: Best case: O(1)
                     Worst case: O(1)
        :return: None
        """
        if type(probe_length) != int:
            raise TypeError("probe_length has to be an Integer")

        # if probe_length is larger than the probe_max instance var, reassign the probe_max value to the probe_length
        if probe_length > self.probe_max:
            self.probe_max = probe_length

    def __str__(self) -> str:
        """
        Returns all they key/value pairs in our hash table (no particular order)
        :complexity: O(N) where N is the table size
        """
        result = ""
        for item in self.table:
            if item is not None:
                (key, value) = item
                result += "(" + str(key) + "," + str(value) + ")\n"
        return result