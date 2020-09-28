package com.scullyapps.spoonsbottleup.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Space
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.ui.Fridge
import com.scullyapps.spoonsbottleup.ui.Plaque
import kotlinx.android.synthetic.main.activity_count.*
import kotlin.collections.ArrayList

class CountActivity : AppCompatActivity() {
    private var bottlingUp = false
    private var fridges: ArrayList<Fridge> = ArrayList()

    override fun onDestroy() {
        Plaque.totalSelected = 0
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        val actionBar = supportActionBar
            actionBar?.hide()

        // get all fridges
        fridges = BottleDatabase.fridges

        // add default fridge to front
        val defaultFridge = BottleDatabase.FridgeUtils.getDefault()

        if (defaultFridge.size > 0)
            fridges.add(defaultFridge)

        for (f in fridges) {
            count_layout_main.addView(f, 0)
        }

        val padding = Space(this)
            padding.visibility = View.INVISIBLE

        count_layout_main.addView(padding)

        count_button_bottleup.setOnClickListener {
            for (f in fridges)
                f.bottleUp(!bottlingUp)

            // we're done here; invert flag
            bottlingUp = !bottlingUp
        }
    }

    override fun onBackPressed() {
        // show a dialog if modified list (losing them is not fun!)
        if(Plaque.totalSelected > 0) {
            val dialog = DataWarningDialog(this).apply {
                setPositiveButton("Exit") { d, _  ->
                    super.onBackPressed()
                }
            }
            dialog.create().show()
        } else {
            super.onBackPressed()
        }
    }

    class DataWarningDialog(context: Context) : AlertDialog.Builder(context) {
        init {
            setTitle("Modified list")
            setMessage("Are you sure you wish to discard this list?")
            setCancelable(false)
            setNegativeButton("Stay") { d, i ->
                d.cancel()
            }
        }
    }
}
