package com.fit2081.a3.providers;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.fit2081.a3.schemas.Category;

import java.util.List;

public class CategoryRepository {
    private EventAndCategoryDAO mEventAndCategoryDAO;
    private LiveData<List<Category>> mAllCategories;

    CategoryRepository(Application application) {
        EventAndCategoryDatabase db = EventAndCategoryDatabase.getDatabase(application);
        mEventAndCategoryDAO = db.eventAndCategoryDAO();
        mAllCategories = mEventAndCategoryDAO.getAllCategories();
    }

    LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    void insert(Category category) {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.addCategory(category);
        });
    }

    void deleteAllCategories() {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.deleteAllCategories();
        });
    }

    void decrementEventCount(String categoryId) {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.decrementEventCount(categoryId);
        });
    }

    void incrementEventCount(String categoryId) {
        EventAndCategoryDatabase.databaseWriteExecutor.execute(() -> {
            mEventAndCategoryDAO.incrementEventCount(categoryId);
        });
    }
}
