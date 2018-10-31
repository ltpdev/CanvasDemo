package com.example.administrator.canvasdemo;

import android.animation.TypeEvaluator;

public class MyFloatTypeEvaluator implements TypeEvaluator<Float>{
    private float width;

    public MyFloatTypeEvaluator(float width) {
        this.width = width;
    }

    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        float startInt = startValue;
         float curX=startInt + fraction * (endValue - startInt);
         return curX%width;
    }
}
