package com.event_manager.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.event_manager.R;
import com.event_manager.schemas.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    List<Category> data;

    public interface onItemClickListener {
        void onItemClick(Category category);
    }

    private onItemClickListener listener;

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Category> data) {
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(data.get(position));
                    }
                }
            });
        }
    }
}