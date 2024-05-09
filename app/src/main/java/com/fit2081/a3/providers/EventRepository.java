package com.fit2081.a3.providers;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.a3.schemas.Event;

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
}
