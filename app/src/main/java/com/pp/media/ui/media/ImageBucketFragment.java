package com.pp.media.ui.media;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pp.media.MediaApplication;
import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.base.BaseFragment;
import com.pp.media.callback.OnItemListChangedCallback;
import com.pp.media.databinding.ImageBucketBinding;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.media.util.FragmentUtil;
import com.pp.mvvm.event.ViewEvent;


public class ImageBucketFragment extends BaseFragment<ImageBucketBinding, ImageBucketViewModel> {

    private static final String TAG = "ImageBucketFragment";

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

    static FragmentUtil.Adapter<ImageBucketFragment> sBucketFragmentAdapter;

    public static FragmentUtil.Adapter<ImageBucketFragment> getAdapter() {
        if (null == sBucketFragmentAdapter) {
            sBucketFragmentAdapter = new FragmentUtil.Adapter<ImageBucketFragment>() {
                String fragmentTag = FragmentUtil.getFragmentTag(MediaApplication.getInstance(), R.string.title_imagebucket);

                @NonNull
                @Override
                public ImageBucketFragment onCreateFragment(Fragment fragmentByTag) {
                    if (FragmentUtil.isCreateBy(fragmentByTag, ImageBucketFragment.class)) {
//                        Log.e("TAG", "111111");
                        return (ImageBucketFragment) fragmentByTag;
                    } else {
//                        Log.e("TAG", "222222");
                        return new ImageBucketFragment();
                    }
                }

                @Override
                public String getFragmentTag() {
                    return fragmentTag;
                }
            };
        }
        return sBucketFragmentAdapter;
    }

    private MultiltemAdapter<ImageBucketItemViewModel> mAdapter;

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBindingHelper.getDataBinding().setClickHandler(this);

        // init  recyclervew
        RecyclerView recyclerview = mBindingHelper.getDataBinding().bucketRecyclerview;
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter = new MultiltemAdapter<ImageBucketItemViewModel>(null);
        mAdapter.addItemType(ImageBucketItemViewModel.ITEMTYPE, R.layout.item_imagebucket);
        mAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        mAdapter.isFirstOnly(false);
        recyclerview.setAdapter(mAdapter);

        MediaShareViewModel shareViewModel = getShareViewModel();
        initData(shareViewModel);
    }

    private void initData(MediaShareViewModel shareViewModel) {
        MediaRepository repository = shareViewModel.getMediaRepository();

        mBindingHelper.getViewModel().setItemCallbackSender(shareViewModel.mSender);
        mBindingHelper.getViewModel().setData(mAdapter, repository.getImages());

        mBindingHelper.getViewModel().addOnItemChangedCallback(ImageBucketFragment.this, new OnItemListChangedCallback<>(mAdapter));
        mBindingHelper.getViewModel().addOnImageBucketChangedCallback(repository, ImageBucketFragment.this);
    }

    private MediaShareViewModel getShareViewModel() {

        MediaShareViewModel shareViewModel = MediaShareViewModel.get(getActivity());
        shareViewModel.mSender.observe(this, new Observer<ViewEvent>() {
            @Override
            public void onChanged(ViewEvent viewEvent) {
//                Log.e(TAG, viewEvent.getType());
            }
        });


        return shareViewModel;
    }

    public void onBackPressed(View view) {
//        Log.e("TAG", "onBackPressed: " + getClass().getName());
        handleBackPressed();
    }

}
