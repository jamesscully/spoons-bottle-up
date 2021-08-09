package com.scullyapps.spoonsbottleup.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Space
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.core.view.iterator
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.data.BottleRoomDatabase
import com.scullyapps.spoonsbottleup.models.BottleRoom
import com.scullyapps.spoonsbottleup.models.FridgeRoom
import com.scullyapps.spoonsbottleup.ui.CountBottleView
import com.scullyapps.spoonsbottleup.ui.FridgeHeaderView
import com.scullyapps.spoonsbottleup.ui.FridgeView
import com.scullyapps.spoonsbottleup.ui.dialogs.DataWarningDialog
import kotlinx.android.synthetic.main.activity_count.*
import kotlinx.coroutines.launch

class CountActivity : AppCompatActivity() {
    private var bottlingUp = false
    private var fridges: List<FridgeRoom> = emptyList()

    private val TAG = "CountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        supportActionBar?.title = "Overview"

        setupSettings()

        val database = BottleRoomDatabase.getInstance(this)
        val ctx = this;

        fridges = database.fridgeRoomDao.getAll()

        for(fridge in fridges) {
            var bottles: List<BottleRoom> = emptyList()

            bottles = database.bottleRoomDao.queryByFridge(fridge.name)

            if(bottles.isNotEmpty()) {
                count_layout_main.addView(FridgeHeaderView(ctx, fridge.name))

                bottles.forEach { bottle ->
                    count_layout_main.addView(CountBottleView(ctx, bottle))
                }

            } else {
                Log.w(TAG, "X Bottles list was empty!")
            }
        }




        // add default fridge to front
        // todo

//        // add all fridges with their bottles to the front (these are sorted prior)
//        for (f in fridges) {
////            count_layout_main.addView(f, 0)
//        }

        // this adds space at the bottom of the list
        val padding = Space(this).apply {
            visibility = View.INVISIBLE
        }

        count_layout_main.addView(padding)

        count_button_bottleup.setOnClickListener {

            for(view in count_layout_main.allViews) {
                if(view is CountBottleView) {
                    val countBottleView = view as CountBottleView

                    if(countBottleView.getCount() <= 0) {
                        countBottleView.visibility = View.GONE
                    } else {
                        countBottleView.visibility = View.VISIBLE
                    }
                }
            }

//            for (f in fridges)
//                f.bottleUp(!bottlingUp)

            if(bottlingUp) {
                count_button_bottleup.setText(R.string.bottle_up_button_text)
            } else {
                count_button_bottleup.text = "Go back"
            }

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

    override fun onDestroy() {
        // reset total selected when we leave activity
        CountBottleView.totalSelected.postValue(0)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_count_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save_list -> {

            }

            R.id.action_toggle_max_lock -> {
                // propagate locked info to views, inverting our current value
                CountBottleView.lockMaxes = !CountBottleView.lockMaxes

                // update menu button icon
                if(CountBottleView.lockMaxes)
                    item.setIcon(R.drawable.ic_lock)
                else
                    item.setIcon(R.drawable.ic_unlock)

                // hide maxes if we've unlocked
                fridges.forEach { fridgeView ->
//                    fridgeView.showMaxes(CountBottleView.lockMaxes)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSettings() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)

        val allowMaxes = preferences.getBoolean("setting_showMaxAmount", true)

        var lockOnMax = false
        var allowInvertCounting = false

        // since these depend on allowMaxes, we'll set the users prefs only if allowMaxes is true
        if(allowMaxes) {
            lockOnMax = preferences.getBoolean("setting_lockOnMaxAmount", true)
            allowInvertCounting = preferences.getBoolean("setting_allowInvertCounting", true)
        }

        Log.i("SharedPrefs", "Allowing max amounts? $allowMaxes")
        Log.i("SharedPrefs", "Allowing locking on max? $lockOnMax")
        Log.i("SharedPrefs", "Allowing invert counting? $allowInvertCounting")

        CountBottleView.allowInvert = allowInvertCounting
        CountBottleView.lockMaxes = lockOnMax
        CountBottleView.showMaxes = allowMaxes
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
