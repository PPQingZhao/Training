package com.pp.mvvm.databinding;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.IntDef;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

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

    DataBindingHelper() {
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

    /**
     * 创建ViewDataBinding
     *
     * @return
     */
    protected DataBindingHelper<VDB, VM> inflate(@NonNull LayoutInflater inflater,
                                                 int layoutId,
                                                 @Nullable ViewGroup parent,
                                                 boolean attachToParent) {

        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, parent, attachToParent);
        return this;
    }

    /**
     * 创建ViewModel对象
     *
     * @param activity
     * @param factory
     * @param modelClass
     * @return
     */
    protected DataBindingHelper<VDB, VM> createViewModel(@NonNull FragmentActivity activity,
                                                         ViewModelProvider.Factory factory,
                                                         @NonNull Class<VM> modelClass) {
        mViewModel = ViewModelProviders.of(activity, factory).get(modelClass);
        return this;
    }

    /**
     * 创建viewModel对象
     *
     * @param fragment
     * @param modelClass
     * @param modelHost  设置viewModel对象的生命周期
     * @return
     */
    protected DataBindingHelper<VDB, VM> createViewModel(@NonNull Fragment fragment,
                                                         @NonNull ViewModelProvider.Factory factory,
                                                         Class<VM> modelClass,
                                                         @ViewModelHost int modelHost) {
        if (modelHost == VIEWMODEL_HOST_ACTIVITY) {
            return createViewModel(fragment.getActivity(), factory, modelClass);
        }
        mViewModel = ViewModelProviders.of(fragment, factory).get(modelClass);
        return this;
    }

    public static abstract class Builder<builder extends Builder<builder>> {
        private Application mApplicaton;

        private ViewModelProvider.Factory mFactory;
        protected @LayoutRes
        int mLayoutId;

        public Builder(Application application) {
            this.mApplicaton = application;
        }

        protected builder self() {
            return (builder) this;
        }


        public builder setFactory(ViewModelProvider.Factory mFactory) {
            this.mFactory = mFactory;
            return this.self();
        }

        protected ViewModelProvider.Factory getFactory() {
            if (null == mFactory) {
                mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(mApplicaton);
            }
            return mFactory;
        }

        /**
         * 设置布局资源
         *
         * @param layoutId
         * @return
         */
        public builder setLayoutId(@LayoutRes int layoutId) {
            this.mLayoutId = layoutId;
            return this.self();
        }


        public abstract <VDB extends ViewDataBinding, VM extends LifecycleViewModel>

        DataBindingHelper<VDB, VM> build(@NonNull Class<VM> modelClass);

    }

}
