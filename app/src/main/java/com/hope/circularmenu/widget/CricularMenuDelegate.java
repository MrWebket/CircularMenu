package com.hope.circularmenu.widget;

import android.view.View;

/**
 * 圆形菜单动画执行监听
 *
 * Created by Hope on 15/12/1.
 */
public interface CricularMenuDelegate {

    public void onStartAnim(View view);

    public void onEndAnim(View view);
}
