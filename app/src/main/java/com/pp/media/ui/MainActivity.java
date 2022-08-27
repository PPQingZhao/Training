package com.pp.media.ui;

import androidx.annotation.NonNull;

import com.pp.media.R;
import com.pp.mvvm.base.LifecycleActivity;
import com.pp.media.databinding.MainDataBing;


public class MainActivity extends LifecycleActivity<MainDataBing, MainViewModel> {


    @Override
    public Class<MainViewModel> getModelClass() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
