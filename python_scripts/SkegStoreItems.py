import requests
import json

import sqlite3

import re

# retrieve hidden API endpoints
import API_URLs 

venueList = requests.get(url = API_URLs.VenueListURL)
menuList = requests.get(url = API_URLs.MenuListURL)

data = menuList.json()


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

	def print(self):
		print("{} - {}".format(self.name, self.minimumAge))


	def getML(self):
		ret = "XXXXXXXXXXXXXXXXXXXX"
		search = re.search('[0-9]*ml', self.description)
		
		if search: 
			ret = search.group(0)

		return ret


##########################


# Load all sub-categories (i.e. drinks, food)
menus = data['menus']

# Only categoies we want
whitelist_categories = ['Drinks', 'Tea, coffee and soft drinks']

drinks_category = dict
soft_drinks_category = dict

# Load all suitable categories submenus into usable_menus
for x in menus:

	name = x['name']

	if name == 'Drinks':
		print("Found alcoholic drinks")
		drinks_category = x

	if name == 'Tea, coffee and soft drinks':
		print("Found soft drinks")
		soft_drinks_category = x



alcohols = []

soft_drinks = []

# Code for dealing with 'Drinks' tab JSON
def processAlcohol():

	sub_menu = drinks_category['subMenu']

	whitelist_groups = [
		'Craft | Draught, cans and bottles',
		'Cider | Draught and bottles',
		'Wheat beer | Bottles',
		'World beer | Bottles',
		'Low & alcohol free',
		'Prosecco & sparkling',
		'Wine',
	]

	# subMenu
	# 	- 0 (category)
	#		- productGroups (groups)
	#			- groups (p groups)
	#				- products (actual data)
	#					- bottle!

	for category in sub_menu:
		if category['headerText'] in whitelist_groups:

			for group in category['productGroups']:

				# add each categories products into one
				for product in group['products']:
					alcohols.append(Bottle(product))


processAlcohol()


def processSoftDrink():
	sub_menu = soft_drinks_category['subMenu']

	for category in sub_menu:

		headerText = category['headerText']

		if headerText == "Children's drinks" or headerText == 'Soft drinks':
			for group in category['productGroups']:
				for product in group['products']:
					soft_drinks.append(Bottle(product))

processSoftDrink()


conn = sqlite3.connect('bottles.db')

def addBottleToDB(bottle):

	# convert this to hashmap
	blacklist = [
		10000078579, 
		10000078572, 
		10000080891, 
		10000078584, 
		10000078567, 
		10000129914,
		10000125487,
		10000063231,
		10000052367,
		10000063480,
		10000080630,
		10000000310,
		10000007990
	]

	if bottle.id in blacklist:
		print("[IGNORE] {} \n".format(bottle.name))
		return
	else:
		print("[   ADD] {} (size: {}), id: {}\n[  DESC] {}\n".format(bottle.name, bottle.getML(), bottle.id, bottle.description.strip("\n")))

	# cursor = conn.cursor()
	# cursor.execute("INSERT or IGNORE INTO Bottles (ID, Name) VALUES (?,?)", (item.id, item.name))
	# conn.commit()
	# cursor.close()
	





# for x in alcoholNames:
# 	conn.execute("INSERT or IGNORE INTO Bottles (ID, Name) VALUES (?,?)", (x[0],x[1]))


for item in alcohols:
	addBottleToDB(item)
	# item.print()

for item in soft_drinks:
	addBottleToDB(item)
	# item.print()










# alcohol = data['menus'][8]['subMenu']

# alcoholNames = [] 

# cursor = menus

# for sub in alcohol:
# 	menu = sub['productGroups']
# 	for grp in menu:
# 		prods = grp['products']
# 		for x in prods:
# 			if x['minimumAge'] > 0:

# 				tup = (x['productId'], x['displayName']);
# 				alcoholNames.append(tup)






# if len(alcoholNames) == 0:
# 	print("No bottles found, API error?")
# 	exit()

# print(len(alcoholNames))
# # remove duplicates 
# alcoholNames = list(dict.fromkeys(alcoholNames))


# print("Count: " + len(alcoholNames))

# for x in alcoholNames:
# 	print("ID / Alcohol: " + str(x[0]) + " / " + x[1])	


# conn = sqlite3.connect('bottles.db')


# for x in alcoholNames:
# 	conn.execute("INSERT or IGNORE INTO Bottles (ID, Name) VALUES (?,?)", (x[0],x[1]))

# conn.commit()
# conn.close()

# exit()

# for x in test:
# 	prod = x['products']
# 	for y in prod:
# 		print(y['displayName'])

# for x in btlcan:
# 	print(x['displayName'])
# 	print("\n")

