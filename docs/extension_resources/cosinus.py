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

def mon_poly_gamma():
    x = symbols("x")
    base = [2*(-1)**n*J(2*n) for n in range(1,20)]
    return J(0) + sum(base)

def mon_cos_gamma(x):
    return 5.36923126214664e-117*x**76 - 9.18138545827075e-113*x**74 + \
    1.19476512039168e-108*x**72 - 1.37236145747945e-104*x**70 + \
    1.43887270901671e-100*x**68 - 1.38769242112892e-96*x**66 + \
    1.23127942085417e-92*x**64 - 1.00279166468403e-88*x**62 + \
    7.47199933967046e-85*x**60 - 5.07455387910634e-81*x**58 + \
    3.12818379283804e-77*x**56 - 1.74245784661659e-73*x**54 + \
    8.72747376097333e-70*x**52 - 3.91004418776769e-66*x**50 + \
    1.55803098471521e-62*x**48 - 5.48821278213621e-59*x**46 + \
    1.69802377065023e-55*x**44 - 4.58338898467404e-52*x**42 + \
    1.07196093802408e-48*x**40 - 1.91196320504028e-45*x**38 + \
    2.68822026628664e-42*x**36 - 3.38715753552116e-39*x**34 + \
    3.80039075485474e-36*x**32 - 3.76998762881591e-33*x**30 + \
    3.27988923706984e-30*x**28 - 2.4795962632248e-27*x**26 + \
    1.61173757109612e-24*x**24 - 8.89679139245057e-22*x**22 + \
    4.11031762331216e-19*x**20 - 1.56192069685862e-16*x**18 + \
    4.77947733238739e-14*x**16 - 1.14707455977297e-11*x**14 + \
    2.08767569878681e-9*x**12 - 2.75573192239859e-7*x**10 + \
    2.48015873015873e-5*x**8 - 0.00138888888888889*x**6 + \
    0.0416666666666667*x**4 - 0.5*x**2 + 1.0
    
    
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

def = [3,1*0.1,4*0.01,15*0.0001,92*0.000001, 65*0.00000001, 35*0.0000000001,89*0.000000000001, 79*0.00000000000001,32*0.0000000000000001,38*0.000000000000000001,46*0.00000000000000000001,26*0.0000000000000000000001,43*0.000000000000000000000001,38*0.00000000000000000000000001]

def erreur_relative_cos_gamma():
    t = linspace(-pi, pi, 5000)
    a = cos(t)
    b = mon_cos_gamma(t)
    c = ((b-a)/a)
    ylim([-10e-15, 10e-15])
    plot(t,c,'r')
    show()

def comparason_cos():
    t = linspace(-2**30,-2**32,5000)
    ecart1 = []
    point1 = []
    i = 0
    for element in t :
        x1 = se_positionner_dans_lintervalle_bis(element)
        y1 = abs(cos(element)-mon_cos_gamma(x1))/spacing(element)
        ecart1.append(y1)
        point1.append(element)
        i += 1
    plot(point1,ecart1,'r')
    show()

def erreur_relative_ULP_etude_cos():
    t = linspace(-2**32, 2**32, 5001)
    ecart_relatif = []
    bis = []
    for element in t:
        x1 = se_positionner_dans_lintervalle_bis(element)
        a = cos(element)
        b = mon_cos_gamma(x1)
        bis.append(abs((b-a)/spacing(element)))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)

def erreur_relative_etude_cos():
    t = linspace(-pi/4, pi/4, 5001)
    ecart_relatif = []
    bis = []
    for element in t:
        a = cos(element)
        b = mon_cos_gamma(element)
        bis.append(abs((b-a)/a))
    return nanmax(bis), nanmin(bis), nanmean(bis), nanstd(bis), nanvar(bis)