package com.fit2081.a2.schemas;

public class CategoryListItem {
    private int categoryId;
    private String categoryName;
    private int eventCount;
    private boolean isCategoryEventActive;

    public CategoryListItem(int categoryId, String categoryName, int eventCount, boolean isCategoryEventActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.eventCount = eventCount;
        this.isCategoryEventActive = isCategoryEventActive;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getEventCount() {
        return eventCount;
    }

    public boolean isCategoryEventActive() {
        return isCategoryEventActive;
    }
}
