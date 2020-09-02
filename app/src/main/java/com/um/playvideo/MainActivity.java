package com.um.playvideo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.SurfaceTexture;

import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private MediaPlayer mMediaPlayer;
    private Surface mSurface;
    private TextureView mTextureView;
    private ProgressBar mProgressBar;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private UstorageReceiver us = new UstorageReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = findViewById(R.id.surfaceView);
        mProgressBar = findViewById(R.id.progressBar);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addDataScheme("file");
        intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addDataScheme("file");
        registerReceiver(us,intentFilter);

        mMediaPlayer = new MediaPlayer();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //属性动画
                mTextureView.animate().scaleX(0.5f).scaleY(0.5f);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextureView.animate().translationXBy(10).translationYBy(10);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextureView.animate().rotationBy(10);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextureView.animate().alphaBy(0.1f);
            }
        });

        try {

            mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                    mSurface = new Surface(surface);
                    //TextureView，5.0以前在主线程渲染，在5.0以后有独立的渲染线程，所以5.0的系统需要自己开启线程。
                    //必须在onSurfaceTextureAvailable回调后才能开启线程。

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(us);
    }

    class MediaThread implements Runnable{

        @Override
        public void run() {
            try {
                String sdDir = Environment.getExternalStorageDirectory().toString();
                String uri = sdDir + "/1080P.ts";
                Log.d("sdDir:",sdDir);
                Log.d("uri:",uri);
                if (sourcePath != null) {
                    uri = sourcePath;
                }
                mMediaPlayer.setSurface(mSurface);
                mMediaPlayer.setDataSource(uri);
                // mMediaPlayer.setDataSource(this, Uri.parse(uri));
                mMediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        System.out.println("===========video=================" + width + "   " + height + "    " + mp.getVideoWidth() + "   " + mp.getVideoHeight());
                        //尺寸动态变化
                        if (mp != null && width > 0 && height > 0 && mp.getVideoHeight() > 0 && mp.getVideoWidth() > 0) {
                            int videoWidth = mp.getVideoWidth();
                            int videoHeight = mp.getVideoHeight();
                            WindowManager wm = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
                            int screenwidth = wm.getDefaultDisplay().getWidth();
                            float size = (videoHeight * 1.0f) / (videoWidth * 1.0f);
                            int surfaceHeight = (int) (screenwidth *size);
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mTextureView.getLayoutParams();
                            // layoutParams.width = width;
                            layoutParams.height = surfaceHeight;
                            mTextureView.setLayoutParams(layoutParams);
                        }
                    }
                });
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mMediaPlayer.start();
                        mMediaPlayer.setLooping(true);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String sourcePath;
    public class UstorageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
                sourcePath = intent.getData().getPath() + "/1080P.ts";
                Log.d("sourcePath:" ,sourcePath);
                new Thread(new MediaThread()).start();
            }else if(intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)){

            }
        }
    }


    /**
     * 获取外置SD卡路径
     * @return	应该就一条记录或空
     */
    public List<String> getExtSDCardPath()
    {
        List<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("sda"))
                {
                    String [] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory())
                    {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }
}
