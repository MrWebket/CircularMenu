package com.hope.circularmenu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hope.circularmenu.R;
import com.hope.circularmenu.model.Entity;
import com.hope.circularmenu.widget.BaseCricularMenuAdapter;

/**
 * 圆形菜单Adapter
 *
 * Created by Hope on 15/12/2.
 */
public class CricularAdapter extends BaseCricularMenuAdapter<Entity> {

    public CricularAdapter(Context context) {
        super(context);
    }

    @Override
    protected View getView(int position) {

        Entity entity = mDataSource.get(position);
        View contentView = mInflater.inflate(R.layout.item, null);

        TextView cententTv = (TextView) contentView.findViewById(R.id.centent_tv);
        cententTv.setText(entity.name);

        ImageView image = (ImageView) contentView.findViewById(R.id.item_image);
        image.setBackgroundResource(entity.backgroundId);
        return contentView;
    }
}
