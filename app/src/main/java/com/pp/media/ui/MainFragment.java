package com.pp.media.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.pp.media.R;
import com.pp.mvvm.base.LifecycleFragment;
import com.pp.media.databinding.MainFragmentBinding;
import com.pp.media.network.NetworkChange;

public class MainFragment extends LifecycleFragment<MainFragmentBinding, MainFragmentViewModel> {
    @Override
    public Class<MainFragmentViewModel> getModelClass() {
        return MainFragmentViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_main;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        NetworkChange networkChange = new NetworkChange(new NetworkChange.Factory());
        // 添加生命周期感应
        getLifecycle().addObserver(networkChange);

        // 开始网络监听
        networkChange.startObserve(getContext())
                .observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(String state) {
                        mBindingHelper.getViewModel().contentFiled.set(state);
                    }
                });


    }
}
