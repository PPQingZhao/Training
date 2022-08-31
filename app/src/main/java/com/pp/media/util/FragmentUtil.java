package com.pp.media.util;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.pp.media.ui.media.ImageBucketFragment;

public abstract class FragmentUtil {


    public static <fragment extends Fragment> void addFragment(@NonNull FragmentActivity activity, @IdRes int container, @NonNull Adapter<fragment> factory) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment tagFragment = getFragment(fragmentManager, factory);
        String tag = factory.getFragmentTag();
        if (!tagFragment.isAdded()) {
            transaction.add(container, tagFragment, tag);
        }
        transaction.show(tagFragment);
        transaction.commit();
    }

    public static <fragment extends Fragment> fragment getFragment(@NonNull FragmentManager fragmentManager, @NonNull Adapter<fragment> factory) {
        Fragment fragmentByTag = fragmentManager.findFragmentByTag(factory.getFragmentTag());
        return factory.onCreateFragment(fragmentByTag);
    }

    public static abstract class Adapter<T extends Fragment> {
        public abstract @NonNull
        T onCreateFragment(Fragment fragmentByTag);

        public abstract String getFragmentTag();
    }

}
