package com.bazukaa.secured.models;

public class OnboardingItem {
    private int indexImage;
    private int image;
    private String title;
    private String description;

    public int getIndexImage() {
        return indexImage;
    }

    public void setIndexImage(int indexImage) {
        this.indexImage = indexImage;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
