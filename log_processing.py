import sys


def nonblank_lines(f):
	for l in f:
		line = l.rstrip()
		if line:
			yield line


if __name__ == "__main__":
	TSs = []
	TJs = []
	with open(sys.argv[1]) as f:
		for line in nonblank_lines(f):
			temp = line.split(',')
			TSs.append(int(temp[0].split(':')[1]))
			TJs.append(int(temp[1].split(':')[1].strip("\n")))
	print("AVE TS : ", (sum(TSs)/len(TSs))/(10**6), 
    	  "AVE TJ : ", (sum(TJs)/len(TJs))/(10**6))
    	  
	f.close()
