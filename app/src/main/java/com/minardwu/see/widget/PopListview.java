package com.minardwu.see.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * Created by MinardWu on 2017/7/10.
 */
public class PopListview extends ListView{

    public PopListview(Context context) {
        super(context);
    }

    public PopListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = meathureWidthByChilds() + getPaddingLeft() + getPaddingRight();
        super.onMeasure(MeasureSpec.makeMeasureSpec(maxWidth,MeasureSpec.UNSPECIFIED),heightMeasureSpec);//注意，这个地方一定是MeasureSpec.UNSPECIFIED
    }
    public int meathureWidthByChilds() {
        int maxWidth = 0;
        View view = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            view = getAdapter().getView(i, view, this);
            view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (view.getMeasuredWidth() > maxWidth){
                maxWidth = view.getMeasuredWidth();
            }
            view = null;
        }
        return maxWidth;
    }
}
