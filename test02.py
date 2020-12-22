class Test:
    def prt(self):
        print(self)
        print(self.__class__)

def area(width, height):
    return width * height

def multiArgs(**var):
    print("==>" + str(var))

def check1(answer, wenwen):
    n = 0
    for i in range(len(answer)):
        if answer[i]!= wenwen[i]:
            n += 1
    return n

def check2(answer, wenwen):
    result = [1 for i in range(len(answer)) if answer[i]!= wenwen[i]]
    print(result)
    return len([1 for i in range(len(answer)) if answer[i]!= wenwen[i]])

def check3(answer, wenwen):
    return len([1 for x, y in zip(answer, wenwen) if x != y])


