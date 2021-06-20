import re

class Bottle:
	id = -1
	name = 'Default'
	minimumAge = 0

	image = ""

	description = ""

	isAlcohol = True

	milliliters = 0

	def __init__(self, parent : dict):
		self.id = parent['productId']
		self.name = parent['displayName']
		self.minimumAge = parent['minimumAge']

		self.image = parent['image']

		self.description = parent['description']

		self.isAlcohol = self.minimumAge >= 18
		self.milliliters = self.getML()

		self.eposName = parent['eposName']

	def print(self):
		print("{} - {}".format(self.name, self.minimumAge))


	def getML(self):
		ret = -1
		search = re.search('[0-9]*ml', self.description)
		
		if search: 
			ret = search.group(0)[:-2]

		return ret