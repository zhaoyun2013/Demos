package com.zhaoyun.test.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by zhaoyun on 17-3-31.
 */

public class GooView extends View {

    private static final String TAG = "GooView";
    private PointF mDragCenter;
    private PointF mStickCenter;
    private float mDragRadius;
    private float mStickRadius;
    private PointF[] mStickPoints;
    private PointF[] mDragPoints;
    private PointF mCenterPonit;
    float farestDistance = 80f;
    private boolean isOutofRange;
    private boolean isDisppaer;

    public GooView(Context context) {
        super(context);

        mDragCenter = new PointF(350f, 350f);
        mStickCenter = new PointF(350f, 350f);
        mDragRadius = 18f;
        mStickRadius = 14f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isDisppaer)
            return;
        float tempStickRadius = getTempStickRadius();
        //计算经过圆心的垂直线的lineK
        float xDiff = mStickCenter.x - mDragCenter.x;
        float yDiff = mStickCenter.y - mDragCenter.y;
        Double lineK = null;
        if (xDiff != 0) {
            lineK = (double) (yDiff / xDiff);
        }
        mDragPoints = GeometryUtil.getIntersectionPoints(mDragCenter, mDragRadius, lineK);
        mStickPoints = GeometryUtil.getIntersectionPoints(mStickCenter, tempStickRadius, lineK);
        mCenterPonit = GeometryUtil.getMiddlePoint(mDragCenter, mStickCenter);


        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        if (!isOutofRange) {
            Path path = new Path();
            path.moveTo(mStickPoints[1].x, mStickPoints[1].y);
            path.quadTo(mCenterPonit.x, mCenterPonit.y, mDragPoints[1].x, mDragPoints[1].y);
            path.lineTo(mDragPoints[0].x, mDragPoints[0].y);
            path.quadTo(mCenterPonit.x, mCenterPonit.y, mStickPoints[0].x, mStickPoints[0].y);
            path.close();
            canvas.drawPath(path, paint);

            //固定圆
            canvas.drawCircle(mStickCenter.x, mStickCenter.y, tempStickRadius, paint);
        }
        // 画附着点(参考用)
        paint.setColor(Color.BLUE);
        canvas.drawCircle(mDragPoints[0].x, mDragPoints[0].y, 3f, paint);
        canvas.drawCircle(mDragPoints[1].x, mDragPoints[1].y, 3f, paint);
        canvas.drawCircle(mStickPoints[0].x, mStickPoints[0].y, 3f, paint);
        canvas.drawCircle(mStickPoints[1].x, mStickPoints[1].y, 3f, paint);
        paint.setColor(Color.RED);

        //画出最大范围圆形（参考用）
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mStickCenter.x, mStickCenter.y, farestDistance, paint);
        paint.setStyle(Paint.Style.FILL);


        //拖拽圆
        canvas.drawCircle(mDragCenter.x, mDragCenter.y, mDragRadius, paint);
    }

    // 获取固定圆半径(根据两圆圆心距离)
    private float getTempStickRadius() {
        float distance = GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);

//		if(distance> farestDistance){
//			distance = farestDistance;
//		}
        distance = Math.min(distance, farestDistance);

        // 0.0f -> 1.0f
        float percent = distance / farestDistance;
        Log.d(TAG, "percent: " + percent);

        // percent , 100% -> 20%
        return evaluate(percent, mStickRadius, mStickRadius * 0.2f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = MotionEventCompat.getActionMasked(event);
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                isOutofRange = false;
                isDisppaer = false;
//                Log.d(TAG, "onTouchEvent: " + event.getX() + "," + event.getY());
                updateDragCenter(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                updateDragCenter(event.getX(), event.getY());

                float distance =  GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                if (distance > farestDistance) {
                    isOutofRange = true;
                }else{
                    //isOutofRange = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isOutofRange){
                    float d =  GeometryUtil.getDistanceBetween2Points(mDragCenter, mStickCenter);
                    if (d > farestDistance) {//松手没有放回去说明要消失
                        isDisppaer = true;
                        invalidate();
                    }else{
                        updateDragCenter(mStickCenter.x,mStickCenter.y);
                    }
                }else{//没有超出范围
                    final PointF tempDragCenter = new PointF(mDragCenter.x,mDragCenter.y);
                    final ValueAnimator mAnim = ValueAnimator.ofFloat(1.0f);
                    mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float fraction = mAnim.getAnimatedFraction();
                            PointF p = GeometryUtil.getPointByPercent(tempDragCenter, mStickCenter, fraction);
                            updateDragCenter(p.x,p.y);
                        }
                    });
                    //回弹效果 4为张力
                    mAnim.setInterpolator(new OvershootInterpolator(5));
                    mAnim.setDuration(1500);
                    mAnim.start();
                }
                break;
        }
        return true;
    }

    private void updateDragCenter(float x, float y) {
        mDragCenter.x = x;
        mDragCenter.y = y;
        invalidate();
    }

    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

}
