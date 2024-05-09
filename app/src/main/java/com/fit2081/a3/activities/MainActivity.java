package com.fit2081.a3.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.fit2081.a3.KeyStore;
import com.fit2081.a3.R;
import com.fit2081.a3.components.FragmentListCategory;
import com.fit2081.a3.schemas.Category;
import com.fit2081.a3.schemas.Event;
import com.fit2081.a3.utils.NewEventUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.fit2081.a3.components.FragmentCreateEventForm;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;
    EditText etEventId, etEventName, etEventCategoryId, etTicketsAvailable;
    Switch isEventActive;
    String[] splitMessage;
    public ArrayList<Category> displayedCategories = new ArrayList<>();

    // REMOVE LATER
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.RECEIVE_SMS,
                android.Manifest.permission.READ_SMS
        }, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(KeyStore.KEY_USERNAME, null);
        String password = sharedPreferences.getString(KeyStore.KEY_PASSWORD, null);

        if (username == null || password == null || !sharedPreferences.getBoolean(KeyStore.KEY_IS_LOGGED_IN, false)) {
            // User is not logged in, redirect to Sign Up activity
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentViewCreate, new FragmentCreateEventForm()).addToBackStack("f1").commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main_category_list, new FragmentListCategory()).addToBackStack("f1").commit();

        // Find views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);

        // Drawer layout toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavigationMenu();

        // Gets executed when fragments are created
        Log.d(TAG, "onCreate");
        awaitFragmentCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        FragmentListCategory fragment = (FragmentListCategory) getSupportFragmentManager().findFragmentById(R.id.fragment_main_category_list);
        fragment.displayData(displayedCategories);
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
                } else if (fragment instanceof FragmentListCategory) {
                    FragmentListCategory fragmentListCategory = (FragmentListCategory) fragment;
                    fragmentListCategory.refreshView();
                    displayedCategories = fragmentListCategory.getData();
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
                    intent = new Intent(this, ListCategoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_category:
                    intent = new Intent(this, NewEventCategoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.view_all_events:
                    intent = new Intent(this, ListEventActivity.class);
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
            drawerLayout.closeDrawer(GravityCompat.START);
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

        FragmentListCategory fragment = (FragmentListCategory) getSupportFragmentManager().findFragmentById(R.id.fragment_main_category_list);
        switch (item.getItemId()) {
            case R.id.option_clear_event_form:
                etEventId.setText("");
                etEventCategoryId.setText("");
                etEventName.setText("");
                etTicketsAvailable.setText("");
                isEventActive.setChecked(false);
                Snackbar.make(findViewById(R.id.fragmentViewCreate), "Cleared form", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.option_delete_all_categories:
                deleteAllCategories();
                fragment.refreshView();
                return true;
            case R.id.option_delete_all_events:
                deleteAllEvents();
                fragment.refreshView();
                return true;
            case R.id.option_refresh:
                displayedCategories = fragment.getData();
                fragment.refreshView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllCategories() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KeyStore.KEY_CATEGORIES);
        editor.apply();
        displayedCategories.clear();

        FragmentListCategory fragment = (FragmentListCategory) getSupportFragmentManager().findFragmentById(R.id.fragment_main_category_list);
        fragment.refreshView();
    }

    private void deleteAllEvents() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String allEventsStr = sharedPreferences.getString(KeyStore.KEY_EVENTS, "");
        String categoriesStr = sharedPreferences.getString(KeyStore.KEY_CATEGORIES, "");
        Gson gson = new Gson();
        Type eventType = new TypeToken<ArrayList<Event>>() {}.getType();
        Type categoryType = new TypeToken<ArrayList<Category>>() {}.getType();
        ArrayList<Event> allEvents = gson.fromJson(allEventsStr, eventType);
        ArrayList<Category> categories = gson.fromJson(categoriesStr, categoryType);

        if (allEvents == null) {
            allEvents = new ArrayList<>();
        }

        if (categories == null) {
            categories = new ArrayList<>();
        }

        for (Event event: allEvents) {
            String eventCategoryId = event.getEventCategoryId();
            for (Category category: categories) {
                if (category.getCategoryId().equalsIgnoreCase(eventCategoryId)) {
                    category.decrementEventCount();
                    break;
                }
            }
        }

        String newCategoriesStr = gson.toJson(categories);
        editor.putString(KeyStore.KEY_CATEGORIES, newCategoriesStr);
        editor.remove(KeyStore.KEY_EVENTS);
        editor.apply();
    }
}