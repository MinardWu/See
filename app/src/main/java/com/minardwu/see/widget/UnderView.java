package com.minardwu.see.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.minardwu.see.event.ResultCodeEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by MinardWu on 2017/7/11.
 */
public class UnderView extends View {

    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    int mWidth = wm.getDefaultDisplay().getWidth();
    float mStartX;
    View mMoveView;

    public UnderView(Context context) {
        super(context);
    }

    public UnderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void  setView(View view){
        mMoveView = view;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float nx = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mStartX = nx;
                onAnimationEnd();
            case MotionEvent.ACTION_MOVE:
                handleMoveView(nx);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                doTriggerEvent(nx);
                break;
        }
        return true;
    }

    private void handleMoveView(float x) {
        float movex = x - mStartX;
        if (movex < 0)
            movex = 0;
        mMoveView.setTranslationX(movex);

        float mWidthFloat = (float) mWidth;//屏幕显示宽度
        if(getBackground()!=null){
            getBackground().setAlpha((int) ((mWidthFloat - mMoveView.getTranslationX()) / mWidthFloat * 200));//初始透明度的值为200
        }
    }

    private void doTriggerEvent(float x) {
        float movex = x - mStartX;
        if (movex > (mWidth * 0.4)) {
            moveMoveView(mWidth-mMoveView.getLeft(),true);//自动移动到屏幕右边界之外，并finish掉
        } else {
            moveMoveView(-mMoveView.getLeft(),false);//自动移动回初始位置，重新覆盖
        }
    }

    private void moveMoveView(float to,boolean exit){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveView, "translationX", to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(getBackground()!=null){
                    getBackground().setAlpha((int) (((float) mWidth - mMoveView.getTranslationX()) / (float) mWidth * 200));
                }
            }
        });//随移动动画更新背景透明度
        animator.setDuration(250).start();

        if(exit){
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    EventBus.getDefault().post(new ResultCodeEvent(1));
                }
            });
        }//监听动画结束，利用Handler通知Activity退出
    }
}
