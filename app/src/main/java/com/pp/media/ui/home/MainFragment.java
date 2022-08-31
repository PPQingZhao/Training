package com.pp.media.ui.home;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pp.media.R;
import com.pp.media.base.BaseFragment;
import com.pp.media.databinding.MainFragmentBinding;
import com.pp.media.ui.media.ImageBucketFragment;
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

    public static MainFragment newInstance(){
        return new MainFragment();
    }


    public static void injectInto(@NonNull FragmentActivity activity, @IdRes int container) {
        FragmentUtil.addFragment(
                    activity,
                    container,
                            new FragmentUtil.Adapter<MainFragment>() {
                        @NonNull
                        @Override
                        public MainFragment onCreateFragment(Fragment fragmentByTag) {

                            if (null != fragmentByTag
                                    && fragmentByTag.getClass().isAssignableFrom(ImageBucketFragment.class)
                                    && ImageBucketFragment.class.isAssignableFrom(fragmentByTag.getClass())) {
                                return (MainFragment) fragmentByTag;
                            } else {
                                return new MainFragment();
                            }
                        }

                        @Override
                        public String getFragmentTag() {
                        return String.valueOf(R.string.title_home);
                    }
                });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
