package com.pp.media.ui;

import android.Manifest;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;

import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.event.MediaEvent;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.List;

public class MediaShareViewModel extends LifecycleViewModel {
    public final MutableLiveData<MediaEvent> mSender = new MutableLiveData<>();
    private MediaRepository mRepository;

    public MediaShareViewModel(@NonNull Application application) {
        super(application);
        mRepository = MediaRepository.MediaRepositoryFactory.create();
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void loadData() {
//        Log.e("TAG", "load data");
        mRepository.init(getApplication());
    }

    public MediaRepository getMediaRepository() {
        return mRepository;
    }

    public static MediaShareViewModel get(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(MediaShareViewModel.class);
    }
}
