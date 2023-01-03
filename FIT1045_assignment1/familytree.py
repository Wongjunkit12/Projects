"""
Template for Programming Assignment FIT1045 - OCT 2021
Family Trees

In this assignment we create a Python module to implement some
basic family tree software, where users can look up various relationships
that exist in the database, as well as merging two family tree databases
that may contain overlapping information.

Functions  1-7 are due in Part 1 of the assignment. Functions
for 8 and 9 are due in Part 2.

We represent each entry in a family tree database as a list of three
strings [name, father, mother], where name is a person's name, father
is the name of their father, and mother is the name of their mother.
Where a particular relationship is unknown, the value None is used.
For example:

>>> duck_tree = [['Fergus McDuck', None, None],
...           ['Downy ODrake', None, None],
...           ['Quackmore Duck', None, None],
...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
...           ['Huey Duck', None, 'Della Duck'],
...           ['Dewey Duck', None, 'Della Duck'],
...           ['Louie Duck', None, 'Della Duck']]


The file hobbit-family.txt is also provided for testing. The database
used in this file has been compiled using the info at
http://lotrproject.com/hobbits.php. Character names are by J.R.R. Tolkein.

For more information see the function documentations below and the
assignment sheet.

"""

# Part 1 (due Week 6) #

# Task A
def read_family(filename):
    """
    Input: A filename (filename) containing a family tree database where
    each line is in the form name, father, mother
    Output: A family tree database containing the contents of the file
    in the format specified above, or None if the file is in the incorrect
    format.
    
    For example:

    >>> hobbits = read_family('hobbit-family.txt')
    >>> len(hobbits)
    119
    >>> hobbits[118]
    ['Sancho Proudfoot', 'Olo Proudfoot', None]

     """
    file = open(filename)
    new_list = []
    table = []
    
    for name in file: # Adds names into new_list and strips empty space after each family to get rid of new line '/n'
        new_list.append(name.strip())
        
    for x in range(len(new_list)): # Split each family to a list separated by a ','
        rows = new_list[x].split(',')
        for y in range(len(rows)): # Checks through each name in the row and removes empty spaces.
           rows[y] = rows[y].strip()
           if rows[y] == '': # If no parent, replace empty '' with None
               rows[y] = None
        table.append(rows) # Adds edited rows into table
    file.close()
    return table


# Task B
def person_index(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: The index value of the person's entry in the family tree,
            or None if they have no entry.

    For example:
    >>> duck_tree = [['Fergus McDuck', None, None],
    ...           ['Downy ODrake', None, None],
    ...           ['Quackmore Duck', None, None],
    ...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
    ...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
    ...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Huey Duck', None, 'Della Duck'],
    ...           ['Dewey Duck', None, 'Della Duck'],
    ...           ['Louie Duck', None, 'Della Duck']]
    >>> person_index('Dewey Duck', duck_tree)
    8
    >>> person_index('Daffy Duck', duck_tree)
    
    """
    for row in family: # Checks through each row of the family tree and returns index of first entry of person
        if row[0] == person:
            return family.index(row)
    return None # Return None if unable to find person


def father(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: The name of person's father, or None if the information is
            not in the database.

    For example:
    >>> duck_tree = [['Fergus McDuck', None, None],
    ...           ['Downy ODrake', None, None],
    ...           ['Quackmore Duck', None, None],
    ...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
    ...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
    ...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Huey Duck', None, 'Della Duck'],
    ...           ['Dewey Duck', None, 'Della Duck'],
    ...           ['Louie Duck', None, 'Della Duck']]
    >>> father('Della Duck', duck_tree)
    'Quackmore Duck'
    >>> father('Huey Duck', duck_tree)

    >>> father('Daffy Duck', duck_tree)
    
    """
    Index_of_person = person_index(person, family) # Calls function from Task B part 1
    has_no_father = Index_of_person == None or family[Index_of_person][1] == None
    
    if has_no_father: # If person is not in the list or have no father, return None. Otherwise return father's name
        return None
    else:
        return family[Index_of_person][1]


def mother(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: The name of person's mother, or None if the information is
            not in the database.

    For example:
    >>> duck_tree = [['Fergus McDuck', None, None],
    ...           ['Downy ODrake', None, None],
    ...           ['Quackmore Duck', None, None],
    ...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
    ...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
    ...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Huey Duck', None, 'Della Duck'],
    ...           ['Dewey Duck', None, 'Della Duck'],
    ...           ['Louie Duck', None, 'Della Duck']]
    >>> mother('Hortense McDuck', duck_tree)
    'Downy ODrake'
    >>> mother('Fergus McDuck', duck_tree)

    >>> mother('Daffy Duck', duck_tree)
    
    """
    Index_of_person = person_index(person, family)
    has_no_mother = Index_of_person == None or family[Index_of_person][2] == None
    
    if has_no_mother: # If person is not in the list or have no mother, return None. Otherwise return mother's name
        return None
    else:
        return family[Index_of_person][2]


# Task C
def children(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: A list containing the names of all of person's children.
    
    For example:
    >>> duck_tree = [['Fergus McDuck', None, None],
    ...           ['Downy ODrake', None, None],
    ...           ['Quackmore Duck', None, None],
    ...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
    ...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
    ...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Huey Duck', None, 'Della Duck'],
    ...           ['Dewey Duck', None, 'Della Duck'],
    ...           ['Louie Duck', None, 'Della Duck']]
    >>> sorted(children('Della Duck', duck_tree))
    ['Dewey Duck', 'Huey Duck', 'Louie Duck']
    >>> children('Donald Duck', duck_tree)
    []
    >>> sorted(children('Fergus McDuck', duck_tree))
    ['Hortense McDuck', 'Scrooge McDuck']
    >>> children('Donald Mallard', duck_tree)
    []
    
    """
    children = []
    
    for row in family: # Check through each family and if either the father or mother is the specified person, add their children into children list
        if row[1] == person or row[2] == person:
            children.append(row[0])
    return children


# Task D
def grandchildren(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: A list containing only the names of the grandchildren of person
        that are stored in the database.
    
    For example:
    >>> duck_tree = [['Fergus McDuck', 'Dingus McDuck', 'Molly Mallard'],
    ...           ['Downy ODrake', None, None],
    ...           ['Quackmore Duck', None, None],
    ...           ['Donald Duck','Quackmore Duck','Hortense McDuck'],
    ...           ['Della Duck', 'Quackmore Duck', 'Hortense McDuck'],
    ...           ['Hortense McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Scrooge McDuck', 'Fergus McDuck', 'Downy ODrake'],
    ...           ['Huey Duck', None, 'Della Duck'],
    ...           ['Dewey Duck', None, 'Della Duck'],
    ...           ['Louie Duck', None, 'Della Duck']]
    >>> sorted(grandchildren('Quackmore Duck', duck_tree))
    ['Dewey Duck', 'Huey Duck', 'Louie Duck']
    >>> sorted(grandchildren('Downy ODrake', duck_tree))
    ['Della Duck', 'Donald Duck']
    >>> grandchildren('Della Duck', duck_tree)
    []
    
    """
    person_children = children(person, family) # Calls function from Task C
    grandchildren = []

    for child in person_children: # Loops for each child in the children list.
        for row in family:
            if row[1] == child or row[2] == child: # If father or mother is the child of the person, append into list
                grandchildren.append(row[0])
    return grandchildren # Returns empty list if no grandchildren


# Task E
def cousins(person, family):
    """
    Input: A person's name (person) and a family tree database (family)
            as specified above.
    Output: A list containing the names of all cousins of person that
            are stored in the database.
    
    For example:
    
    >>> hobbits = read_family('hobbit-family.txt')
    >>> sorted(cousins('Frodo Baggins', hobbits))
    ['Daisy Baggins', 'Merimac Brandybuck', 'Milo Burrows', 'Saradoc Brandybuck', 'Seredic Brandybuck']
    
    """
    person_father = father(person, family)
    father_father = father(person_father, family) # Grandfather on father's side
    gff = grandchildren(father_father, family) # Grandchildren of grandfather on father's side
    father_mother = mother(person_father, family)
    gmf = grandchildren(father_mother, family) # Grandchildren of grandmother on father's side
    person_mother = mother(person, family)
    mother_father = father(person_mother, family)
    gfm = grandchildren(mother_father, family) # Grandchildren of grandfather on mother's side
    mother_mother = mother(person_mother, family)
    gmm = grandchildren(mother_mother, family)
    cousins = set() # Set is used to prevent duplicates being added into the cousin list
    
    has_father_father = False
    has_mother_father = False
    i = 0
    while father_father != None and i < len(gff): # Adds all grandchildren from father's side
        if gff[i] != person and father(gff[i], family) != person_father: # To exclude the person him/herself and his/her siblings
            cousins.add(gff[i])
        i += 1
        has_father_father = True
    
    j = 0
    while father_mother != None and j < len(gmf) and not has_father_father:
        if gmf[j] != person and mother(gmf[j], family) != person_mother:
            cousins.add(gmf[j])
        j += 1

    k = 0
    while mother_father != None and k < len(gfm): # Adds all grandchildren from mother's side
        if gfm[k] != person and father(gfm[k], family) != person_father:
            cousins.add(gfm[k])
        k += 1
        has_mother_father = True
        
    l = 0
    while mother_mother != None and l < len(gmm) and not has_mother_father:
        if gmm[l] != person and mother(gmm[l], family) != person_mother:
            cousins.add(gmm[l])
        l += 1
        
    return list(cousins) # returns cousins set converted into a list


# Part 2: (due Week 11) #

def options(partial, visited, family):
    parents = []
    # Adds all possible parents of people in the partial solutions list into the parents list.
    for person in partial:
        if person not in visited: # To not add duplicate parents.
            parents.append(father(person, family))
            parents.append(mother(person, family))
            visited.append(person)
    return [i for i in parents if i != None]


# Recursive function to find direct ancestor
def ancestor_recursion(p1, p2, family, partial=[], visited=[], has_ran = False):
    # Base case. Returns partial solution as complete solution if either p1 or p2 is found as a direct ancestor of the either.
    if (len(partial) != 0) and (partial[-1] == p1 or partial[-1] == p2):
        return partial
    else:
        res = []
        # Runs the first time to add the two people p1 and p2 into partial so that options function will find their parents
        if not has_ran:
            partial.append(p1)
            partial.append(p2)
            has_ran = True
        # For loop to loop through all the parents and add it into augmented solutions.
        for parent in options(partial, visited, family):
            augmented = partial + [parent]
            res += ancestor_recursion(p1, p2, family, augmented, visited, has_ran) # Recursive call and pass augmented solutions along with visited list to next call.
        return res
     
     
def direct_ancestor(p1, p2, family):
    """
    Input: Two names (p1, p2) and a family tree database (family).
    Output: One of the following three string outputs (where p1 and
            p2 are the given input strings, and n is a non-negative integer):
            "p1 is a direct ancestor of p2, n generations apart."
            "p2 is a direct ancestor of p1, n generations apart."
            "p1 is not a direct ancestor or descendant of p2."

    For example:

    >>> hobbits = read_family('hobbit-family.txt')
    >>> direct_ancestor('Frodo Baggins', 'Frodo Baggins', hobbits)
    'Frodo Baggins is a direct ancestor of Frodo Baggins, 0 generations apart.'
    >>> direct_ancestor('Frodo Baggins', 'Gormadoc Brandybuck', hobbits)
    'Gormadoc Brandybuck is a direct ancestor of Frodo Baggins, 5 generations apart.'
    
    """
    # If both people inputted are the same, return this string
    if p1 == p2:
        return "{0} is a direct ancestor of {1}, 0 generations apart.".format(p1, p2)
    
    # Backtracking method to find whether or not they are direct ancestors/descendants.
    generation_lst = ancestor_recursion(p1, p2, family)
    
    # Removes duplicated p1 or p2 at the beginning of the list
    if len(generation_lst) != 0 and generation_lst[0] == generation_lst[-1]:
        generation_lst.remove(p1)
    elif len(generation_lst) != 0 and generation_lst[1] == generation_lst[-1]:
        generation_lst.remove(p2)
    
    # Counting the generations apart of p1 and p2
    has_found = False
    i = 1
    while not has_found and i < len(generation_lst):
        if generation_lst[i] == p1 or generation_lst[i] == p2:
            has_found = True
            generations_apart = i
        i += 1
        
    # If the first index of the list is p1 and the last index is p2, return p2 is direct ancestor of p1. If first index is p2 and last index is p1, return p1 is a direct ancestor of p2.
    if len(generation_lst) != 0 and generation_lst[0] == p1 and generation_lst[-1] == p2:
        return "{0} is a direct ancestor of {1}, {2} generations apart.".format(p2, p1, generations_apart)
    elif len(generation_lst) != 0 and generation_lst[0] == p2 and generation_lst[-1] == p1:
        return "{0} is a direct ancestor of {1}, {2} generations apart.".format(p1, p2, generations_apart)


# Function to check whether p1 or p2 are siblings or not. If same parents/parent, return True
def are_siblings(p1, p2, family):
    if father(p1, family) != None:
        return father(p1, family) == father(p2, family)
    elif mother(p1, family) != None:
        return mother(p1, family) == mother(p2, family)


# Returns all the parents of each person in the inputted list
def parents(people, family):
    parents = []
    for person in people:
        parents += [father(person, family)]
        parents += [mother(person, family)]
    return [parent for parent in parents if parent != None]


# Recursive function to find closest common ancestors between p1 and p2
def cousin_recursion(p1, p2, family, p1_lst, p2_lst):
    common_ancestors, visited = [], []
    # For loop to add common ancestors of p1 and p2 into an empty list
    for person in p1_lst:
        if person in p2_lst and person not in visited:
            common_ancestors.append(person)
            visited.append(person) # Visited list used to not add duplicates
    # Base case. If common ancestors list contains more than one person, exit recursion
    if len(common_ancestors) > 0:
        return common_ancestors
    else:
        # Adds parents of everybody in p1_lst and p2_lst and pass it to next recursive call
        p1_ancestors = p1_lst + parents(p1_lst, family)
        p2_ancestors = p2_lst + parents(p2_lst, family)
        return cousin_recursion(p1, p2, family, p1_ancestors, p2_ancestors)

    
def cousin_degree(p1, p2, family):
    """
    Input: Two names (p1, p2) and a family tree database (family).
    Output: A number representing the minimum distance cousin relationship between p1 and p2, as follows:
            -   0 if p1 and p2 are siblings
            -   1 if p1 and p2 are cousins
            -   n if p1 and p2 are nth cousins, as defined at https://www.familysearch.org/blog/en/cousin-chart/
            -   -1 if p1 and p2 have no cousin or sibling relationship
    
    For example:
    >>> hobbits = read_family("hobbit-family.txt")
    >>> cousin_degree('Minto Burrows', 'Myrtle Burrows', hobbits)
    0
    >>> cousin_degree('Estella Bolger', 'Meriadoc Brandybuck', hobbits)
    3
    >>> cousin_degree('Frodo Baggins', 'Bilbo Baggins', hobbits)
    -1
    
    """
    degree = -1
    # If does not exist in family tree, return -1
    if person_index(p1, family) == None or person_index(p2, family) == None:
        return degree
    # If same parents, degree = 0
    elif are_siblings(p1, p2, family):
        degree = 0
    else:
        # List of parents for p1 and p2
        p1_parents = [father(p1, family)]
        p1_parents += [mother(p1, family)]
        p2_parents = [father(p2, family)]
        p2_parents += [mother(p2, family)]
        
        # List of grandparents for p1 and p2 respectively
        p1_grandparents = parents(p1_parents, family)
        p2_grandparents = parents(p2_parents, family)
        
        # List of common ancestors between p1 and p2
        common_anc = cousin_recursion(p1, p2, family, p1_grandparents, p2_grandparents)
        ancestor_line = ancestor_recursion(p1, common_anc[0], family, [], [], False)
        ancestor_line2 = ancestor_recursion(p2, common_anc[0], family, [], [], False)
        ancestor_line.remove(common_anc[0])
        ancestor_line2.remove(common_anc[0])
        generations_apart = 0
        generations_apart2 = 0
        
        # While loop to count the generations apart between p1 and a common ancestor
        i = 1
        has_found = False
        while i < len(ancestor_line) and not has_found:
            if ancestor_line[i] == common_anc[0]:
                generations_apart = i
                has_found = True
            i += 1
        
        # While loop to count the generations apart between p2 and a common ancestor
        j = 1
        has_found2 = False
        while j < len(ancestor_line2) and not has_found2:
            if ancestor_line[j] == common_anc[0]:
                generations_apart2 = j
                has_found2 = True
            j += 1
        
        # Compares generation apart of p1 and p2 to common ancestor. If different, returns -1
        if generations_apart == generations_apart2 and len(ancestor_line) > 0:
            degree = generations_apart - 1
        
    return degree
    

if __name__ == "__main__":
    import doctest
    doctest.testmod(verbose=True)