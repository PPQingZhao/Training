package com.pp.media.ui.media;

import android.app.Application;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TextAppearanceSpan;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;

import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.callback.OnBeanListChangedCallBack;
import com.pp.media.callback.OnListHoldCallbackListener;
import com.pp.media.callback.RecycleCallbackHolder;
import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.ui.event.MediaEvent;
import com.pp.media.ui.media.model.ImageListItemViewModel;
import com.pp.mvvm.base.LifecycleViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageDetailViewModel extends LifecycleViewModel {

    public final ObservableField<String> imagePath = new ObservableField<>();
    public final ObservableField<CharSequence> date = new ObservableField<>();
    public final ObservableField<CharSequence> hour = new ObservableField<>();
    private Image mImage;

    public ImageDetailViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
    }

    public void setData(Image image) {
        if (null != mImage && image.getId() == mImage.getId()) {
            return;
        }

        this.mImage = image;
        String thumbnailpath = image.getThumbnailpath();
        String path = image.getPath();
        imagePath.set(TextUtils.isEmpty(thumbnailpath) ? path : thumbnailpath);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm:ss");
        Date date = new Date(image.getAddDateTimeMill());
        String dateStr = dateFormat.format(date);
        String hourStr = hourFormat.format(date);

        this.date.set(dateStr);
        this.hour.set(hourStr);

    }
}
