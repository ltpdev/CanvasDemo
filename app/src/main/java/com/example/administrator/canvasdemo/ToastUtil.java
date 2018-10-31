package com.example.administrator.canvasdemo;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtil{
    private static Toast mToast;
    /*private static Handler mHandler=new Handler();
    private static Runnable r=new Runnable() {
        @Override
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context context,String text,int duration){
         mHandler.removeCallbacks(r);
         if (mToast!=null){
             mToast.setText(text);
         }else {
             mToast=Toast.makeText(context,text,Toast.LENGTH_LONG);
         }
         mHandler.postDelayed(r,duration);
         mToast.show();
    }*/


    public static void showToast(Context context,String content){
        if (mToast==null){
            mToast=Toast.makeText(context,content,Toast.LENGTH_SHORT);
        }else {
            mToast.setText(content);
        }
        mToast.show();
    }
}
