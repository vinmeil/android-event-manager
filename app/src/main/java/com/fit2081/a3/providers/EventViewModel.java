package com.event_manager.providers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.event_manager.schemas.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;
    private LiveData<List<Event>> mAllEvents;
    public EventViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EventRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    public void addEvent(Event event) {
        mRepository.insert(event);
    }

    public void deleteAllEvents() {
        mRepository.deleteAllEvents();
    }

    public void deleteEvent(String eventId) {
        mRepository.deleteEvent(eventId);
    }
}
