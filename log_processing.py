import sys

if __name__ == "__main__":
    TSs = []
    TJs = []
    with open(sys.argv[1]) as f:
        lines = f.readlines()
        for line in lines:
            temp = line.split(',')
            TSs.append(int(temp[0].split(':')[1]))
            TJs.append(int(temp[1].split(':')[1].strip("\n")))
    print("AVE TS : ", (sum(TSs)/len(TSs))/(10**6), 
    	  "AVE TJ : ", (sum(TJs)/len(TJs))/(10**6))