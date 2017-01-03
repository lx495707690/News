package com.topnews;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class TestActivity extends Activity {

    private ImageView img;
    private GifImageView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        gifView = (GifImageView) findViewById(R.id.info_gif1);

        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))
                .setRadius(DensityUtil.dip2px(5))
                // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                // 加载中或错误图片的ScaleType
                //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.drawable.ic_launcher)
                .setFailureDrawableId(R.drawable.ic_launcher)
                .build();

        x.image().loadFile("http://top-news.oss-cn-shanghai.aliyuncs.com/1276434149927cadeef9f1e499fdf56f.jpg",imageOptions, new Callback.CacheCallback<File>() {
            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(TestActivity.this,"1",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(TestActivity.this,"2",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
                Toast.makeText(TestActivity.this,"3",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onCache(File result) {

                GifDrawable gifFrom = null;
                try {
                    gifFrom = new GifDrawable(result.getAbsolutePath());
                    gifView.setImageDrawable(gifFrom);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return false;
            }
        });

    }


}