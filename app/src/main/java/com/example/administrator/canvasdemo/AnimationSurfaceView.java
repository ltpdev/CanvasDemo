package com.example.administrator.canvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AnimationSurfaceView extends SurfaceView {
    private SurfaceHolder surfaceHolder;
    private boolean flag = false;
    private Bitmap bitmap_bg;
    private float mWidth, mHeight;
    private int bitPosX;
    private Canvas canvas;
    private Thread thread;

    private enum State {
        LEFT, RINGHT
    }

    private State state = State.LEFT;
    private final int BITMAP_STEP = 1;

    public AnimationSurfaceView(Context context) {
        super(context);
    }

    public AnimationSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                flag = true;
                startAnimation();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                flag = false;
            }
        });
    }

    public AnimationSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void startAnimation() {
        mWidth = getWidth();
        mHeight = getHeight();
        int mWindth = (int) (mWidth * 3 / 2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timg);
        bitmap_bg = Bitmap.createScaledBitmap(bitmap, mWindth, (int) mHeight, true);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    canvas = surfaceHolder.lockCanvas();
                    drawView();
                    surfaceHolder.unlockCanvasAndPost(canvas);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    private void drawView() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(bitmap_bg, bitPosX, 0, null);
        switch (state) {
            case LEFT:
                bitPosX = bitPosX - BITMAP_STEP;
                break;
            case RINGHT:
                bitPosX = bitPosX + BITMAP_STEP;
                break;
        }
        if (bitPosX<=-mWidth/2){
            state=State.RINGHT;
        }
        if (bitPosX>=0){
            state=State.LEFT;
        }
    }
}
