package com.pp.media.databinding;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pp.media.R;


public class BindingAdapter extends BaseObservable {

    @androidx.databinding.BindingAdapter("android:bindText")
    public static void setText(TextView view, String content) {
        view.setText(content);
    }


    @androidx.databinding.BindingAdapter("android:bindSrc")
    public static void setSrc(ImageView imageView, String src) {
        RequestOptions options = new RequestOptions()
//                .placeholder(R.drawable.ic_placeholder)//设置占位图
                .error(R.drawable.ic_placeholder_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(imageView)
                .load(src)
//                .thumbnail(0.1f)
                .apply(options)
                .into(imageView);
    }
}
