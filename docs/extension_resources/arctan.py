from sympy import symbols
from math import pi,sin
from math import factorial,gamma
from sympy import symbols
from numpy import diff, array,zeros,setdiff1d
from math import factorial,sqrt, cos
from math import inf,pi,ceil,sin, frexp, atan2
from sympy import sieve, symbols
from numpy import array, linalg, polynomial,angle
from statistics import mode,mean,pstdev
from scipy import integrate, interpolate
from matplotlib.pyplot import plot, show, xlim, ylim
from numpy import *
from scipy.special import gamma,poch
from scipy.stats import describe,binom
from decimal import *

def cordit_arctan(a):
    nos_arctan  =[0.7853981633974483, 0.4636476090008061, 0.24497866312686414, \
    0.12435499454676144, 0.06241880999595735, 0.031239833430268277, 0.015623728620476831, \
    0.007812341060101111, 0.0039062301319669718, 0.0019531225164788188, \
    0.0009765621895593195, 0.0004882812111948983, 0.00024414062014936177, \
    0.00012207031189367021, 6.103515617420877e-05, 3.0517578115526096e-05, \
    1.5258789061315762e-05, 7.62939453110197e-06, 3.814697265606496e-06, \
    1.907348632810187e-06, 9.536743164059608e-07, 4.7683715820308884e-07, \
    2.3841857910155797e-07, 1.1920928955078068e-07, 5.960464477539055e-08, \
    2.9802322387695303e-08, 1.4901161193847655e-08, 7.450580596923828e-09, \
    3.725290298461914e-09, 1.862645149230957e-09, 9.313225746154785e-10, \
    4.656612873077393e-10, 2.3283064365386963e-10, 1.1641532182693481e-10, \
    5.820766091346741e-11, 2.9103830456733704e-11, 1.4551915228366852e-11, \
    7.275957614183426e-12, 3.637978807091713e-12, 1.8189894035458565e-12]
    x, y, z = 1, a, 0
    t = 1;
    for i in range(len(nos_arctan)):
        x1 = 0
        if (y < 0):
            x1 = x - y*t
            y = y + x*t
            z = z - nos_arctan[i]
        else:
            x1 = x + y*t
            y = y - x*t
            z = z + nos_arctan[i]
        x = x1
        t /= 2
    return z
    
def erreur_relative_cordic_arctant():
    t = linspace(1, 1000, 5000)
    ecart = []
    points = []
    for element in t:
        a = arctan(element)
        b = cordit_arctan(element)
        c = ((b-a)/a)
        ecart.append(c)
        points.append(element)
    plot(points,ecart,'r')
    show()

def etude_stat_arctan():
    t = linspace(1, 1000, 5000)
    bis = []
    for element in t:
        a = arctan(element)
        b = cordit_arctan(element)
        c = ((b-a)/a)
        bis.append(abs(c))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)

def erreur_relative_ULP_etude_arctan():
    t = linspace(1, 2**32, 5001)
    ecart_relatif = []
    bis = []
    for element in t:
        a = arctan(element)
        b = cordit_arctan(element)
        bis.append(abs((b-a)/spacing(element)))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)