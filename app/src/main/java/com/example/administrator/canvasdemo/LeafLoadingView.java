package com.example.administrator.canvasdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LeafLoadingView extends View {
    private static final String TAG = "LeafLoadingView";
    //淡白色
    private static final int WHITE_COLOR = 0xfffde399;
    //橙色
    private static final int ORANGE_COLOR = 0xffffa800;
    //中等振幅大小
    private static final int MIDDLE_AMPLITUDE = 13;
    //不同类型之间的振幅差距
    private static final int AMPLITUDE_DISPARITY = 5;
    //总进度
    private static final int TOTAL_PROGRESS = 100;
    //叶子飘动一个周期所花的时间
    private static final long LEAF_FLOAT_TIME = 3000;
    //叶子旋转一周需要的时间
    private static final long LEAF_ROTATE_TIME = 2000;

    //用于控制绘制的进度条距离左上下的距离
    private static final int LEFT_MARGIN = 9;
    //用于控制绘制的进度条距离右的距离
    private static final int RIGHT_MARGIN = 25;

    private int mLeftMargin, mRightMargin;
    //中等振幅大小
    private int mMiddleAmplitude = MIDDLE_AMPLITUDE;
    //振幅差
    private int mAmplitudeDisparity = AMPLITUDE_DISPARITY;
    //叶子飘动一个周期所需要的时间
    private long mLeafFloatTime = LEAF_FLOAT_TIME;
    //叶子旋转一周所需要的时间
    private long mLeafRotateTime = LEAF_ROTATE_TIME;
    private Resources mResources;
    private Bitmap mLeafBitmap;
    //叶子的宽度，高度
    private int mLeafWidth, mLeafHeight;
    private Bitmap mOuterBitmap;
    private Rect mOuterSrcRect, mOuterDestRect;
    private int mOuterWidth, mOuterHeight;

    private int mTotalWidth, mTotalHeight;
    private Paint mBitmapPaint, mWhitePaint, mOrangePaint;
    private RectF mWhiteRectF, mOrangleRectF, mArcRectF;
    //当前进度
    private int mProgress;
    //所绘制的进度条部分的宽度
    private int mProgressWidth;
    //当前所在的绘制的进度条的位置
    private int mCurrentProgressPosition;
    //所绘制的进度条弧形的半径
    private int mArcRadius;
    //用于产生叶子的信息
    private LeafFactory mLeafFactory;
    //产生出的叶子信息
    private List<Leaf> mLeafInfos;

    //用于控制随机增加的时间不报团
    private int mAddTime;
    //arc的右上角的x坐标，也就是矩形x坐标的起始点
    private int mArcRightLocation;


    public LeafLoadingView(Context context) {
        super(context);
        init();
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LeafLoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制进度条和叶子
        //之所以把叶子放在进度条里绘制，主要是层级的问题
        drawProgressAndLeafs(canvas);
        canvas.drawBitmap(mOuterBitmap, mOuterSrcRect, mOuterDestRect, mBitmapPaint);
        postInvalidate();
    }


    private void init() {
        mResources = getResources();
        mLeftMargin = UiUtils.dipToPx(getContext(), LEFT_MARGIN);
        mRightMargin = UiUtils.dipToPx(getContext(), RIGHT_MARGIN);
        mLeafFloatTime = LEAF_FLOAT_TIME;
        mLeafRotateTime = LEAF_ROTATE_TIME;
        initBitmap();
        initPaint();
        mLeafFactory = new LeafFactory();
        mLeafInfos = mLeafFactory.generateLeafs();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);
        mBitmapPaint.setFilterBitmap(true);
        mWhitePaint = new Paint();
        mWhitePaint.setAntiAlias(true);
        mWhitePaint.setColor(WHITE_COLOR);
        mOrangePaint = new Paint();
        mOrangePaint.setAntiAlias(true);
        mOrangePaint.setColor(ORANGE_COLOR);
    }

    private void initBitmap() {
        mLeafBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf)).getBitmap();
        mLeafWidth = mLeafBitmap.getWidth();
        mLeafHeight = mLeafBitmap.getHeight();

        mOuterBitmap = ((BitmapDrawable) mResources.getDrawable(R.drawable.leaf_kuang)).getBitmap();
        mOuterWidth = mOuterBitmap.getWidth();
        mOuterHeight = mOuterBitmap.getHeight();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mProgressWidth = mTotalWidth - mLeftMargin - mRightMargin;
        mArcRadius = (mTotalHeight - 2 * mLeftMargin) / 2;

        mOuterSrcRect = new Rect(0, 0, mOuterWidth, mOuterHeight);
        mOuterDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
        mWhiteRectF = new RectF(mLeftMargin + mCurrentProgressPosition, mLeftMargin, mTotalWidth
                - mRightMargin, mTotalHeight - mLeftMargin);
        mOrangleRectF = new RectF(mLeftMargin + mArcRadius, mLeftMargin, mCurrentProgressPosition, mTotalHeight - mLeftMargin);
        mArcRectF = new RectF(mLeftMargin, mLeftMargin, mLeftMargin + 2 * mArcRadius, mTotalHeight - mLeftMargin);
        mArcRightLocation = mLeftMargin + mArcRadius;

    }

    private class LeafFactory {
        private static final int MAX_LEAFS = 8;
        Random random = new Random();


        //生成一个叶子的信息
        public Leaf generateLeaf() {
            Leaf leaf = new Leaf();
            int randomType = random.nextInt(3);
            //随时类型-随机振幅
            StartType type = StartType.MIDDLE;
            switch (randomType) {
                case 0:
                    break;
                case 1:
                    type = StartType.LITTLE;
                    break;
                case 2:
                    type = StartType.BIG;
                    break;
            }
            leaf.type = type;
            //随机起始的旋转角度
            leaf.rotateAngle = random.nextInt(360);
            //随机的旋转方向
            leaf.rotateDirection = random.nextInt(2);
            //为了产生交错的感觉，让开始的时间有一定的随机性
            mLeafFloatTime = mLeafFloatTime <= 0 ? LEAF_FLOAT_TIME : mLeafFloatTime;
            mAddTime += random.nextInt((int) (mLeafFloatTime * 2));
            leaf.startTime = System.currentTimeMillis() + mAddTime;
            return leaf;
        }

        public List<Leaf> generateLeafs() {
            return generateLeafs(MAX_LEAFS);
        }

        // 根据传入的叶子数量产生叶子信息
        public List<Leaf> generateLeafs(int leafSize) {
            List<Leaf> leafs = new LinkedList<Leaf>();
            for (int i = 0; i < leafSize; i++) {
                leafs.add(generateLeaf());
            }
            return leafs;
        }
    }

    /*叶子的对象，用来记录叶子的主要数据*/
    private class Leaf {
        //在绘制部分的位置
        float x, y;
        //控制叶子飘动的幅度
        StartType type;
        // 旋转角度
        int rotateAngle;
        // 旋转方向--0代表顺时针，1代表逆时针
        int rotateDirection;
        // 起始时间(ms)
        long startTime;
    }

    private enum StartType {
        LITTLE, MIDDLE, BIG
    }


    private void drawProgressAndLeafs(Canvas canvas) {
        if (mProgress >= TOTAL_PROGRESS) {
            mProgress = 0;
        }
        //mProgressWidth 为进度条的宽度，根据当前进度算出进度条的位置
        mCurrentProgressPosition = mProgressWidth * mProgress / TOTAL_PROGRESS;
        if (mCurrentProgressPosition < mArcRadius) {
            //1绘制白色arc
            //??????
            canvas.drawArc(mArcRectF, 90, 180, false, mWhitePaint);
            //2,绘制白色矩形
            mWhiteRectF.left=mArcRightLocation;
            canvas.drawRect(mWhiteRectF,mWhitePaint);
            //绘制叶子
            drawLeafs(canvas);
            //3,绘制棕色
            //单边角度
            int angle= (int) Math.toDegrees(Math.acos((mArcRadius - mCurrentProgressPosition)/(float)mArcRadius));
            //起始位置
            int startAngle=180-angle;
            //扫过的角度
            int sweepAngle=2*angle;
            canvas.drawArc(mArcRectF,startAngle,sweepAngle,false,mOrangePaint);
        }else {//mCurrentProgressPosition>=mArcRadius
            // 1.绘制white RECT
            mWhiteRectF.left=mCurrentProgressPosition;
            canvas.drawRect(mWhiteRectF,mWhitePaint);
            //绘制叶子
            drawLeafs(canvas);
             // 2.绘制Orange ARC
            canvas.drawArc(mArcRectF, 90, 180, false, mOrangePaint);
            //3.绘制orange RECT
            mOrangleRectF.left=mArcRightLocation;
            mOrangleRectF.right = mCurrentProgressPosition;
            canvas.drawRect(mOrangleRectF, mOrangePaint);

        }
    }


    //绘制叶子
    private void drawLeafs(Canvas canvas) {
        mLeafRotateTime=mLeafFloatTime<=0?LEAF_FLOAT_TIME:mLeafRotateTime;
        long currentTime=System.currentTimeMillis();
        for (int i = 0; i < mLeafInfos.size(); i++) {
            Leaf leaf=mLeafInfos.get(i);
            if (currentTime>leaf.startTime&&leaf.startTime!=0){
                //绘制叶子--根据叶子的类型和当前时间得出叶子的（x,y）
                getLeafLocation(leaf,currentTime);
                //根据时间计算旋转角度
                canvas.save();

            }
        }
    }

    private void getLeafLocation(Leaf leaf, long currentTime) {
        long intervalTime=currentTime-leaf.startTime;
        mLeafFloatTime=mLeafFloatTime<=0?LEAF_FLOAT_TIME:mLeafFloatTime;
        if (intervalTime<0){
            return;
        }else if (intervalTime>mLeafFloatTime){
            leaf.startTime=System.currentTimeMillis()
                    +new Random().nextInt((int) mLeafFloatTime);
        }
        //部分片段
        float fraction= (float) intervalTime/mLeafFloatTime;
        leaf.x=(int)(mProgressWidth-mProgressWidth*fraction);
        leaf.y=getLocationY(leaf);
    }

    private float getLocationY(Leaf leaf) {
        float w=(float) ((float) 2 * Math.PI / mProgressWidth);
        float a=mMiddleAmplitude;
        switch (leaf.type) {
            case LITTLE:
                // 小振幅 ＝ 中等振幅 － 振幅差
                a = mMiddleAmplitude - mAmplitudeDisparity;
                break;
            case MIDDLE:
                a = mMiddleAmplitude;
            case BIG:
                // 小振幅 ＝ 中等振幅 + 振幅差
                a = mMiddleAmplitude + mAmplitudeDisparity;
                break;
            default:
                break;
        }
        return (int) (a * Math.sin(w * leaf.x)) + mArcRadius * 2 / 3;
    }
}
