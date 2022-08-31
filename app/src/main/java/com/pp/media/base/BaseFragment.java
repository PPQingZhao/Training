package com.pp.media.base;


import android.util.Log;

import androidx.databinding.ViewDataBinding;

import com.pp.media.callback.BackPressedHandler;
import com.pp.mvvm.base.LifecycleFragment;
import com.pp.mvvm.base.LifecycleViewModel;

public abstract class BaseFragment<DB extends ViewDataBinding, VM extends LifecycleViewModel> extends LifecycleFragment<DB, VM> implements BackPressedHandler {

    /**
     * 处理返回按钮事件
     *
     * @return
     */
    @Override
    public boolean handleBackPressed() {
        return false;
    }

    /**
     * fragment 可见时希望处理 backPressed 事件
     *
     * @return
     */
    @Override
    public boolean isHandle() {
        Log.e("TAG", "isHandle(): " + getClass().getName());
        return isVisible() && getUserVisibleHint();
    }

}
