def romanToInt(s):
        """
        :type s: str
        :rtype: int
        """
        
        romanMap = {'I':1,
                   'V':5,
                   'X':10,
                   'L':50,
                   'C':100,
                   'D':500,
                   'M':1000}
        result = 0
        
        for c in s:
            result = result + romanMap[c]
        
        
        return result

print(romanToInt("IV"))
