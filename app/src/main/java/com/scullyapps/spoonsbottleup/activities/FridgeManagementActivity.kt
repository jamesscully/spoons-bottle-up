package com.scullyapps.spoonsbottleup.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.Fridge
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchCallback
import com.scullyapps.spoonsbottleup.ui.fridgeman.RecyclerListAdapter
import kotlinx.android.synthetic.main.activity_fridge_management.*
import kotlin.collections.ArrayList

class FridgeManagementActivity : AppCompatActivity() {

    var bottles: ArrayList<Bottle> = ArrayList()
    var toRemove: MutableList<Bottle> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge_management)

        val bundle = intent.extras
        val fridge: Fridge
        val toolbar = findViewById<Toolbar>(R.id.toolbar_fridgeman)

        if (bundle != null) {
            // retrieve our
            val fridgeName = bundle.getString("name", "NONE")
            fridge = Fridge(this, fridgeName)
            bottles = BottleDatabase.getBottlesByFridge(fridgeName)
            fridge.bottles = bottles

            toolbar.title = "Editing $fridgeName"
        }
        val adapter = RecyclerListAdapter(bottles)
        val callback: ItemTouchHelper.Callback = ItemTouchCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)

        fridgeman_recycler.setHasFixedSize(true)
        fridgeman_recycler.adapter = adapter
        fridgeman_recycler.layoutManager = LinearLayoutManager(this)

        itemTouchHelper.attachToRecyclerView(fridgeman_recycler)
        adapter.setIth(itemTouchHelper)
    }

    // since the adapter will map any changes to the order,
    // we can just write the current order of items back to the db
    private fun saveOrder() {
        val adapter = fridgeman_recycler.adapter as RecyclerListAdapter

        for(i in 0 until adapter.items.size) {
            val bottle = adapter.items[i]

            Log.d("SaveOrder", "Updating (${bottle.name}) ${bottle.id} to LO: $i")

            BottleDatabase.setBottleListOrder(i, bottle.id)
        }
    }

    override fun onBackPressed() {
        val adapter = fridgeman_recycler.adapter as RecyclerListAdapter

        // if no changes made, then we can safely exit
        if (!adapter.modified) {
            super.onBackPressed()
            return
        }

        val confirm = AlertDialog.Builder(this)
                .setMessage("Do you wish to save changes?")
                .setPositiveButton("Save changes") { dialog, _ ->
                    saveOrder()
                    super.onBackPressed()
                    dialog.cancel()
                }
                .setNegativeButton("Discard") { dialog, _ ->
                    toRemove.clear()
                    super.onBackPressed()
                    dialog.cancel()
                }
                .create()
        confirm.show()
    }
}