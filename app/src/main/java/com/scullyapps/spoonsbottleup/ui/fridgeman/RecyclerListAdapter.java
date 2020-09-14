package com.scullyapps.spoonsbottleup.ui.fridgeman;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.scullyapps.spoonsbottleup.models.Bottle;
import com.scullyapps.spoonsbottleup.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private final List<Bottle> items = new ArrayList<>();

    public List<Bottle> toRemove = new ArrayList<>();

    private ItemTouchHelper ith;

    public boolean modified;

    public RecyclerListAdapter(ArrayList<Bottle> bottles) {

        items.addAll(bottles);
    }

    public List<Bottle> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_bottle_management, parent, false);

        ViewHolder vh = new ViewHolder(view);

        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.textView.setText(items.get(position).getName());

        holder.maxText.setText(String.format("Max: %d", items.get(position).getMax()));

        holder.drag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    if(ith == null)
                        return false;
                    ith.startDrag(holder);
                }

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

        modified = true;

        Collections.swap(items, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    public void setIth(ItemTouchHelper ith) {
        this.ith = ith;
    }

    @Override
    public void onItemDismiss(int position) {

        modified = true;

        toRemove.add(items.get(position));
        items.remove(position);

        notifyItemRemoved(position);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        public final TextView textView;
        public final TextView maxText;
        public final ImageView drag;

        private boolean expanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.btlm_text);
            maxText = itemView.findViewById(R.id.btlm_text_max);
            drag = itemView.findViewById(R.id.btlm_dragview);

            // maxText.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(expanded) {
                        maxText.setVisibility(View.GONE);
                    } else {
                        maxText.setVisibility(View.VISIBLE);
                    }

                    expanded = !expanded;
                }
            });

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0xFFF0F0F0);
        }
    }
}
