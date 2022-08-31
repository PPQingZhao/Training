package com.pp.media.media;


import androidx.collection.ArrayMap;

public class ImageBucket {
    private int id;
    private String disPlayName;

    private final ArrayMap<Integer, Image> mImageMap = new ArrayMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisPlayName() {
        return disPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        this.disPlayName = disPlayName;
    }

    public void putImage(Image image) {
        mImageMap.put(image.getId(), image);
    }

    public Image getImage(int imageId) {
        return mImageMap.get(imageId);
    }

    public ArrayMap<Integer, Image> getImageMap() {
        return mImageMap;
    }
}
