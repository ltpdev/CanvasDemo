package com.example.administrator.canvasdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class PathView extends View{
    private int mViewWidth;
    private int mViewHeight;
    private Paint paint;
    public PathView(Context context) {
        super(context);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth=w;
        mViewHeight=h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mViewWidth/2,mViewHeight/2);
        Path path=new Path();
        path.addRect(-100,-100,100,100, Path.Direction.CW);
        path.addRect(-200,-200,200,200, Path.Direction.CW);
        canvas.drawPath(path,paint);
        PathMeasure measure=new PathMeasure(path,false);
        float len1=measure.getLength();
        measure.nextContour();
        float len2=measure.getLength();
        Log.i("LEN","len1="+len1);                              // 输出两条路径的长度
        Log.i("LEN","len2="+len2);
    }

    private void init() {
       paint=new Paint();
       paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }
}
