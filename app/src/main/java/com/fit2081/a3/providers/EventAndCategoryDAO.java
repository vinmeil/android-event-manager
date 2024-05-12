package com.fit2081.a3.providers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.fit2081.a3.schemas.Category;
import com.fit2081.a3.schemas.Event;

import java.util.List;

@Dao
public interface EventAndCategoryDAO {
    @Query("SELECT * FROM categories")
    LiveData<List<Category>> getAllCategories();

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getAllEvents();

    @Insert
    void addEvent(Event event);

    @Insert
    void addCategory(Category category);

    @Query("DELETE FROM categories")
    void deleteAllCategories();

    @Query("UPDATE categories SET categoryEventCount = categoryEventCount - 1 WHERE categoryID = :categoryId")
    void decrementEventCount(String categoryId);

    @Query("UPDATE categories SET categoryEventCount = categoryEventCount + 1 WHERE categoryID = :categoryId")
    void incrementEventCount(String categoryId);

    @Query("DELETE FROM events")
    void deleteAllEvents();

    @Query("DELETE FROM events WHERE eventId = :eventId")
    void deleteEvent(String eventId);
}
