#!/usr/bin/env python3

import sys
import os
import numpy
import math

# libraries for the performance plot
import matplotlib.pyplot as plt
from timeit import timeit

def main():
   sinpath = sys.argv[1]
   ulppath = sys.argv[2]

   if not os.path.isfile(sinpath):
       print("File path {} does not exist. Exiting...".format(filepath))
       sys.exit()

   if not os.path.isfile(ulppath):
       print("File path {} does not exist. Exiting...".format(filepath))
       sys.exit()

   s = open(sinpath, "r")
   u = open(ulppath, "r")

   sin_values = s.readlines()
   ulp_values = u.readlines()

   sin_values = hex_to_decimal(sin_values)
   ulp_values = hex_to_decimal(ulp_values)

   # print(sin_values)
   # print(ulp_values)

   print_plot(sin_values, ulp_values)
#    for i in range(0, len(sin_values)):
#         str = sin_values[i]
#         sin_all = []
#         p = str.index("p")
#         # print('p=',p)
#         x = str.index("x")
#         sin_values[i] = str[x+1:p]
#         sin_all.append(str[x+1:])
#         print(sin_all)
#         res = int(sin_values[i][0])
#         # print('res=',res)
#         for index, element in enumerate(sin_values[i]):
#            if index != 0:
#                if (element != "."):
#                     try:
#                        # print(int(element))
#                        res += int(element)*16**-(index-1)
#                        # print(int(element)*16**-(index-1))
#                        # print('res=',res)
#                     except:
#                        # print(int(element,16))
#                        # print(index)
#                        res += int(element,16)*16**-(index-1)
#                        # print('res=',res)
#         new_p = sin_all[0].index('p')
#         # print(sin_all[0][new_p+1:-1])
#         b = int(sin_all[0][new_p+1:-1])
#         # print(b)
#         res *= 2**b
#         print(res)
#            # res += element
#         print("\n")
#        # print(sin_values[i])
#



def hex_to_decimal(liste):

   new_list = []

   for i in range(0, len(liste)):
        str = liste[i]
        list_all = []
        p = str.index("p")
        # print('p=',p)
        x = str.index("x")
        liste[i] = str[x+1:p]
        list_all.append(str[x+1:])
        # print(list_all)
        res = int(liste[i][0])
        # print('res=',res)
        for index, element in enumerate(liste[i]):
           if index != 0:
               if (element != "."):
                    try:
                       # print(int(element))
                       res += int(element)*16**-(index-1)
                       # print(int(element)*16**-(index-1))
                       # print('res=',res)
                    except:
                       # print(int(element,16))
                       # print(index)
                       res += int(element,16)*16**-(index-1)
                       # print('res=',res)
        new_p = list_all[0].index('p')
        # print(sin_all[0][new_p+1:-1])
        b = int(list_all[0][new_p+1:-1])
        # print(b)
        res *= 2**b
        if str[0] == '-':
            res = -res
        new_list.append(res)
        # print(res)
           # res += element
        # print("\n")
   return new_list


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



def print_plot(sin_values, ulp_values):
    """
    print plot
    """
    points_num = []
    sin_values_py = []
    diff_values = []
    length = len(sin_values)
    for index, i in enumerate(numpy.arange(-1000.14, 1000.14, 5.0)):
        points_num.append(i)
        sin_values_py.append(math.sin(i))
        diff_values.append((sin_values_py[index] - sin_values[index] )/ ulp_values[index])
#         print("test", diff_values[index])
        # sin_values[index] = round(sin_values[index], 5)

    # plt.plot(points_num, sin_values, color='blue', label="Notre sin")
    # plt.plot(points_num, sin_values_py, color='orange', label="Le vrai sin")
    plt.plot(points_num, diff_values, color='green', label="La différence")
    plt.legend(bbox_to_anchor=(0.03, 0.95), loc=2, borderaxespad=0.)
    plt.ylabel('y')
    plt.xlabel('x')
    # plt.xlim([-1000,-250])
    # plt.ylim([0,3])
    plt.show()


if __name__ == '__main__':
   main()
