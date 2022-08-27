package com.pp.mvvm.databinding;

import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


public class BindingAdapter extends BaseObservable {
    /**
     * 在 BR 中定义 viewModel 字段
     *
     * @return
     */
    @Bindable
    public int getViewModel() {
        return 0;
    }

}
