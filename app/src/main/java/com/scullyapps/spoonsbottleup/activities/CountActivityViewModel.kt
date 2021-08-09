package com.scullyapps.spoonsbottleup.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.BottleRoom
import java.util.*

class CountActivityViewModel : ViewModel() {
    lateinit var bottles : LiveData<List<BottleRoom>>

    var lockMaxes = MutableLiveData<Boolean>(true)
    var allowInvertCounting = MutableLiveData<Boolean>(true)

}