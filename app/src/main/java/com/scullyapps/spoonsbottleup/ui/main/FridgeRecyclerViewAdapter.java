package com.scullyapps.spoonsbottleup.ui.main;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.scullyapps.spoonsbottleup.Bottle;
import com.scullyapps.spoonsbottleup.Fridge;
import com.scullyapps.spoonsbottleup.R;
import com.scullyapps.spoonsbottleup.ui.main.FridgeFragment.OnListFragmentInteractionListener;
import com.scullyapps.spoonsbottleup.ui.main.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FridgeRecyclerViewAdapter extends RecyclerView.Adapter<FridgeRecyclerViewAdapter.ViewHolder> {

    private final List<Fridge> mValues;
    private final OnListFragmentInteractionListener mListener;

    public FridgeRecyclerViewAdapter(List<Fridge> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_fridge, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.fridge = mValues.get(position);

        TextView txtName = holder.txtName;

        txtName.setText(mValues.get(position).getName());

        if(holder.fridge.getName().equals("Default")) {
            txtName.setTypeface(null, Typeface.BOLD);
            txtName.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtName.getTextSize() + 12);
            holder.disableButtons();
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.fridge);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView txtName;
        public final Button btnEdit;
        public final Button btnDel;

        public Fridge fridge;

        public ViewHolder(View view) {
            super(view);

            mView = view;

            txtName = view.findViewById(R.id.txt_fridgevh_name);
            btnEdit = view.findViewById(R.id.btn_fridgevh_edit);
            btnDel = view.findViewById(R.id.btn_fridgevh_add);

        }

        public void disableButtons() {
            btnDel.setVisibility(View.INVISIBLE);
            btnEdit.setVisibility(View.INVISIBLE);
        }
    }
}

//    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
//        public DummyItem mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
//            mContentView = (TextView) view.findViewById(R.id.content);
//        }
//
//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
//    }

