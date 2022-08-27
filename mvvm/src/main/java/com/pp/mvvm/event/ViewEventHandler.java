package com.pp.mvvm.event;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.pp.mvvm.base.LifecycleFragment;
import com.pp.mvvm.base.LifecycleViewModel;

public final class ViewEventHandler {
    public static final String UI_EVENT_SHOWTOAST = "show toast";
    public static final String UI_EVENT_STARTACTIVITY = "start activity";
    public static final String UI_EVENT_FINISHACTIVITY = "finish activity";


    public static boolean handle(@NonNull Activity activity, ViewEvent viewEvent) {
        if (null == viewEvent) {
            return false;
        }

        String type = viewEvent.getType();
        switch (type) {
            case UI_EVENT_FINISHACTIVITY:
                activity.finish();
                return true;

            default:
                break;
        }
        return handleDateEvent(activity, viewEvent.getDataOwner(), type);

    }

    private static boolean handleDateEvent(Activity activity, ViewEvent.DataOwner dataOwner, String type) {

        if (null == dataOwner
                || null == dataOwner.getData()) {
            return false;
        }

        Object data = dataOwner.getData();

        switch (type) {
            case UI_EVENT_FINISHACTIVITY:
                activity.finish();
                return true;
            case UI_EVENT_SHOWTOAST:
                if (data instanceof String) {
                    Toast.makeText(activity, (String) data, Toast.LENGTH_SHORT).show();
                    return true;
                }
                break;

            case UI_EVENT_STARTACTIVITY:
                if (data instanceof Intent) {
                    activity.startActivity((Intent) data);
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }
}
