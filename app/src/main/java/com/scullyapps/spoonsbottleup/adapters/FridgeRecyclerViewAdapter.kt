package com.scullyapps.spoonsbottleup.adapters

import android.graphics.Typeface
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment.OnListFragmentInteractionListener
import com.scullyapps.spoonsbottleup.ui.FridgeView

class FridgeRecyclerViewAdapter(private var mValues: List<FridgeView>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<FridgeRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_fridge_list, parent, false)
        return ViewHolder(view)
    }

    public fun setDataSet(data : List<FridgeView>) {
        this.mValues = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val txtName = holder.txtName
        val txtAmt  = holder.txtAmt

        val bottlesAmount = mValues[position].fridge.bottles.size

        holder.fridge = mValues[position]

        txtName.text = mValues[position].fridge.name
        txtAmt.text = "Bottles: $bottlesAmount"

        if (holder.fridge!!.fridge.name == "Default") {
            txtName.setTypeface(null, Typeface.BOLD)
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtName.textSize + 12)
            holder.disableButtons()
        }

        holder.btnEdit.setOnClickListener { mListener?.onListFragmentInteraction(holder.fridge, FridgeFragment.InteractionAction.EDIT) }
        holder.btnDel.setOnClickListener { mListener?.onListFragmentInteraction(holder.fridge, FridgeFragment.InteractionAction.DELETE) }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txt_fridgevh_name)
        val txtAmt : TextView = view.findViewById(R.id.txt_fridgevh_amount)
        val btnEdit: Button = view.findViewById(R.id.btn_fridgevh_edit)
        val btnDel: Button = view.findViewById(R.id.btn_fridgevh_del)

        var fridge: FridgeView? = null

        fun disableButtons() {
            btnDel.visibility = View.INVISIBLE
            btnEdit.visibility = View.INVISIBLE
        }
    }
}