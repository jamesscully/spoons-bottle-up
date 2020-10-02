

# Bottling Up Assistant

This has been a project for me over two years of seasonal employment at Wetherspoons. Its primary purpose is to speed up re-stocking of fridges, namely by removing the need to write down acronyms/names of products and amount needed on the back of a receipt with a pen, and guessing how much of it is needed. 

As of the latest version, counting is done by incrementing/decrementing by one or how many of the product is in a row, or by subtracting how many are left against a known maximum capacity (long-pressing an item).

Initial data is pulled from the Wetherspoon app API, and processed with Python into a SQLite database for use in the app; the script can be found [here](python_scripts/SkegStoreItems.py). I've hidden the API endpoints from the repo for now, since they aren't public and involved some reverse engineering to get.






<!-- Initially, each product name was hand-written into a database in-order then displayed in-app alongside an increment/decrement counter. 

The current version does this, but also allows for counting in steps (e.g. three-rows of a product is +/- 3) and counting by using the difference between how many are left in the fridge against the known max. 

It also pulls data from the Wetherspoon App API using Python into a SQLite database.
 -->


