package com.example.administrator.canvasdemo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import javax.xml.validation.Validator;

/*
 * 用于结束录制以及显示录制时间的试图
 * */

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class RecorderView {
    //窗口管理器
    private WindowManager windowManager;
    //窗口管理器的参数
    private WindowManager.LayoutParams wmLayoutParams;
    //上下文
    private Activity activity;
    //主视图
    private LinearLayout mMainView;

    //结束录制的按钮
    private ImageView mRecordFinishView;
    //显示录制的时间
    private TimerTextView mTimerTextView;
    //媒体
    private MediaProjection mediaProjection;
    //录屏类
    private MediaRecorder mediaRecorder;
    //虚拟屏幕，录屏或者截屏时创建的
    private VirtualDisplay virtualDisplay;
    //是否在录制
    private boolean isRecorder = false;
    //是否暂停
    private boolean isPause = false;
    private int width = 720;
    private int height = 1080;
    private int dpi = 0;
    //横竖屏标识
    private int orientation = 0;
    //当前录制视频视频的路径
    private String currentVideoFilePatgh;
    //录制视频的集合
    private ArrayList<String> mediaPathList;
    //最后录制完成之后的视频文件路径
    private String saveMediaPath;
    private static RecorderView instance;
    private View.OnClickListener onRecordListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //停止录屏
            finishRecorder();
        }
    };


    private RecorderView() {

    }

    public static RecorderView getInstance() {
        if (instance == null) {
            instance = new RecorderView();
        }
        return instance;
    }

    //显示录制按钮
    public RecorderView showRecord() {
        //mActivity = AnFengPaySDK.getInstance().gameActivity  // 使用游戏的activity进行显示
        initView();
        initParams();
        try {
            windowManager.addView(mMainView, wmLayoutParams);
        } catch (Exception e) {
            Log.i("showRecord", "添加视图异常：" + e.toString());
        }
        return this;
    }

    private void initParams() {
        windowManager = activity.getWindowManager();
        wmLayoutParams = new WindowManager.LayoutParams();
        wmLayoutParams.type = WindowManager.LayoutParams.LAST_APPLICATION_WINDOW;
        //设置悬浮球的背景为透明
        wmLayoutParams.format = PixelFormat.RGBA_8888;
        wmLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        //设置固定在是图书的中上部
        wmLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
        wmLayoutParams.width = 100;
        wmLayoutParams.height = 100;
    }


    private void initView() {
        mMainView = (LinearLayout) View.inflate(activity, R.layout.window_record_ball, null);
        mRecordFinishView = mMainView.findViewById(R.id.iv_record);
        mTimerTextView = mMainView.findViewById(R.id.tv_time);
        mRecordFinishView.setOnClickListener(onRecordListener);
        mediaPathList = new ArrayList<>();

    }

    //初始化录屏参数
    private void initRecorder() {
        mediaRecorder = new MediaRecorder();
        if (dpi >= DisplayMetrics.DENSITY_XHIGH) {
            width = orientation != Configuration.ORIENTATION_LANDSCAPE ? 720 : 1280;
            height = orientation != Configuration.ORIENTATION_LANDSCAPE ? 1280 : 720;
        }
        mediaRecorder.setOrientationHint(orientation != Configuration.ORIENTATION_LANDSCAPE ? 0 : 90);
        //音频源
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        //视频来源
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        //视频输出格式
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //获取当前视频路径
        currentVideoFilePatgh = getRecordDir();
        //录制输出文件名
        mediaRecorder.setOutputFile(currentVideoFilePatgh);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setMaxDuration(1 * 60 * 1000);
        mediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
        mediaRecorder.setVideoSize(width, height);
        mediaRecorder.setVideoFrameRate(30);
        mediaRecorder.setOnErrorListener(onRecordErrorListener);
        try {
            mediaRecorder.prepare();
            mediaPathList.add(currentVideoFilePatgh);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getRecordDir() {
        return Environment.getExternalStorageDirectory().getAbsoluteFile() + "/" + "AF_" + System.currentTimeMillis() + ".mp4";
    }

    private void finishRecorder() {
           if (isRecorder){
               isRecorder=false;
               isPause=false;
               mTimerTextView.stopTimer();
               stopMedia();
               // 结束录屏之后将画布和录屏管理器设置为空
               virtualDisplay.release();
               mediaProjection.stop();
               // 合并此次录制的所有屏幕
               try {
                   saveMediaPath = getRecordDir();
                   VideoUtils.appendMp4List(mediaPathList, saveMediaPath);
                   mediaPathList.clear();
               } catch (Exception e) {
               }
               removeRecorderView();
           }


    }

    private MediaRecorder.OnErrorListener onRecordErrorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mediaRecorder, int i, int i1) {
            Log.i("onError", "录屏错误");
        }
    };

    //设置录屏参数
    public RecorderView setRecorderConfig(MediaProjection mp, DisplayMetrics displayMetrics, int orientation) {
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        dpi = displayMetrics.densityDpi;
        this.orientation = orientation;
        mediaProjection = mp;
        return this;
    }

    //开始录屏
    public void startRecorder() {
        if (orientation == 0) {
            throw new RuntimeException("请先设置录屏参数");
        }
        //如果没有在录制时
        if (!isRecorder) {
            startMedia();
            isRecorder = true;
            mTimerTextView.startTimer();
        }
    }

    //录屏开始
    private void startMedia() {
        initRecorder();
        createVirtualDislay();
        mediaRecorder.start();
    }

    //创建虚拟屏幕以进行录屏
    private void createVirtualDislay() {
        // 如果当前屏幕 尺寸 大于 XHIGH  则统一使用 720 * 1280 ，其他就使用本身屏幕的大小
        try {
            virtualDisplay = mediaProjection.createVirtualDisplay("MainScreen", width, height, dpi,
                    DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mediaRecorder.getSurface(), null, null);
        } catch (Exception e) {
            //LogUtil.e(TAG, "创建画布异常:" + e.toString())
        }

    }

    //暂停录屏
    private void pauseRecorder() {
        if (isRecorder && !isPause) {
            mTimerTextView.pauseTimer();
            isPause = true;
            stopMedia();
        }
    }

    /*重启录屏*/
    private void resumeRecorder() {
        if (isRecorder&&isPause){
            mTimerTextView.resumeTimer();
            isRecorder=true;
            isPause=false;
            startMedia();
        }
    }


    //停止录屏
    private void stopMedia() {
        mediaRecorder.stop();
        mediaRecorder.reset();
    }

    //移除悬浮按钮
    private void removeRecorderView() {
        windowManager.removeView(mMainView);

    }


}
