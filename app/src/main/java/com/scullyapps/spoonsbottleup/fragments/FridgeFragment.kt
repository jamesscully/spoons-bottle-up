package com.scullyapps.spoonsbottleup.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.adapters.FridgeRecyclerViewAdapter
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.FridgeView
import java.util.*

class FridgeFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_settings_fridges, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fridge_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val context = view.getContext()
            val fridges = BottleDatabase.FridgeUtils.getAll()

            val fridgeViews = ArrayList<FridgeView>()

            fridges.forEach {fridge ->
                fridgeViews.add(fridge.toView(context))
            }

            // add the default fridge to the first/top of the list
            val defaultFridge = BottleDatabase.FridgeUtils.getDefault().toView(context)

            if (defaultFridge.size > 0) {
                fridgeViews.add(0, defaultFridge)
            }

            view.adapter = FridgeRecyclerViewAdapter(fridgeViews, mListener)
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: FridgeView?)
        fun onListFragmentInteraction(item: Bottle?)
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
    }
}