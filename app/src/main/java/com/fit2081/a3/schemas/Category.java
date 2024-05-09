package com.fit2081.a2.schemas;

public class Category {
    private String categoryId;
    private String categoryName;
    private String categoryEventCount;
    private boolean isCategoryEventActive;

    public Category(String categoryId, String categoryName, String categoryEventCount, boolean isCategoryEventActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryEventCount = categoryEventCount;
        this.isCategoryEventActive = isCategoryEventActive;
    }

    public String getCategoryId() {
        return categoryId;
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
