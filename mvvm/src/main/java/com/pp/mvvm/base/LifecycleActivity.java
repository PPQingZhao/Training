package com.pp.mvvm.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pp.mvvm.BR;
import com.pp.mvvm.databinding.DataBindingHelper;
import com.pp.mvvm.databinding.IResource;
import com.pp.mvvm.event.ViewEvent;
import com.pp.mvvm.event.ViewEventHandler;


public abstract class LifecycleActivity<DB extends ViewDataBinding, VM extends LifecycleViewModel>
        extends AppCompatActivity
        implements IResource<VM> {

    protected DataBindingHelper<DB, VM> mBindingHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingHelper = DataBindingHelper.get(this, new DataBindingHelper.Adapter<DB, VM>() {
            @Override
            public int getLayoutRes() {
                return LifecycleActivity.this.getLayoutRes();
            }

            @NonNull
            @Override
            public Class<VM> getViewModelClazz() {
                return getModelClass();
            }
        });

        init(mBindingHelper);
    }

    protected ViewModelProvider.Factory getFactory() {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
    }

    protected void init(DataBindingHelper<DB, VM> helper) {

        if (null == helper)
            return;

        //感应生命周期
        getLifecycle().addObserver(helper.getViewModel());

        // 为预定义的viewModel字段赋值  (在BindingAdapter 定义)
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
        // 处理事件
        return ViewEventHandler.handle(this, viewEvent);
    }


}
