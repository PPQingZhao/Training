package com.pp.media.media;

public class Image {
    private int id;
    private long addDateTimeMill;
    private String path;
    private String thumbnailpath;
    private String disPlayName;

    public String getThumbnailpath() {
        return thumbnailpath;
    }

    public void setThumbnailpath(String thumbnailpath) {
        this.thumbnailpath = thumbnailpath;
    }

    public String getDisPlayName() {
        return disPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        this.disPlayName = disPlayName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getAddDateTimeMill() {
        return addDateTimeMill;
    }

    public void setAddDateTimeMill(long addDateTimeMill) {
        this.addDateTimeMill = addDateTimeMill;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
