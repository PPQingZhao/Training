package com.pp.media.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pp.media.MediaApplication;
import com.pp.media.R;
import com.pp.media.base.BaseFragment;
import com.pp.media.databinding.MainFragmentBinding;
import com.pp.media.util.FragmentUtil;

public class MainFragment extends BaseFragment<MainFragmentBinding, HomeFragmentViewModel> {

    @Override
    public Class<HomeFragmentViewModel> getModelClass() {
        return HomeFragmentViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }


    static FragmentUtil.Adapter<MainFragment> sAapter;

    public static FragmentUtil.Adapter<MainFragment> getAdapter() {
        if (null == sAapter) {

            sAapter = new FragmentUtil.Adapter<MainFragment>() {
                String fragmentTag = FragmentUtil.getFragmentTag(MediaApplication.getInstance(), R.string.title_home);

                @NonNull
                @Override
                public MainFragment onCreateFragment(Fragment fragmentByTag) {
                    if (FragmentUtil.isCreateBy(fragmentByTag, MainFragment.class)) {
                        return (MainFragment) fragmentByTag;
                    } else {
                        return new MainFragment();
                    }
                }

                @Override
                public String getFragmentTag() {
                    return fragmentTag;
                }
            };
        }
        return sAapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
