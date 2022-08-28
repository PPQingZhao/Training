package com.pp.media.repository.datasource;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.pp.media.repository.IMediaDataSource;
import com.pp.media.repository.bean.Media;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class LocalMediaDataSource implements IMediaDataSource {
    private Cursor mCursor;

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    @Override
    public Observable<ObservableList<Media>> load(@NonNull final Context ctx) {

        stop();

        return Observable.just(ctx)
                .map(new Function<Context, ObservableList<Media>>() {

                    @Override
                    public ObservableList<Media> apply(Context context) throws Exception {
                        Cursor thumbnailCursor = context.getContentResolver()
                                .query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                                        new String[]{MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID},
                                        null,
                                        null,
                                        MediaStore.Images.Thumbnails.DEFAULT_SORT_ORDER);

                        // 获取查询列索引
                        int columnIndexID = thumbnailCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
                        int columnIndexData = thumbnailCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

                        thumbnailCursor.moveToFirst();

                        ObservableList<Media> list = new ObservableArrayList<>();
                        do {
                            // 原图路径
                            int imageId = thumbnailCursor.getInt(columnIndexID);
                            String thumbnailPath = thumbnailCursor.getString(columnIndexData);

                            // 根据imageId 获取缩略图路径
                            Cursor imageCursor = context.getContentResolver()
                                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            new String[]{MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED},
                                            MediaStore.Images.Media._ID + " = ?",
                                            new String[]{String.valueOf(imageId)},
                                            null);
                            String imagePath = "";
                            int dateAddTime = 0;
                            if (imageCursor.getCount() > 0) {
                                imageCursor.moveToFirst();
                                imagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                                dateAddTime = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));
                            }
                            imageCursor.close();

                            Media media = new Media();
                            media.setPath(imagePath);
                            media.setAddMillsTime(dateAddTime * 1000L);
                            media.setThumbnailsPath(thumbnailPath);
                            list.add(media);

                        } while (thumbnailCursor.moveToNext());

                        thumbnailCursor.close();
                        return list;
                    }
                });

//        // 查询系统相册数据库
//        return Observable.just(ctx)
//                .map(new Function<Context, Cursor>() {
//                    @Override
//                    public Cursor apply(Context context) throws Exception {
//                        // 创建 contentresolver
//                        return ctx.getContentResolver().query(
//                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                                new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED},
//                                null,
//                                null,
//                                MediaStore.Images.Media.DATE_ADDED);
//
//                    }
//                }).doOnNext(new Consumer<Cursor>() {
//                    @Override
//                    public void accept(Cursor cursor) throws Exception {
//                        mCursor = cursor;
//                        cursor.moveToFirst();
//                    }
//                }).flatMap(new Function<Cursor, ObservableSource<ObservableList<Media>>>() {
//                    @Override
//                    public ObservableSource<ObservableList<Media>> apply(Cursor cursor) throws Exception {
//                        ObservableList<Media> list = new ObservableArrayList<>();
//
//                        int columnIndexId = cursor.getColumnIndex(MediaStore.Images.Media._ID);
//                        int columnIndexData = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
//                        int columnIndexDataAdded = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED);
//                        do {
//                            //从数据库中读取数据
//                            int id = cursor.getInt(columnIndexId);
//                            String path = cursor.getString(columnIndexData);
//                            int add_date = cursor.getInt(columnIndexDataAdded);
//
//                            // 将读取的信息存储到 media
//                            Media media = new Media();
//                            media.setPath(path);
//                            media.setAddMillsTime(add_date);
//
//                            list.add(media);
//                        } while (cursor.moveToNext());
//
//                        return Observable.just(list);
//                    }
//                }).doOnComplete(new Action() {
//                    @Override
//                    public void run() throws Exception {
////                        Log.e("TAG", "complete  close");
//                        if (null != mCursor) {
//                            mCursor.close();
//                        }
//                    }
//                });
    }

    @Override
    public void stop() {

    }
}
