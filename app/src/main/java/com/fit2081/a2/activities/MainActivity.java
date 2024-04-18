package com.fit2081.a2.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.components.FragmentListCategory;
import com.fit2081.a2.utils.NewEventUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.fit2081.a2.components.FragmentCreateEventForm;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;
    EditText etEventId, etEventName, etEventCategoryId, etTicketsAvailable;
    Switch isEventActive;
    String[] splitMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KeyStore.KEY_USERNAME, null);
        String password = sharedPreferences.getString(KeyStore.KEY_PASSWORD, null);

        if (username == null || password == null || !sharedPreferences.getBoolean(KeyStore.KEY_IS_LOGGED_IN, false)) {
            // User is not logged in, redirect to Sign Up activity
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
            finish();
        }

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewCreate, new FragmentCreateEventForm()).addToBackStack("f1").commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_category_list, new FragmentListCategory()).addToBackStack("f1").commit();

        // Find views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);

        FragmentCreateEventForm createEventForm = (FragmentCreateEventForm) getSupportFragmentManager().findFragmentById(R.id.fragmentViewCreate);

        // Drawer layout toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavigationMenu();
        waitForFragmentCreated();

        // Add listener to the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewEventUtils.onCreateNewEventButtonClick(
                        MainActivity.this,
                        view,
                        etEventId,
                        etEventName,
                        etEventCategoryId,
                        etTicketsAvailable,
                        splitMessage,
                        isEventActive
                );
            }
        });
    }

    private void waitForFragmentCreated() {
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(
                    @NonNull FragmentManager fragmentManager,
                    @NonNull Fragment fragment,
                    @NonNull View view,
                    @Nullable Bundle savedInstanceState
            ) {
                super.onFragmentViewCreated(fragmentManager, fragment, view, savedInstanceState);

                if (fragment instanceof FragmentCreateEventForm) {
                    etEventId = view.findViewById(R.id.editTextEventId);
                    etEventName = view.findViewById(R.id.editTextEventName);
                    etEventCategoryId = view.findViewById(R.id.editTextEventCategoryId);
                    etTicketsAvailable = view.findViewById(R.id.editTextTicketsAvailable);
                    isEventActive = view.findViewById(R.id.switchEventIsActive);

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NewEventUtils.onCreateNewEventButtonClick(
                                    MainActivity.this,
                                    view,
                                    etEventId,
                                    etEventName,
                                    etEventCategoryId,
                                    etTicketsAvailable,
                                    splitMessage,
                                    isEventActive
                            );
                        }
                    });
                }
            }
        }, false);
    }

    private void setupNavigationMenu() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.view_all_categories:
                    intent = new Intent(this, ViewAllCategoriesActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_category:
                    intent = new Intent(this, NewEventCategoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.view_all_events:
                    intent = new Intent(this, ViewAllEventsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(KeyStore.KEY_IS_LOGGED_IN, false);
                    editor.apply();
                    intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.option_clear_event_form:
                Toast.makeText(this, "Clicked Clear Event Form", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_delete_all_categories:
                Toast.makeText(this, "Clicked Delete All Categories", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_delete_all_events:
                Toast.makeText(this, "Clicked Delete All Events", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.option_refresh:
                FragmentListCategory fragment = (FragmentListCategory) getSupportFragmentManager().findFragmentById(R.id.fragment_main_category_list);
                fragment.refreshView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}