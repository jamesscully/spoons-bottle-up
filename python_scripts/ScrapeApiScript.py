import requests
import json
import sqlite3
import re
import os

from KnownBottleMaxesDict import KnownBottleMaxes
from Bottle import Bottle
from IDWhitelist import IDWhiteList
from IDBlacklist import IDBlackList

# retrieve hidden API endpoints
import API_URLs 

venueList = requests.get(url = API_URLs.VenueListURL)
menuList = requests.get(url = API_URLs.MenuListURL)

data = menuList.json()

##########################


if os.path.exists("bottles.db"):
	os.remove("bottles.db")
else:
	print("No pre-generated database found.")


# Load all sub-categories (i.e. drinks, food)
menus = data['menus']

# Only categoies we want
whitelist_categories = ['Drinks', 'Tea, coffee and soft drinks']

drinks_category = dict

# Load all suitable categories submenus into usable_menus
for x in menus:

	name = x['name']

	if name == 'Drinks':
		print("Found alcoholic drinks")
		drinks_category = x

alcohols = []
soft_drinks = []

allEposNames = dict

def findAllCansAndBottles():

	# subMenu
	# 	- 0 (category)
	#		- productGroups (groups)
	#			- groups (p groups)
	#				- products (actual data)
	#					- bottle!
	
	sub_menu = drinks_category['subMenu']

	for category in sub_menu:
		for group in category['productGroups']:
			for product in group['products']:
				try:
					eposName = product['eposName']
					# print("Product " + product['displayName'] + " has an eposName of " + eposName)

				except KeyError:
					# print("Product has no ePos name")
					pass

				portion = ""

				try:
					# we can use the api's portion size, which will report Can/Bottle or xxxML Bottle; 
					# thus giving us only stuff that can be in a fridge
					portion = product['defaultPortionName']

					# some products dont have a portion size, avoid breaking everything!
				except KeyError:
					print("- No portion size for: " + product['displayName'])
					
				
				# if we have match for can or bottle
				if portion == "Can" or re.match('^.*(B|b)ottle', portion) or re.match('^.*(G|g)lass', portion) or product['productId'] in IDWhiteList:
					print("+ Found product: " + product['displayName'] + ", can/bottle: " + portion)

					# add to appropriate array
					if product['minimumAge'] >= 18:
						alcohols.append(Bottle(product))
					else:
						soft_drinks.append(Bottle(product))
					
				
				
						
findAllCansAndBottles()

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

epos_table_create = '''
	CREATE TABLE "EposNames" (
	"ID"	INTEGER UNIQUE,
	"EposName"	TEXT NOT NULL UNIQUE,
	"Name"	TEXT NOT NULL,
	PRIMARY KEY("ID")
	)
'''


def createTable():
	try:
		cursor = conn.cursor()

		# create all of our tables
		cursor.execute(bottles_table_create)
		cursor.execute(fridges_table_create)
		cursor.execute(epos_table_create)

		conn.row_factory = sqlite3.Row


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

	if bottle.id in IDBlackList:
		print("[IGNORE] {}, id: {}".format(bottle.name, bottle.id))
		return
		# print("[   ADD] {} (size: {}), id: {}\n[  DESC] {}\n".format(bottle.name, bottle.getML(), bottle.id, bottle.description.strip("\n")))

	cursor = conn.cursor()
	cursor.execute("INSERT or IGNORE INTO Bottles (ID, Name, ListOrder, StepAmount, MaxAmount, MinimumAge, SizeML) VALUES (?,?,?,?,?,?,?)", (item.id, item.name, 0, 2, -1, item.minimumAge, item.getML()))
	cursor.execute("INSERT or IGNORE INTO EposNames (ID, EposName, Name) VALUES (?,?,?)", (item.id, item.eposName, item.name))
	conn.commit()
	cursor.close()
	
for item in alcohols:
	addBottleToDB(item)

for item in soft_drinks:
	addBottleToDB(item)

def presets():
	#######################
	### Presets		    ###
	#######################

	cursor = conn.cursor()

	### Move all Alcohol / minimum age of 18 bottles to Alcohol fridge
	cursor.execute("UPDATE Bottles SET FridgeID = ? WHERE MinimumAge >= ?", ("Alcohol", 18))

	### Assign Tonic FridgeID to neccessary bottles 
	cursor.execute("UPDATE Bottles SET FridgeID = 'Tonic' WHERE Name LIKE '%britvic%'")   # For Britvic tonics / juices
	cursor.execute("UPDATE Bottles SET FridgeID = 'Tonic' WHERE Name LIKE '%fentimans%'") # For Fentimans

	### Assign Cordial FridgeID to cordials
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

	# wines which are not kept in fridge
	for id in cupboard_wines:
		cursor.execute("UPDATE Bottles SET FridgeID = 'Cupboard' WHERE ID = " + str(id))

	conn.commit()



	for key in KnownBottleMaxes:
		value = KnownBottleMaxes[key]

		rows = cursor.execute("UPDATE Bottles SET MaxAmount = {} WHERE Name LIKE '%{}%'".format(str(value), key))

		show_logs = True

		if(rows.rowcount > 0 and show_logs):
			print("[PASS {}] Set {} max to {}".format(rows.rowcount, key, str(value)))
		else:
			print("[FAIL #] Set {} max to {}".format(key, str(value)))


	### Tonics Max / Step

	# Diet / Normal
	cursor.execute("UPDATE Bottles SET MaxAmount = 24 WHERE (ID = ?) OR (ID = ?)", (str(10000000431), str(10000000455)))

	# Flavours
	tonic_flavours = [10000000423, 10000000425, 10000006650, 10000009754, 10000061457]

	for id in tonic_flavours:
		cursor.execute("UPDATE Bottles SET MaxAmount = 6 WHERE ID = " + str(id))

		conn.commit()
	

presets()

