package com.pp.media.ui;

import android.app.Application;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.pp.media.ui.media.MediaActivity;
import com.pp.mvvm.base.LifecycleViewModel;
import com.pp.mvvm.event.ViewEvent;
import com.pp.mvvm.event.ViewEventHandler;

public class MainFragmentViewModel extends LifecycleViewModel {
    public final ObservableField<String> contentFiled = new ObservableField<>("Main Fragmennt");

    public MainFragmentViewModel(@NonNull Application application) {
        super(application);
    }


    public void onMediaClick(View view) {

        Intent intent = new Intent(getApplication(), MediaActivity.class);
        ViewEvent<Intent> event = new ViewEvent<Intent>(new ViewEvent.DataOwner<Intent>(intent), ViewEventHandler.UI_EVENT_STARTACTIVITY);
        setEvent(event);
    }
}
