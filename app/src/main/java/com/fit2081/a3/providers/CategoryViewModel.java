package com.event_manager.providers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.event_manager.schemas.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository mRepository;
    private LiveData<List<Category>> mAllCategories;
    public CategoryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CategoryRepository(application);
        mAllCategories = mRepository.getAllCategories();
    }

    public LiveData<List<Category>> getAllCategories() {
        return mAllCategories;
    }

    public void addCategory(Category category) {
        mRepository.insert(category);
    }

    public void deleteAllCategories() {
        mRepository.deleteAllCategories();
    }

    public void decrementEventCount(String categoryId) {
        mRepository.decrementEventCount(categoryId);
    }

    public void incrementEventCount(String categoryId) {
        mRepository.incrementEventCount(categoryId);
    }
}
