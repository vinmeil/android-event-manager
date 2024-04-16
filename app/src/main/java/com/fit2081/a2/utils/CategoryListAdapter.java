package com.fit2081.a2.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.a2.R;
import com.fit2081.a2.schemas.Category;
import com.fit2081.a2.schemas.Event;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    ArrayList<Category> data = new ArrayList<>();

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameTv.setText(data.get(position).getCategoryName());
        holder.idTv.setText(String.valueOf(data.get(position).getCategoryId()));
        holder.countTv.setText(String.valueOf(data.get(position).getCategoryEventCount()));
        holder.activeTv.setText(data.get(position).isCategoryEventActive() ? "Active" : "Inactive");
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idTv;
        public TextView nameTv;
        public TextView countTv;
        public TextView activeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            idTv = itemView.findViewById(R.id.list_category_id);
            nameTv = itemView.findViewById(R.id.list_category_name);
            countTv = itemView.findViewById(R.id.list_category_count);
            activeTv = itemView.findViewById(R.id.list_category_active);
        }
    }
}