package com.pp.media.base;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.pp.media.callback.BackPressedHandler;
import com.pp.mvvm.base.LifecycleFragment;
import com.pp.mvvm.base.LifecycleViewModel;

public abstract class BaseFragment<DB extends ViewDataBinding, VM extends LifecycleViewModel> extends LifecycleFragment<DB, VM> implements BackPressedHandler {

    private static final String TAG = "BaseFragment";
    private boolean mHidden = true;

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
        boolean handle = isVisible()
                && !mHidden
                && getUserVisibleHint();
//        Log.e("TAG","可见： " +  !mHidden +  "    handle:  " + handle +   "    " + toString());
        return handle;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
        mHidden = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHidden = true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        mHidden = hidden;
//        Log.e("TAG", hidden + "    onHiddenChanged: " + toString());
    }

}
