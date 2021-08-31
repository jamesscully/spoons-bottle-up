package com.scullyapps.spoonsbottleup.activities

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Space
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.allViews
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import com.scullyapps.spoonsbottleup.ui.CountBottleView
import com.scullyapps.spoonsbottleup.ui.FridgeHeaderView
import com.scullyapps.spoonsbottleup.ui.dialogs.DataWarningDialog
import kotlinx.android.synthetic.main.activity_count.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CountActivity : AppCompatActivity() {
    private var bottlingUp = false
    private var fridges: List<Fridge> = emptyList()

    private val TAG = "CountActivity"

    private var _keepScreenOn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_count)

        supportActionBar?.title = "Overview"

        setupSettings()

        val database = BottleDatabase.getInstance(this)

        fridges = database.fridgeRoomDao.getAll()

        fridges.forEach { fridge ->

            var bottles: List<Bottle> = database.bottleRoomDao.queryByFridge(fridge.name)

            if(bottles.isNotEmpty()) {
                // add header previous to CountBottleViews
                count_layout_main.addView(FridgeHeaderView(this, fridge.name))

                // load each bottles countview into the recycler
                bottles.forEach { bottle ->
                    count_layout_main.addView(
                            CountBottleView(this, bottle)
                    )
                }

            } else { Log.w(TAG, "X Bottles list was empty!") }
        }

        // this adds space at the bottom of the list
        val padding = Space(this).apply {
            visibility = View.VISIBLE

            // set minimum height so that space is consistent
            minimumHeight = 250
        }

        count_layout_main.addView(padding)

        // since all views should now be loaded - we can display accents (for legibility)
        accentizeCountViews()

        count_button_bottleup.setOnClickListener {

            forAllCountViews { view, _ ->
                if(!bottlingUp) {
                    if(view.getCount() <= 0 && !view.inverted) {
                        view.visibility = View.GONE
                    } else if (view.inverted) {
                        view.setCount(view.max - view.getCount())
                        view.invert(false)
                    }
                } else {
                    view.visibility = View.VISIBLE
                }

                view.setInputMode(bottlingUp)
            }

            if(bottlingUp) {
                count_button_bottleup.setText(R.string.bottle_up_button_text)
            } else {
                count_button_bottleup.text = "Go back"
            }

            // we're done here; invert flag
            bottlingUp = !bottlingUp

            // accentize again if we've entered/exited bottling up mode
            accentizeCountViews()
        }

        // we only want to show the button if our total selected (amongst views) is > 0
        CountBottleView.totalSelected.observe(this) { count ->
            showControls(count > 0)
        }
    }

    private fun accentizeCountViews() {
        forAllCountViews(affectVisibleOnly = true) { view, index ->
            if(index % 2 == 0) {
                view.setBackgroundResource(R.color.plaqueBackgroundAcc)
            } else {
                view.setBackgroundResource(R.color.plaqueBackground)
            }
        }
    }

    // Runs f() for each CountBottleView in the RecyclerView
    private fun forAllCountViews(affectVisibleOnly: Boolean = false, f: (CountBottleView, Int) -> (Unit)) {
        var index = 0
        for(view in count_layout_main.allViews) {
            if (view is CountBottleView) {

                // if we only want visible ones, then skip
                if(affectVisibleOnly && view.visibility != View.VISIBLE) {
                    continue
                }

                f(view, index)

                index++
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
            R.id.action_keep_screen_on -> {
                if(_keepScreenOn) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Toast.makeText(this, "Screen lock disabled", Toast.LENGTH_LONG).show()
                } else {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    Toast.makeText(this, "Screen lock enabled", Toast.LENGTH_LONG).show()
                }

                _keepScreenOn = !_keepScreenOn
            }

            R.id.action_toggle_max_lock -> {
                // propagate locked info to views, inverting our current value
                CountBottleView.lockMaxes = !CountBottleView.lockMaxes

                // update menu button icon
                if(CountBottleView.lockMaxes)
                    item.setIcon(R.drawable.ic_lock)
                else
                    item.setIcon(R.drawable.ic_unlock)

                // hide maxes on lock / unlock
                forAllCountViews { countBottleView, i ->
                    countBottleView.showMaxes(CountBottleView.lockMaxes)
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

    private fun showControls(show : Boolean) {
        if(show)
            count_button_bottleup.visibility = View.VISIBLE
        else
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
