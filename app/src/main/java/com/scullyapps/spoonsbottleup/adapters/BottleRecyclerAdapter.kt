package com.scullyapps.spoonsbottleup.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.dialogs.EditBottleDialog
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchHelperAdapter
import com.scullyapps.spoonsbottleup.ui.fridgeman.ItemTouchHelperViewHolder
import java.util.*
import kotlin.collections.ArrayList

class BottleRecyclerAdapter(bottles: ArrayList<Bottle>, private val fridgeName : String) : RecyclerView.Adapter<BottleRecyclerAdapter.ViewHolder>(), ItemTouchHelperAdapter {
    val items: ArrayList<Bottle> = ArrayList()
    private var toRemove: MutableList<Bottle> = ArrayList()
    private var touchHelper: ItemTouchHelper? = null
    var modified = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.widget_bottle_management, parent, false)
        return ViewHolder(view)
    }


    init {
        items.addAll(bottles)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val bottle : Bottle = items[position]

        holder.textView.text = bottle.name
        holder.maxText.text = String.format("Max: %d", items[position].max)

        holder.drag.setOnTouchListener(OnTouchListener {_, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (touchHelper == null) return@OnTouchListener false
                touchHelper!!.startDrag(holder)
            }
            false
        })

        holder.edit.setOnClickListener {
            val dialog = EditBottleDialog(holder.itemView.context, bottle)
            dialog.create().show()

            // when the user has finished with the form, update our dataset
            dialog.onSubmitted = {b ->
                Log.d("BottleRecyclerAdapter: ", "Returned bottle has an ID: ${b.id} and LO of ${b.listOrder}")
                // remove if not relevant to this fridge
                if(fridgeName != b.fridgeName) {
                    Log.d("RecyclerListAdapter", "Moving fridge")
                    items.removeAt(position)
                    notifyItemRemoved(position)
                } else {
                    items[position] = b
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        modified = true
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    fun setTouchHelper(ith: ItemTouchHelper?) {
        this.touchHelper = ith
    }

    override fun onItemDismiss(position: Int) {
        modified = true
        toRemove.add(items[position])
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemTouchHelperViewHolder {
        val textView: TextView  = itemView.findViewById(R.id.btlm_text)
        val maxText : TextView  = itemView.findViewById(R.id.btlm_text_max)
        val drag    : ImageView = itemView.findViewById(R.id.btlm_dragview)
        val edit    : Button    = itemView.findViewById(R.id.btlm_edit)

        private var expanded = false

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(-0xf0f10)
        }
    }

}