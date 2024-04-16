package com.fit2081.a2.components;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit2081.a2.KeyStore;
import com.fit2081.a2.R;
import com.fit2081.a2.schemas.Event;
import com.fit2081.a2.utils.EventListAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ArrayList<Event> events = new ArrayList<Event>();
    EventListAdapter eventListAdapter;
    private RecyclerView recyclerView;

    public EventList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventList.
     */
    // TODO: Rename and change types and number of parameters
    public static EventList newInstance(String param1, String param2) {
        EventList fragment = new EventList();
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
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.event_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        eventListAdapter = new EventListAdapter();
        eventListAdapter.setData(events);
        recyclerView.setAdapter(eventListAdapter);
        displayEvents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    public void displayEvents() {
        Gson gson = new Gson();
        String events = getContext().getSharedPreferences(KeyStore.FILE_NAME, 0).getString(KeyStore.KEY_EVENTS, "");
        Type type = new TypeToken<ArrayList<Event>>() {}.getType();
        ArrayList<Event> dbEvents = gson.fromJson(events, type);
        eventListAdapter.setData(dbEvents);
        eventListAdapter.notifyDataSetChanged();
    }
}