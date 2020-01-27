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
    atan_values = []
    diff_values = []
    length = len(values)
    for index, i in enumerate(numpy.arange(-1000.14, 1000.14, 2.5)):
        points_num.append(i)
        atan_values.append(math.atan(i))
        diff_values.append(values[index] - atan_values[index])
        print("test", diff_values[index])
        atan_values[index] = round(atan_values[index], 5)

    plt.plot(points_num, values, color='blue', label="Notre atan")
    plt.plot(points_num, atan_values, color='orange', label="Le vrai atan")
#     plt.plot(points_num, diff_values, color='green', label="La différence")
    plt.legend(bbox_to_anchor=(0.03, 0.95), loc=2, borderaxespad=0.)
    plt.ylabel('y')
    plt.xlabel('x')
    plt.show()


if __name__ == '__main__':
   main()
