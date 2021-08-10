package com.scullyapps.spoonsbottleup.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.adapters.SettingsPagerAdapter
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment.OnListFragmentInteractionListener
import com.scullyapps.spoonsbottleup.fragments.GeneralSettingsFragment
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import com.scullyapps.spoonsbottleup.ui.FridgeView
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), OnListFragmentInteractionListener {
    private var CURRENT_TAB = 0

    lateinit var toolbar: Toolbar
    lateinit var sectionsPagerAdapter : SettingsPagerAdapter

    lateinit var database: BottleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        database = BottleDatabase.getInstance(this)

        sectionsPagerAdapter = SettingsPagerAdapter(this, supportFragmentManager)
        toolbar = findViewById(R.id.toolbar_settings)

        view_pager.adapter = sectionsPagerAdapter

        tabs.setupWithViewPager(view_pager)

        setupListeners()

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.view_pager, GeneralSettingsFragment())
                .commit()
    }

    override fun onListFragmentInteraction(item: Bottle?) {
        Toast.makeText(this, "Item Pressed " + item?.name, Toast.LENGTH_SHORT).show()
    }

    override fun onListFragmentInteraction(item: FridgeView?, action: FridgeFragment.InteractionAction) {

        // do nothing if item null
        if(item == null)
            return

        when (action) {
            // Delete from database, update recycler
            FridgeFragment.InteractionAction.DELETE -> {

                val builder = AlertDialog.Builder(this).apply {
                    setTitle("Delete fridge")
                    setMessage("Are you sure you wish to delete fridge: " + item.fridge.name + "?")
                    setPositiveButton("Delete") {_, _ ->
                        database.fridgeRoomDao.delete(item.fridge)
                        getFridgeFragment()?.refreshRecyclerView(this@SettingsActivity)
                    }
                }.create().show()
            }

            // Open fridge edit activity
            FridgeFragment.InteractionAction.EDIT -> {
                val intent = Intent(this, FridgeManagementActivity::class.java).apply {
                    putExtra("name", item.fridge.name)
                }

                startActivity(intent)
            }
        }
    }

    private fun getFridgeFragment() : FridgeFragment? {
        val key = SettingsPagerAdapter.KEY_FRIDGE_FRAGMENT

        return if(sectionsPagerAdapter.hasFragment(key)) {
            sectionsPagerAdapter.getFragment(key) as FridgeFragment
        } else {
            null
        }
    }

    private fun addFridge() {
        AlertDialog.Builder(this).apply {
            val editText = EditText(this@SettingsActivity).apply {
                inputType = InputType.TYPE_CLASS_TEXT
            }

            setView(editText)
            setTitle("Add a new fridge")

            setPositiveButton("Add") { _, _ ->
                database.fridgeRoomDao.insert(Fridge(0, editText.text.toString(), 0))
                getFridgeFragment()?.refreshRecyclerView(this@SettingsActivity)
            }

        }.create().show()
    }

    private fun setupListeners() {
        // menu options
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_fridge -> addFridge()
            }
            false
        }

        tabs!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // update class-wide tab selection
                CURRENT_TAB = tab.position
                when (CURRENT_TAB) {
                    0 -> {}
                    1 -> toolbar.inflateMenu(R.menu.menu_settings_fridges)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // remove previously inflated menus
                toolbar.menu.clear()
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }



}