package com.pp.media.ui.media.callback;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.pp.media.media.ImageBucket;
import com.pp.media.repository.bean.Media;
import com.pp.media.ui.media.model.ImageBucketItemViewModel;
import com.pp.media.ui.media.model.MediaItemViewModel;

import java.util.ArrayList;
import java.util.List;

public class OnImageBucketChangeCallBack extends ObservableList.OnListChangedCallback<ObservableList<ImageBucket>> {
    private static final String TAG = "OnListChangeCallBack";

    final ObservableList<ImageBucketItemViewModel> mMediaList;

    public OnImageBucketChangeCallBack(@NonNull ObservableList<ImageBucketItemViewModel> list) {
        this.mMediaList = list;
    }

    @Override
    public void onChanged(ObservableList<ImageBucket> list) {
        mMediaList.clear();
        ImageBucketItemViewModel itemViewModel;
        for (ImageBucket m : list) {
            itemViewModel = new ImageBucketItemViewModel();
            itemViewModel.setImageBucket(m);
            mMediaList.add(itemViewModel);
        }

    }

    @Override
    public void onItemRangeChanged(ObservableList<ImageBucket> sender, int positionStart, int itemCount) {
    }

    @Override
    public void onItemRangeInserted(ObservableList<ImageBucket> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeInserted size:  " + sender.size() + "   start: " + positionStart + "   count: " + itemCount);
        ImageBucket bucket;
        ImageBucketItemViewModel itemViewModel;
        List<ImageBucketItemViewModel> tempList = new ArrayList<>();
        for (int i = positionStart; i < sender.size(); i++) {
            bucket = sender.get(i);

            itemViewModel = new ImageBucketItemViewModel();
            itemViewModel.setImageBucket(bucket);
            tempList.add(i, itemViewModel);
        }

        mMediaList.addAll(tempList);
    }

    @Override
    public void onItemRangeMoved(ObservableList<ImageBucket> sender, int fromPosition, int toPosition, int itemCount) {

    }

    @Override
    public void onItemRangeRemoved(ObservableList<ImageBucket> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeRemoved");
        for (int i = positionStart; i < itemCount; i++) {
            mMediaList.remove(i);
        }
    }

    public static abstract class Adapter<T>{

    }
}
