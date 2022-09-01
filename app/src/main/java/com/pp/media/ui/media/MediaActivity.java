package com.pp.media.ui.media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;

import com.pp.media.R;
import com.pp.media.base.BaseActivity;
import com.pp.media.databinding.MediaDataBinding;
import com.pp.media.ui.MediaShareViewModel;
import com.pp.media.ui.event.MediaEvent;
import com.pp.media.util.FragmentUtil;
import com.pp.mvvm.event.ViewEvent;

public class MediaActivity extends BaseActivity<MediaDataBinding, MediaViewModel> {
    private static final String TAG = "MediaActivity";
    private static final int CODE_PERMISSION = 1;
    private Fragment mShowFragment;

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

        MediaShareViewModel shareViewModel = initShareViewModel();

        loadMedia(shareViewModel);

        // 解决多个fragment ，状态栏被第一个fargment消费,导致沉浸式状态栏失效bug
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mBindingHelper.getDataBinding().getRoot().setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    return mShowFragment.getView().dispatchApplyWindowInsets(insets);
                }
            });
        }

        showFragment(R.string.title_imagebucket);
    }

    private MediaShareViewModel initShareViewModel() {

        MediaShareViewModel shareViewModel = MediaShareViewModel.get(this);
        getLifecycle().addObserver(shareViewModel);
        shareViewModel.mSender.observe(this, new Observer<ViewEvent>() {
            @Override
            public void onChanged(ViewEvent viewEvent) {
//                Log.e(TAG, viewEvent.getType());
                switch (viewEvent.getType()) {
                    case MediaEvent.ACTION_SEND_IMAGEBUCKET_FOR_IMAGLIST:
                        showFragment(R.string.title_imagelist);
                        return;
                    case MediaEvent.ACTION_ON_IMAGELIST_BACKPRESSED:
                        showFragment(R.string.title_imagebucket);
                        return;
                    case MediaEvent.ACTION_ON_IMAGEDETAIL_BACKPRESSED:
                        showFragment(R.string.title_imagelist);
                        return;
                    case MediaEvent.ACTION_SEND_IMAGE_FOR_IMAGDETAIL:
                        showFragment(R.string.title_imagedetail);
                        return;
                    default:
                        break;
                }
            }
        });
        return shareViewModel;
    }

    private void showFragment(int tagFragment) {

        if (null != mShowFragment) {
//            Log.e(TAG, "old show:  " + mShowFragment.toString());
            getSupportFragmentManager().beginTransaction().hide(mShowFragment).commit();
        }

        Fragment showFragment;
        switch (tagFragment) {
            case R.string.title_imagedetail:
                showFragment = FragmentUtil.addFragment(this, R.id.media_fl_content, ImageDetailFragment.getAdapter());
                break;
            case R.string.title_imagelist:
                showFragment = FragmentUtil.addFragment(this, R.id.media_fl_content, ImageListFragment.getAdapter());
                break;
            default:
                showFragment = FragmentUtil.addFragment(this, R.id.media_fl_content, ImageBucketFragment.getAdapter());
                break;
        }

        // 重新消费状态栏-->解决状态栏被第一个fragment的 view消费,导致其他fragment沉浸式失效
        mBindingHelper.getDataBinding().mediaFlContent.requestFitSystemWindows();
        mShowFragment = showFragment;
//        Log.e(TAG, "show: " + mShowFragment.toString());
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

    private void loadMedia(MediaShareViewModel shareViewModel) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_PERMISSION);
            return;
        }

        // init data
        shareViewModel.loadData();
    }

}
