package com.softtanck.circleview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * @author : Tanck
 * @Description : TODO
 * @date 7/23/2015
 */
public class TestView extends View {
    private Path mPath;
    private Paint mBackPaint;
    private float mWidth;
    private float PULL_HEIGHT = 200;
    private float springDelta = 0;
    private float oldY;
    private float temp;
    private int mHeight;
    private boolean isOver;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mPath = new Path();

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xff8b90af);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mHeight = 200; //getHeight();
            mWidth = getWidth();
        }
        Log.d("Tanck", "mWidth:" + mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isOver)
            drawDrag(canvas);
        else
            drawSpringUp(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                temp = event.getY() - oldY;
                if (0 < temp && temp < 350) {
                    temp = temp * 0.04f;
                    mHeight += temp;
//                    PULL_HEIGHT += temp;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mHeight > 200) {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(mHeight, 200).setDuration(500);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (Float) animation.getAnimatedValue();
                            Log.d("Tanck", "va:" + value);
                            mHeight = (int) value;
                            invalidate();
                        }
                    });
                    valueAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isOver = true;
                            up();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    valueAnimator.start();
                }
                break;
        }

        return true;
    }


    private void drawSpringUp(Canvas canvas) {
        mPath.reset();
        mPath.moveTo(0, 0);
        mPath.lineTo(0, PULL_HEIGHT);
        mPath.quadTo(mWidth / 2, PULL_HEIGHT - springDelta,
                mWidth, PULL_HEIGHT);
        mPath.lineTo(mWidth, 0);
        canvas.drawPath(mPath, mBackPaint);
    }

    private void drawDrag(Canvas canvas) { //画贝塞尔曲线
        canvas.drawRect(0, 0, mWidth, PULL_HEIGHT, mBackPaint);

        mPath.reset();
        mPath.moveTo(0, PULL_HEIGHT);
        mPath.quadTo(0.5f * mWidth, PULL_HEIGHT + (mHeight - PULL_HEIGHT) * 2,
                mWidth, PULL_HEIGHT);
        canvas.drawPath(mPath, mBackPaint);
    }

    private void up(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,150).setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                springDelta = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                isOver =false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }
}
