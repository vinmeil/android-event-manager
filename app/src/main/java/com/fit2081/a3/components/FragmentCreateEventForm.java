package com.fit2081.a3.components;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.fit2081.a3.R;
import com.fit2081.a3.utils.NewEventUtils;
import com.fit2081.a3.utils.SMSReceiver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCreateEventForm#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCreateEventForm extends Fragment {

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
    FragmentCreateEventForm.EventBroadCastReceiver eventBroadCastReceiver;

    public FragmentCreateEventForm() {
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
    public static FragmentCreateEventForm newInstance(String param1, String param2) {
        FragmentCreateEventForm fragment = new FragmentCreateEventForm();
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
        eventBroadCastReceiver = new FragmentCreateEventForm.EventBroadCastReceiver();
        getActivity().registerReceiver(eventBroadCastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER), getActivity().RECEIVER_EXPORTED);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(eventBroadCastReceiver);
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
                isMessageValid = NewEventUtils.checkValidMessage(context, splitMessage);
            } else {
                isMessageValid = false;
            }

            if (isMessageValid) {
                String eventId = NewEventUtils.generateEventId();
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