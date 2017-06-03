package com.zhaoyun.test.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zhaoyun on 17-4-6.
 */

public class PianoView extends View {

    private static final String TAG = PianoView.class.getSimpleName();
    private int mWidth;
    private int mHeight;
    private int columns = 4;
    private int rows = 4;
    private Paint mPaint;
    private float mCellWidth;
    private float mCellHeight;

    public PianoView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        drawPiano(canvas);
        
    }

    private void drawPiano(Canvas canvas) {

//        mPaint.setColor(Color.WHITE);
//        Rect screen = new Rect(0,(int) -mCellHeight,mWidth,mHeight);
//        canvas.drawRect(screen,mPaint);

        for(int j = 0;j <= rows;j++) {
            //随机1个变黑格子的索引
            int randomIndex = (int) (Math.random()*100 % (columns - 1));
            for (int i = 0; i < columns; i++) {
                mPaint.setColor(randomIndex == i ? Color.BLACK : Color.WHITE);
                mPaint.setStyle(Paint.Style.FILL);
                int left = (int) (mCellWidth * i);
                int top = (int) (mHeight - mCellHeight*j - mCellHeight);
                int right = (int) (left + mCellWidth);
                int bottom = (int) (mHeight - mCellHeight*j);

                canvas.drawRect(new Rect(left, top, right, bottom), mPaint);
                mPaint.setColor(Color.BLACK);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawRect(new Rect(left, top, right, bottom), mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                verticalRun();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void verticalRun()
    {
        ValueAnimator animator = ValueAnimator.ofFloat(0, mHeight);
        animator.setTarget(this);
        animator.setDuration(8000).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
//                Log.d(TAG, "animation.getAnimatedValue: "+animation.getAnimatedValue());
                setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        Log.d(TAG, "onSizeChanged: mWidth:"+mWidth+"**mHeight:"+mHeight);
        mCellWidth = mWidth/columns;
        mCellHeight = mHeight/rows;
        Log.d(TAG, "onSizeChanged: mCellWidth:"+mCellWidth+"**mCellHeight:"+mCellHeight);
    }


}
