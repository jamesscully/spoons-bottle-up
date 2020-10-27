package com.scullyapps.spoonsbottleup.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scullyapps.spoonsbottleup.R;
import com.scullyapps.spoonsbottleup.adapters.FridgeRecyclerViewAdapter;
import com.scullyapps.spoonsbottleup.data.BottleDatabase;
import com.scullyapps.spoonsbottleup.models.Bottle;
import com.scullyapps.spoonsbottleup.models.Fridge;
import com.scullyapps.spoonsbottleup.ui.FridgeView;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FridgeFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_settings_fridges, menu);
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FridgeFragment() {

    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FridgeFragment newInstance(int columnCount) {
        FridgeFragment fragment = new FridgeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fridge_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            ArrayList<Fridge> fridges = BottleDatabase.INSTANCE.getFridges();

            ArrayList<FridgeView> fridgeViews = new ArrayList<>();

            for(Fridge f : fridges) {
                fridgeViews.add(
                        f.toView(context)
                );
            }

            // add the default fridge to the first/top of the list
            FridgeView defaultFridge = new FridgeView(view.getContext(), BottleDatabase.FridgeUtils.INSTANCE.getDefault());

            if(defaultFridge.getSize() > 0) {
                fridgeViews.add(0, defaultFridge);
            }


            recyclerView.setAdapter(new FridgeRecyclerViewAdapter(fridgeViews, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FridgeView item);
        void onListFragmentInteraction(Bottle item);
    }
}
