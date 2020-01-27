#!/usr/bin/env python3

import sys
import os
import numpy
import math

# libraries for the performance plot
import matplotlib.pyplot as plt
from timeit import timeit

def main():
   filepath = sys.argv[1]

   if not os.path.isfile(filepath):
       print("File path {} does not exist. Exiting...".format(filepath))
       sys.exit()

   f = open(filepath, "r")

   values = f.readlines()

   for i in range(0, len(values)):
       values[i] = values[i].replace(',', '.')
       values[i] = float(values[i])
       print(values[i])

   print_plot(values)

def order_bag_of_words(bag_of_words, desc=False):
   words = [(word, cnt) for word, cnt in bag_of_words.items()]
   return sorted(words, key=lambda x: x[1], reverse=desc)

def record_word_cnt(words, bag_of_words):
   for word in words:
       if word != '':
           if word.lower() in bag_of_words:
               bag_of_words[word.lower()] += 1
           else:
               bag_of_words[word.lower()] = 1



def print_plot(values):
    """
    print plot
    """
    points_num = []
    cos_values = []
    diff_values = []
    length = len(values)
    for index, i in enumerate(numpy.arange(-3.14, 3.14, 0.025)):
        points_num.append(i)
        cos_values.append(math.cos(i))
        diff_values.append(values[index] - cos_values[index])
        print("test", diff_values[index])
        cos_values[index] = round(cos_values[index], 5)

    plt.plot(points_num, values, color='blue', label="Notre cos")
    plt.plot(points_num, cos_values, color='orange', label="Le vrai cos")
#     plt.plot(points_num, diff_values, color='green', label="La différence")
    plt.legend(bbox_to_anchor=(0.03, 0.95), loc=2, borderaxespad=0.)
    plt.ylabel('y')
    plt.xlabel('x')
    plt.show()


if __name__ == '__main__':
   main()
