package com.event_manager.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.event_manager.R;
import com.event_manager.components.FragmentListEvent;
import com.event_manager.providers.EventViewModel;

public class ListEventActivity extends AppCompatActivity {

    EventViewModel mEventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAllEventList, new FragmentListEvent()).addToBackStack("f1").commit();

        mEventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mEventViewModel.getAllEvents().observe(this, newData -> {
            FragmentListEvent fragment = (FragmentListEvent) getSupportFragmentManager().findFragmentById(R.id.fragmentAllEventList);
            fragment.displayData(newData);
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