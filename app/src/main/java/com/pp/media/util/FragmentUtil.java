package com.pp.media.util;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class FragmentUtil {


    public static void addToActivity(@NonNull FragmentActivity activity, Fragment fragment, String fragmentTag, @IdRes int container) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(container, fragment, fragmentTag)
                .commit();
    }

}
