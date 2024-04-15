package com.fit2081.a2;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FloatingActionButton fab;

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

        // Add listener to the floating action button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Saved Event", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar.make(view, "Undo Save Event", Snackbar.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }

    private void setupNavigationMenu() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.view_all_categories:
                    Toast.makeText(this, "Clicked View All Categories", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.add_category:
                    intent = new Intent(this, NewEventCategoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.view_all_events:
                    Toast.makeText(this, "Clicked View All Events", Toast.LENGTH_SHORT).show();
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

    public void onCreateNewEventCategoryButtonClick(View view) {
        Intent intent = new Intent(this, NewEventCategoryActivity.class);
        startActivity(intent);
    }

    public void onAddEventButtonClick(View view) {
        Intent intent = new Intent(this, NewEventActivity.class);
        startActivity(intent);
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
                Toast.makeText(this, "Clicked Refresh", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}