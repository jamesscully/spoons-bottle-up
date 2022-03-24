package com.scullyapps.spoonsbottleup.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.adapters.BottleRecyclerAdapter
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.models.Fridge
import com.scullyapps.spoonsbottleup.ui.dialogs.EditBottleDialog
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchCallback
import kotlinx.android.synthetic.main.activity_fridge_management.*

class FridgeManagementActivity : AppCompatActivity() {

    var bottles: List<Bottle> = emptyList()
    var toRemove: MutableList<Bottle> = ArrayList()

    private val TAG = "FridgeManagementActivity"

    lateinit var database: BottleDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge_management)

        // initialize database
        database = BottleDatabase.getInstance(this)

        val bundle = intent.extras
        val fridge: Fridge
        val toolbar = findViewById<Toolbar>(R.id.toolbar_fridgeman)

        toolbar?.inflateMenu(R.menu.menu_settings_bottles)
        toolbar?.setOnMenuItemClickListener { menuItem ->

            when(menuItem.itemId) {
                R.id.action_add_bottle -> {
                    var bottleToAdd = Bottle(
                        0,
                        "New Bottle",
                        "",
                        "",
                        2,
                        -1,
                        "Cupboard",
                        0,
                        0,
                        isDrink = false,
                        isMisc = false,
                        isDraught = false,
                        isSpirit = false,
                        isCanOrBottle = true
                    )

                    val dialog = EditBottleDialog(this, bottleToAdd)
                    dialog.setPositiveButton("Create", null)
                    dialog.onSubmitted = { bottle ->
                        database.bottleRoomDao.insert(bottle)
                    }

                    dialog.show()
                }
            }
            false
        }



        if (bundle != null) {
            // retrieve our fridge
            val fridgeName = bundle.getString("name", "NONE")

            fridge = database.fridgeRoomDao.query(fridgeName)
            bottles = database.bottleRoomDao.queryBottlesByFridge(fridge.name)

            toolbar.title = "Editing $fridgeName"

            val adapter = BottleRecyclerAdapter(bottles as ArrayList<Bottle>, fridge.name)
            val callback: ItemTouchHelper.Callback = ItemTouchCallback(adapter)
            val itemTouchHelper = ItemTouchHelper(callback)

//            fridgeman_recycler.setHasFixedSize(true)
            fridgeman_recycler.adapter = adapter
            fridgeman_recycler.layoutManager = LinearLayoutManager(this)

            itemTouchHelper.attachToRecyclerView(fridgeman_recycler)
            adapter.setTouchHelper(itemTouchHelper)

            fridgeman_nodata_text.text = getString(R.string.fridge_management_no_data, fridge.name)

            if(bottles.isEmpty()) {
                fridgeman_nodata.visibility = View.VISIBLE
            } else {
                fridgeman_nodata.visibility = View.GONE
            }
        }


    }



    // since the adapter will map any changes to the order,
    // we can just write the current order of items back to the db
    private fun saveOrder() {
        val adapter = fridgeman_recycler.adapter as BottleRecyclerAdapter

        for(i in 0 until adapter.items.size) {
            val bottle = adapter.items[i]

            Log.d(TAG, "Updating (${bottle.name}) ${bottle.ID} to LO: $i")

            bottle.listOrder = i

            run {
                database.bottleRoomDao.update(bottle)
            }
        }
    }

    override fun onBackPressed() {
        val adapter = fridgeman_recycler.adapter as BottleRecyclerAdapter

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