package com.hope.circularmenu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 圆形菜单,带动画
 *
 * Created by Hope on 15/12/1.
 */
public class CricularMenu extends ViewGroup {

    private float radius;

    public CricularMenu(Context context) {
        this(context, null);
    }

    public CricularMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CricularMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init() {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
