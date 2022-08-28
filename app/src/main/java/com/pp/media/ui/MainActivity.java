package com.pp.media.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ReportFragment;

import com.pp.media.R;
import com.pp.media.ui.home.MainFragment;
import com.pp.media.util.FragmentUtil;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 添加 MainFragment
        MainFragment.injectInto(this, R.id.main_fl_content);
    }
}
