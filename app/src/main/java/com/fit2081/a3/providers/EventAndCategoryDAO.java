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

    @Query("DELETE FROM events")
    void deleteAllEvents();
}
