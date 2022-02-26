import json

from termcolor import colored


class Product:

    json = json.loads("{}")

    displayName = ""
    eposName = ""
    description = ""
    productId = -1
    defaultPortionId = -1
    defaultPortionName = ""
    defaultCourseId = -1
    priceValue = 0.0

    calories = -1

    isDrink = False
    isFood = False
    isMisc = False

    def __init__(self, json) -> None:
        json = json

        self.displayName = json['displayName']
        self.eposName = json['eposName']
        self.description = json['description']
        self.productId = json['productId']
        self.calories = json['calories']
        self.defaultPortionId = json['defaultPortionId']
        try:
            self.defaultPortionName = json['defaultPortionName']
        except KeyError:
            self.defaultPortionName = "Unknown"

        self.defaultCourseId = json['defaultCourseId']
        self.priceValue = json['priceValue']

        if self.defaultPortionId != 15:
            # do stuff if we're a drink / bottle / can, not food

            self.isDrink = self.calories != 0
            self.isMisc = self.calories == 0

            pass
        else:
            # do stuff if we're food items

            self.isFood = True

            pass

    def __str__(self) -> str:
        return "{name} (id: {id}) | EPOS : {epos} | Portion: {portion}".format(name = self.displayName, id = self.productId, epos = self.eposName, portion = self.defaultPortionName)


        