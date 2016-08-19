package me.wangyuwei.signuptransition;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 作者： 巴掌 on 16/8/19 09:00
 * Github: https://github.com/JeasonWong
 */
public class LoginLoadingView extends View {

    //正常状态
    public static final int STATUS_LOGIN = 0;
    //正在登录中
    public static final int STATUS_LOGGING = 1;
    //登录成功
    public static final int STATUS_LOGIN_SUCCESS = 2;

    private int mWidth, mHeight;
    private Paint mPaint;

    private int mDuration;
    private int mStatus = STATUS_LOGIN;
    //下方线条长度
    private float mLineWidth;
    //成功Text的x坐标
    private float mSuccessTextX;
    //成功Text的文案
    private String mSuccessText = "SUCCESS";
    //登录Text的文案
    private String mLoginText = "SIGN UP";
    //登录Text的alpha值
    private int mLoginTextAlpha;

    public LoginLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mDuration = getResources().getInteger(R.integer.duration);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 18));
        mPaint.setStrokeWidth(DensityUtil.dp2px(getContext(), 3));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStatus) {
            case STATUS_LOGIN:
                canvas.drawText(mLoginText, (mWidth - getTextWidth(mLoginText)) / 2, (mHeight + getTextHeight(mLoginText)) / 2, mPaint);
                break;
            case STATUS_LOGGING:
                canvas.drawText(mLoginText, (mWidth - getTextWidth(mLoginText)) / 2, (mHeight + getTextHeight(mLoginText)) / 2, mPaint);
                canvas.drawLine((mWidth - getTextWidth(mLoginText)) / 2, mHeight, (mWidth - getTextWidth(mLoginText)) / 2 + mLineWidth, mHeight, mPaint);
                break;
            case STATUS_LOGIN_SUCCESS:
                mPaint.setAlpha(mLoginTextAlpha);
                canvas.drawText(mLoginText, mSuccessTextX + getTextWidth(mSuccessText) + DensityUtil.dp2px(getContext(), 10), (mHeight + getTextHeight(mLoginText)) / 2, mPaint);

                mPaint.setAlpha(255 - mLoginTextAlpha);
                canvas.drawText(mSuccessText, mSuccessTextX, (mHeight + getTextHeight(mSuccessText)) / 2, mPaint);

                mPaint.setAlpha(255);
                canvas.drawLine((mWidth - getTextWidth(mSuccessText)) / 2, mHeight, (mWidth + getTextWidth(mSuccessText)) / 2, mHeight, mPaint);
                break;
        }
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(int status) {
        mStatus = status;
        switch (status) {
            case STATUS_LOGIN:

                break;
            case STATUS_LOGGING:
                startLoggingAnim();
                break;
            case STATUS_LOGIN_SUCCESS:
                startLoginSuccessAnim();
                break;
        }
    }

    /**
     * 启动登录动画
     */
    private void startLoggingAnim() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, getTextWidth(mLoginText));
        animator.setDuration(1000);
        animator.setRepeatCount(2);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLineWidth = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    /**
     * 启动登录成功动画
     */
    private void startLoginSuccessAnim() {
        ValueAnimator textXAnim = ValueAnimator.ofFloat(0, (mWidth - getTextWidth(mSuccessText)) / 2);
        textXAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mSuccessTextX = (float) animation.getAnimatedValue();
            }
        });

        ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 0);
        alphaAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mLoginTextAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.playTogether(textXAnim, alphaAnim);
        set.setDuration(mDuration);
        set.setInterpolator(new LinearInterpolator());
        set.start();
    }

    private float getTextHeight(String text) {
        Rect rect = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private float getTextWidth(String text) {
        return mPaint.measureText(text);
    }

}
