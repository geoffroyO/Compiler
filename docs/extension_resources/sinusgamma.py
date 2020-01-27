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


def J(n):
    x = symbols("x")
    tab = [(-1)**j*(x/2)**(2*j+n)/(factorial(j)*gamma(j+1+n)) for j in range(20)]
    return sum(tab)

def mon_poly_gamma_sin():
    x = symbols("x")
    base = [2*(-1)**n*J(2*n+1) for n in range(20)]
    return sum(base)

def mon_sin_gamma(x):
    return 4.62864763978159e-119*x**77 - 8.16123151846289e-115*x**75 + \
    1.09833853775474e-110*x**73 - 1.30781946141419e-106*x**71 + \
    1.42415458303408e-102*x**69 - 1.42898354402356e-98*x**67 + \
    1.32135226734692e-94*x**65 - 1.123451437064e-90*x**63 + \
    8.75511516272559e-87*x**61 - 6.23107349011173e-83*x**59 + \
    4.03388091880092e-79*x**57 - 2.36515267259952e-75*x**55 + \
    1.25006464928876e-71*x**53 - 5.92569378024983e-68*x**51 + \
    2.50554521103306e-64*x**49 - 9.39436902952749e-61*x**47 + \
    3.10392192793059e-57*x**45 - 8.97759910897905e-54*x**43 + \
    2.2576132028441e-50*x**41 - 4.90246975651354e-47*x**39 + \
    7.26546017915307e-44*x**37 - 9.67759295863189e-41*x**35 + \
    1.1516335620772e-37*x**33 - 1.21612504155352e-34*x**31 + \
    1.13099628864477e-31*x**29 - 9.18368986379555e-29*x**27 + \
    6.44695028438447e-26*x**25 - 3.86817017063068e-23*x**23 + \
    1.95729410633913e-20*x**21 - 8.22063524662433e-18*x**19 + \
    2.81145725434552e-15*x**17 - 7.64716373181982e-13*x**15 + \
    1.60590438368216e-10*x**13 - 2.50521083854417e-8*x**11 + \
    2.75573192239859e-6*x**9 - 0.000198412698412698*x**7 + \
    0.00833333333333333*x**5 - 0.166666666666667*x**3 + x


def se_positionner_dans_lintervalle_bis(x):
    a1 = [3.14, 0.0015, 0.000092, 0.00000065, 0.0000000035, 0.000000000089, 0.00000000000079, 0.0000000000000032, 0.000000000000000038, 0.00000000000000000046, 0.0000000000000000000026, 0.000000000000000000000043, 0.00000000000000000000000038]
    k = floor(x/(2*pi))
    res = x
    n = 2*k
    for i in range(len(a1)):
        res -= n*a1[i]
    if res > pi:
        res -= 2*pi
    if res < -pi:
        res += 2*pi
    return res


def erreur_relative_sin_gamma():
    t = linspace(-pi, pi, 5000)
    a = sin(t)
    b = mon_sin_gamma(t)
    c = abs((b-a)/a)
    ylim([-10e-14, 10e-14])
    plot(t,c,'r')
    show()


def etude_stat_sin_gamma():
    t = linspace(-pi/2, pi/2, 5000)
    bis = []
    for element in t:
        a = sin(element)
        b = mon_sin_gamma(element)
        c = ((b-a)/a)
        bis.append(abs(c))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)

def erreur_relative_ULP_etude_sin():
    t = linspace(-2**32, 2**32, 5001)
    ecart_relatif = []
    bis = []
    for element in t:
        x1 = se_positionner_dans_lintervalle_bis(element)
        a = sin(element)
        b = mon_sin_gamma(x1)
        bis.append(abs((b-a)/spacing(element)))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)