package com.pp.media.ui.home;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pp.media.R;
import com.pp.media.databinding.MainFragmentBinding;
import com.pp.media.util.FragmentUtil;
import com.pp.mvvm.base.LifecycleFragment;

public class MainFragment extends LifecycleFragment<MainFragmentBinding, HomeFragmentViewModel> {

    @Override
    public Class<HomeFragmentViewModel> getModelClass() {
        return HomeFragmentViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }

    public static void injectInto(@NonNull FragmentActivity activity, @IdRes int container) {
        FragmentUtil.addToActivity(
                activity,
                getInstance(activity),
                getFragmentTag(),
                container);
    }

    public static MainFragment getInstance(@NonNull FragmentActivity activity) {
        Fragment fragment = activity.getSupportFragmentManager()
                .findFragmentByTag(getFragmentTag());
        if (!(fragment instanceof MainFragment)) {
            fragment = new MainFragment();
        }
        return (MainFragment) fragment;
    }

    private static String getFragmentTag() {
        return String.valueOf(R.string.title_home);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
