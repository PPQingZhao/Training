package com.pp.media.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.pp.media.repository.MediaRepository;
import com.pp.mvvm.base.LifecycleViewModel;

public class MainViewModel extends LifecycleViewModel {
    public final ObservableField<String> titleFiled = new ObservableField<>("title");

    public MainViewModel(@NonNull Application application) {
        super(application);
        MediaRepository.MediaRepositoryFactory.create().init(application);
    }


}
