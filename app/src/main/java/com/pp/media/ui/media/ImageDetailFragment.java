package com.pp.media.ui.media;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.pp.media.MediaApplication;
import com.pp.media.R;
import com.pp.media.base.BaseFragment;
import com.pp.media.databinding.ImageDetailDataBinding;
import com.pp.media.media.Image;
import com.pp.media.ui.MediaShareViewModel;
import com.pp.media.ui.event.MediaEvent;
import com.pp.media.util.FragmentUtil;


public class ImageDetailFragment extends BaseFragment<ImageDetailDataBinding, ImageDetailViewModel> {

    private static final String TAG = "ImageDetailFragment";

    @Override
    public Class<ImageDetailViewModel> getModelClass() {
        return ImageDetailViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_imagedetail;
    }

    static FragmentUtil.Adapter<ImageDetailFragment> sAapter;

    public static FragmentUtil.Adapter<ImageDetailFragment> getAdapter() {
        if (null == sAapter) {

            sAapter = new FragmentUtil.Adapter<ImageDetailFragment>() {
                String fragmentTag = FragmentUtil.getFragmentTag(MediaApplication.getInstance(), R.string.title_imagedetail);

                @NonNull
                @Override
                public ImageDetailFragment onCreateFragment(Fragment fragmentByTag) {
                    if (FragmentUtil.isCreateBy(fragmentByTag, ImageDetailFragment.class)) {
                        return (ImageDetailFragment) fragmentByTag;
                    } else {
                        return new ImageDetailFragment();
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


        MediaShareViewModel mediaShareViewModel = getShareViewModel();
    }

    private MediaShareViewModel getShareViewModel() {

        MediaShareViewModel shareViewModel = MediaShareViewModel.get(getActivity());
        shareViewModel.mSender.observe(this, new Observer<MediaEvent>() {
            @Override
            public void onChanged(MediaEvent mediaEvent) {
//                Log.e(TAG, mediaEvent.getType());
                switch (mediaEvent.getType()) {
                    case MediaEvent.ACTION_SEND_IMAGE_FOR_IMAGDETAIL:
                        Image image = (Image) mediaEvent.getDataOwner().getData();

                        mBindingHelper.getViewModel().setData(image);
                        return;
                    default:
                        break;
                }
            }
        });

        return shareViewModel;
    }

    @Override
    public boolean handleBackPressed() {
        MediaShareViewModel.get(getActivity()).mSender.setValue(MediaEvent.newEvent(MediaEvent.ACTION_ON_IMAGEDETAIL_BACKPRESSED));

        return true;
    }

    public void onBackPressed(View view) {
        handleBackPressed();
    }
}
