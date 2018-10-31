package com.example.administrator.canvasdemo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TimerTextView extends AppCompatTextView{
    public static final String TAG="TimerTextView";
    /*是否还在进行及时*/
    private boolean isRun=false;
    /*上下文*/
    private Context context;
    /*计时器*/
    private Timer mTimer;
    //秒数
    private int second;
    public static final int SET_TEXT=111;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==SET_TEXT){
                setTimerText(timeFormat(second));
            }
        }
    };

    private void setTimerText(String text) {
        this.setText(text);
    }



    public TimerTextView(Context context) {
        super(context);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TimerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
    }

    public void startTimer() {
         if (mTimer==null){
             mTimer=new Timer(true);
         }
         mTimer.schedule(new TimerTask() {
             @Override
             public void run() {
                 second++;
                 handler.sendEmptyMessageAtTime(SET_TEXT,0);
             }
         },500,1000);
         isRun=true;
    }

    public void pauseTimer() {
        isRun=false;
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
            mTimer = null;
        }

    }



    public void resumeTimer() {
        startTimer();
    }

    public void stopTimer() {
        pauseTimer();
        second=0;
    }


    public boolean isRun(){
        return isRun;
    }

    private String timeFormat(int second) {
        int seconds = second % 60;
        int minutes = (second / 60) % 60;
        int hours = second / 3600;
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.getDefault());
        if (hours > 0) {
            return formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return formatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


}
