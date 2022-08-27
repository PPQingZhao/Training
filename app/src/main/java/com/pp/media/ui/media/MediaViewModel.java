package com.pp.media.ui.media;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.callback.OnMediaListChangeCallBack;
import com.pp.media.ui.media.model.MediaItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;
import com.pp.mvvm.event.ViewEvent;
import com.pp.mvvm.event.ViewEventHandler;

public class MediaViewModel extends LifecycleViewModel {

    public final ObservableField<String> title = new ObservableField<>();
    private ObservableList.OnListChangedCallback<ObservableList<MediaItemViewModel>> mMediaListChangeCallback;

    public MediaViewModel(@NonNull Application application) {
        super(application);
        title.set("Media");
    }

    public void onBack(View v) {
        setEvent(new ViewEvent(ViewEventHandler.UI_EVENT_FINISHACTIVITY));
    }

    private final ObservableList<MediaItemViewModel> mMediaList = new ObservableArrayList<>();

    public void addOnMediaChangCallback(ObservableList.OnListChangedCallback<ObservableList<MediaItemViewModel>> callback) {
        this.mMediaListChangeCallback = callback;
        mMediaList.addOnListChangedCallback(callback);
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public ObservableList<MediaItemViewModel> loadMedia(Context ctx, MediaRepository repository) {
        repository.addOnMediaListChangeCallBack((new OnMediaListChangeCallBack(mMediaList)));
        if (mMediaList.size() == 0) {
            repository.load(ctx);
        }
        return mMediaList;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
//        Log.e("TAG","remove callback 22222");
        mMediaList.removeOnListChangedCallback(mMediaListChangeCallback);
    }

}
