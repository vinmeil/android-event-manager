package com.fit2081.a3.providers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.fit2081.a3.schemas.Category;

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
}
