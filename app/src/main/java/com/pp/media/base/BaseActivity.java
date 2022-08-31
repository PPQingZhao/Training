package com.pp.media.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.pp.media.callback.BackPressedHandler;
import com.pp.mvvm.base.LifecycleActivity;
import com.pp.mvvm.base.LifecycleViewModel;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity<DB extends ViewDataBinding, VM extends LifecycleViewModel> extends LifecycleActivity<DB, VM> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.e("TAG", "onCreate: " + getClass().getName());
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
//                Log.e("TAG", "onFragmentAttached: " + f.getClass().getName());
                if (f instanceof BackPressedHandler) {
                    addOnBackPressedListener((BackPressedHandler) f);
                }
            }

            @Override
            public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
//                Log.e("TAG", "onFragmentDetached: " + f.getClass().getName());
                if (f instanceof BackPressedHandler) {
                    removeOnBackPressedListeneer((BackPressedHandler) f);
                }
            }
        }, true);
    }

    public void addOnBackPressedListener(BackPressedHandler listener) {
        if (null == mOnBackPressedListeners) {
            mOnBackPressedListeners = new ArrayList<>();
        }
        if (null != listener && !mOnBackPressedListeners.contains(listener)) {
            mOnBackPressedListeners.add(listener);
        }
    }

    public void removeOnBackPressedListeneer(BackPressedHandler listener) {
        if (null != mOnBackPressedListeners) {
            mOnBackPressedListeners.remove(listener);
        }
    }

    @Override
    public void onBackPressed() {
        boolean result = dispatchBackPressed();
        if (result) {
            return;
        }
        super.onBackPressed();
    }

    private List<BackPressedHandler> mOnBackPressedListeners;

    private boolean dispatchBackPressed() {
        boolean result = false;
        if (null != mOnBackPressedListeners) {
            for (BackPressedHandler listener : mOnBackPressedListeners) {
                if (listener.isHandle()) {
                    if (result = listener.handleBackPressed()) {
                        break;
                    }
                }
            }
        }
        return result;
    }

}


