# Evan Greif, egreif1@gmail.com
# recursively converts pdfs to txt in place
# 8/20/13
#

import string
import sys
import os
from subprocess import call


# checks if a line ends a paragraph
def ends_para(p):
	if (p == "\n"):
		return 1
	if (len(p) < 2):
		return 1
	if (len(p) < 60 and p[-2] == "."):
		return 1

	return 0

# class for individual company
class Company:

	# list of txt files that correspond to the company
	files = []	
	matches = []

	def __init__(self,directory):
		self.dir = directory
		self.recConvert(directory)	

	# recursively converts all pdfs to txt then adds to file list
	def recConvert(self,directory):
		for file in os.listdir(directory):
			
			if (os.path.isdir(directory+file)):
				if(not(file[0] == ".")):

					self.recConvert(directory+file+"/")

			elif (not(file[0] == ".")):
				# check that file is pdf then convert to txt
				if (file[-4:] == ".pdf"):
					self.files.append(directory+file[0:-4]+".txt")
					call(["pdftotext",directory+file])

	# find all paragraphs in files containing one of the terms
	def find_paras(self, file, terms):

		# open file hold paragraphs as list
		with open(file) as f:
			pars = f.readlines()

		# for each paragraph check if it contains any terms
		on_block = 0
		cur_par = []
		for p in pars:
			
			if (on_block):
				self.matches.append(string.strip(p))
				if (ends_para(p)):
					on_block = 0
					cur_par = []	
					self.matches.append("\n")
				continue
			
			cur_par.append(p)
			for term in terms:
				if term in p.lower():
					for l in cur_par:
						self.matches.append(string.strip(l))
					on_block = 1
					break	
				if (ends_para(p)):
					cur_par = []
				
		# list of paragraphs is now set for this file

	# go through all documents and find all the relevant paragraphs
	def find_all_paras(self,terms):
		# go through all relevant files
		for f in self.files:
			print(f)
			self.find_paras(f,terms)

if __name__ == "__main__":
	if (len(sys.argv) < 2):
		sys.exit("Not enough arguments.")

	terms = []
	terms.append("china")
	terms.append("asia")
	terms.append("japan")

	c = Company(sys.argv[1])
	
	c.find_all_paras(terms)
	m = c.matches

	# output to file
	fout = open("out.txt","w")
	for p in m:
		fout.write(p+"\n")




