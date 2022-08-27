package com.pp.media.ui.media;


import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.ObservableList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pp.media.R;
import com.pp.media.adapter.MultiltemAdapter;
import com.pp.media.adapter.OnItemListChangedCallback;
import com.pp.media.databinding.MediaListDataBinding;
import com.pp.media.repository.MediaRepository;
import com.pp.media.ui.media.callback.FullOnListChangeCallBack;
import com.pp.media.ui.media.model.MediaItemViewModel;
import com.pp.mvvm.base.LifecycleFragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


class MediaListFragment extends LifecycleFragment<MediaListDataBinding, MediaViewModel> {

    private MultiltemAdapter<MediaItemViewModel> mAdapter;
    private static final int CODE_PERMISSION = 1;

    @Override
    public Class<MediaViewModel> getModelClass() {
        return MediaViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_medialist;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init  recyclervew
        RecyclerView recyclerview = mBindingHelper.getDataBinding().mediaRecyclerview;
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new MultiltemAdapter<MediaItemViewModel>(null);
        mAdapter.addItemType(MediaItemViewModel.ITEMTYPE, R.layout.item_media);
        recyclerview.setAdapter(mAdapter);

        mBindingHelper.getViewModel().addOnMediaChangCallback(new OnItemListChangedCallback<MediaItemViewModel>(mAdapter));
        // 用于更新 progressbar
        mBindingHelper.getViewModel().addOnMediaChangCallback(new FullOnListChangeCallBack<ObservableList<MediaItemViewModel>>() {


            @Override
            public void onItemRangeInserted(ObservableList<MediaItemViewModel> sender, int positionStart, int itemCount) {
                sender.removeOnListChangedCallback(this);
//                Log.e("TAG", "count: " + sender.size() + "    " + itemCount);
                mBindingHelper.getViewModel().title.set(new StringBuilder("Media")
                        .append("(")
                        .append(sender.size())
                        .append(")")
                        .toString());

                final ProgressBar mediaProgressbar = mBindingHelper.getDataBinding().mediaProgressbar;
                final RecyclerView mediaRecyclerview = mBindingHelper.getDataBinding().mediaRecyclerview;

                mediaProgressbar.setVisibility(View.GONE);
                Observable.just(1)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                mediaRecyclerview.setVisibility(View.VISIBLE);
                                ObjectAnimator alphaRV = ObjectAnimator.ofFloat(mediaRecyclerview, "alpha", 0.1f, 1f);
                                alphaRV.setDuration(1000);
                                alphaRV.start();
                            }
                        });
            }
        });


        mBindingHelper.getDataBinding().mediaProgressbar.setVisibility(View.VISIBLE);
        mBindingHelper.getDataBinding().mediaRecyclerview.setVisibility(View.GONE);
        Observable.just(1)
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        loadMedia();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (CODE_PERMISSION != requestCode) {
            return;
        }
        String[] deniedPermissons = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            // 权限拒绝
            if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                deniedPermissons[i] = permissions[i];
            } else {
                onGrantedPermission(permissions[i]);
            }
        }

        if (deniedPermissons.length > 0) {
            onDeniedPermission(deniedPermissons);
        }
    }

    private void onDeniedPermission(String[] deniedPermissons) {

    }

    private void onGrantedPermission(String permission) {
        if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
            loadMedia();
        }
    }

    private void loadMedia() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_PERMISSION);
            return;
        }

        MediaRepository mediaRepository = MediaRepository.MediaRepositoryFactory.create();
        getLifecycle().addObserver(mediaRepository);

        // load data
        ObservableList<MediaItemViewModel> dataList = mBindingHelper.getViewModel().loadMedia(getContext(), mediaRepository);
        mAdapter.setNewData(dataList);

    }
}
