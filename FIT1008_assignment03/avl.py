""" AVL Tree implemented on top of the standard BST. """

__author__ = 'Alexey Ignatiev'
__docformat__ = 'reStructuredText'

from bst import BinarySearchTree
from typing import TypeVar, Generic, Any, Union
from node import AVLTreeNode

K = TypeVar('K')
I = TypeVar('I')


class AVLTree(BinarySearchTree, Generic[K, I]):
    """ Self-balancing binary search tree using rebalancing by sub-tree
        rotations of Adelson-Velsky and Landis (AVL).
    """

    def __init__(self) -> None:
        """
            Initialises an empty Binary Search Tree
            :complexity: O(1)
        """

        BinarySearchTree.__init__(self)

    def update_weight(self, current: AVLTreeNode)-> None:
        """
        Update the weight of current node which is the number of subnodes it has
        :param current: An AVLTreeNode
        :return: None
        :Complexity: Best Case: O(1) where the current is a leaf node
                     Worst Case: O(k) where the current has k number of children nodes
        """
        # update the weight of current node by getting weight of left and right subtree
        current.weight = self.get_weight(current.left) + self.get_weight(current.right)

    def update_height(self, current: AVLTreeNode) -> int:
        """
        Updates the height the ancestor node and return the new height
        :param current: An AVLTreeNode
        :return An integer representing the height of the node
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # update the height of current node by getting the maximum height of left and right subtree and then + 1(itself)
        current.height = 1 + max(self.get_height(current.left), self.get_height(current.right))
        return current.height


    def is_leaf(self, current: AVLTreeNode) -> bool:
        """ 
        Simple check whether or not the node is a leaf.
        :param current: AVLTreenode
        :return: A boolean value, True if it's a leaf, False otherwise
        :pre: current has to be an AVLTreeNode class object
        :raises TypeError: if current is not an AVLTreeNode object
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # If current is not an AVLTreeNode object, raise TypeError
        if not isinstance(current, AVLTreeNode):
            raise TypeError("current has to be an AVLTreeNode object")
        
        return current.left is None and current.right is None

    def get_weight(self, current: AVLTreeNode)-> int:
        """
        Get the size of a node
        :param current: An AVLTreeNode
        :return An integer representing the weight of the node
        :complexity Best Case: O(1) where the current is a leaf node
                    Worst Case: O(k), where k is number of children nodes of current
        """
        # if current is not None get the weight of left child + weight of right child + 1(itself)
        if current is not None:
            return self.get_weight(current.left) + self.get_weight(current.right) + 1
        else:
            return 0

    def get_height(self, current: AVLTreeNode) -> int:
        """
        Get the height of a node. Return current.height if current is
        not None. Otherwise, return -1.
        :param current: An AVLTreeNode
        :return An integer representing the height of the node
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # if current is not None return its height else return -1
        if current is not None:
            return current.height
        else:
            return -1

    def get_balance(self, current: AVLTreeNode) -> int:
        """
        Compute the balance factor for the current sub-tree as the value (right.height - left.height).
        If current is None, return 0.
        :param current: AVLTreenode
        :return: int value representing the difference between (right.height - left.height)
        :pre: current has to be an AVLTreeNode class object
        :raises TypeError: if current is not an AVLTreeNode object
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # If current is not an AVLTreeNode object, raise TypeError
        if not isinstance(current, AVLTreeNode):
            raise TypeError("current has to be an AVLTreeNode object")
        
        if current is None: # if current is None return None else return right height - left height
            return 0
        return self.get_height(current.right) - self.get_height(current.left)

    def is_balanced(self, current: AVLTreeNode) -> bool:
        """
        Compute whether the tree rooted by the current is balanced or not where (right.height - left.height) is either
        -1, 0 or 1
        :param current: AVLTreenode
        :return: A boolean value, True if it's balanced, False otherwise
        :pre: current has to be an AVLTreeNode class object
        :raises TypeError: if current is not an AVLTreeNode object
        :complexity: Best Case: O(1)
                     Worst Case: O(1)
        """
        # If current is not an AVLTreeNode object, raise TypeError
        if not isinstance(current, AVLTreeNode):
            raise TypeError("current has to be an AVLTreeNode object")

        value = [-1, 0, 1]  # Accepted values to show that tree is balanced
        balance = self.get_balance(current)
        # If the balance (right.height - left.height) is not -1, 0 or 1, tree is not balance so return False
        if balance not in value:
            return False
        # Otherwise return True
        else:
            return True

    def insert_aux(self, current: AVLTreeNode, key: K, item: I) -> AVLTreeNode:
        """
        Attempts to insert an item into the tree, it uses the Key to insert it.
        After insertion, performs sub-tree rotation whenever it becomes unbalanced.
        :param current: AVLTreenode
                      : key which is the key to represent the item
                      : item which is the value stored in the tree
        :return: The root of the subtree
        :complexity: Best Case: O(1)
                     Worst Case: O(log n), where n is the depth of the tree
        """

        if current is None: # it is the node after leaf or when the tree is empty
            node = AVLTreeNode(key,item) # create a AVLTreeNode
            node.height = 0 # change its height to 0 since its a leaf node at the moment
            self.length += 1  # increase the length of the tree
            return node # return the node
        # if key is < current's key then go to the left subtree to insert it there
        elif key < current.key:
            current.left = self.insert_aux(current.left,key,item)
        # if key is > current's key then go to the right subtree to insert it there
        else:
            current.right = self.insert_aux(current.right,key,item)


        # update the height of the current node
        self.update_height(current)
        # update the weight of the current node
        self.update_weight(current)

        # check if the tree is balance
        balance = self.is_balanced(current)

        # if its not balance then balance the tree and return the new root, else just return the current
        if not balance:
            return self.rebalance(current)
        else:
            return current

    def delete_aux(self, current: AVLTreeNode, key: K) -> Union[AVLTreeNode, None, Any]:
        """
            Attempts to delete an item from the tree, it uses the Key to
            determine the node to delete. After deletion,
            performs sub-tree rotation whenever it becomes unbalanced.
            returns the new root of the subtree.
            :complexity: Best Case: O(1)
                         Worst Case: O(log n), where n is the depth of the tree
        """

        if current is None:  # key not found
            raise ValueError('Deleting non-existent item')
        elif key < current.key:
            current.left = self.delete_aux(current.left, key)
        elif key > current.key:
            current.right = self.delete_aux(current.right, key)

        else:# if same key, meaning you have found the node
            if self.is_leaf(current):
                self.length -= 1
                return None

            if current.left is None:
                self.length -= 1
                temp = current.right
                return temp

            elif current.right is None:
                self.length -= 1
                temp = current.left
                return temp

            successor = self.get_successor(current) # get the successor of current node
            succ2 = self.get_successor(successor) # get the successor of successor node (variable)
            # if the successor node has not successor then get its predecessor
            if succ2 is None:
                predecessor = self.get_predecessor(current)
                # update the tree to change current node to the predecessor node and delete the predecessor
                current.key = predecessor.key
                current.item = predecessor.item
                current.left = self.delete_aux(current.left,predecessor.key)

            # update the tree to change current node to the successor node and delete the successor
            else:
                current.key = successor.key
                current.item = successor.item
                current.right = self.delete_aux(current.right, successor.key)

        # update the height of the current node
        self.update_height(current)
        # update the weight of the current node
        self.update_weight(current)

        # check if the tree is balance
        balance = self.is_balanced(current)

        # if its not balance then balance the tree and return the new root, else just return the new root
        if not balance:
            return self.rebalance(current)
        else:
            return current

    def left_rotate(self, current: AVLTreeNode) -> AVLTreeNode:
        """
            Perform left rotation of the sub-tree.
            Right child of the current node, i.e. of the root of the target
            sub-tree, should become the new root of the sub-tree.
            returns the new root of the subtree.
            Example:

                  current                                      child
                /       \                                      /   \
            l-tree     child           -------->        current     r-tree
                      /     \                           /     \
                 center     r-tree                 l-tree     center

            :complexity: Best Case: O(1)
                         Worst Case: O(k), where k is the number of children nodes of current
        """
        child = current.right # child = current's right node
        center = child.left # center = child's left node

        # perform left rotation
        child.left = current # child's left = current
        current.right = center # current's right = center

        # update the height/weight for both current and child node
        self.update_height(current)
        self.update_height(child)
        self.update_weight(current)
        self.update_weight(child)

        return child

    def right_rotate(self, current: AVLTreeNode) -> AVLTreeNode:
        """
            Perform right rotation of the sub-tree.
            Left child of the current node, i.e. of the root of the target
            sub-tree, should become the new root of the sub-tree.
            returns the new root of the subtree.
            Example:

                       current                                child
                      /       \                              /     \
                  child       r-tree     --------->     l-tree     current
                 /     \                                           /     \
            l-tree     center                                 center     r-tree

            :complexity: Best Case: O(1)
                         Worst Case: O(k), where k is the number of children nodes of current
        """

        child = current.left  # child = current's left node
        center = child.right  # center = child's right node

        # perform right rotation
        child.right = current  # child's left = current
        current.left = center  # current's right = center

        # update the height/weight for both current and child node
        self.update_height(current)
        self.update_height(child)
        self.update_weight(current)
        self.update_weight(child)

        return child

    def rebalance(self, current: AVLTreeNode) -> AVLTreeNode:
        """ Compute the balance of the current node.
            Do rebalancing of the sub-tree of this node if necessary.
            Rebalancing should be done either by:
            - one left rotate
            - one right rotate
            - a combination of left + right rotate
            - a combination of right + left rotate
            returns the new root of the subtree.
        """
        if self.get_balance(current) >= 2:
            child = current.right
            if self.get_height(child.left) > self.get_height(child.right):
                current.right = self.right_rotate(child)
            return self.left_rotate(current)

        if self.get_balance(current) <= -2:
            child = current.left
            if self.get_height(child.right) > self.get_height(child.left):
                current.left = self.left_rotate(child)
            return self.right_rotate(current)

        return current



    def kth_largest(self, k: int) -> AVLTreeNode:
        """
        Returns the kth largest element in the tree.
        k=1 would return the largest.
        :param k: An integer representing which largest element in the tree
        :return: An AVLTreeNode which will be the kth Largest in the tree
        :complexity: Best Case: O(1)
                     Worst case: O(log n)
        """

        node = self.kth_largest_aux(self.root,k)
        return node



    def kth_largest_aux(self, current: AVLTreeNode, k: int) -> AVLTreeNode:
        """
        Method to get the Kth largest Node
        :param current: An AVLTreeNode
        :param k: An integer representing which kth largest element in the tree
        :return: An AVLTreeNode which will be the kth Largest in the tree
        :complexity: Best Case: O(1) where current is the kth largest
                     Worst Case: O(log n)
        """
        # If the current node has a right node then the current has (right_node_weight+1) right elements
        if current.right is not None:
            right_weight = current.right.weight + 1
        # Else the current has 0 right nodes
        else:
            right_weight = 0

        # If right_weight == k-1 it means this node is the Kth largest
        if right_weight == k - 1:
            return current
        # If right_weight >= k means that the Kth element is in the right subtree
        elif right_weight >= k: 
            return self.kth_largest_aux(current.right, k)  # Look into the right subtree
        # Else the Kth largest element is in the left subtree so we go to the left subtree
        else:
            k = k - right_weight - 1 # Because none of the elements in the right is the answer so we subtract the number of K by the element of the right subtree
            return self.kth_largest_aux(current.left, k)





