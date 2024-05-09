package com.fit2081.a3.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fit2081.a3.R;
import com.fit2081.a3.schemas.Category;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    ArrayList<Category> data = new ArrayList<>();

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View rowView;
        rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = data.get(position);
        holder.nameTv.setText(category.getCategoryName());
        holder.idTv.setText(String.valueOf(category.getCategoryId()));
        holder.countTv.setText(String.valueOf(category.getCategoryEventCount()));
        holder.activeTv.setText(category.isCategoryEventActive() ? "Active" : "Inactive");
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