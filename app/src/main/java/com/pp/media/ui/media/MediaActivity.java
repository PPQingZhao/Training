package com.pp.media.ui.media;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.pp.media.R;
import com.pp.media.databinding.MediaDataBinding;
import com.pp.mvvm.base.LifecycleActivity;

public class MediaActivity extends LifecycleActivity<MediaDataBinding, MediaViewModel> {
    @Override
    public Class<MediaViewModel> getModelClass() {
        return MediaViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_media;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.media_fl_content, new MediaListFragment())
                .commit();
    }
}
