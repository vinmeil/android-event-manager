package com.fit2081.a3.components;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit2081.a3.KeyStore;
import com.fit2081.a3.R;
import com.fit2081.a3.activities.MapsActivity;
import com.fit2081.a3.providers.CategoryViewModel;
import com.fit2081.a3.schemas.Category;
import com.fit2081.a3.utils.CategoryListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListCategory extends Fragment {
    // Not sure what these do, I'll leave them here
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "FragmentListCategory";
    private String mParam1;
    private String mParam2;

//    ArrayList<Category> categories = new ArrayList<>();

    List<Category> categories;
    CategoryListAdapter categoryListAdapter;

    private RecyclerView recyclerView;

    CategoryViewModel mCategoryViewModel;

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
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        categoryListAdapter.setData(categories);
        categoryListAdapter.setOnItemClickListener(new CategoryListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Category category) {
                String location = category.getLocation();
                Intent mapIntent = new Intent(getContext(), MapsActivity.class);
                mapIntent.putExtra("location", location);
                startActivity(mapIntent);
            }
        });
        recyclerView.setAdapter(categoryListAdapter);

        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    public void refreshView() {
        mCategoryViewModel.getAllCategories().observe(getViewLifecycleOwner(), newData -> {
            categoryListAdapter.setData(newData);
            categories = newData;
            categoryListAdapter.notifyDataSetChanged();
        });
    }

    public void displayData(List<Category> data) {
        categories = data;
        categoryListAdapter.setData(categories);
        categoryListAdapter.notifyDataSetChanged();
    }
}