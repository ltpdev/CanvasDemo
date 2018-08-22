package com.example.administrator.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class CustomView extends View{
    private Picture picture=new Picture();
    public CustomView(Context context) {
        super(context);
        recording();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        recording();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        recording();
    }
//调用录制
    private void recording() {
        Canvas canvas=picture.beginRecording(500,500);
        Paint paint=new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        canvas.translate(250,250);
        canvas.drawCircle(0,0,100,paint);
        picture.endRecording();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (picture!=null){
            //这里我们的绘制的时候传入了一个矩形，这个矩形就是用来压缩我们的begin开始录制的时候返回的画布的，当时我们设置为500，500.可以看到我们这里的方法是宽保持不变，高度变为原来的二分之一一，就把原来的圆压缩为现在的椭圆了。
            /*canvas.drawPicture(picture,new RectF(0,0,picture.getWidth(),200));
           */
            PictureDrawable drawable = new PictureDrawable(picture);
            //设置绘制的区域 ，每次都从Picture的左上角开始绘制。
           drawable.setBounds(250,0,500,picture.getHeight());
            //drawable.setBounds(0,0,500,picture.getHeight());
            drawable.draw(canvas);

        }
    }
}
