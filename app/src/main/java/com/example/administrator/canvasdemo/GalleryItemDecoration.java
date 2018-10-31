package com.example.administrator.canvasdemo;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GalleryItemDecoration extends RecyclerView.ItemDecoration {
    private int pageMargin = 10;//自定义默认item边距
    //第一张图片的左边距
    private int leftPageVisibleWidth = 125;
    private Context context;

    public GalleryItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);//获得当前item的position
        int itemCount = parent.getAdapter().getItemCount();//获得item的数量
        int leftMargin;
        //如果position为0，设置leftMargin为计算后边距，其他为默认边距
        if (position == 0) {
            leftMargin = dp2px(leftPageVisibleWidth);
        } else {
            leftMargin = dp2px(pageMargin);
        }
        int rightMargin;
        //同上，设置最后一张图片
        if (position == itemCount - 1) {
            rightMargin = dp2px(leftPageVisibleWidth);
        } else {
            rightMargin = dp2px(pageMargin);
            ;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        //30和60分别是item到上下的margin
        layoutParams.setMargins(leftMargin, 30, rightMargin, 60);
        view.setLayoutParams(layoutParams);
        super.getItemOffsets(outRect, view, parent, state);
    }

    private int dp2px(int dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;//获得当前屏幕密度
        return (int) (dpValue * scale + 0.5f);
    }

}
