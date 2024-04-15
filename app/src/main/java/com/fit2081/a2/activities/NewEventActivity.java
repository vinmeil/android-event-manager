package com.fit2081.a2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.fragments.CreateEventForm;
import com.fit2081.a2.utils.NewEventUtils;
import com.fit2081.a2.utils.SMSReceiver;

public class NewEventActivity extends AppCompatActivity {
    EditText etEventId, etEventName, etEventCategoryId, etTicketsAvailable;
    Switch isEventActive;
    String[] splitMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewCreate, new CreateEventForm()).addToBackStack("f1").commit();
    }

    public void onCreateNewEventButtonClick(View view) {
        NewEventUtils.onCreateNewEventButtonClick(
                this,
                view,
                etEventId,
                etEventName,
                etEventCategoryId,
                etTicketsAvailable,
                splitMessage,
                isEventActive
        );
    }
}