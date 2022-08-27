package com.pp.mvvm.databinding;

import androidx.annotation.LayoutRes;
import androidx.lifecycle.AndroidViewModel;


public interface IResource<VM extends AndroidViewModel> {

    /**
     * 获取viweModel class对象
     *
     * @return
     */
    Class<VM> getModelClass();

    /**
     * 获取布局资源
     *
     * @return
     */
    @LayoutRes
    int getLayoutRes();

}
