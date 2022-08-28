package com.pp.media.ui.media.callback;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableList;

import com.pp.media.repository.bean.Media;
import com.pp.media.ui.media.model.MediaItemViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OnMediaListChangeCallBack extends ObservableList.OnListChangedCallback<ObservableList<Media>> {
    private static final String TAG = "OnListChangeCallBack";

    final ObservableList<MediaItemViewModel> mMediaList;

    public OnMediaListChangeCallBack(@NonNull ObservableList<MediaItemViewModel> list) {
        this.mMediaList = list;
    }

    @Override
    public void onChanged(ObservableList<Media> list) {
        mMediaList.clear();
        MediaItemViewModel itemViewModel;
        for (Media m : list) {
            itemViewModel = new MediaItemViewModel();
            itemViewModel.setMedia(m);
            mMediaList.add(itemViewModel);
        }

    }

    @Override
    public void onItemRangeChanged(ObservableList<Media> sender, int positionStart, int itemCount) {
    }

    @Override
    public void onItemRangeInserted(ObservableList<Media> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeInserted size:  " + sender.size() + "   start: " + positionStart + "   count: " + itemCount);
        Media media;
        MediaItemViewModel itemViewModel;
        List<MediaItemViewModel> tempList = new ArrayList<>();
        for (int i = positionStart; i < sender.size(); i++) {
            media = sender.get(i);

            itemViewModel = new MediaItemViewModel();
            itemViewModel.setMedia(media);
            tempList.add(i, itemViewModel);
        }

        mMediaList.addAll(tempList);
    }

    @Override
    public void onItemRangeMoved(ObservableList<Media> sender, int fromPosition, int toPosition, int itemCount) {

    }

    @Override
    public void onItemRangeRemoved(ObservableList<Media> sender, int positionStart, int itemCount) {
//        Log.e(TAG, "onItemRangeRemoved");
        for (int i = positionStart; i < itemCount; i++) {
            mMediaList.remove(i);
        }
    }
}
