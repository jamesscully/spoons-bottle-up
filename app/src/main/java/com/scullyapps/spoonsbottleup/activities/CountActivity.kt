package com.scullyapps.spoonsbottleup.activities

import android.os.Bundle
import android.view.View
import android.widget.Space
import androidx.appcompat.app.AppCompatActivity
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.ui.Fridge
import kotlinx.android.synthetic.main.activity_count.*
import kotlin.collections.ArrayList

class CountActivity : AppCompatActivity() {
    private var bottlingUp = false
    private var fridges: ArrayList<Fridge> = ArrayList()
    private lateinit var db : BottleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        db = BottleDatabase(this, null, null, 1)

        val actionBar = supportActionBar
            actionBar?.hide()

        // get all fridges
        fridges = db.fridges

        // add default fridge to front
        val defaultFridge = db.getDefaultFridge(this)

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
}
