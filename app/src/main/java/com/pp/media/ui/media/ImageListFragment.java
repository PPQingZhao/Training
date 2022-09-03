package com.pp.media.ui.media;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pp.media.MediaApplication;
import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.base.BaseFragment;
import com.pp.media.callback.OnItemListChangedCallback;
import com.pp.media.databinding.ImageListDataBinding;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.ui.media.event.MediaEvent;
import com.pp.media.ui.media.model.ImageListItemViewModel;
import com.pp.media.ui.media.model.ImagePageItemViewModel;
import com.pp.media.ui.media.model.ImagePageListItemViewModel;
import com.pp.media.util.FragmentUtil;
import com.pp.media.widget.imagepage.ImagePageView;
import com.pp.media.widget.window.PhotoWindow;

import java.util.List;

import static com.chad.library.adapter.base.BaseQuickAdapter.ALPHAIN;


public class ImageListFragment extends BaseFragment<ImageListDataBinding, ImageListViewModel> {

    private static final String TAG = "ImageListFragment";
    private MultiltemAdapter<ImageListItemViewModel> mAdapter;
    private PhotoWindow mPhotoWindow;
    private ImagePageView mImagePageView;

    @Override
    public Class<ImageListViewModel> getModelClass() {
        return ImageListViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_imagelist;
    }

    static FragmentUtil.Adapter<ImageListFragment> sAapter;

    public static FragmentUtil.Adapter<ImageListFragment> getAdapter() {
        if (null == sAapter) {

            sAapter = new FragmentUtil.Adapter<ImageListFragment>() {
                String fragmentTag = FragmentUtil.getFragmentTag(MediaApplication.getInstance(), R.string.title_imagelist);

                @NonNull
                @Override
                public ImageListFragment onCreateFragment(Fragment fragmentByTag) {
                    if (FragmentUtil.isCreateBy(fragmentByTag, ImageListFragment.class)) {
//                        Log.e(TAG, "1111111111111");
                        return (ImageListFragment) fragmentByTag;
                    } else {
//                        Log.e(TAG, "22222222222222");
                        return new ImageListFragment();
                    }
                }

                @Override
                public String getFragmentTag() {
                    return fragmentTag;
                }
            };
        }
        return sAapter;

    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBindingHelper.getDataBinding().setClickHandler(this);

        // init  recyclervew
        RecyclerView recyclerview = mBindingHelper.getDataBinding().mediaRecyclerview;
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 4));

        mAdapter = new MultiltemAdapter<ImageListItemViewModel>(null);
        mAdapter.addItemType(ImageListItemViewModel.ITEMTYPE, R.layout.item_image_list);
        recyclerview.setAdapter(mAdapter);
        mAdapter.openLoadAnimation(ALPHAIN);
        mAdapter.isFirstOnly(false);

        mBindingHelper.getViewModel().addOnItemChangedCallback(this, new OnItemListChangedCallback<ImageListItemViewModel>(mAdapter));

        MediaShareViewModel mediaShareViewModel = getShareViewModel();

    }

    private MediaShareViewModel getShareViewModel() {

        MediaShareViewModel shareViewModel = MediaShareViewModel.get(getActivity());
        shareViewModel.mSender.observe(this, new Observer<MediaEvent>() {
            @Override
            public void onChanged(MediaEvent mediaEvent) {
//                Log.e(TAG, mediaEvent.getType());
                switch (mediaEvent.getType()) {
                    case MediaEvent.ACTION_SEND_IMAGEBUCKET_FOR_IMAGLIST:
                        ImageBucket imageBucket = (ImageBucket) mediaEvent.getDataOwner().getData();
                        mBindingHelper.getViewModel().setData(imageBucket, mAdapter);
                        return;
                    case MediaEvent.ACTION_ON_IMAGELIST_ITME_CLICK:
                        Image image = (Image) mediaEvent.getDataOwner().getData();
                        showImageDetailWindow(image);
                        return;
                    default:
                        break;
                }
            }

        });

        mBindingHelper.getViewModel().setItemSender(shareViewModel.mSender);

        return shareViewModel;
    }

    /**
     * 展示ImageDetail window
     *
     * @param image 窗口当前展示的image
     */
    private void showImageDetailWindow(Image image) {
        if (null == mPhotoWindow) {
            mPhotoWindow = new PhotoWindow(getActivity());
        }

        if (null == mImagePageView) {
            mImagePageView = new ImagePageView(getActivity().getApplication(), getContext(), this);
            mImagePageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            mImagePageView.addImagedItemType(ImagePageItemViewModel.ITEM_TYPE, R.layout.item_imagepage);
            mImagePageView.addImageListItemType(ImagePageListItemViewModel.ITEM_TYPE, R.layout.item_imagepagelist);

            List<ImagePageItemViewModel> imagePageData = mBindingHelper.getViewModel().getImagePageData();
            mImagePageView.setData(imagePageData);

            List<ImagePageListItemViewModel> imagePageListData= mBindingHelper.getViewModel().getImagePageListData();
            mImagePageView.setImageListData(imagePageListData);
        }

        int showPosition = mBindingHelper.getViewModel().getImagePos(image);
        mImagePageView.scrollToPos(showPosition);

        mPhotoWindow.show(mImagePageView);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean handleBackPressed() {
        if (null != mPhotoWindow && mPhotoWindow.isShowing()) {
            mPhotoWindow.dismiss();
            return true;
        }

        MediaShareViewModel.get(getActivity()).mSender.setValue(MediaEvent.newEvent(MediaEvent.ACTION_ON_IMAGELIST_BACKPRESSED));

        return true;
    }

    public void onBackPressed(View view) {
        handleBackPressed();
    }
}
