package com.pp.media.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.pp.media.R;
import com.pp.media.base.BaseActivity;
import com.pp.media.databinding.MainDataBing;
import com.pp.media.ui.home.MainFragment;


public class MainActivity extends BaseActivity<MainDataBing, MainViewModel> {


    @Override
    public Class<MainViewModel> getModelClass() {
        return MainViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    final String tagMain = String.valueOf(R.string.title_home);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainFragment mainFragment = MainFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_fl_content, mainFragment, tagMain)
                .show(mainFragment)
                .commit();

    }
}
