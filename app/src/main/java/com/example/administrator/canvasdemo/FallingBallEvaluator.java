package com.example.administrator.canvasdemo;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;

public class FallingBallEvaluator implements TypeEvaluator<Point>{
    private Point point=new Point();
    @Override
    public Point evaluate(float fraction, Point startPoint, Point endPoint) {
        point.x= (int) (startPoint.x+fraction*(endPoint.x-startPoint.x));
        if (fraction*2<=1){
            point.y= (int) (startPoint.y+fraction*2*(endPoint.y-startPoint.y));
        }else {
            point.y=endPoint.y;
        }
        return point;
    }
}
