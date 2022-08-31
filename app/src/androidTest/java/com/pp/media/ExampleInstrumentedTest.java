package com.pp.media;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import androidx.collection.ArrayMap;
import androidx.databinding.ObservableList;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.MediaRepository;
import com.pp.media.repository.bean.Media;
import com.pp.media.repository.datasource.LocalMediaDataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Set;

import io.reactivex.functions.Consumer;

import static junit.framework.TestCase.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private Context appContext;

    @Before
    public void onBefore(){

        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void meidaTest() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Cursor cursor = context.getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.Media.BUCKET_ID,
                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME},
                        MediaStore.Images.Media.MIME_TYPE + " = ? or "
                                + MediaStore.Images.Media.MIME_TYPE + " = ?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_ADDED);

        int count = cursor.getCount();
        Log.e("TAG", "count: " + count);

        if (count > 0) {

            int columIndexBucketId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int columIndexBucketName = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);


            ArrayMap<String, ImageBucket> bucketMap = new ArrayMap<>();
            cursor.moveToFirst();

            ImageBucket imageBucket;
            int bucketId;
            String bucketName;
            String key;
            do {
                bucketId = cursor.getInt(columIndexBucketId);
                bucketName = cursor.getString(columIndexBucketName);
                Log.e("TAG", bucketId + " -- " + bucketName);

                key = bucketName + bucketId;

                //  ①从缓存中获取ImageBucket
                imageBucket = bucketMap.get(key);
                if (null == imageBucket) {
                    // ② 缓存中没有则创建ImageBucket，存入缓存bucketMap中
                    imageBucket = new ImageBucket();
                    bucketMap.put(key, imageBucket);
                }
                imageBucket.setId(bucketId);
                imageBucket.setDisPlayName(bucketName);

            } while (cursor.moveToNext());
            cursor.close();


            for (int i = 0; i < bucketMap.size(); i++) {

                ImageBucket bucket = bucketMap.valueAt(i);
                Log.e("TAG", bucket.getId() + "  -222222-  " + bucket.getDisPlayName());
            }
        }

    }

    @Test
    public void LocalMediaDataSourceTest() {
        Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        new LocalMediaDataSource().load(targetContext).subscribe(new Consumer<ObservableList<ImageBucket>>() {
            @Override
            public void accept(ObservableList<ImageBucket> imageBuckets) throws Exception {

                for (ImageBucket bucket : imageBuckets) {
                    Log.e("TAG", bucket.getId() + "  -222222-  " + bucket.getDisPlayName());
                    for (Image image : bucket.getImageMap().values()) {
                        Log.e("TAG", image.getThumbnailpath() + "  --------------  " + image.getPath());
                    }
                }
            }
        });
    }


    @Test
    public void testMediaRepository() {

        Log.e("TAG","testMediaRepository start");
        MediaRepository.MediaRepositoryFactory.create().init(appContext);
    }
}
