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
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public void setData(ArrayList<Category> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View rowView;

        if (viewType == TYPE_HEADER) {
            rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_header_layout, viewGroup, false);
        } else {
            rowView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_list_item, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != 0) {
            Category category = data.get(position);
            holder.nameTv.setText(category.getCategoryName());
            holder.idTv.setText(String.valueOf(category.getCategoryId()));
            holder.countTv.setText(String.valueOf(category.getCategoryEventCount()));
            holder.activeTv.setText(category.isCategoryEventActive() ? "Active" : "Inactive");
        }
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size() + 1;
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

//    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
//        public TextView headerIdTv;
//        public TextView headerNameTv;
//        public TextView headerCountTv;
//        public TextView headerActiveTv;
//
//        public HeaderViewHolder(View itemView) {
//            super(itemView);
//            headerIdTv = itemView.findViewById(R.id.list_category_id_header);
//            headerNameTv = itemView.findViewById(R.id.list_category_name_header);
//            headerCountTv = itemView.findViewById(R.id.list_category_count_header);
//            headerActiveTv = itemView.findViewById(R.id.list_category_active_header);
//        }
//    }
}