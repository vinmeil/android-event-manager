package com.fit2081.a2.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.activities.MainActivity;
import com.fit2081.a2.utils.SMSReceiver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateEventForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateEventForm extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText etEventId, etEventName, etEventCategoryId, etTicketsAvailable;
    Switch isEventActive;
    String[] splitMessage;
    CreateEventForm.EventBroadCastReceiver eventBroadCastReceiver;

    public CreateEventForm() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateEventForm newInstance(String param1, String param2) {
        CreateEventForm fragment = new CreateEventForm();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etEventId = view.findViewById(R.id.editTextEventId);
        etEventName = view.findViewById(R.id.editTextEventName);
        etEventCategoryId = view.findViewById(R.id.editTextEventCategoryId);
        etTicketsAvailable = view.findViewById(R.id.editTextTicketsAvailable);
        isEventActive = view.findViewById(R.id.switchEventIsActive);

        etEventId.setFocusable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBroadCastReceiver = new CreateEventForm.EventBroadCastReceiver();
        getActivity().registerReceiver(eventBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), getActivity().RECEIVER_EXPORTED);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(eventBroadCastReceiver);
    }

    public void onCreateNewEventButtonClick(View view) {
        String eventId = etEventId.getText().toString().isEmpty() ? generateEventId() : etEventId.getText().toString();
        String[] temporaryMessage = new String[4];
        temporaryMessage[0] = etEventName.getText().toString();
        temporaryMessage[1] = etEventCategoryId.getText().toString();
        temporaryMessage[2] = etTicketsAvailable.getText().toString();
        temporaryMessage[3] = String.valueOf(isEventActive.isChecked());
        boolean isValid = checkValidMessage(temporaryMessage);

        if (isValid) {
            splitMessage = temporaryMessage;
            saveDataToSharedPreference(eventId, splitMessage);
            String toastMessage = String.format("Event saved: %s to %s.", eventId, splitMessage[1]);
            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Invalid inputs in fields!", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveDataToSharedPreference(String eventId, String[] messageDetails) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyStore.FILE_NAME, getActivity().MODE_PRIVATE);
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


    private String generateEventId() {
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

    private boolean checkValidMessage(String[] splitMessage) {
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

    public class EventBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            String[] identifier = message.split(":");
            boolean isCorrectIdentifier = true;

            if (!identifier[0].equals("event")) {
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
                String eventId = generateEventId();
                etEventId.setText(eventId);
                etEventName.setText(splitMessage[0]);
                etEventCategoryId.setText(splitMessage[1]);
                etTicketsAvailable.setText(splitMessage[2]);
                isEventActive.setChecked(Boolean.parseBoolean(splitMessage[3]));
            } else {
                Toast.makeText(context, "Invalid message format!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event, container, false);
    }
}