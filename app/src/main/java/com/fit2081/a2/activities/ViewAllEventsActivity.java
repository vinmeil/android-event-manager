package com.fit2081.a2.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fit2081.a2.R;
import com.fit2081.a2.components.EventList;

public class ViewAllEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_events);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAllEventList, new EventList()).addToBackStack("f1").commit();
    }
}