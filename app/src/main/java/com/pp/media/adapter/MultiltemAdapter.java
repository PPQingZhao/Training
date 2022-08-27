package com.pp.media.adapter;

import android.view.View;

import androidx.annotation.CallSuper;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pp.media.BR;

import java.util.List;

public class MultiltemAdapter<T extends BaseAbstractExpandleItem> extends BaseMultiItemQuickAdapter<T, MultiltemAdapter.ViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MultiltemAdapter(List<T> data) {
        super(data);
    }

    @Override
    public void addItemType(int type, int layoutResId) {
        super.addItemType(type, layoutResId);
    }

    @CallSuper
    @Override
    protected void convert(ViewHolder helper, T item) {
        if (null == helper.mBind) {
            return;
        }
        helper.mBind.setVariable(BR.viewModel, item);
    }

    public static class ViewHolder extends BaseViewHolder {

        final ViewDataBinding mBind;

        public ViewHolder(View view) {
            super(view);
            mBind = DataBindingUtil.bind(view);
        }
    }
}
