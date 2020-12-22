import sys
import test02

argList = list()

print(test02.area(100,40))

for i in sys.argv:
    argList.append(i)

for index in range(len(argList)):
    print("=>", argList[index])

print(dir(test02))

if __name__ == '__main__':
   print('{0} 和 {item1}'.format('Google', item1='Twcode01'))
else:
   print('我來自另一模組')

with open("/Users/henry/Documents/Python/Test01/test03.py","w") as test03:

    test03.write("THis is a test\n2nd line")

test03 = open("/Users/henry/Documents/Python/Test01/test03.py","r")

content = test03.readlines()

print(content)

t1 = test02.Test()
t1.prt()

import time

localtime = time.asctime(time.localtime(time.time()))
print ("本地時間為 :", localtime)


