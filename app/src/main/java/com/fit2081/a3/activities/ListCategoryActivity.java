package com.event_manager.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.event_manager.R;
import com.event_manager.components.FragmentCreateEventForm;
import com.event_manager.components.FragmentListCategory;
import com.event_manager.providers.CategoryViewModel;
import com.event_manager.schemas.Category;
import com.event_manager.utils.NewEventUtils;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryActivity extends AppCompatActivity {
    public List<Category> displayedCategories = new ArrayList<>();

    private static final String TAG = "ListCategoryActivity";

    CategoryViewModel mCategoryViewModel;
    
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view_all_categories_activity, new FragmentListCategory()).addToBackStack("f1").commit();

        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        mCategoryViewModel.getAllCategories().observe(this, newData -> {
            displayedCategories = newData;
            FragmentListCategory fragment = (FragmentListCategory) getSupportFragmentManager().findFragmentById(R.id.fragment_view_all_categories_activity);
            fragment.displayData(displayedCategories);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}