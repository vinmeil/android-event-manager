package com.event_manager.schemas;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categories")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uniqueCategoryID")
    @NonNull
    private int uniqueID;

    public int getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(int uniqueID) {
        this.uniqueID = uniqueID;
    }

    @ColumnInfo(name = "categoryID")
    private String categoryId;

    @ColumnInfo(name = "categoryName")
    private String categoryName;

    @ColumnInfo(name = "categoryEventCount")
    private String categoryEventCount;

    @ColumnInfo(name = "isCategoryEventActive")
    private boolean isCategoryEventActive;

    @ColumnInfo(name = "location")
    private String location;

    public String getCategoryId() {
        return categoryId;
    }

    public Category(String categoryId, String categoryName, String categoryEventCount, boolean isCategoryEventActive, String location) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryEventCount = categoryEventCount;
        this.isCategoryEventActive = isCategoryEventActive;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryEventCount() {
        return categoryEventCount;
    }

    public void setCategoryEventCount(String categoryEventCount) {
        this.categoryEventCount = categoryEventCount;
    }

    public void incrementEventCount() {
        if (categoryEventCount == null || categoryEventCount.isEmpty()) {
            categoryEventCount = "0";
        }

        int count = Integer.parseInt(categoryEventCount);
        count++;
        this.categoryEventCount = String.valueOf(count);
    }

    public void decrementEventCount() {
        int count = Integer.parseInt(categoryEventCount);
        count--;
        this.categoryEventCount = String.valueOf(count);
    }

    public boolean isCategoryEventActive() {
        return isCategoryEventActive;
    }

    public void setCategoryEventActive(boolean categoryEventActive) {
        isCategoryEventActive = categoryEventActive;
    }
}
