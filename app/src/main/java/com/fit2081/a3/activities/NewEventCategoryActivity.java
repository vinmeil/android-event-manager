package com.fit2081.a3.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a3.KeyStore;
import com.fit2081.a3.R;
import com.fit2081.a3.providers.CategoryViewModel;
import com.fit2081.a3.schemas.Category;
import com.fit2081.a3.utils.SMSReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewEventCategoryActivity extends AppCompatActivity {
    EditText etCategoryId, etCategoryName, etCategoryEventCount, etLocation;
    Switch isCategoryEventActive;
    String[] splitMessage;
    EventCategoryBroadCastReceiver eventCategoryBroadCastReceiver;
    ArrayList<Category> categoriesDb = new ArrayList<>();

    CategoryViewModel mCategoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        etCategoryId = findViewById(R.id.editTextCategoryId);
        etCategoryName = findViewById(R.id.editTextCategoryName);
        etCategoryEventCount = findViewById(R.id.editTextCategoryCount);
        isCategoryEventActive = findViewById(R.id.switchCategoryIsActive);
        etLocation = findViewById(R.id.editTextCategoryLocation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCategoryId.setFocusable(false);

        mCategoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventCategoryBroadCastReceiver = new EventCategoryBroadCastReceiver();
        registerReceiver(eventCategoryBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(eventCategoryBroadCastReceiver);
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

    public void onCreateNewCategoryButtonClick(View view) {
        String categoryId = etCategoryId.getText().toString().isEmpty() ? generateCategoryId() : etCategoryId.getText().toString();
        String categoryName = etCategoryName.getText().toString();
        String categoryEventCount = etCategoryEventCount.getText().toString();
        String isCategoryActive = String.valueOf(isCategoryEventActive.isChecked());
        String location = etLocation.getText().toString();

        String[] temporaryMessage = {categoryName, categoryEventCount, isCategoryActive};
        boolean isValid = checkValidMessage(temporaryMessage);

        if (isValid) {
            if (isCategoryActive.isEmpty()) {
                isCategoryActive = "false";
            }

            Category category = new Category(categoryId, categoryName, categoryEventCount, Boolean.parseBoolean(isCategoryActive), location);
            mCategoryViewModel.addCategory(category);

            finish();
        } else {
            Toast.makeText(this, "Invalid inputs in fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private String generateCategoryId() {
        String categoryId = "C";
        for (int i = 0; i < 2; ++i) {
            categoryId += (char)('A' + (int)(Math.random() * 26));
        }

        categoryId += "-";
        for (int i = 0; i < 4; ++i) {
            categoryId += (char)('0' + (int)(Math.random() * 10));
        }

        return categoryId;
    }

    private boolean checkValidMessage(String[] splitMessage) {
        boolean isValid = true;
        if (splitMessage.length != 3) {
            isValid = false;
        } else {
            String categoryName = splitMessage[0];
            String categoryEventCount = splitMessage[1];
            String isCategoryEventActive = splitMessage[2];
            if (categoryName.isEmpty()) {
                isValid = false;
                Toast.makeText(this, "Category name is empty!", Toast.LENGTH_SHORT).show();
            }

            if (!categoryName.matches("[a-zA-Z0-9 ]+")  || !categoryName.matches(".*[a-zA-Z].*")) {
                isValid = false;
                Toast.makeText(this, "Invalid category name", Toast.LENGTH_SHORT).show();
            }

            if (!categoryEventCount.isEmpty()) {
                try {
                    int eventCountInt = Integer.parseInt(categoryEventCount);
                    if (eventCountInt <= 0) {
                        isValid = false;
                        Toast.makeText(this, "Invalid event count", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    isValid = false;
                    Toast.makeText(this, "Invalid event count", Toast.LENGTH_SHORT).show();
                }
            }

            if (!isCategoryEventActive.isEmpty() && !isCategoryEventActive.equalsIgnoreCase("true") && !isCategoryEventActive.equalsIgnoreCase("false")) {
                isValid = false;
            }
        }

        return isValid;
    }

    public class EventCategoryBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            String[] identifier = message.split(":");
            boolean isCorrectIdentifier = true;

            if (!identifier[0].equals("category")) {
                isCorrectIdentifier = false;
            }

            boolean isMessageValid;
            if (isCorrectIdentifier) {
                splitMessage = identifier[1].split(";", -1);;
                isMessageValid = checkValidMessage(splitMessage);
            } else {
                isMessageValid = false;
            }

            if (isMessageValid) {
                String categoryId = generateCategoryId();
                etCategoryId.setText(categoryId);
                etCategoryName.setText(splitMessage[0]);
                etCategoryEventCount.setText(splitMessage[1]);
                isCategoryEventActive.setChecked(Boolean.parseBoolean(splitMessage[2]));
            } else {
                Toast.makeText(context, "Invalid message format!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}