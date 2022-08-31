package com.pp.media.repository.datasource;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.pp.media.media.Image;
import com.pp.media.media.ImageBucket;
import com.pp.media.repository.IMediaDataSource;
import com.pp.media.repository.bean.Media;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LocalMediaDataSource implements IMediaDataSource {

    @Override
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public Observable<ObservableList<Media>> load1(@NonNull final Context ctx) {

        stop();

        return Observable.just(ctx)
                .map(new Function<Context, ObservableList<Media>>() {

                    @Override
                    public ObservableList<Media> apply(Context context) throws Exception {
                        Cursor imageCursor = context.getContentResolver()
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED},
                                        null,
                                        null,
                                        MediaStore.Images.Media.DATE_ADDED);

                        // 获取查询列索引
                        int columnIndexID = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        int columnIndexData = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        int columnIndexDateAdd = imageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);

                        imageCursor.moveToFirst();

                        ObservableList<Media> list = new ObservableArrayList<>();
                        do {
                            // 原图路径
                            int imageId = imageCursor.getInt(columnIndexID);
                            String imagePath = imageCursor.getString(columnIndexData);
                            int dateAddTime = imageCursor.getInt(columnIndexDateAdd);

                            // 根据imageId 获取缩略图路径
                            Cursor thumbnailCursor = context.getContentResolver()
                                    .query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                                            new String[]{MediaStore.Images.Thumbnails.DATA},
                                            MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                                            new String[]{String.valueOf(imageId)},
                                            null);
                            String thumbnailPath = "";
                            if (thumbnailCursor.getCount() > 0) {
                                thumbnailCursor.moveToFirst();
                                thumbnailPath = thumbnailCursor.getString(thumbnailCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
                            }
                            thumbnailCursor.close();

                            Media media = new Media();
                            media.setPath(imagePath);
                            media.setAddMillsTime(dateAddTime * 1000L);
                            media.setThumbnailsPath(thumbnailPath);
                            list.add(media);

                        } while (imageCursor.moveToNext());

                        imageCursor.close();
                        return list;
                    }
                });
    }

    @SuppressLint("MissingPermission")
    public Observable<ObservableList<ImageBucket>> load(@NonNull final Context ctx) {

        return Observable.just(ctx)
                .map(new Function<Context, Cursor>() {
                    @Override
                    public Cursor apply(Context context) throws Exception {
                        return ctx.getContentResolver()
                                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        new String[]{MediaStore.Images.Media.BUCKET_ID,
                                                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                                                MediaStore.Images.Media._ID,
                                                MediaStore.Images.Media.DATA,
                                                MediaStore.Images.Media.DATE_ADDED,
                                                MediaStore.Images.Media.DISPLAY_NAME},

                                        MediaStore.Images.Media.MIME_TYPE + " = ? or "
                                                + MediaStore.Images.Media.MIME_TYPE + " = ?",

                                        new String[]{"image/jpeg", "image/png"},

                                        MediaStore.Images.Media.DATE_ADDED);
                    }
                })
                .map(new Function<Cursor, ObservableList<ImageBucket>>() {
                    @Override
                    public ObservableList<ImageBucket> apply(Cursor cursor) throws Exception {

                        ObservableArrayList<ImageBucket> list = new ObservableArrayList<>();

                        try {
                            int count = cursor.getCount();
//                            Log.e("TAG", "count: " + count);

                            ArrayMap<String, ImageBucket> bucketMap = new ArrayMap<>();
                            if (count > 0) {

                                // 获取列索引
                                int columIndexBucketId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                                int columIndexBucketName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                                int columnIndexImageId = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID);
                                int columnIndexImageData = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA);
                                int columnIndexImageDateAdd = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED);
                                int columnIndexImageDispalyName = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DISPLAY_NAME);

                                ImageBucket imageBucket;
                                int bucketId;
                                String bucketName;
                                String key;

                                cursor.moveToFirst();
                                String imagePath;
                                int imageId;
                                int imageDateAdd;
                                String imageDisPlayName;
                                do {

                                    imagePath = cursor.getString(columnIndexImageData);
                                    // 文件不存在
                                    if (!new File(imagePath).exists()) {
                                        continue;
                                    }
                                    bucketId = cursor.getInt(columIndexBucketId);
                                    bucketName = cursor.getString(columIndexBucketName);
//                                    Log.e("TAG", bucketId + " -- " + bucketName);

                                    key = bucketName + bucketId;

                                    // 1. 获取相册
                                    //  1.1 从缓存中获取ImageBucket
                                    imageBucket = bucketMap.get(key);
                                    if (null == imageBucket) {
                                        // 1.2 缓存中没有则创建ImageBucket，存入缓存bucketMap中
                                        imageBucket = new ImageBucket();
                                        bucketMap.put(key, imageBucket);
                                    }
                                    imageBucket.setId(bucketId);
                                    imageBucket.setDisPlayName(bucketName);

                                    // 2 获取当前照片
                                    imageId = cursor.getInt(columnIndexImageId);
                                    imageDateAdd = cursor.getInt(columnIndexImageDateAdd);
                                    imageDisPlayName = cursor.getString(columnIndexImageDispalyName);
                                    // 缩略图路径
                                    String imageThumbnailPath = getThumbnail(ctx, imageId);

                                    Image image = new Image();
                                    image.setId(imageId);
                                    image.setAddDateTimeMill(imageDateAdd * 1000L);
                                    image.setPath(imagePath);
                                    image.setDisPlayName(imageDisPlayName);
                                    image.setThumbnailpath(imageThumbnailPath);

                                    // 3 照片存入相册
                                    imageBucket.putImage(image);

                                } while (cursor.moveToNext());
                            }
                            cursor.close();

                            list.addAll(bucketMap.values());
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }
                        return list;
                    }
                });

    }

    private String getThumbnail(Context context, int imageId) {
        String path = "";
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                    new String[]{MediaStore.Images.Thumbnails.DATA},
                    MediaStore.Images.Thumbnails.IMAGE_ID + " = ?",
                    new String[]{imageId + ""},
                    null);

//            Log.e("TAG", "thumb count: " + cursor.getCount());
            if (null != cursor && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int columIndexData = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);
                path = cursor.getString(columIndexData);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (null != cursor) {
                cursor.close();
            }
        }

        return path;
    }

    @Override
    public void stop() {

    }
}
