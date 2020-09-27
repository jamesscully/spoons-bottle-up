package com.scullyapps.spoonsbottleup.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.adapters.SectionsPagerAdapter
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.fragments.BottleListFragment.OnFragmentInteractionListener
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment.OnListFragmentInteractionListener
import com.scullyapps.spoonsbottleup.fragments.GeneralSettingsFragment
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.Fridge
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity(), OnFragmentInteractionListener, OnListFragmentInteractionListener {
    private var CURRENT_TAB = 0
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
            toolbar = findViewById(R.id.toolbar_settings)

        view_pager.adapter = sectionsPagerAdapter

        tabs.setupWithViewPager(view_pager)

        setupListeners()
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.view_pager, GeneralSettingsFragment())
                .commit()
    }

    override fun onFragmentInteraction(uri: Uri) {}
    override fun onListFragmentInteraction(item: Bottle) {
        Toast.makeText(this, "Item Pressed " + item.name, Toast.LENGTH_SHORT).show()
    }

    override fun onListFragmentInteraction(item: Fridge) {
        val i = Intent(this, FridgeManagementActivity::class.java)
        i.putExtra("name", item.name)
        startActivity(i)
    }

    private fun addFridge() {
        val dialog = AlertDialog.Builder(this)

        val editText = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_TEXT
        }

        dialog.setView(editText)

        dialog.setTitle("Add a new fridge")

        dialog.setPositiveButton("Add") { _, _ ->
            BottleDatabase.createFridge(editText.text.toString())
        }

        dialog.create().show()
    }

    private fun setupListeners() {
        // menu options
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_add_bottle -> Toast.makeText(this, "Pressed adding bottle", Toast.LENGTH_LONG).show()
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
                    1 -> toolbar.inflateMenu(R.menu.menu_settings_bottles)
                    2 -> toolbar.inflateMenu(R.menu.menu_settings_fridges)
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