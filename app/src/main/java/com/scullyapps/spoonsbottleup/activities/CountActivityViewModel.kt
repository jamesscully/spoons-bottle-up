package com.scullyapps.spoonsbottleup.activities


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.scullyapps.spoonsbottleup.ui.FridgeView

class CountActivityViewModel : ViewModel() {
    private val TAG: String = "CountActivityViewModel"

    val fridges : MutableLiveData<List<FridgeView>> by lazy {
        MutableLiveData()
    }
}