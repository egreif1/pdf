# Evan Greif, egreif1@gmail.com
# recursively converts pdfs to txt in place
# 8/20/13
#

import sys
import os
from subprocess import call


# class for individual company
class Company:

	# list of txt files that correspond to the company
	files = []	

	def __init__(self,directory):
		self.dir = directory
		self.recConvert(directory)	


	def recConvert(self,directory):
		for file in os.listdir(directory):
			
			if (os.path.isdir(directory+file)):
				if(not(file[0] == ".")):
					print(directory+file)
					self.recConvert(directory+file+"/")

			elif (not(file[0] == ".")):
				# check that file is pdf then convert to txt
				if (file[-4:] == ".pdf"):
					print(directory+file)
					call(["pdftotext",directory+file])
					files.append(directory+file)				
		
		
		

	

if __name__ == "__main__":
	if (len(sys.argv) < 2):
		sys.exit("Not enough arguments.")

	c = Company(sys.argv[1])
