import json
import re

from IDWhitelist import IDWhiteList
from termcolor import colored


class Product:

    json = json.loads("{}")

    displayName: str = ""
    eposName: str = ""
    description: str = ""
    productId:int  = -1
    defaultPortionId:int = -1
    defaultPortionName:str = ""
    defaultCourseId:int = -1
    priceValue:float = 0.0

    minimumAge:int = 0

    calories:int = -1

    isDrink: bool = False
    isFood: bool = False
    isMisc: bool = False
    isDraught: bool = False
    isCanOrBottle: bool = False
    isSpirit: bool = False

    def __init__(self, json, forceIsDrink: bool = False, forceIsDraught: bool = False, forceIsSpirit: bool = False) -> None:
        json = json

        self.displayName = json['displayName']
        self.eposName = json['eposName']
        self.description = json['description']
        self.productId = json['productId']
        self.calories = json['calories']
        self.defaultPortionId = json['defaultPortionId']
        self.minimumAge = json['minimumAge']

        try:
            self.defaultPortionName = json['defaultPortionName']
        except KeyError:
            self.defaultPortionName = "Unknown"

        self.defaultCourseId = json['defaultCourseId']
        self.priceValue = json['priceValue']

        if self.defaultCourseId == 15 or self.productId in IDWhiteList:
            # do stuff if we're a drink / bottle / can, not food
            self.isDrink = self.calories != 0
            self.isMisc = self.calories == 0
        elif self.defaultCourseId == 10 or self.defaultCourseId == 20:
            # do stuff if we're food items
            self.isFood = True
        else:
            # Unknown ID
            pass

        #
        # Find Draught products

        try:
            portion_name = json['portions'][0]['name'].lower()
            if portion_name == 'half pint' or portion_name == 'half':
                self.isDraught = True
        except IndexError:
            pass

        if forceIsDraught: 
            self.isDraught = True

        #
        # Find Spirits

        try:
            portion_name = self.defaultPortionName.lower()
            if portion_name == 'single' or portion_name == 'double' or portion_name == '25ml':
                self.isSpirit = True
        except IndexError:
            pass

        if forceIsSpirit:
            self.isSpirit = True

        #
        # Find Bottles/Cans

        regexMatchesCanOrBottle = False

        if self.defaultPortionName == "Can" or re.match('^.*(B|b)ottle', self.defaultPortionName) or re.match('^.*(G|g)lass', self.defaultPortionName):
            regexMatchesCanOrBottle = True

        self.isCanOrBottle = regexMatchesCanOrBottle or self.productId in IDWhiteList


    def isDeadObject(self):
        return not self.isCanOrBottle and not self.isDraught and not self.isDrink and not self.isFood and not self.isMisc

    def __str__(self) -> str:
        return "{name} (id: {id}) | EPOS : {epos} | Portion: {portion}".format(name = self.displayName, id = self.productId, epos = self.eposName, portion = self.defaultPortionName)


        