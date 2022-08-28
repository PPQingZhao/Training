package com.pp.media.repository.bean;

public class Media {
    private String path;
    private String thumbnailsPath;
    private long addMillsTime;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAddMillsTime() {
        return addMillsTime;
    }

    public void setAddMillsTime(long addMillsTime) {
        this.addMillsTime = addMillsTime;
    }

    public String getThumbnailsPath() {
        return thumbnailsPath;
    }

    public void setThumbnailsPath(String thumbnailsPath) {
        this.thumbnailsPath = thumbnailsPath;
    }
}
