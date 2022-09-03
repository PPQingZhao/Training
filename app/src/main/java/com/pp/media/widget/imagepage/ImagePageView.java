package com.pp.media.widget.imagepage;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.pp.media.R;
import com.pp.media.adapter.BaseAbstractExpandleItem;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.databinding.ImagePageBinding;
import com.pp.mvvm.base.LifecycleViewModel;
import com.pp.mvvm.databinding.DataBindingHelper;

import java.util.List;

public class ImagePageView<ImageData extends BaseAbstractExpandleItem,ImageListData extends BaseAbstractExpandleItem> extends FrameLayout {

    private static final String TAG = "ImagePageView";
    private DataBindingHelper<ImagePageBinding, ImagepPageViewModel> mBindingHelper;
    private MultiltemAdapter<ImageData> mAdapter;
    private MultiltemAdapter<ImageListData> mImageListAdapter;

    public ImagePageView(Application application, Context context, ViewModelStoreOwner owner) {
        super(context);
        mBindingHelper = DataBindingHelper.get(application, owner, getInflateAdapter());
        initView();
    }

    DataBindingHelper.InflateAdapter<ImagePageBinding, ImagepPageViewModel> getInflateAdapter() {
        return new DataBindingHelper.InflateAdapter<ImagePageBinding, ImagepPageViewModel>() {
            @NonNull
            @Override
            public LayoutInflater getInflater() {
                return LayoutInflater.from(getContext());
            }

            @Override
            public boolean attachToParrent() {
                return true;
            }

            @Override
            public ViewGroup getParent() {
                return ImagePageView.this;
            }

            @Override
            public int getLayoutRes() {
                return R.layout.view_imagepage;
            }

            @NonNull
            @Override
            public Class<ImagepPageViewModel> getViewModelClazz() {
                return ImagepPageViewModel.class;
            }
        };
    }

    public void setData(List<ImageData> data) {
        mAdapter.setNewData(data);
    }

    public void setImageListData(List<ImageListData> data) {
        mImageListAdapter.setNewData(data);
    }

    public void addImagedItemType(int type, @LayoutRes int layoutRes) {
        mAdapter.addItemType(type, layoutRes);
    }

    public void addImageListItemType(int type, @LayoutRes int layoutRes) {
        mImageListAdapter.addItemType(type, layoutRes);
    }

    private void initView() {
        RecyclerView rvImagepage = mBindingHelper.getDataBinding().rvImagepage;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, RecyclerView.VERTICAL, false);
        rvImagepage.setLayoutManager(gridLayoutManager);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rvImagepage);

        mAdapter = new MultiltemAdapter<>(null);
        rvImagepage.setAdapter(mAdapter);


        // image list
        RecyclerView rvImagepageList = mBindingHelper.getDataBinding().rvImagepageList;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvImagepageList.setLayoutManager(linearLayoutManager);

        mImageListAdapter = new MultiltemAdapter<>(null);
        rvImagepageList.setAdapter(mImageListAdapter);


    }

    public void scrollToPos(int showPosition) {
        Log.e(TAG, "position: " + showPosition);
        mBindingHelper.getDataBinding().rvImagepage.scrollToPosition(showPosition);
        mBindingHelper.getDataBinding().rvImagepageList.scrollToPosition(showPosition);
    }


    public static class ImagepPageViewModel extends LifecycleViewModel {

        public ImagepPageViewModel(@NonNull Application application) {
            super(application);
        }
    }

}
