package com.fit2081.a2.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

public class NewEventUtils {
    public static void onCreateNewEventButtonClick(
            Context context,
            View view,
            EditText etEventId,
            EditText etEventName,
            EditText etEventCategoryId,
            EditText etTicketsAvailable,
            String[] splitMessage,
            Switch isEventActive
    ) {
        String eventId = etEventId.getText().toString().isEmpty() ? generateEventId() : etEventId.getText().toString();
        String[] temporaryMessage = new String[4];
        temporaryMessage[0] = etEventName.getText().toString();
        temporaryMessage[1] = etEventCategoryId.getText().toString();
        temporaryMessage[2] = etTicketsAvailable.getText().toString();
        temporaryMessage[3] = String.valueOf(isEventActive.isChecked());
        boolean isValid = checkValidMessage(temporaryMessage);

        if (isValid) {
            splitMessage = temporaryMessage;
            etEventName.setText("");
            etEventCategoryId.setText("");
            etTicketsAvailable.setText("");
            isEventActive.setChecked(false);
            saveDataToSharedPreference(context, eventId, splitMessage);
            Snackbar.make(view, "Saved Event Successfully", Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar.make(view, "Undo Save Event", Snackbar.LENGTH_SHORT).show();
                        }
                    }).show();
        } else {
            Snackbar.make(view, "Invalid inputs in fields!", Snackbar.LENGTH_SHORT).show();
        }
    }

    public static boolean checkValidMessage(String[] splitMessage) {
        boolean isValid = true;
        if (splitMessage.length != 4) {
            isValid = false;
        } else {
            String eventName = splitMessage[0];
            String eventCategoryId = splitMessage[1];
            String ticketsAvailable = splitMessage[2];
            String isEventActive = splitMessage[3];
            if (eventName.isEmpty() || eventCategoryId.isEmpty()) {
                isValid = false;
            }

            if (!eventName.matches("[a-zA-Z0-9 ]+")) {
                isValid = false;
            }

            // TODO: Check if categoryId exists in shared preferences.

            if (!ticketsAvailable.isEmpty()) {
                try {
                    int ticketsAvailableInt = Integer.parseInt(splitMessage[2]);

                    if (ticketsAvailableInt <= 0) {
                        isValid = false;
                    }
                } catch (Exception e) {
                    isValid = false;
                }
            }

            if (!isEventActive.isEmpty() && !isEventActive.equalsIgnoreCase("true") && !isEventActive.equalsIgnoreCase("false")) {
                isValid = false;
            }
        }

        return isValid;
    }

    public static String generateEventId() {
        String eventId = "E";
        for (int i = 0; i < 2; ++i) {
            eventId += (char)('A' + (int)(Math.random() * 26));
        }

        eventId += "-";
        for (int i = 0; i < 5; ++i) {
            eventId += (char)('0' + (int)(Math.random() * 10));
        }

        return eventId;
    }

    private static void saveDataToSharedPreference(Context context, String eventId, String[] messageDetails) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KeyStore.FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KeyStore.KEY_EVENT_ID, eventId);
        editor.putString(KeyStore.KEY_EVENT_NAME, messageDetails[0]);
        editor.putString(KeyStore.KEY_EVENT_CATEGORY_ID, messageDetails[1]);

        if (!messageDetails[2].isEmpty()) {
            int ticketsAvailable = Integer.parseInt(messageDetails[2]);
            editor.putInt(KeyStore.KEY_EVENT_TICKETS_AVAILABLE, ticketsAvailable);
        }

        if (!messageDetails[3].isEmpty()) {
            boolean isEventActive = Boolean.parseBoolean(messageDetails[3]);
            editor.putBoolean(KeyStore.KEY_IS_EVENT_ACTIVE, isEventActive);
        }

        editor.apply();
    }
}
