# Evan Greif, egreif1@gmail.com
# Finds paragraphs with specific term in txt document
# 8/20/2013
#

# returns a list of paragraphs from file with the term
def find_paras(file,term):
	# get the lines of a file as a list
	with open(file) as f:
		paragraphs = f.readlines()

	# for each paragraph, check if it contains the term
	list = []
	for p in paragraphs:
		if term in p.lower():
			list.append(p)

	return list

# main
if __name__ == "__main__":

	p = find_paras("test.txt","dog")	
	for line in p:
		print line
	


