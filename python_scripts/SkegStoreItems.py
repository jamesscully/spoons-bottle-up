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
		self.milliliters = self.getML()

	def print(self):
		print("{} - {}".format(self.name, self.minimumAge))


	def getML(self):
		ret = -1
		search = re.search('[0-9]*ml', self.description)
		
		if search: 
			ret = search.group(0)[:-2]

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

bottles_table_create = ''' 
CREATE TABLE "Bottles" (
	"ID"	INTEGER UNIQUE,
	"Name"	TEXT UNIQUE,
	"ListOrder"	INTEGER,
	"StepAmount"	INTEGER,
	"MaxAmount"	INTEGER,
	"FridgeID"	TEXT,
	"MinimumAge" INTEGER,
	"SizeML" INTEGER,
	PRIMARY KEY("ID")
) '''

fridges_table_create = '''
	CREATE TABLE "Fridges" (
	"Name"	TEXT NOT NULL UNIQUE,
	"ListOrder"	INTEGER UNIQUE,
	PRIMARY KEY("Name")
	)
'''


def createTable():
	try:
		cursor = conn.cursor()
		cursor.execute(bottles_table_create)
		cursor.execute(fridges_table_create)


		# create our fridges at pub in order
		fridges = ["Cupboard", "Cordial", "Front", "Tonic", "Alcohol", "Small"]
		i = 0

		# for each, populate name + index
		for name in fridges:
			cursor.execute("INSERT or IGNORE INTO Fridges (Name, ListOrder) VALUES (?,?)", (name, i))
			i += 1

		conn.commit()
		

	except sqlite3.OperationalError as e:
		print(e)




createTable()


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
		10000007990,
		10000000415,
		10000000412,
		10000001319,
		10000000413
	]

	if bottle.id in blacklist:
		print("[IGNORE] {} \n".format(bottle.name))
		return
	else:
		print("[   ADD] {} (size: {}), id: {}\n[  DESC] {}\n".format(bottle.name, bottle.getML(), bottle.id, bottle.description.strip("\n")))

	cursor = conn.cursor()
	cursor.execute("INSERT or IGNORE INTO Bottles (ID, Name, ListOrder, StepAmount, MaxAmount, MinimumAge, SizeML) VALUES (?,?,?,?,?,?,?)", (item.id, item.name, 0, 2, 32, item.minimumAge, item.getML()))
	conn.commit()
	cursor.close()


	
for item in alcohols:
	addBottleToDB(item)
	# item.print()

for item in soft_drinks:
	addBottleToDB(item)
	# item.print()



def presets():
	#######################
	### Presets		    ###
	#######################

	cursor = conn.cursor()

	### Alcohol

	cursor.execute("UPDATE Bottles SET FridgeID = ? WHERE MinimumAge >= ?", ("Alcohol", 18))



	### Tonics

	# For Britvic tonics / juices
	cursor.execute("UPDATE Bottles SET FridgeID = 'Tonic' WHERE Name LIKE '%britvic%'")


	# For Fentimans
	cursor.execute("UPDATE Bottles SET FridgeID = 'Tonic' WHERE Name LIKE '%fentimans%'")


	### Cordials
	cursor.execute("UPDATE Bottles SET FridgeID = 'Cordial' WHERE Name LIKE '%cordial%'")



	### Small fridge
	juices = ["10000005291", "10000005292", "10000007056"]

	for id in juices:
		cursor.execute("UPDATE Bottles SET FridgeID = 'Small' WHERE ID = " + id)



	### Front-fridge - most will be NULL by this point!
	cursor.execute("UPDATE Bottles SET FridgeID = 'Front' WHERE FridgeID IS NULL")

	# Product names /shouldnt/ contain the world alcohol unless low/free - kept in front
	cursor.execute("UPDATE Bottles SET FridgeID = 'Front' WHERE Name LIKE '%alcohol%'")

	# Hardy's wines stored in front fridge
	cursor.execute("UPDATE Bottles SET FridgeID = 'Front' WHERE Name LIKE '%hardy%'")


	# Monsters
	cursor.execute("UPDATE Bottles SET MaxAmount = 12 WHERE Name LIKE '%monster%'")
	cursor.execute("UPDATE Bottles SET StepAmount = 2 WHERE Name LIKE '%monster%'")


	front_wines = [10000000399, 10000017483]

	for id in front_wines:
		cursor.execute("UPDATE Bottles SET FridgeID = 'Front' WHERE ID = " + str(id))


	cupboard_wines = [10000139071, 10000086214, 10000014636]

	for id in cupboard_wines:
		cursor.execute("UPDATE Bottles SET FridgeID = 'Cupboard' WHERE ID = " + str(id))



	### Update max/step bottles based upon size of drink


	### Possible choices:
		# -1
		# 150
		# 187
		# 200
		# 250
		# 275
		# 330 - 6 max (dep on rows)
		# 355
		# 398 - Ignore 
		# 470
		# 500
		# 550
		# 568
		# 640
		# 660


	### Tonics Max / Step

	# Diet / Normal
	cursor.execute("UPDATE Bottles SET MaxAmount = 24 WHERE (ID = ?) OR (ID = ?)", (str(10000000431), str(10000000455)))

	# Flavours
	tonic_flavours = [10000000423, 10000000425, 10000006650, 10000009754, 10000061457]

	for id in tonic_flavours:
		cursor.execute("UPDATE Bottles SET MaxAmount = 6 WHERE ID = " + str(id))

	conn.commit()




presets()


