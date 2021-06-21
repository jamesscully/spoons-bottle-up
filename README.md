

# Bottling Up Assistant

[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](LICENSE) [![API](https://img.shields.io/badge/API-19%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=19)

### Summary
An Android application developed over the course of three years of seasonal employment at JD Wetherspoon, used to speed up the process of restocking needed products at the end of the night. This is primarily done by using a list of viable products, generated from an API reverse engineered from the Wetherspoons Order and Pay app. 

The Python script used to generate a list of viable products can be found [here](python_scripts/SkegStoreItems.py), although unrunnable as the API endpoint is not public; an example SQLite DB can be found in the [Android assets folder](app\src\main\assets), which will allow you to clone and build. 

### Features
- Simple 'tally counter' design
- Count each product by using difference between max and amount left
- Update fridge names, bottle information (max amount, steps, list order) via settings

### Installation
#### Google Play Store
You can find this application on the Google Play Store, which is used as a stable channel to share with colleagues.

- [Bottling Up Assistant (Store)](https://play.google.com/store/apps/details?id=com.scullyapps.spoonsbottleup)


#### Cloning

Cloning this repository may include experimental or non-functional changes; but is done as usual!

1. Clone the repository:
`$ git clone https://github.com/jamesscully/spoons-bottle-up`
2. Open in Android Studio
3. Build and run!
