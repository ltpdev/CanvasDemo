package com.example.administrator.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TeleScopeView extends View{
    private Paint paint;
    private Bitmap bitmap,bitmapBG;
    private int mDx=-1;
    private int mDy=-1;
    public TeleScopeView(Context context) {
        super(context);
    }

    public TeleScopeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        bitmap= BitmapFactory.decodeResource(getResources(),R.mipmap.bg);
    }

    public TeleScopeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDx= (int) event.getX();
                mDy= (int) event.getY();
                postInvalidate();
               return true;
            case MotionEvent.ACTION_MOVE:
                mDx= (int) event.getX();
                mDy= (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDx= -1;
                mDy= -1;
                break;
        }
        postInvalidate();
        return super.onTouchEvent(event);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmapBG==null){
            bitmapBG=Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvasBg=new Canvas(bitmapBG);
            canvasBg.drawBitmap(bitmap,null,new Rect(0,0,getWidth(),getHeight()),paint);
        }

        if (mDx!=-1&&mDy!=-1){
            paint.setShader(new BitmapShader(bitmapBG, Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
            canvas.drawCircle(mDx,mDy,150,paint);
        }


    }
}
