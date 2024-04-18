package com.fit2081.a2.components;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.schemas.Category;
import com.fit2081.a2.utils.CategoryListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {
    // Not sure what these do, I'll leave them here
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public interface onDataUpdateListener {
        void onDataUpdate(ArrayList<Category> data);
        ArrayList<Category> getData();
    }

    onDataUpdateListener onDataUpdateListener;
    ArrayList<Category> categories = new ArrayList<>();
    CategoryListAdapter categoryListAdapter;
    private RecyclerView recyclerView;

    public FragmentListCategory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryList.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentListCategory newInstance(String param1, String param2) {
        FragmentListCategory fragment = new FragmentListCategory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        categoryListAdapter = new CategoryListAdapter();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onDataUpdateListener = (onDataUpdateListener) context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        categoryListAdapter.setData(categories);
        recyclerView.setAdapter(categoryListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    public void refreshView() {
        Gson gson = new Gson();
        String categoriesStr = getContext().getSharedPreferences(KeyStore.FILE_NAME, 0).getString(KeyStore.KEY_CATEGORIES, "");
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        ArrayList<Category> dbCategories = gson.fromJson(categoriesStr, type);

        onDataUpdateListener.onDataUpdate(dbCategories);

        categoryListAdapter.setData(dbCategories);
        categoryListAdapter.notifyDataSetChanged();
    }

    public void displayData(ArrayList<Category> data) {
        categories = data;
        categoryListAdapter.setData(categories);
        categoryListAdapter.notifyDataSetChanged();
    }
}