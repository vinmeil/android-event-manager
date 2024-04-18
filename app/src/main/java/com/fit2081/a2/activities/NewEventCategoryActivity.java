package com.fit2081.a2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.schemas.Category;
import com.fit2081.a2.utils.SMSReceiver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NewEventCategoryActivity extends AppCompatActivity {
    EditText etCategoryId, etCategoryName, etCategoryEventCount;
    Switch isCategoryEventActive;
    String[] splitMessage;
    EventCategoryBroadCastReceiver eventCategoryBroadCastReceiver;
    ArrayList<Category> categoriesDb = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event_category);

        etCategoryId = findViewById(R.id.editTextCategoryId);
        etCategoryName = findViewById(R.id.editTextCategoryName);
        etCategoryEventCount = findViewById(R.id.editTextCategoryCount);
        isCategoryEventActive = findViewById(R.id.switchCategoryIsActive);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etCategoryId.setFocusable(false);
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
        String[] temporaryMessage = new String[3];
        temporaryMessage[0] = etCategoryName.getText().toString();
        temporaryMessage[1] = etCategoryEventCount.getText().toString();
        temporaryMessage[2] = String.valueOf(isCategoryEventActive.isChecked());
        boolean isValid = checkValidMessage(temporaryMessage);

        if (isValid) {
            splitMessage = temporaryMessage;

            if (splitMessage[2].isEmpty()) {
                splitMessage[2] = "false";
            }

            loadDataFromSharedPreference();
            Category category = new Category(categoryId, splitMessage[0], splitMessage[1], Boolean.parseBoolean(splitMessage[2]));
            categoriesDb.add(category);
            System.out.println(categoriesDb);
            System.out.println(categoriesDb.size());
            saveDataToSharedPreference(categoriesDb);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid inputs in fields!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadDataFromSharedPreference() {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String categoriesStr = sharedPreferences.getString(KeyStore.KEY_CATEGORIES, "");
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        categoriesDb = gson.fromJson(categoriesStr, type);
        if (categoriesDb == null) {
            categoriesDb = new ArrayList<>();
        }
    }


    private void saveDataToSharedPreference(ArrayList<Category> categories) {
        SharedPreferences sharedPreferences = getSharedPreferences(KeyStore.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String categoriesStr = gson.toJson(categories);
        editor.putString(KeyStore.KEY_CATEGORIES, categoriesStr);

        editor.apply();
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
            }

            if (!categoryName.matches("[a-zA-Z0-9 ]+")) {
                isValid = false;
            }

            if (!categoryEventCount.isEmpty()) {
                try {
                    Integer.parseInt(categoryEventCount);
                } catch (Exception e) {
                    isValid = false;
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