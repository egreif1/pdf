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
	# matching paragraphs
	matches = []
	# matching lines
	lines = []
	# name of company
	name = ""
	# directory for company
	directory = ""

	def __init__(self,directory):
		self.files = []
		self.matches = []
		self.lines = []
		self.directory = directory
		words = string.split(directory,"/")
		self.name = words[-1]
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
					self.files.append(directory+"/"+file[0:-4]+".txt")
					call(["pdftotext",directory+"/"+file])
		for f in self.files:
			print(f)

	# find all lines in files containing one or more terms
	def find_lines(self,file,terms):
		with open(file) as f:
			lines = f.readlines()

		for l in lines:
			for term in terms:
				if term in l.lower():
					self.lines.append(l)
					break
				
	# find all paragraphs in files containing one of the terms
	def find_paras(self, file, terms):

		# declare what file we are in
		parts = string.split(file,"/")
		filename = parts[-1]
		self.matches.append(filename + ":\n\n")

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
					self.matches.append("\n\n\n")
				continue
			
			cur_par.append(p)
			for term in terms:
				if term in p.lower():
					for l in cur_par:
						self.matches.append(string.strip(l))
					on_block = 1
					cur_par = []
					break	
			if (ends_para(p)):
				cur_par = []
				
		# list of paragraphs is now set for this file


	# go through all documents and find all the relevant paragraphs or lines
	def find_all(self,terms,paras):

		# go through all relevant files
		for f in self.files:
			if(paras):
				self.find_paras(f,terms)
			else:
				self.find_lines(f,terms)



# writes the paragraph report to file
def write_para_report(file,comps,terms):
	f = open(file,"w")
	
	# write out a terms header
	f.write("PARAGRAPHS MATCHING ")
	for t in terms:
		f.write(t+" ")
	f.write("\n\n")

	# now write paragraphs for each company
	for c in comps:
		f.write("COMPANY: " + c.name)
		f.write("\n\n")
		matches = c.matches
		for m in matches:
			f.write(m)
		
	# now highlight the terms in the output file
	for t in terms:
		call(["sed","-i","s/"+t+"/***"+t+"***/g",file])
		call(["sed","-i","s/"+t.title()+"/***"+t.title()+"***/g",file])
	


if __name__ == "__main__":
	if (len(sys.argv) < 4):
		sys.exit("Not enough arguments.")

	# list of companies
	comps = []

	# directory holding the company directories
	parent_dir = sys.argv[1]
	# output file	
	fout = sys.argv[2]
	
	# terms are the trailing arguments
	terms = []
	for t in sys.argv[3:]:
		terms.append(t.lower())

	# make a company object for each directory
	for file in os.listdir(parent_dir):
		if os.path.isdir(parent_dir+file):
			c = Company(parent_dir+file)
			comps.append(c)

	# find all paragraphs for each company
	for c in comps:
		c.find_all(terms,1)

	# now write out the report
	write_para_report(fout,comps,terms)


