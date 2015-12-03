package com.hope.circularmenu.widget;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆形菜单Adapter
 *
 * Created by Hope on 15/12/1.
 */
public abstract class BaseCricularMenuAdapter<T> implements CricularMenuDelegate {

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    protected List<T> mDataSource = new ArrayList<>();

    private Context context;

    protected LayoutInflater mInflater;

    public BaseCricularMenuAdapter(Context context) {
        this.context = context;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onEndAnim(View view) {

    }

    @Override
    public void onStartAnim(View view) {

    }

    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged(List<T> dataList) {
        mDataSource.clear();

        if (dataList != null) {
            mDataSource.addAll(dataList);
        }

        mDataSetObservable.notifyChanged();
    }

    public List<T> getDataSource() {
        return mDataSource;
    }

    protected abstract View getView(int position);
}
