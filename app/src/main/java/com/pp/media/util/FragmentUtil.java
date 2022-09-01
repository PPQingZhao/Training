package com.pp.media.util;

import android.content.Context;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.pp.media.ui.media.ImageBucketFragment;

public abstract class FragmentUtil {


    public static <fragment extends Fragment> Fragment addFragment(@NonNull FragmentActivity activity, @IdRes int container, @NonNull Adapter<fragment> factory) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment tagFragment = getFragment(fragmentManager, factory);
        String tag = factory.getFragmentTag();
        if (!tagFragment.isAdded()) {
            transaction.add(container, tagFragment, tag);
        }
        transaction.show(tagFragment);
        transaction.commit();
        return tagFragment;
    }

    public static <fragment extends Fragment> fragment getFragment(@NonNull FragmentManager fragmentManager, @NonNull Adapter<fragment> factory) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(factory.getFragmentTag());
        return factory.onCreateFragment(fragmentByTag);
    }

    public static String getFragmentTag(Context context, @StringRes int tag) {
        return context.getResources().getString(tag);
    }


    /**
     * @param f1
     * @param localClazz
     * @return
     */
    public static boolean isCreateBy(Fragment f1, Class<? extends Fragment> localClazz) {
        if (null == f1 || null == localClazz) {
            return false;
        }

        Class<? extends Fragment> fClazz = f1.getClass();
        return fClazz.isAssignableFrom(localClazz) && localClazz.isAssignableFrom(fClazz);
    }

    public static abstract class Adapter<T extends Fragment> {
        public abstract @NonNull
        T onCreateFragment(Fragment fragmentByTag);

        public abstract String getFragmentTag();
    }

}
