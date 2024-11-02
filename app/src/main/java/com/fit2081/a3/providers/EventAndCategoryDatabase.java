package com.event_manager.providers;

import android.content.Context;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.event_manager.schemas.Category;
import com.event_manager.schemas.Event;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Event.class, Category.class}, version = 1)
public abstract class EventAndCategoryDatabase extends RoomDatabase {
    public static final String EVENT_AND_CATEGORY_DATABASE_NAME = "event_and_category_database";
    public abstract EventAndCategoryDAO eventAndCategoryDAO();
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private static volatile EventAndCategoryDatabase INSTANCE;
    static EventAndCategoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (EventAndCategoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(), EventAndCategoryDatabase.class, EVENT_AND_CATEGORY_DATABASE_NAME).build();
                }
            }
        }

        return INSTANCE;
    }
}
