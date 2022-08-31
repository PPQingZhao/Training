package com.pp.media.ui.media;

import android.Manifest;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import com.pp.media.adapter.OnItemListChangedCallback;
import com.pp.media.callback.LifecycleCallbackHolder;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.List;

public class MediaViewModel extends LifecycleViewModel {
    private MediaRepository mRepository;

    public MediaViewModel(@NonNull Application application) {
        super(application);
        mRepository = MediaRepository.MediaRepositoryFactory.create();
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void loadMedia() {
       mRepository.init(getApplication());
    }

    public MediaRepository getRepository() {
        return mRepository;
    }

}
