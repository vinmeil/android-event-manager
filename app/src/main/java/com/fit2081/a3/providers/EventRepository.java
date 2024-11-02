package com.event_manager.providers;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.event_manager.schemas.Event;

import java.util.List;

public class EventRepository {
    private EventAndCategoryDAO mEventAndCategoryDAO;
    private LiveData<List<Event>> mAllEvents;

    EventRepository(Application application) {
        EventAndCategoryDatabase db = EventAndCategoryDatabase.getDatabase(application);
        mEventAndCategoryDAO = db.eventAndCategoryDAO();
        mAllEvents = mEventAndCategoryDAO.getAllEvents();
    }

    LiveData<List<Event>> getAllEvents() {
        return mAllEvents;
    }

    void insert(Event event) {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.addEvent(event);
        });
    }

    void deleteAllEvents() {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.deleteAllEvents();
        });
    }

    void deleteEvent(String eventId) {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.deleteEvent(eventId);
        });
    }
}
