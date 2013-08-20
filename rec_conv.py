# Evan Greif, egreif1@gmail.com
# recursively converts pdfs to txt in place
# 8/20/13
#

import sys
import os

def rec_convert(directory):
	if(not(os.path.exists(directory) or os.path.isdir(directory))):
		return -1

	# get contents of the directory
	for file in os.listdir(directory):
		if (os.path.isdir(directory+file)):
			rec_convert(directory+file)
		elif (not(file[0] == ".")):
			# put the conversion here
			print(file)




	

if __name__ == "__main__":
	if (len(sys.argv) < 2):
		sys.exit("Not enough arguments.")

	rec_convert(sys.argv[1])
