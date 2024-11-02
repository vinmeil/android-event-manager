package com.event_manager.components;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.event_manager.KeyStore;
import com.event_manager.R;
import com.event_manager.activities.WebViewActivity;
import com.event_manager.providers.EventViewModel;
import com.event_manager.schemas.Event;
import com.event_manager.utils.EventListAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListEvent extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Event> events = new ArrayList<>();
    EventViewModel mEventViewModel;
    EventListAdapter eventListAdapter;
    private RecyclerView recyclerView;

    public FragmentListEvent() {
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
    public static FragmentListEvent newInstance(String param1, String param2) {
        FragmentListEvent fragment = new FragmentListEvent();
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
        eventListAdapter.setOnItemClickListener(new EventListAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Event event) {
                String eventName = event.getEventName();
                Intent webIntent = new Intent(getContext(), WebViewActivity.class);
                webIntent.putExtra("eventName", eventName);
                startActivity(webIntent);
            }
        });

        recyclerView.setAdapter(eventListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    public void displayData(List<Event> data) {
        events = data;
        eventListAdapter.setData(events);
        eventListAdapter.notifyDataSetChanged();
    }
}