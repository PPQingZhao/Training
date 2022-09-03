package com.pp.mvvm.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.pp.mvvm.BR;
import com.pp.mvvm.databinding.DataBindingHelper;
import com.pp.mvvm.databinding.IResource;
import com.pp.mvvm.event.ViewEvent;


public abstract class LifecycleFragment<DB extends ViewDataBinding, VM extends LifecycleViewModel>
        extends Fragment
        implements IResource<VM> {

    protected DataBindingHelper<DB, VM> mBindingHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingHelper = DataBindingHelper.get(this, new DataBindingHelper.FragmentAdapter<DB, VM>() {
            @NonNull
            @Override
            public LayoutInflater getInflater() {
                return getLayoutInflater();
            }

            @Override
            public int getLayoutRes() {
                return LifecycleFragment.this.getLayoutRes();
            }

            @NonNull
            @Override
            public Class<VM> getViewModelClazz() {
                return getModelClass();
            }

            @Override
            public int getModelHost() {
                return LifecycleFragment.this.getModelHost();
            }
        });

        init(mBindingHelper);

    }

    protected @DataBindingHelper.ViewModelHost
    int getModelHost() {
        return DataBindingHelper.VIEWMODEL_HOST_FRAGMENT;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = mBindingHelper.getDataBinding().getRoot();
        if (null != rootView.getParent()) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    protected void init(DataBindingHelper<DB, VM> helper) {

        if (null == helper)
            return;

        // viewModel感应生命周期
        getLifecycle().addObserver(helper.getViewModel());

        // 设置默认viewModel
        helper.getDataBinding().setVariable(BR.viewModel, helper.getViewModel());

        // 事件接收
        helper.getViewModel().getEventSender()
                .observe(this, new Observer<ViewEvent>() {
                    @Override
                    public void onChanged(ViewEvent viewEvent) {
                        dispatchViewEvent(viewEvent);
                    }
                });

    }

    protected boolean dispatchViewEvent(ViewEvent viewEvent) {
        if (getActivity() instanceof LifecycleActivity) {
            return ((LifecycleActivity) getActivity()).dispatchViewEvent(viewEvent);
        }
        return false;
    }

}
