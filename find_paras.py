# Evan Greif, egreif1@gmail.com
# Finds paragraphs with specific term in txt document
# 8/20/2013
#

# returns a list of paragraphs from file with any of the terms in the list terms
def find_paras(file,terms):
	# get the lines of a file as a list
	with open(file) as f:
		paragraphs = f.readlines()

	# for each paragraph, check if it contains the term
	list = []
	for p in paragraphs:
		include = FALSE
		for term in terms:
			if term in p.lower():
				include = TRUE
			if include:
				list.append(p)
			break

	return list

# main
if __name__ == "__main__":

	p = find_paras("test.txt","dog")	
	for line in p:
		print line
	


