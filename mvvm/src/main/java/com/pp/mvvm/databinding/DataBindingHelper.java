package com.pp.mvvm.databinding;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.pp.mvvm.base.LifecycleViewModel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DataBindingHelper<VDB extends ViewDataBinding, VM extends LifecycleViewModel> {

    /**
     * framgent中viewModel的生命周期跟随 activity
     */
    public static final int VIEWMODEL_HOST_ACTIVITY = 0;

    /**
     * fragment中vewModel的生命周期跟随 fragment
     */
    public static final int VIEWMODEL_HOST_FRAGMENT = 1;

    @IntDef({VIEWMODEL_HOST_ACTIVITY, VIEWMODEL_HOST_FRAGMENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ViewModelHost {
    }

    protected VDB mDataBinding;
    protected VM mViewModel;

    private DataBindingHelper() {
    }

    public VDB getDataBinding() {
        return mDataBinding;
    }

    public VM getViewModel() {
        return mViewModel;
    }

    protected DataBindingHelper<VDB, VM> setContentView(@NonNull FragmentActivity activity, @LayoutRes int layoutId) {
        mDataBinding = DataBindingUtil.setContentView(activity, layoutId);
        return this;
    }

    public static <VDB extends ViewDataBinding, VM extends LifecycleViewModel> DataBindingHelper<VDB, VM> get(@NonNull FragmentActivity activity, @NonNull Adapter<VDB, VM> adapter) {

        Application application = activity.getApplication();
        int layoutRes = adapter.getLayoutRes();
        ViewModelProvider.Factory factory = adapter.getFactory(application);
        Class<VM> viewModelClazz = adapter.getViewModelClazz();

        VDB binding = DataBindingUtil.setContentView(activity, layoutRes);
        VM vieweModel = ViewModelProviders.of(activity, factory).get(viewModelClazz);

        DataBindingHelper<VDB, VM> helper = new DataBindingHelper<>();

        helper.mDataBinding = binding;
        helper.mViewModel = vieweModel;

        helper.mDataBinding.setLifecycleOwner(activity);

        return helper;
    }

    public static <VDB extends ViewDataBinding, VM extends LifecycleViewModel> DataBindingHelper<VDB, VM> get(@NonNull Fragment fragment, @NonNull FragmentAdapter<VDB, VM> adapter) {

        int modelHost = adapter.getModelHost();
        if (VIEWMODEL_HOST_ACTIVITY == modelHost) {
            return get(fragment.getActivity(), adapter);
        }

        Application application = fragment.getActivity().getApplication();
        int layoutRes = adapter.getLayoutRes();
        ViewModelProvider.Factory factory = adapter.getFactory(application);
        Class<VM> viewModelClazz = adapter.getViewModelClazz();
        LayoutInflater inflater = adapter.getInflater();

        VDB binding = DataBindingUtil.inflate(inflater, layoutRes, null, false);

        VM vieweModel = ViewModelProviders.of(fragment, factory).get(viewModelClazz);

        DataBindingHelper<VDB, VM> helper = new DataBindingHelper<>();

        helper.mDataBinding = binding;
        helper.mViewModel = vieweModel;

        helper.mDataBinding.setLifecycleOwner(fragment);

        return helper;
    }

    public static <VDB extends ViewDataBinding, VM extends LifecycleViewModel> DataBindingHelper<VDB, VM> get(@NonNull Application application,
                                                                                                              @NonNull ViewModelStoreOwner owner,
                                                                                                              @NonNull InflateAdapter<VDB, VM> adapter) {
        ViewModelProvider.Factory factory = adapter.getFactory(application);
        int layoutRes = adapter.getLayoutRes();
        Class<VM> viewModelClazz = adapter.getViewModelClazz();
        LayoutInflater inflater = adapter.getInflater();

        VDB binding = DataBindingUtil.inflate(inflater, layoutRes, adapter.getParent(), adapter.attachToParrent());

        VM vieweModel = new ViewModelProvider(owner, factory).get(viewModelClazz);

        DataBindingHelper<VDB, VM> helper = new DataBindingHelper<>();

        helper.mDataBinding = binding;
        helper.mViewModel = vieweModel;
        return helper;
    }

    public static abstract class FragmentAdapter<VDB extends ViewDataBinding, VM extends LifecycleViewModel> extends InflateAdapter<VDB, VM> {
        /**
         * @return 1. DataBindingHelper.VIEWMODEL_HOST_ACTIVITY VM缓存在activity  2. DataBindingHelper.VIEWMODEL_HOST_FRAGMEBT VM缓存在fragment
         */
        public @ViewModelHost
        int getModelHost() {
            return DataBindingHelper.VIEWMODEL_HOST_ACTIVITY;
        }


    }

    public static abstract class InflateAdapter<VDB extends ViewDataBinding, VM extends LifecycleViewModel> extends Adapter<VDB, VM> {

        public abstract @NonNull
        LayoutInflater getInflater();

        public boolean attachToParrent() {
            return false;
        }

        public ViewGroup getParent() {
            return null;
        }
    }

    public static abstract class Adapter<VDB extends ViewDataBinding, VM extends LifecycleViewModel> {
        /**
         * 获取布局资源
         *
         * @return
         */
        public abstract @LayoutRes
        int getLayoutRes();

        /**
         * 获取VM calss
         *
         * @return
         */
        public abstract @NonNull
        Class<VM> getViewModelClazz();


        /**
         * 获取VM 创建工厂
         *
         * @param application
         * @return
         */
        public @NonNull
        ViewModelProvider.Factory getFactory(@NonNull Application application) {
            return ViewModelProvider.AndroidViewModelFactory.getInstance(application);
        }
    }
}
