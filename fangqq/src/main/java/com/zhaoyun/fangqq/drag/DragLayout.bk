package com.zhaoyun.fangqq.drag;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.TypeEvaluator;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by zhaoyun on 17-3-13.
 */

public class DragLayout extends FrameLayout {
    public static final String TAG = DragLayout.class.getSimpleName();
    private ViewDragHelper mViewDragHelper;
    private ViewGroup mLeftContent;
    private ViewGroup mMainContent;
    private int mWidth;
    private int mHeight;
    private int mRange;
    private OnDragStatusChangeListener mListener;
    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }

    private Status mStatus = Status.Close;
    public enum Status{
        Open,Close,Draging;
    }

    public interface OnDragStatusChangeListener{
        void onClose();
        void onOpen();
        void onDraging(float percent);
    }

    public void setDragStatusListener(OnDragStatusChangeListener mListener){
        this.mListener = mListener;
    }

    public DragLayout(Context context) {
        this(context,null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //1.创建draghelper对象
        mViewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //2.将拦截事件交给draghelper处理
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try{
            //3.将触摸事件交给draghelper处理，并消费事件
            mViewDragHelper.processTouchEvent(event);
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //4.在界面填充完成后，获取左右布局对象
        if(getChildCount() < 2){
            throw new IllegalStateException("至少包含两个ViewGroup.");
        }
        if(!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("子类必须为ViewGroup的实现类");
        }
        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //5.计算宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //6.设置偏移范围
        mRange = (int) (mWidth * 0.7f);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //根据返回结果决定当前child是否可以拖拽,true可拖拽
            return true;
        }

        //在真正发生偏移之前调用，此方法执行时，还没有偏移，先返回将要偏移的值。
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //9.在将要偏移前修改右布局偏移的值
            if(mMainContent == child){
                left = fixLeft(left);
            }
            return left;
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
            // 返回拖拽的范围, 不对拖拽进行真正的限制. 仅仅决定了动画执行速度
            //此方法必须重写，不然内嵌的listview或有事件冲突
            return mRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.d(TAG, "onViewPositionChanged: left:"+left+" dx:"+dx);
            //7.在移动左布局时，将偏移量传递给右布局，让右布局移动，左布局保持不动
            int newLeft = left;
            if(changedView == mLeftContent){
                newLeft = mMainContent.getLeft() + dx;
                //8.计算偏移量
                newLeft = fixLeft(newLeft);

                mLeftContent.layout(0, 0, mWidth, mHeight);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);


            }

            //14.分发事件，更新状态
            dispatchDragEvent(newLeft);
            // 为了兼容低版本, 每次修改值之后, 进行重绘
            invalidate();
        }

        protected void dispatchDragEvent(int newLeft) {
            float percent = newLeft * 1.0f/ mRange;
            //0.0f -> 1.0f
            Log.d(TAG, "percent: " + percent);

            if(mListener != null){
                mListener.onDraging(percent);
            }

            // 更新状态, 执行回调
            Status preStatus = mStatus;
            mStatus = updateStatus(percent);
            if(mStatus != preStatus){
                // 状态发生变化
                if(mStatus == Status.Close){
                    // 当前变为关闭状态
                    if(mListener != null){
                        mListener.onClose();
                    }
                }else if (mStatus == Status.Open) {
                    if(mListener != null){
                        mListener.onOpen();
                    }
                }
            }
            animViews(newLeft);
        }

        private Status updateStatus(float percent) {
            if(percent == 0f){
                return Status.Close;
            }else if (percent == 1.0f) {
                return Status.Open;
            }
            return Status.Draging;
        }

        /**
         * 当手指抬起释放时,被调用
         * @param releasedChild
         * @param xvel 水平方向速度，向右为正
         * @param yvel 竖直方向速度，向下为正
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //10.实现自动完成打开（关闭）过程
            if(xvel == 0 && mMainContent.getLeft() > mRange / 2){
                open(true);
            }else if(xvel > 0){
                open(true);
            }else{
                close(true);
            }
        }


    };

    private void open(boolean isSmooth) {
        int finalLeft = mRange;
        if(isSmooth){
            //11.平滑移动
            if(mViewDragHelper.smoothSlideViewTo(mMainContent,finalLeft,0)){
                //12.异步重绘动画
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        //13.持续平滑动画 (高频率调用)
        if(mViewDragHelper.continueSettling(true)){
            //如果返回true, 动画还需要继续执行
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void close(boolean isSmooth) {
        int finalLeft = 0;
        if(isSmooth){
            // 1. 触发一个平滑动画
            if(mViewDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)){
                // 返回true代表还没有移动到指定位置, 需要刷新界面.
                // 参数传this(child所在的ViewGroup)
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    public void close(){
        close(true);
    }

    private void animViews(int newLeft) {
        float percent = newLeft * 1.0f / mRange;
        ViewHelper.setScaleX(mMainContent, evaluate(percent,1.0f,0.8f));
        ViewHelper.setScaleY(mMainContent,evaluate(percent,1.0f,0.8f));

        ViewHelper.setScaleX(mLeftContent,evaluate(percent,0.5f,1.0f));
        ViewHelper.setScaleY(mLeftContent,evaluate(percent,0.5f,1.0f));

        getBackground().setColorFilter((Integer) evaluateColor(percent, Color.BLUE,Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    /**
     * 计算向左偏移的值，不超过mRange范围，不小于0
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if(left > mRange){
            left = mRange;
        }else if(left < 0){
            left = 0;
        }
        return left;
    }

    /**
     * 估值器，计算百分比对应两个数字之间的那个数字
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }

    /**
     * 估值器，计算百分比对应两个颜色之间的颜色
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public Object evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24);
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24);
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
    }
}
