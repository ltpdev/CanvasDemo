package com.example.administrator.canvasdemo;

import android.animation.FloatArrayEvaluator;
import android.animation.FloatEvaluator;
import android.animation.IntArrayEvaluator;
import android.animation.IntEvaluator;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

public class MarqueeView extends View {
    private Paint paint;
    private float curX = 0;
    private String marqueeString="我是滚动条";
    //圈数
    private final int COUNT_CICRLE = 5;
    //文本占用的宽度
    private float textWidth;

    public MarqueeView(Context context) {
        super(context);
        initPaint();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MarqueeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        textWidth=paint.measureText(marqueeString);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.RED);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float baselineY = (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(marqueeString, curX, baselineY, paint);
    }


    public void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(getWidth() * COUNT_CICRLE, 0);
        valueAnimator.setDuration(30 * 1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                curX = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new MyFloatTypeEvaluator(getWidth()));
        valueAnimator.start();
    }

}
