package com.pp.media.ui.media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.pp.media.R;
import com.pp.media.base.BaseActivity;
import com.pp.media.databinding.MediaDataBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MediaActivity extends BaseActivity<MediaDataBinding, MediaViewModel> {
    private static final int CODE_PERMISSION = 1;

    @Override
    public Class<MediaViewModel> getModelClass() {
        return MediaViewModel.class;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_media;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadMedia();

        ImageBucketFragment.injectInto(MediaActivity.this, R.id.media_fl_content);
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
        }
    }

    private void loadMedia() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_PERMISSION);
            return;
        }

        // init data
        mBindingHelper.getViewModel().loadMedia();
    }

}
