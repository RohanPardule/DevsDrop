package com.example.devdrops.news;

import android.graphics.drawable.Drawable;

public class CategoriesRVModal {

    private String category;
    private int categoryImageUrl;

    public CategoriesRVModal(String category, int categoryImageUrl) {
        this.category = category;
        this.categoryImageUrl = categoryImageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryImageUrl() {
        return categoryImageUrl;
    }

    public void setCategoryImageUrl(int categoryImageUrl) {
        this.categoryImageUrl = categoryImageUrl;
    }
}
