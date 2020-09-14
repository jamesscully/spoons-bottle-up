package com.scullyapps.spoonsbottleup.activities

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.data.BottleDatabase.getBottlesByFridge
import com.scullyapps.spoonsbottleup.data.BottleDatabase.updateListOrder
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.Fridge
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchCallback
import com.scullyapps.spoonsbottleup.ui.fridgeman.RecyclerListAdapter
import kotlinx.android.synthetic.main.activity_fridge_management.*
import java.util.*
import kotlin.collections.ArrayList

class FridgeManagementActivity : AppCompatActivity() {

    var bottles: ArrayList<Bottle> = ArrayList()
    var toRemove: MutableList<Bottle> = ArrayList()
    var fridgeName: String = "Default Name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge_management)

        val bundle = intent.extras
        val fridge: Fridge
        val toolbar = findViewById<Toolbar>(R.id.toolbar_fridgeman)

        if (bundle != null) {
            fridgeName = bundle.getString("name", "NONE")

            fridge = Fridge(this, fridgeName)

            bottles = getBottlesByFridge(bundle.getString("name"))
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

        for (i in bottles.indices) {
            val (id, name, type, step, max, fridgeName1, listOrder) = bottles[i]
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
                .setPositiveButton("Save changes") { dialog: DialogInterface, which: Int ->
                    val toChange = adapter.items
                    if (toChange == null) super.onBackPressed()
                    for (i in toChange.indices) {
                        val (id) = toChange[i]
                        updateListOrder(i, id)
                        updateListOrder(99, 5)
                    }
                    super.onBackPressed()
                    dialog.cancel()
                }
                .setNegativeButton("Discard") { dialog: DialogInterface, which: Int ->
                    toRemove.clear()
                    super.onBackPressed()
                    dialog.cancel()
                }
                .create()
        confirm.show()
    }
}