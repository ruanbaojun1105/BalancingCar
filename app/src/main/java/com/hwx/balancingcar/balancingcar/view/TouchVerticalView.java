package com.hwx.balancingcar.balancingcar.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.hwx.balancingcar.balancingcar.App;
import com.hwx.balancingcar.balancingcar.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

/**
 * 方向盘控件。
 */
public class TouchVerticalView extends View {
    private static final String TAG = "SteeringWheelView";
    /**
     * 当前方向无效，方向盘没有触摸时处于该状态
     */
    public static final int INVALID = -1;
    /**
     * 外部监听器
     */
    private SteeringWheelListener mListener;
    private static final int mDefaultWidthDp = 200;
    private static final int mDefaultHeightDp = 200;
    /**
     * 当采用wrap_content测量模式时，默认宽度
     */
    private static int mDefaultWidth;
    /**
     * 当采用wrap_content测量模式时，默认高度
     */
    private static int mDefaultHeight;
    /**
     * 画笔对象
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint();
    /**
     * 画笔的颜色
     */
    private int mColor;
    /**
     * 当前中心X
     */
    private float mCenterX;
    /**
     * 当前中心Y
     */
    private float mCenterY;
    /**
     * 球
     */
    private Drawable mBallDrawable;
    /**
     * 被按下后，球的图片
     */
    private Drawable mBallPressedDrawable;
    /**
     * 当前球中心X坐标
     */
    private float mBallCenterX;
    /**
     * 当前球中心Y坐标
     */
    private float mBallCenterY;
    /**
     * 球的半径
     */
    private float mBallRadius;
    /**
     * 当前半径
     */
    private float mRadius;
    /**
     * 当前偏离中心的百分比，取值为 0 - 100
     */
    private int mPower;
    /**
     * 通知的时间最小间隔
     */
    private long mNotifyInterval = 0;
    /**
     * 通知者
     */
    private Runnable mNotifyRunnable;
    /**
     * 上次通知监听者的时间
     */
    private long mLastNotifyTime;
    /**
     * 当前方向
     */
    private int mDirection = INVALID;
    private Drawable mBgDrawable;
    /**
     * 回弹动画
     */
    private ObjectAnimator mAnimator;

    /**
     * 时间插值器
     */
    private TimeInterpolator mInterpolator;
    private boolean mWasTouched;

    /**
     * 获取球X坐标
     *
     * @return 球X坐标
     */
    public float getBallX() {
        return mBallCenterX;
    }

    /**
     * 设置球X坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballX 球X坐标
     */
    public void setBallX(float ballX) {
        if (ballX != mBallCenterX) {
            mBallCenterX = ballX;
            updatePower();
            invalidate();
            notifyStatusChanged();
        }
    }

    /**
     * 获取球Y坐标
     *
     * @return 球Y坐标
     */
    public float getBallY() {
        return mBallCenterY;
    }

    /**
     * 设置球Y坐标。目前该API的执行时机为Choreographer中每帧中的动画阶段,由底层动画框架反射调用
     *
     * @param ballY 球Y坐标
     */
    public void setBallY(float ballY) {
        if (mBallCenterY != ballY) {
            mBallCenterY = ballY;
            updatePower();
            invalidate();
            notifyStatusChanged();
        }
    }

    public TouchVerticalView(Context context) {
        super(context);
        init(null, 0);
    }

    public TouchVerticalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TouchVerticalView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SteeringWheelView);
        //读取XML配置
        mColor = a.getColor(R.styleable.SteeringWheelView_ballColor, Color.RED);
        mBgDrawable = a.getDrawable(R.styleable.SteeringWheelView_arrowBg);

        //mBallDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballSrc);
        mBallDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_dot_circle_o).colorRes(R.color.blue_ael).sizeDp(25);
        //mBallPressedDrawable = a.getDrawable(R.styleable.SteeringWheelView_ballPressedSrc);
        mBallPressedDrawable=new IconDrawable(getContext(), FontAwesomeIcons.fa_circle).colorRes(R.color.red_ael2).sizeDp(35);
        a.recycle();
        mBallRadius = mBallDrawable.getIntrinsicWidth() >> 1;
        mDefaultWidth = App.dip2px( mDefaultWidthDp);
        mDefaultHeight = App.dip2px(mDefaultHeightDp);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(App.dip2px(5));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(App.dip2px(15));
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure: ");
        //handle wrap_content
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        //解析上层ViewGroup传下来的数据，高两位是模式，低30位是大小
        //主要需要特殊处理wrap_content情形
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mDefaultHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: w=" + w + "#h=" + h + "#oldw=" + oldw + "#oldh=" + oldh);
        //在layout过程中会回调该方法
        //handle padding
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        mRadius = (Math.max(width, height) >> 1)-(mBallRadius*2f);
        mBallCenterX = mCenterX = paddingLeft + (width >> 1);
        mBallCenterY = mCenterY = paddingTop + (height >> 1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画横线
        //canvas.drawLine(mCenterX - mRadius, mCenterY, mCenterX + mRadius, mCenterY, mPaint);
        //画竖线
        RectF rectF=new RectF(mCenterX, mCenterY - mRadius, mCenterX, mCenterY + mRadius);
        canvas.drawOval(rectF, mPaint);
        //画大圆
        //canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        //画背景
        //drawBigBall(canvas);
        //画球
        drawBall(canvas);
        canvas.drawText(String.valueOf(mPower),mCenterX,mBallCenterY+10,mTextPaint);
    }

    private void drawBall(Canvas canvas) {
        Drawable drawable;
        //point to the right drawable instance
        if (mWasTouched) {
            drawable = mBallPressedDrawable;
        } else {
            drawable = mBallDrawable;
        }
        drawable.setBounds((int) (mBallCenterX - (mWasTouched?(float) drawable.getIntrinsicWidth():((float) drawable.getIntrinsicWidth()/1.5f))),
                (int) (mBallCenterY - (mWasTouched?(float) drawable.getIntrinsicHeight():((float) drawable.getIntrinsicHeight()/1.5f))),
                (int) (mBallCenterX + (mWasTouched?(float) drawable.getIntrinsicWidth():((float) drawable.getIntrinsicWidth()/1.5f))),
                (int) (mBallCenterY + (mWasTouched?(float) drawable.getIntrinsicHeight():((float) drawable.getIntrinsicHeight()/1.5f))));
//        drawable.setBounds((int) (mBallCenterX - ((float) drawable.getIntrinsicWidth())),
//                (int) (mBallCenterY - ((float) drawable.getIntrinsicHeight())),
//                (int) (mBallCenterX + ((float) drawable.getIntrinsicWidth())),
//                (int) (mBallCenterY + (float) drawable.getIntrinsicHeight()));
        drawable.draw(canvas);
    }
    private void drawBigBall(Canvas canvas) {
        //Drawable drawable=getResources().getDrawable(R.drawable.rocker_base);
        //point to the right drawable instance
        mBgDrawable.setBounds((int) (mCenterX - mRadius),
                (int) (mCenterY - mRadius),
                (int) (mCenterX + mRadius),
                (int) (mCenterY + mRadius));
        mBgDrawable.draw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mWasTouched = checkTouch(y);
                if (!mWasTouched)
                    return false;
                if (mAnimator != null && mAnimator.isRunning()) {
                    //在本次触摸事件序列中，如果上一个复位动画还没执行完毕，则需要取消动画，及时响应用户输入
                    mAnimator.cancel();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                updateBallData(x, y);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                mWasTouched = false;
                resetBall();
                break;
            }
            default:
                break;
        }

        notifyStatusChanged();
        return true;
    }

    private boolean checkTouch(int y) {
        //x+=mArrowRightDrawable.getIntrinsicWidth();
        //y+=mArrowRightDrawable.getIntrinsicWidth();
        //int mPower = (int) (100 * Math.sqrt(Math.pow(x - mCenterX, 2) + Math.pow(y - mCenterY, 2)) / (mRadius - mBallRadius));
        Log.e("xxxxxx：",y-mCenterY+"-------------"+mBallDrawable.getIntrinsicWidth());
        return Math.abs(y-mCenterY)<mBallDrawable.getIntrinsicWidth();
    }

    /**
     * 指定球回弹动画时间插值器
     *
     * @param value 插值器
     */
    public TouchVerticalView interpolator(TimeInterpolator value) {
        if (value != null) {
            mInterpolator = value;
        } else {
            mInterpolator = new OvershootInterpolator();
        }
        return this;
    }

    private TimeInterpolator getInterpolator() {
        if (mInterpolator == null) {
            mInterpolator = new OvershootInterpolator();
        }
        return mInterpolator;
    }

    /**
     * 弹性滑动
     */
    private void resetBall() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("BallX", mBallCenterX, mCenterX);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("BallY", mBallCenterY, mCenterY);
        mAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(150);
        mAnimator.setInterpolator(getInterpolator());
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mPower = 0;
                mDirection = INVALID;
                notifyStatusChanged();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }

    private void updateBallData(int x, int y) {
        //mBallCenterX = x;
        mBallCenterY = y;
        boolean outOfRange = outOfRange(x, y);
        if (outOfRange) {
            mBallCenterY =mCenterY+(mBallCenterY-mCenterY>0?mRadius:-mRadius);
        }
        updatePower();
        invalidate();
    }

    private void updatePower() {
        mPower = (int) (100 * Math.sqrt(Math.pow(mBallCenterX - mCenterX, 2) + Math.pow(mBallCenterY - mCenterY, 2)) / (mRadius));
        Log.d(TAG, "updatePower: mPower = " + mPower);
    }

    private boolean outOfRange(int newX, int newY) {
        Log.d(TAG, "updateoutOfRange" + (Math.pow(newX - mCenterX, 2) + Math.pow(newY - mCenterY, 2)) +"--------"+ Math.pow(mRadius+mBallRadius, 2));
        return (Math.pow(newX - mCenterX, 2) + Math.pow(newY - mCenterY, 2)) > Math.pow(mRadius+mBallRadius, 2);
    }


    /**
     * 通知监听者方向盘状态改变
     */
    private void notifyStatusChanged() {
        if (mListener == null)
            return;

        long delay = 0;
        if (mNotifyRunnable == null) {
            mNotifyRunnable = createNotifyRunnable();
        } else {
            long now = System.currentTimeMillis();
            if (now - mLastNotifyTime < mNotifyInterval) {
                //移除旧消息
                removeCallbacks(mNotifyRunnable);
                delay = mNotifyInterval - (now - mLastNotifyTime);
            }
        }

        postDelayed(mNotifyRunnable, delay);
    }

    private Runnable createNotifyRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: ");
                mLastNotifyTime = System.currentTimeMillis();
                //取当前数据，而非过去数据的snapshot
                mListener.onStatusChanged(TouchVerticalView.this, mPower);
            }
        };
    }

    /**
     * 设置回调时间间隔
     *
     * @param interval 回调时间间隔
     */
    public TouchVerticalView notifyInterval(long interval) {
        if (interval < 0) {
            throw new RuntimeException("notifyInterval interval < 0 is not accept");
        }

        mNotifyInterval = interval;
        return this;
    }

    /**
     * 设置监听器
     *
     * @param listener 监听器对象
     */
    public TouchVerticalView listener(SteeringWheelListener listener) {
        mListener = listener;
        return this;
    }

    interface SteeringWheelListener {
        /**
         * 方向盘状态改变的回调
         *
         * @param view      方向盘实例对象
         * @param power     方向上的力度。范围0-100
         */
        void onStatusChanged(TouchVerticalView view, int power);
    }
}
