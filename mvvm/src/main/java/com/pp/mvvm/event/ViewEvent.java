package com.pp.mvvm.event;

import androidx.annotation.NonNull;

public class ViewEvent<T> {
    private DataOwner<T> mDataOwner;
    private String mType;

    public ViewEvent() {
        this(null);
    }

    public ViewEvent(String type) {
        this(null, type);
    }

    public ViewEvent(DataOwner<T> owner, String type) {
        this.mDataOwner = owner;
        this.mType = type;
    }

    public DataOwner<T> getDataOwner() {
        return mDataOwner;
    }

    public void setDataOwner(DataOwner<T> owner) {
        this.mDataOwner = owner;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        this.mType = type;
    }

    public static class DataOwner<data> {
        private data data;
        private DataOwner subData;

        public DataOwner(data data) {
            this.data = data;
        }

        public data getData() {
            return data;
        }

        public void setData(data data) {
            this.data = data;
        }

        public DataOwner getSubData() {
            return subData;
        }

        public void setSubData(DataOwner subData) {
            this.subData = subData;
        }
    }
}
