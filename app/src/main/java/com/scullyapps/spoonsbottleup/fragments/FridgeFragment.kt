package com.scullyapps.spoonsbottleup.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.scullyapps.spoonsbottleup.App
import com.scullyapps.spoonsbottleup.R
import com.scullyapps.spoonsbottleup.adapters.FridgeRecyclerViewAdapter
import com.scullyapps.spoonsbottleup.data.BottleDatabase
import com.scullyapps.spoonsbottleup.models.Bottle
import com.scullyapps.spoonsbottleup.ui.FridgeView
import java.util.*

class FridgeFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null

    private var recyclerView : RecyclerView? = null

    lateinit var database: BottleDatabase

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_settings_fridges, menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = BottleDatabase.getInstance(App.getContext())

        if (arguments != null) {
            mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fridge_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            recyclerView = view

            val fridgeViews = getAllFridgeViews(view.context)

            view.adapter = FridgeRecyclerViewAdapter(fridgeViews, mListener)
        }
        return view
    }

    fun getAllFridgeViews(context: Context) : List<FridgeView> {
        val fridges = database.fridgeRoomDao.getAll()
        val fridgeViews = ArrayList<FridgeView>()

        fridges.forEach {fridge ->
            fridgeViews.add(FridgeView(context, fridge))
        }

        return fridgeViews
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    fun refreshRecyclerView(context: Context) {
        if(recyclerView != null) {
            val adapter = recyclerView?.adapter as FridgeRecyclerViewAdapter
            adapter.setDataSet(getAllFridgeViews(context))
        }
    }

    override fun onDetach() {
        super.onDetach()
        recyclerView = null
        mListener = null
    }

    enum class InteractionAction {
        EDIT, DELETE
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: FridgeView?, action: InteractionAction)
        fun onListFragmentInteraction(item: Bottle?)
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
    }
}