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
import com.scullyapps.spoonsbottleup.fragments.FridgeFragment.OnListFragmentInteractionListener
import com.scullyapps.spoonsbottleup.ui.Fridge

class FridgeRecyclerViewAdapter(private val mValues: List<Fridge>, private val mListener: OnListFragmentInteractionListener?) : RecyclerView.Adapter<FridgeRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.widget_fridge_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fridge = mValues[position]
        val txtName = holder.txtName
        txtName.text = mValues[position].name
        if (holder.fridge!!.name == "Default") {
            txtName.setTypeface(null, Typeface.BOLD)
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtName.textSize + 12)
            holder.disableButtons()
        }
        holder.mView.setOnClickListener { mListener?.onListFragmentInteraction(holder.fridge) }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val txtName: TextView
        val btnEdit: Button
        val btnDel: Button
        var fridge: Fridge? = null
        fun disableButtons() {
            btnDel.visibility = View.INVISIBLE
            btnEdit.visibility = View.INVISIBLE
        }

        init {
            txtName = mView.findViewById(R.id.txt_fridgevh_name)
            btnEdit = mView.findViewById(R.id.btn_fridgevh_edit)
            btnDel = mView.findViewById(R.id.btn_fridgevh_add)
        }
    }
}