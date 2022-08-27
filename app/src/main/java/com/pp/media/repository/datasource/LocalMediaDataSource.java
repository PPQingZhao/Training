package com.pp.media.repository.datasource;

import android.Manifest;
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
        // 查询系统相册数据库
        return Observable.just(ctx)
                .map(new Function<Context, Cursor>() {
                    @Override
                    public Cursor apply(Context context) throws Exception {
                        // 创建 contentresolver
                        return ctx.getContentResolver().query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  //限制类型为图片
                                null,
                                null,
                                null,
                                MediaStore.Images.Media.DATE_ADDED);
                    }
                }).doOnNext(new Consumer<Cursor>() {
                    @Override
                    public void accept(Cursor cursor) throws Exception {
                        mCursor = cursor;
                        cursor.moveToFirst();
                    }
                }).flatMap(new Function<Cursor, ObservableSource<ObservableList<Media>>>() {
                    @Override
                    public ObservableSource<ObservableList<Media>> apply(Cursor cursor) throws Exception {
                        ObservableList<Media> list = new ObservableArrayList<>();
                        do {
                            //从数据库中读取数据
                            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                            int add_date = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED));

                            // 将读取的信息存储到 media
                            Media media = new Media();
                            media.setPath(path);
                            media.setAddMillsTime(add_date);

                            list.add(media);
                        } while (cursor.moveToNext());

                        return Observable.just(list);
                    }
                }).doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e("TAG", "complete  close");
                        if (null != mCursor) {
                            mCursor.close();
                        }
                    }
                });
    }

    @Override
    public void stop() {
        if (null != mCursor) {
            mCursor.close();
            mCursor = null;
        }
    }
}
