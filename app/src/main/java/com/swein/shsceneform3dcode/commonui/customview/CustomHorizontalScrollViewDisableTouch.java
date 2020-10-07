package com.swein.shsceneform3dcode.commonui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class CustomHorizontalScrollViewDisableTouch extends HorizontalScrollView {


    public CustomHorizontalScrollViewDisableTouch(Context context) {
        super(context);
    }

    public CustomHorizontalScrollViewDisableTouch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomHorizontalScrollViewDisableTouch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomHorizontalScrollViewDisableTouch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

}
