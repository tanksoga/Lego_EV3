numbers = []
for x in range(10):
    numbers.append(x * 3 + 5)
print(numbers)

from sys import getsizeof
num = [x for x in range(10)]
print(getsizeof(num))

num1 = (x for x in range(10))
print(getsizeof(num1))

for y in num1:
    print(y)

titles = "Python"
result = {index: letter for index, letter in enumerate(titles)}
print(result)

valeus = [*range(10)]

print(valeus)

combine = [*valeus,*"abc"]

combine1 = [valeus,"abc","!@#"]

combine2 = zip(combine1)

print(combine)
print(combine1)
print(list(combine2))

method1 = lambda x,y : x*y

print(method1(3,5))

(lambda x,y : print(str(x*y)))(4,6)

person = "this is henry chen"
(lambda p:print(p))(person)

result1 = filter(lambda p: int(p) < 5, valeus)

print(list(result1))

try:
    result2 = map(lambda p: int(p) * 2, ['1','2','3'])
    print(list(result2))
except Exception as e:
    print("==>" + str(e))
finally:
    print("keep running")

import test02

answer = ['A', 'B', 'B', 'E', 'D', 'C']
wenwen = ['B', 'B', 'B', 'E', 'A', 'C']

print(list(zip(answer,wenwen)))

print(test02.check1(answer,wenwen))

print(test02.check2(answer,wenwen))

print(test02.check3(answer,wenwen))

datas=[('芸芸', '天秤', '餅乾'),
('萱萱', '金牛', '巧克力'),
('琳琳', '雙魚', '草莓'),
('娜娜', '雙子', '蛋糕')]


B = [
  [1,2,3,0],
  [4,5,6],
  [7,8,9]
]

A = B[::-1]
C = [i[::-1] for i in B]
print(B)
print(A)
print(C)

print(list(zip(*A)))
print(list(zip(*C)))

first = "Henry"
last = "chen"
print(f"Test1 {first} {last} {30 + 10} ".title())


textContent = ["flower","flow","flight","fly","first"]

for x in zip(*textContent):
    print(x)


print('{0:b}'.format(int("11", 2) + int("1", 2)))
