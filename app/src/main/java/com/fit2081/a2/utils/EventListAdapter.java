package com.fit2081.a2.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.a2.R;
import com.fit2081.a2.schemas.Event;

import java.util.ArrayList;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    ArrayList<Event> data = new ArrayList<Event>();
    private static final int ITEM_HEADER = 0;
    private static final int ITEM_CHILD = 1;

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_item, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_HEADER;
        } else {
            return ITEM_CHILD;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTv.setText("Name: " + data.get(position).getEventName());
        holder.idTv.setText("ID: " + String.valueOf(data.get(position).getEventId()));
        holder.categoryIdTv.setText("Category ID: " + String.valueOf(data.get(position).getEventCategoryId()));
        holder.countTv.setText("Tickets: " + String.valueOf(data.get(position).getTicketsAvailable()));
        holder.activeTv.setText(data.get(position).isEventActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idTv;
        public TextView nameTv;
        public TextView categoryIdTv;
        public TextView countTv;
        public TextView activeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.event_list_item_id);
            nameTv = itemView.findViewById(R.id.event_list_item_name);
            categoryIdTv = itemView.findViewById(R.id.event_list_item_category_id);
            countTv = itemView.findViewById(R.id.event_list_item_count);
            activeTv = itemView.findViewById(R.id.event_list_item_active);
        }
    }
}