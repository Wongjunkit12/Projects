from typing import Generator


def lcg(modulus: int, a: int, c: int, seed: int) -> Generator[int, None, None]:
    """Linear congruential generator."""
    while True:
        seed = (a * seed + c) % modulus
        yield seed


class RandomGen:

    def __init__(self, seed: int = 0) -> None:
        """
        RandomGem constructor
        
        """
        self.seed = seed
        self.Random_gen = lcg(pow(2, 32), 134775813, 1, self.seed)

    def randint(self, k: int) -> int:
        """
        Method to generate a random number from 1 to k(inclusive)
        :param k: An integer to represent the limit of the value that the random generator can generate
        :return: An integer that is in the range of 1 to k(inclusive)
        :Complexity : Best Case: O(1) since it always does constant loop
                      Worst Case: O(1) since it always does a constant loop
        """
        num = 0
        new_num = ''
        for _ in range(5):  # Generates only 5 numbers
            random_num = bin(next(self.Random_gen))  # Generates a random number and converts into binary
            if len(random_num) >= 18:                # If more than 16 bits, +2 because including the first 2 bits
                num += int(random_num[2:-16])        # [2:18] removes first 2 as it is string formatting
                                                     # and dropping the 16 least significant bits

        for i in str(num):  # Turns num into a string and runs through it
            new_num += str(int(int(i) >= 3))

        return int(new_num, 2) % k + 1

if __name__ == "__main__":
    r = RandomGen(seed=0)
    print(r.randint(100))  # 77
