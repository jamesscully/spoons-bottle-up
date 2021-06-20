package com.scullyapps.spoonsbottleup.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.ui.CountBottleView
import com.scullyapps.spoonsbottleup.ui.FridgeView
import com.scullyapps.spoonsbottleup.ui.dialogs.DataWarningDialog
import kotlinx.android.synthetic.main.activity_count.*
import kotlinx.android.synthetic.main.activity_setup.*

class CountActivity : AppCompatActivity() {
    private var bottlingUp = false
    private var fridges: ArrayList<FridgeView> = ArrayList()

    override fun onDestroy() {
        // reset total selected when we leave activity
        CountBottleView.totalSelected.postValue(0)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            menuInflater.inflate(R.menu.menu_count_activity, menu)
        }

        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        supportActionBar?.title = "Overview"

        // get all fridges as views
        fridges = BottleDatabase.fridges.map {fridge ->
            fridge.toView(this)
        } as ArrayList<FridgeView>

        // add default fridge to front
        val defaultFridge = BottleDatabase.FridgeUtils.getDefault()

        if (defaultFridge.bottles.isNotEmpty())
            fridges.add(defaultFridge.toView(this))

        // add all fridges with their bottles to the front (these are sorted prior)
        for (f in fridges) {
            count_layout_main.addView(f, 0)
        }

        // this adds space at the bottom of the list
        val padding = Space(this).apply {
            visibility = View.INVISIBLE
        }

        count_layout_main.addView(padding)

        count_button_bottleup.setOnClickListener {
            for (f in fridges)
                f.bottleUp(!bottlingUp)

            // we're done here; invert flag
            bottlingUp = !bottlingUp
        }

        CountBottleView.totalSelected.observe(this) { count ->
            if(count > 0) {
                showControls()
            } else {
                hideControls()
            }
        }
    }

    private fun showControls() {
        count_button_bottleup.visibility = View.VISIBLE
    }

    private fun hideControls() {
        count_button_bottleup.visibility = View.GONE
    }

    override fun onBackPressed() {
        // show a dialog if modified list (losing them is not fun!)
        if(CountBottleView.totalSelected.value!! > 0) {
            val dialog = DataWarningDialog(this, "Modified list", "Are you sure you wish to discard this list?") { _, _ ->
                super.onBackPressed()
            }
            dialog.create().show()
        } else {
            super.onBackPressed()
        }
    }
}
