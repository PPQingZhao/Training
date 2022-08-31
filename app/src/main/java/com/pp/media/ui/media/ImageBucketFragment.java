package com.pp.media.ui.media;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.adapter.OnItemListChangedCallback;
import com.pp.media.base.BaseFragment;
import com.pp.media.databinding.ImageBucketBinding;
import com.pp.media.repository.MediaRepository;
import com.pp.media.rxjava.DisposableLifecycleObserver;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.media.util.FragmentUtil;
import com.pp.mvvm.base.LifecycleFragment;


public class ImageBucketFragment extends BaseFragment<ImageBucketBinding, ImageBucketViewModel> {

    private MultiltemAdapter<ImageBucketItemViewModel> mAdapter;
    private DisposableLifecycleObserver mDisposableLifecycleObserver;

    @Override
    public Class<ImageBucketViewModel> getModelClass() {
        return ImageBucketViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_imagebucket;
    }

    @Override
    public boolean handleBackPressed() {
        getActivity().finish();
        return true;
    }

    public static ImageBucketFragment newInstance() {
        return new ImageBucketFragment();
    }

    public static void injectInto(@NonNull FragmentActivity activity, @IdRes int container) {
        FragmentUtil.addFragment(
                activity,
                container,
                new FragmentUtil.Adapter<ImageBucketFragment>() {
                    @NonNull
                    @Override
                    public ImageBucketFragment onCreateFragment(Fragment fragmentByTag) {
                        if (null != fragmentByTag
                                && fragmentByTag.getClass().isAssignableFrom(ImageBucketFragment.class)
                                && ImageBucketFragment.class.isAssignableFrom(fragmentByTag.getClass())) {
                            return (ImageBucketFragment) fragmentByTag;
                        } else {
                            return new ImageBucketFragment();
                        }
                    }

                    @Override
                    public String getFragmentTag() {
                        return String.valueOf(R.string.title_imagebucket);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingHelper.getDataBinding().setClickHandler(this);

        mDisposableLifecycleObserver = DisposableLifecycleObserver.newInstance();
        getLifecycle().addObserver(mDisposableLifecycleObserver);

        MediaViewModel mediaViewModel = ViewModelProviders.of(getActivity()).get(MediaViewModel.class);
        MediaRepository repository = mediaViewModel.getRepository();
        getLifecycle().addObserver(repository);

        // init  recyclervew
        RecyclerView recyclerview = mBindingHelper.getDataBinding().bucketRecyclerview;
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new MultiltemAdapter<ImageBucketItemViewModel>(null);
        mAdapter.addItemType(ImageBucketItemViewModel.ITEMTYPE, R.layout.item_imagebucket);
        recyclerview.setAdapter(mAdapter);

        mBindingHelper.getViewModel().setData(mAdapter, repository.getImages());

        mBindingHelper.getViewModel().addOnItemChangedCallback(this, new OnItemListChangedCallback<>(mAdapter));
        mBindingHelper.getViewModel().addOnImageBucketChangedCallback(repository, this);

    }

    public void onBackPressed(View view) {
        Log.e("TAG", "onBackPressed: " + getClass().getName());
        handleBackPressed();
    }

}
