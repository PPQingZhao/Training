package com.pp.mvvm.databinding;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pp.mvvm.base.LifecycleViewModel;

public interface BindingHelperBulders {

    class ActivityBindingHelperBuilder extends InflateBindingHelperBuilder<ActivityBindingHelperBuilder> {

        private final FragmentActivity mActivity;

        public ActivityBindingHelperBuilder(@NonNull FragmentActivity activity) {
            super(activity.getApplication());
            this.mActivity = activity;
        }

        @Override
        public <VDB extends ViewDataBinding, VM extends LifecycleViewModel> DataBindingHelper<VDB, VM> build(@NonNull Class<VM> modelClass) {
            return new DataBindingHelper<VDB, VM>()
                    .setContentView(mActivity, mLayoutId)
                    .createViewModel(mActivity, getFactory(), modelClass);
        }
    }

    class FragmentBindingHelperBuilder
            extends InflateBindingHelperBuilder<FragmentBindingHelperBuilder> {

        private final Fragment mFragment;

        private @DataBindingHelper.ViewModelHost
        int mModelHost;

        public FragmentBindingHelperBuilder(@NonNull Fragment fragment) {
            super(fragment.getActivity().getApplication());
            this.mFragment = fragment;
        }

        public FragmentBindingHelperBuilder setModelHost(@DataBindingHelper.ViewModelHost int host) {
            this.mModelHost = host;
            return this;
        }

        @Override
        public <VDB extends ViewDataBinding, VM extends LifecycleViewModel> DataBindingHelper<VDB, VM> build(@NonNull Class<VM> modelClass) {
            return new DataBindingHelper<VDB, VM>()
                    .inflate(mInflate, mLayoutId, mParent, mAttachParent)
                    .createViewModel(mFragment, getFactory(), modelClass, mModelHost);
        }
    }


    abstract class InflateBindingHelperBuilder<builder extends InflateBindingHelperBuilder<builder>>
            extends DataBindingHelper.Builder<builder> {

        protected LayoutInflater mInflate;
        protected ViewGroup mParent;
        protected boolean mAttachParent;

        public InflateBindingHelperBuilder(Application applicaton) {
            super(applicaton);
        }

        public builder setInflate(LayoutInflater inflate) {
            this.mInflate = inflate;
            return this.self();
        }

        public builder setParent(ViewGroup parent) {
            this.mParent = parent;
            return this.self();
        }

        public builder setAttachParent(boolean attachParent) {
            this.mAttachParent = attachParent;
            return this.self();
        }
    }
}
