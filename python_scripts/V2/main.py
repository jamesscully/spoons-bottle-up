from sqlite3.dbapi2 import Error
import requests
import json
import sqlite3
import re
import os
import subprocess

from product import Product 
from KnownBottleMaxesDict import KnownBottleMaxes
from IDBlacklist import IDBlackList
from IDWhitelist import IDWhiteList

import tables

# retrieve hidden API endpoints
import API_URLs 

venueList = requests.get(url = API_URLs.VenueListURL)
menuList = requests.get(url = API_URLs.MenuListURL)

data = menuList.json()

file_log_found = "logs/found.txt"
file_log_ignored = "logs/ignore.txt"

##########################


try:
	if os.path.exists("bottles.db"):
		os.remove("bottles.db")
	else:
		print("No pre-generated database found.")
except PermissionError as e:
	print("Could not remove existing database, ignoring")


# Load all sub-categories (i.e. drinks, food)
menus = data['menus']

products = [Product]

def loopAllProducts():

	print("\n**********\n" * 5)

	found_file = open(file_log_found, 'w')
	ignore_file = open(file_log_ignored, 'w')

	print("f: Food | d: Drink | m: Misc | x: Draught | c: Can/Bottle | r: 18+ | s: Spirits")
	choice = input()

	for category in menus:
		for subMenu in category['subMenu']:

			_products = [Product]

			for group in subMenu['productGroups']:
				for product in group['products']:
					spiritHeaders = ["Vodka", "Rum", "Gin", "Whisky", "Liqueur and brandy"]
					isSpirit: bool = subMenu['headerText'] in spiritHeaders

					if subMenu['headerText'] != 'Cocktails':
						submit = Product(product, forceIsSpirit = isSpirit)
						products.append(submit)
						_products.append(submit)
					else:
						print("Ignoring", Product(product))

			# main
						
			filtered = [Product]

			l = lambda x: x.isDrink

			if choice == 'f':
				l = lambda x: x.isFood
			elif choice == 'd':
				l = lambda x: x.isDrink
			elif choice == 'm':
				l = lambda x: x.isMisc
			elif choice == 'x':
				l = lambda x: x.isDraught
			elif choice == 'c':
				l = lambda x: x.isCanOrBottle
			elif choice == 'r':
				l = lambda x: x.minimumAge >= 18
			elif choice == 's':
				l = lambda x: x.isSpirit
			elif choice == 'b':
				l = lambda x: x.productId in IDBlackList
			else:
				loopAllProducts()

			filtered = list(filter(l, _products))

			if len(filtered) > 0 and choice != 'b': 
				print("\n--------------")
				print(subMenu['headerText'])
				print("--------------")

			for p in filtered:
				if choice == 'b':
					print(p.productId, "# ", p.displayName)
				else:
					print(p)
					
						
	found_file.close()
	ignore_file.close()	

	if choice == 'b': 
		exit(0)
										
loopAllProducts()

conn = sqlite3.connect('bottles.db')

def createTable():
	try:
		cursor = conn.cursor()

		# create all of our tables
		cursor.execute(tables.BOTTLE_TABLE_CREATE)
		cursor.execute(tables.FRIDGES_TABLE_CREATE)
		cursor.execute(tables.EPOS_TABLE_CREATE)

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


def addBottleToDB(product: Product):

	if product.productId in IDBlackList:
		print("[IGNORE] {}, id: {}".format(product.displayName, product.productId))
		return

	if product.isFood:
		return

	cursor = conn.cursor()
	cursor.execute("INSERT or IGNORE INTO Product (ID, Name, Description, EposName, ListOrder, StepAmount, MaxAmount, MinimumAge, FridgeID, IsDrink, IsMisc, IsDraught, IsSpirit, IsCanOrBottle) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
		(product.productId, 
		product.displayName, 
		product.description, 
		product.eposName, 
		0,
		2, 
		-1,
		product.minimumAge, 
		"", 
		product.isDrink, 
		product.isMisc, 
		product.isDraught, 
		product.isSpirit,
		product.isCanOrBottle)
	)
	cursor.execute("INSERT or IGNORE INTO EposNames (ID, EposName, Name) VALUES (?,?,?)", (product.productId, product.eposName, product.displayName))
	conn.commit()
	cursor.close()

print("Found {} products".format(len(products)))
	
for product in products:
	addBottleToDB(product)

def presets():
	#######################
	### Presets		    ###
	#######################

	cursor = conn.cursor()

	### Move all Alcohol / minimum age of 18 bottles to Alcohol fridge
	cursor.execute("UPDATE Product SET FridgeID = ? WHERE MinimumAge >= ?", ("Alcohol", 18))

	### Assign Tonic FridgeID to neccessary bottles 
	cursor.execute("UPDATE Product SET FridgeID = 'Tonic' WHERE Name LIKE '%britvic%'")   # For Britvic tonics / juices

	### Assign Cordial FridgeID to cordials
	cursor.execute("UPDATE Product SET FridgeID = 'Cordial' WHERE Name LIKE '%cordial%'")

	### Small fridge
	juices = ["10000005291", "10000005292", "10000007056"]

	for id in juices:
		cursor.execute("UPDATE Product SET FridgeID = 'Small' WHERE ID = " + id)

	### Front-fridge - most will be NULL by this point!
	cursor.execute("UPDATE Product SET FridgeID = 'Front' WHERE FridgeID IS NULL")

	# Product names /shouldnt/ contain the world alcohol unless low/free - kept in front
	cursor.execute("UPDATE Product SET FridgeID = 'Front' WHERE Name LIKE '%alcohol%'")

	# Hardy's wines stored in front fridge
	cursor.execute("UPDATE Product SET FridgeID = 'Front' WHERE Name LIKE '%hardy%'")

	# Monsters
	cursor.execute("UPDATE Product SET MaxAmount = 12 WHERE Name LIKE '%monster%'")
	cursor.execute("UPDATE Product SET StepAmount = 2 WHERE Name LIKE '%monster%'")


	front_wines = [10000000399, 10000017483]

	for id in front_wines:
		cursor.execute("UPDATE Product SET FridgeID = 'Front' WHERE ID = " + str(id))


	cupboard_wines = [10000139071, 10000086214, 10000014636]

	# wines which are not kept in fridge
	for id in cupboard_wines:
		cursor.execute("UPDATE Product SET FridgeID = 'Cupboard' WHERE ID = " + str(id))

	conn.commit()



	for key in KnownBottleMaxes:
		value = KnownBottleMaxes[key]

		rows = cursor.execute("UPDATE Product SET MaxAmount = {} WHERE Name LIKE '%{}%'".format(str(value), key))

		show_logs = True

		if(rows.rowcount > 0 and show_logs):
			print("[PASS {}] Set {} max to {}".format(rows.rowcount, key, str(value)))
		else:
			print("[FAIL #] Set {} max to {}".format(key, str(value)))


	### Tonics Max / Step

	# Diet / Normal
	cursor.execute("UPDATE Product SET MaxAmount = 24 WHERE (ID = ?) OR (ID = ?)", (str(10000000431), str(10000000455)))

	# Flavours
	tonic_flavours = [10000000423, 10000000425, 10000006650, 10000009754, 10000061457]

	for id in tonic_flavours:
		cursor.execute("UPDATE Product SET MaxAmount = 6 WHERE ID = " + str(id))
		conn.commit()


	# Set max amounts for unspecified; rough estimate!
	# cursor.execute("UPDATE Product SET MaxAmount = 6 WHERE (SizeML <= 330) AND MaxAmount = -1")
	# cursor.execute("UPDATE Product SET MaxAmount = 5 WHERE (SizeML >= 500) AND MaxAmount = -1")

	# Use stable-db to import maxes from up-to-date database
	cursor.execute("ATTACH DATABASE 'stabledb/Bottles.db' AS BottlesProd")

	# Copy over listOrder and maxes if ID's match
	cursor.execute('''	
						UPDATE main.Product
						SET (ListOrder, MaxAmount) = (BottlesProd.Bottles.ListOrder, BottlesProd.Bottles.MaxAmount)
						FROM BottlesProd.Bottles
						WHERE BottlesProd.Bottles.ID = main.Product.ID;
				''')
	conn.commit()	

	conn.close()

presets()
