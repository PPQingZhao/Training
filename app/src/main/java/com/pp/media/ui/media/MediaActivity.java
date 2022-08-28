package com.pp.media.ui.media;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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


        // 添加 MedaListFragment
        MediaListFragment.injectInto(this, R.id.media_fl_content);

    }
}
