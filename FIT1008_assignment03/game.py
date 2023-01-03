""" 
Summary: Python file containing methods related to the generation of prime numbers
Author: Teoh Tian Zhi, Toh Thien Yew, Tong Jet Kit, Bryan Wong Jun Kit
Last Modified: 24/5/2022
"""

from __future__ import annotations
# ^ In case you aren't on Python 3.10

from random_gen import RandomGen
from hash_table import LinearProbePotionTable
from potion import Potion
from avl import AVLTree
from typing import TypeVar
R = TypeVar('R', int, float)  # R represents int and float numbers


class Game:

    def __init__(self, seed: int=0) -> None:
        """
        Game class constructor
        :param: seed an Integer to save the state
        :pre: seed has to be an Integer
        :raises TypeError: if seed is not an Integer
        """
        if type(seed) != int:
            raise TypeError("seed has to be an Integer")
        
        self.rand = RandomGen(seed)

    def set_total_potion_data(self, potion_data: list) -> None:
        """
        Method which creates the hash table and sets all potion date before adding it to the hash table
        :param: potion_data is a list containing all the data of the potions
        :complexity: Best Case: O(1)
                     Worst Case: O(n) where n is the number of elements in potion_data


        I have decided to use a Dictionary ADT which is a container of objects that can be identified with a key/label.
        I used a Dictionary ADT so that we can have direct access to each of the potion object without
        the need to traversing an array to find said object.

        The complexity is O(n) because there is for loop there needed to loop through all the elements in potion_data
        which can be an arbitrary size of n
        """
        self.table = LinearProbePotionTable(len(potion_data))  # Create the hash table with the maximum size the length of the potion_data list

        for potions in potion_data:
            potion_obj = Potion.create_empty(potions[0], potions[1], potions[2])  # Create a Potion object with the data from the list
            self.table.insert(potions[1], potion_obj)  # Insert the Potion object into the hash table

    def add_potions_to_inventory(self, potion_name_amount_pairs: list[tuple[str, float]]) -> None:
        """
        Method to update potion quantity in PotionCorp's inventory and then create an AVL to sort the potions based on price
        :param: potion_name_amount_pairs: A list of tuples containing the potion name and the quantity in litres
        :complexity: Best Case: O(1)
                     Worst Case: O(C log N)  where C is the number of elements in potion_name_amount_pairs and N is the
                                             number of nodes in the tree.


        I have decided to use a Tree ADT which is a container of objects that has a parent-child relationship with each
        other. I use this parent-child relationship to sort the potions based on its price.

        The complexity is O(C log N) where C is the number of elements in potion_name_amount_pairs and N is the number
        of nodes in the tree. This is because the for loop will loop through potion_name_amount_pairs (C) and at each 
        iteration it will add the potion_object obtain from the hash table to the tree and this insertion complexity 
        costs O(log N). Therefore the complexity is O(C log N).
        """
        self.tree_price = AVLTree()  # Creates an AVL tree object sorted by price
        self.C = len(potion_name_amount_pairs)  # Instance variable representing the amount of potion in stock

        for potions_to_add in potion_name_amount_pairs:  # O(C) complexity
            potion_obj = self.table[potions_to_add[0]]   # Gets the potion data from the hash table based on their name
            potion_obj.set_quantity(potions_to_add[1])   # Set the quantity of that potion to the new quantity
            price = potion_obj.get_price()               # Get the price of the potion object
            self.tree_price[price] = potion_obj          # Add the potion_obj to the AVLTree where the key is the price

    def choose_potions_for_vendors(self, num_vendors: int) -> list:
        """
        Method that chooses a potion in PotionCorp's inventory at random to add into each vendor's inventory
        :param: num_vendors: An integer representing the number of vendors in the game
        :pre: num_vendors has to be an Integer
              num_vendors has to be > 0
        :raises: TypeError: if num_vendors is not an Integer
                 ValueError: if num_vendors <= 0
        :complexity: Best Case: O(1)
                     Worst Case: O(C log N) where C is the value of num_vendors and N is the number of nodes in the tree.
        :return: A list containing tuples with the name of the potion and the quantity of potion in the vendors' inventory


        I have decided to use 2 List ADT which is a container of objects in sequential order of its addition.
        I use this ADT to temporarily store the potion obtained from the tree and store the potions that will be sold
        by the vendor. This ADT is the simplest kind therefore we use this ADT.

        The complexity is O(C log N) where C is the value of num_vendors and N is the number of nodes in the tree.
        This is because the for loop will iterate num_vendors (C) times and at each iteration it will find the
        Xth largest potion_object by calling the kth_largest() method which cost O(log N).

        Although there is the insertion of removed_potion, the insertion cost O(log N) and the for loop for the insertion
        will iterate num_vendors time too so therefore it will also cost O(C log N). The total cost of operation will
        be O(2ClogN) which will then evaluated to be O(C log N) according to the Big-O theory to drop any constant.
        Therefore the complexity is O(C log N).
        """
        if type(num_vendors) != int:
            raise TypeError("num_vendors has to be Integer")
        elif num_vendors <= 0:
            raise ValueError("num_vendors has to be more than 0")
        
        vendors_inventory = []  # Empty list representing all the vendors' current inventory
        potion_removed = []     # Empty list which will contained all the potion objects that were removed from the AVL
        
        # For loop to loop through each vendor and add a random potion to their inventory
        for vendor in range(num_vendors):
            p = self.rand.randint(self.C)  # p is a random number from 1 to C inclusive
            potion_to_add = self.tree_price.kth_largest(p)  # Finds the kth largest potion to add into vendor's inventory
            potion_removed.append(potion_to_add.item)       # Append the potion_to_add.item to potion_removed list to remember which potion was removed from the AVL
            vendors_inventory.append((potion_to_add.item.get_name(), potion_to_add.item.get_quantity()))   # Appends a tuple containing the potion's name and quantity to the vendor's inventory
            del self.tree_price[potion_to_add.item.get_price()]  # Deletes the potion node from the AVL tree so that different vendors won't have the same potion
            self.C -= 1   # Subtract C by 1 so next iteration will look for potions from 1 to C - 1
        
        # For loop to add all potions removed back into the AVLTree
        for potion_obj in potion_removed:
            price = potion_obj.get_price()        # Get the price of the potion object
            self.tree_price[price] = potion_obj   # Add the potion_obj to the AVLTree where the key is the price
            self.C += 1   # Increment C to restore it back to original value
            
        return vendors_inventory  # Return list of tuples of vendor's inventory

    def solve_game(self, potion_valuations: list[tuple[str, float]], starting_money: list[R]) -> list[float]:
        """
        Method that intake the potion valution and list of starting money and return a list of maximum money that a player has at the end of the day
        :param: potion_valuations: list of tuples containing what potion the vendor is selling and the valuation each litre has
                starting_money: list of floats containing the starting money the player has each day
        :complexity: Best Case: O(N log N + M x N) where N is the length of potion_valuations and M is the length of starting_money
                     Worst Case: O(N log N + M x N) where N is the length of potion_valuations and M is the length of starting_money
        :return: A list max_money which contains the maximum money the player has at the end of the day after sales


        I have decided to use a List ADT which is a container of objects in sequential order of its addition
        and a SortedList ADT which is similar to the List ADT but the elements are sorted.
        I use the List ADT to store the income obtain for every attempt since it is the simplest kind
        For the SortedList ADT I used it to so I can store the profit margin
        (((selling_price - buying_price) / buying_price) * 100) of each potion from the highest to
        the lowest to be able to obtain maximum profit.

        The complexity is O(N log N + M x N) where N is the number of elements in potion_valuation and
        M is the number of elements in starting_money.
        This is because at first I will calculate the profit margins of each selling potion and then append it to a list
        and then I sort it by using merge_sort, so this process will take N+NlogN and will evaluate to O(N log N).

        After that I will use a for loop to loop through each element in starting money to signify each attempts.
        At each iteration I will loop through the profit_margin sorted list and keep buying the maximum amount of potions
        I can get from the potion with the highest profit_margin to the potion with the lowest profit_margin until
        I have no money or I bought all the potions.
        Therfore, this process will be O(M x N).

        After combining both processes the complexity will result it O(N log N + M x N).

        How our solution works:
        First we calculate the profit margins of each selling potion and stored it in a list which will be sorted from
        highest to lowest. Then at each day, we will start the buying from the potion with the highest profit margins
        to the potion with the lowest profit margins.
        At each buying process we will buy the maximum amount of potion we can get according to the starting_money we have.
        At the end of the buying process when we either bought all the potion or no more money left, we then add up how
        much money we currently have at the end of the dayand add it into a list.
        This process will then start over for the next day with a new starting money.
        """

        max_money = []          # Maximum money the player can have at the end of the day
        profit_margin_lst = []  # Creates a list but sorted by profit margins instead

        # For loop to calculate profit margins of each potion and add it to a list and then use mergesort to sort
        for valuation in potion_valuations:
            potion_obj = self.table[valuation[0]]   # Gets the potion object from the hash table by using their name
            potion_obj.set_valuation(valuation[1])  # Sets the valuation instance variable to the selling price

            selling_price = potion_obj.get_valuation()  # How much an adventure is willing to buy the potion for one litre
            buying_price = potion_obj.get_price()       # How much one litre of potion cost from the vendor
            profit_margin = ((selling_price - buying_price) / buying_price) * 100  # Profit margins of each potion in percentage
            profit_margin_lst.append((profit_margin, potion_obj))  # Append a tuple of (profit_margin, potion_obj) into the list called profit_margin_lst

        self.merge_sort(profit_margin_lst)  # Sort profit_margin_lst by using merge sort

        # Start of day
        for i in range(len(starting_money)):
            income = 0  # Total income made from sales to adventurers
            j = 0       # J = 0 for while loop sentinel
            
            # Go through each potion that has been sorted by their profit margin to buy
            while starting_money[i] > 0 and j < len(profit_margin_lst):
                potion = profit_margin_lst[j][1]    # Gets the potion obj from the sorted list
                price = potion.get_price()          # Gets the potion buying price
                quantity = potion.get_quantity()    # Gets the potion quantity
                valuation = potion.get_valuation()  # Gets the potion valuation by adventurers
                total_cost = price * quantity       # Calculates the total cost to buy the entire potion in stock

                # If player can afford to buy entire potion stock, then do so
                if total_cost <= starting_money[i]:
                    starting_money[i] -= total_cost    # Player buys the whole potion so subtract money with total cost
                    income += quantity * valuation     # Immediately sold to adventures and calculate income gain
                
                # If player cannot afford to buy the entire potion in stock, purchase the maximum litres affordable
                elif total_cost > starting_money[i]:
                    max_amount = (1 / price) * starting_money[i]  # Calculate the maximum amount of litres purchaseable
                    starting_money[i] -= starting_money[i]        # Sets the money to the new value after purchase
                    income += max_amount * valuation   # Sells the potion to the adventurers and add the income

                j += 1  # Increment j by one
                
            max_money.append(income + starting_money[i])  # Adds total income for the day into the list plus remaining money and proceed to next day

        return max_money  # Returned the list of maximum money the player has at the end of the day

    def merge_sort(self, arr: list[R]) -> list[R]:
        """
        A sorting algorithm to sort an array in descending order
        :param arr: An unsorted array
        :complexity: Best case: O(n log n) where n is the number of elements in arr
                     Worst case: O(n log n) where n is the number of elements in arr
        :return: A sorted array
        """
        # If the list has more than 1 elements
        if len(arr) > 1:

            # Finding the mid of the array
            mid = len(arr) // 2

            # Dividing the array elements
            L = arr[:mid]

            # into 2 halves
            R = arr[mid:]

            # Sorting the first half
            self.merge_sort(L)

            # Sorting the second half
            self.merge_sort(R)

            i = j = k = 0   # set i,j,k to 0

            # While i is less than length of array L and j is less than length of array R
            while i < len(L) and j < len(R):
                # If L[i][0] is more than or equal to R[i][0]
                if L[i][0] >= R[j][0]:
                    arr[k] = L[i]       # Array element at index k will be the element in L at index i
                    i += 1              # Point to the next element in array L
                # Else if L[i][0] is less than R[i][0]
                else:
                    arr[k] = R[j]   # Array element at index k will be the element in R at index j
                    j += 1          # Point to the next element in array R
                k += 1   # go to the next swapping point in arr

            # Checking if any element was left
            # If there are add them into the array
            while i < len(L):
                arr[k] = L[i]
                i += 1
                k += 1

            while j < len(R):
                arr[k] = R[j]
                j += 1
                k += 1


