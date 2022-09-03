package com.pp.media.widget.window;


import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;

public class PhotoWindow {

    private static final String TAG = "PhotoWindow";
    private final WindowManager mWindowManager;
    private final WindowManager.LayoutParams mWindowParams;
    private final Context mContxt;
    private PhotoBackgroundView mDecorView;
    private boolean mShowing;

    public PhotoWindow(@NonNull Context ctx) {
        this.mContxt = ctx;
        mWindowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);

        mWindowParams = new WindowManager.LayoutParams();

        mWindowParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        mWindowParams.windowAnimations = 0;
        mWindowParams.format = PixelFormat.TRANSPARENT;
        mWindowParams.x = 0;
        mWindowParams.y = 0;
//        mWindowParams.alpha = 0.5f;

    }

    public void show(@NonNull View view) {
        this.mDecorView = createDecorView(view);
        mWindowManager.addView(mDecorView, mWindowParams);
        mShowing = true;

    }

    private PhotoBackgroundView createDecorView(View view) {
        PhotoBackgroundView decorView = new PhotoBackgroundView(mContxt);
        decorView.addView(view, PhotoBackgroundView.LayoutParams.MATCH_PARENT, PhotoBackgroundView.LayoutParams.MATCH_PARENT);
        return decorView;
    }

    public void dismiss() {
        Log.e(TAG,"dismiss");
        if (null != mDecorView) {
            mDecorView.removeAllViews();
            mWindowManager.removeViewImmediate(mDecorView);
            mDecorView = null;
        }
        mShowing = false;
    }

    public boolean isShowing() {
        return mShowing;
    }

    private class PhotoBackgroundView extends FrameLayout {

        public PhotoBackgroundView(Context context) {
            super(context);
        }
    }
}
