package com.example.administrator.canvasdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
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
    private Rect mWhiteRectF, mOrangleRectF, mArcRectF;
    //当前进度
    private int mProgress;
    //所绘制的进度条部分的宽度
    private int mProgressWidth;
    //当前所在的绘制的进度条的位置
    private int mCurrentProgressPosition;
    //弧形的半径
    private int mArcRadius;
    //arc的右上角的x坐标，也就是矩形x坐标的起始点
    private int mRightLocation;
    //用于产生叶子的信息
    private LeafFactory mLeafFactory;
    //产生出的叶子信息
    private List<Leaf> mLeafInfos;

    //用于控制随机增加的时间不报团
    private int mAddTime;


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
          mBitmapPaint=new Paint();
          mBitmapPaint.setAntiAlias(true);
          mBitmapPaint.setDither(true);
    }

    private void initBitmap() {

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
}
