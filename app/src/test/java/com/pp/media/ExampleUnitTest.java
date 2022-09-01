package com.pp.media;

import android.util.Log;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        class F {
        }
        class S extends F {
        }
        F f = new F();
        S s = new S();


        System.out.println(f.getClass().isAssignableFrom(F.class));
        System.out.println(F.class.isAssignableFrom(f.getClass()));

        System.out.println(S.class.isAssignableFrom(s.getClass()));
        System.out.println(s.getClass().isAssignableFrom(S.class));

        System.out.println(F.class.isAssignableFrom(s.getClass()));
        System.out.println(s.getClass().isAssignableFrom(F.class));


    }

    @Test
    public void simpleDateformatTest() {
        SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd \nhh:mm:ss");
        long millis = System.currentTimeMillis();

        System.out.println(yyyy_MM_dd.format(new Date(millis)));
    }
}