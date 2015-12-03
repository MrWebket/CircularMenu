package com.hope.circularmenu.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆形菜单,带动画
 *
 * Created by Hope on 15/12/1.
 */
public class CricularMenu extends FrameLayout implements Handler.Callback{

    private static final int HANDLER_CODE_DELAYED = 1;

    private static final String TAG = CricularMenu.class.getSimpleName();

    private Handler mHandler = null;

    /**
     * 半径
     */
    private float radius;

    private BaseCricularMenuAdapter mAdapter;

    private float mChildAngle;

    private PointF mCenterPointF = new PointF();

    private int mCurrentItem = 0;

    private int mChildWidth;
    private int mChildHeight;

    private List<ChildViewModel> mChildDataSource = new ArrayList<>();

    public CricularMenu(Context context) {
        this(context, null);
    }

    public CricularMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CricularMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init() {
        radius = dip2px(getContext(), 100);

        mChildWidth = dip2px(getContext(), 50);
        mChildHeight = dip2px(getContext(), 50);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mHandler = new Handler(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }


    public void setAdapter(BaseCricularMenuAdapter adapter) {
        if(adapter == null) {
            return;
        }

        this.mAdapter = adapter;
        this.mAdapter.registerDataSetObserver(new AdapterDataSetObserver());
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case HANDLER_CODE_DELAYED:
                if (mChildDataSource != null && mChildDataSource.size() > 0 && mChildDataSource.size() > mCurrentItem) {

                    ChildViewModel entity = (ChildViewModel) msg.obj;
                    new ChildAnimation(entity).start();

                    mCurrentItem += 1;

                    if(mChildDataSource.size() > mCurrentItem) {
                        sendMessageToNext();
                    }
                    break;
                }
        }
        return false;
    }

    private void sendMessageToNext() {
        final ChildViewModel entity = mChildDataSource.get(mCurrentItem);

        Message msg = Message.obtain();
        msg.what = HANDLER_CODE_DELAYED;
        msg.obj = entity;

        if (mHandler != null) {
            mHandler.sendMessageDelayed(msg, 150);
        }
    }

    private class AdapterDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            refreshViews();
        }
    }


    private boolean mInLayout = false;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mInLayout = true;

        if( mCenterPointF.x == 0) {
            mCenterPointF.x = getWidth() / 2;
            mCenterPointF.y = getHeight() / 2;

            if(mInRefresh) {
                refreshViews();
            }
        }
    }


    private boolean mInRefresh = false;

    private void refreshViews() {
        mInRefresh = true;
        removeAllViews();
        mChildDataSource.clear();

        if(mInLayout && mAdapter != null
                && mAdapter.getDataSource() != null && mAdapter.getDataSource().size() > 0) {
            mInRefresh = false;

            int totalChildSize = mAdapter.getDataSource().size();
            mChildAngle = 360 / totalChildSize;

            float totalAngle = mChildAngle;
            for (int i = 0; i < totalChildSize; i++) {

                View childView = mAdapter.getView(i);
                childView.setVisibility(View.INVISIBLE);

                childView.measure(mChildWidth, mChildHeight);

                double radian = ((2 * Math.PI / 360) * totalAngle);

                int x = (int) (mCenterPointF.x + Math.sin(radian) * radius);
                int y = (int) (mCenterPointF.y - Math.cos(radian) * radius);

                if(childView.getLayoutParams() == null) {
                    childView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }

                MarginLayoutParams childLayoutParams = (MarginLayoutParams) childView.getLayoutParams();
                int left = x - mChildWidth / 2;
                int top = y - mChildHeight;

                childLayoutParams.leftMargin = left;
                childLayoutParams.topMargin = top;

                ChildViewModel mChildViewEntity = new ChildViewModel();

                mChildViewEntity.view = childView;
                mChildViewEntity.mPointF.x = x;
                mChildViewEntity.mPointF.y = y;
                mChildViewEntity.leftMargin = left;
                mChildViewEntity.topMargin = top;
                mChildViewEntity.width = mChildWidth;
                mChildViewEntity.height = mChildHeight;

                mChildDataSource.add(mChildViewEntity);

                totalAngle += mChildAngle;

                addView(childView);
            }
        }

        translateAnimation();
    }

    private void translateAnimation() {
        if(mChildDataSource != null && mChildDataSource.size() > 0) {
            mCurrentItem = 0;

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessageToNext();
                }
            }, 50);
        }
    }

    private class ChildAnimation extends AnimationSet implements Animation.AnimationListener{
        private static final int ANIM_DURATION = 100;

        private ChildViewModel mChildView;

        private TranslateAnimation mTranslateAnim;

        public ChildAnimation(ChildViewModel childView) {
            super(true);

            this.mChildView = childView;

            ScaleAnimation scaleAnim = new ScaleAnimation(0.0F, 1.0F,
                    0.0F, 1.0F,
                    Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5F);
            addAnimation(scaleAnim);

            mTranslateAnim = new TranslateAnimation(Animation.ABSOLUTE, -(mChildView.leftMargin - mCenterPointF.x + mChildView.width / 2),
                    Animation.ABSOLUTE, 0,
                    Animation.ABSOLUTE, -(mChildView.topMargin - mCenterPointF.y + mChildView.height), Animation.ABSOLUTE, 0);

            addAnimation(mTranslateAnim);

            setInterpolator(new DecelerateInterpolator());
            setDuration(ANIM_DURATION);
            setFillAfter(true);

            if(mChildView.view.getAnimation() != null) {
                mChildView.view.clearAnimation();
            }

            mChildView.view.setAnimation(this);
            mChildView.view.setVisibility(View.VISIBLE);

            setAnimationListener(this);
        }

        @Override
        public void onAnimationStart(Animation animation) {
            mChildView.view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mChildView.view.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    /**
     * 返回v.measure(int, int)可识别的宽度
     *
     * @param v 测量View
     * @return int
     */
    public int getWidthToPixel(View v) {
        int lpw = 0, w = 0;
        if (v.getLayoutParams() != null) {
            lpw = v.getLayoutParams().width;
        }
        if (lpw == ViewGroup.LayoutParams.MATCH_PARENT) {
            w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST);
        } else if (lpw == ViewGroup.LayoutParams.WRAP_CONTENT) {
            if (v.getMeasuredHeight() <= 0) {
                w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            } else {
                w = View.MeasureSpec.makeMeasureSpec(v.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
            }
        } else {
            w = View.MeasureSpec.makeMeasureSpec(lpw, View.MeasureSpec.EXACTLY);
        }
        return w;
    }

    /**
     * 返回v.measure(int, int)可识别的高度
     *
     * @param v 测量View
     * @return int
     */
    public int getHeightToPixel(View v) {
        int lph = 0, h = 0;
        if (v.getLayoutParams() != null) {
            lph = v.getLayoutParams().height;
        }
        if (lph == ViewGroup.LayoutParams.MATCH_PARENT) {
            h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST);
        } else if (lph == ViewGroup.LayoutParams.WRAP_CONTENT) {
            if (v.getMeasuredHeight() <= 0) {
                h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            } else {
                h = View.MeasureSpec.makeMeasureSpec(v.getMeasuredHeight(), View.MeasureSpec.EXACTLY);
            }
        } else {
            h = View.MeasureSpec.makeMeasureSpec(lph, View.MeasureSpec.EXACTLY);
        }
        return h;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dipValue
     * @return
     */
    private int dip2px(Context context, float dipValue) {
        if (context == null) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private class ChildViewModel {
        private View view;
        private PointF mPointF = new PointF();
        private int leftMargin;
        private int topMargin;
        private int width;
        private int height;
    }

}
