package com.event_manager.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.event_manager.KeyStore;
import com.event_manager.R;
import com.event_manager.components.FragmentListCategory;
import com.event_manager.providers.CategoryViewModel;
import com.event_manager.providers.EventViewModel;
import com.event_manager.schemas.Category;
import com.event_manager.schemas.Event;
import com.event_manager.utils.NewEventUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.event_manager.components.FragmentCreateEventForm;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;
    EditText etEventId, etEventName, etEventCategoryId, etTicketsAvailable;
    Switch isEventActive;
    String[] splitMessage;

    private List<Category> displayedCategories = new ArrayList<Category>() {
    };
    private List<Category> allCategories;
    private List<Event> allEvents;
    private boolean isFirstCreation = true;
    // REMOVE LATER
    private static final String TAG = "MainActivity";

    EventViewModel mEventViewModel;
    CategoryViewModel mCategoryViewModel;
    TextView tvGesture;
    View gestureView;
    private GestureDetector mGestureDetector;

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
        gestureView = findViewById(R.id.gestureView);
        tvGesture = findViewById(R.id.textViewGesture);
        fab = findViewById(R.id.fab);

        // Drawer layout toggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set up the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavigationMenu();

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                etEventId = findViewById(R.id.editTextEventId);
                etEventName = findViewById(R.id.editTextEventName);
                etEventCategoryId = findViewById(R.id.editTextEventCategoryId);
                etTicketsAvailable = findViewById(R.id.editTextTicketsAvailable);
                isEventActive = findViewById(R.id.switchEventIsActive);
                NewEventUtils.onCreateNewEventButtonClick(
                        MainActivity.this,
                        findViewById(R.id.fragmentViewCreate),
                        etEventId,
                        etEventName,
                        etEventCategoryId,
                        etTicketsAvailable,
                        splitMessage,
                        isEventActive
                );
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                etEventName.setText("");
                etEventCategoryId.setText("");
                etTicketsAvailable.setText("");
                isEventActive.setChecked(false);
            }
        });
        gestureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mEventViewModel = new ViewModelProvider(this).get(EventViewModel.class);
        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        mEventViewModel.getAllEvents().observe(this, newData -> {
            allEvents = newData;
        });

        mCategoryViewModel.getAllCategories().observe(this, newData -> {
            allCategories = newData;
        });

        // Gets executed when fragments are created
        awaitFragmentCreated();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstCreation && allCategories != null) {
            isFirstCreation = false;
            displayedCategories.addAll(allCategories);
        }

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
                }

                if (fragment instanceof FragmentListCategory) {
                    FragmentListCategory fragmentListCategory = (FragmentListCategory) fragment;
                    Log.d(TAG, "awaitFragmentCreated: displayedCategories: " + displayedCategories);
                    fragmentListCategory.displayData(displayedCategories);
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
                    Log.d(TAG, "setupNavigationMenu: view_all_categories");
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
                fragment.displayData(displayedCategories);
                return true;
            case R.id.option_delete_all_events:
                deleteAllEvents();
                fragment.refreshView();
                return true;
            case R.id.option_refresh:
                displayedCategories = mCategoryViewModel.getAllCategories().getValue();
                fragment.displayData(displayedCategories);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteAllCategories() {
        mCategoryViewModel.deleteAllCategories();
        displayedCategories.clear();
    }

    private void deleteAllEvents() {
        for (Event event: allEvents) {
            String eventCategoryId = event.getEventCategoryId();
            for (Category category: allCategories) {
                if (category.getCategoryId().equalsIgnoreCase(eventCategoryId)) {
                    mCategoryViewModel.decrementEventCount(eventCategoryId);
                    break;
                }
            }
        }

        mEventViewModel.deleteAllEvents();
    }
}