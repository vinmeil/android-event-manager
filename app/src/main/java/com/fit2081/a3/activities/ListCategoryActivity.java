package com.fit2081.a3.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.fit2081.a3.R;
import com.fit2081.a3.components.FragmentListCategory;
import com.fit2081.a3.schemas.Category;

import java.util.ArrayList;

public class ListCategoryActivity extends AppCompatActivity {
    public ArrayList<Category> displayedCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_categories);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_view_all_categories_activity, new FragmentListCategory()).addToBackStack("f1").commit();
        awaitFragmentCreated();
    }



    private void awaitFragmentCreated() {
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(
                    @NonNull FragmentManager fragmentManager,
                    @NonNull Fragment fragment,
                    @NonNull View view,
                    @Nullable Bundle savedInstanceState
            ) {
                super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState);

                if (fragment instanceof FragmentListCategory) {
                    ((FragmentListCategory) fragment).refreshView();
                }
            }
        }, false);
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