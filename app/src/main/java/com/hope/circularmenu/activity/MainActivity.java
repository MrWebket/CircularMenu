package com.hope.circularmenu.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hope.circularmenu.R;
import com.hope.circularmenu.adapter.CricularAdapter;
import com.hope.circularmenu.model.Entity;
import com.hope.circularmenu.widget.CricularMenu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Hope on 15/12/2.
 */
public class MainActivity extends Activity{

    private CricularMenu mCricularMenu;

    private CricularAdapter mCricularAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mCricularMenu = (CricularMenu) findViewById(R.id.cricular_menu);

        mCricularAdapter = new CricularAdapter(this);

        mCricularMenu.setAdapter(mCricularAdapter);

        mCricularAdapter.notifyDataSetChanged(getTestList());
    }

    private List<Entity> getTestList() {
        List<Entity> list = new ArrayList<>();

        Entity entity = new Entity();
        entity.name = "二手房";
        entity.backgroundId = R.drawable.near_2nd_hand_house_bg;
        list.add(entity);

        Entity entity1 = new Entity();
        entity1.name = "二手物品";
        entity1.backgroundId = R.drawable.near_2nd_hand_wupin_bg;
        list.add(entity1);

        Entity entity2 = new Entity();
        entity2.name = "房产";
        entity2.backgroundId = R.drawable.near_fangchan_bg;
        list.add(entity2);

        Entity entity3 = new Entity();
        entity3.name = "老乡交友";
        entity3.backgroundId = R.drawable.near_friends_bg;
        list.add(entity3);

        Entity entity4 = new Entity();
        entity4.name = "家政服务";
        entity4.backgroundId = R.drawable.near_life_serv_bg;
        list.add(entity4);

        Entity entity5 = new Entity();
        entity5.name = "全职工作";
        entity5.backgroundId = R.drawable.near_fulltime_jobs_bg;
        list.add(entity5);


        Entity entity6 = new Entity();
        entity6.name = "兼职工作";
        entity6.backgroundId = R.drawable.near_parttime_jobs_bg;
        list.add(entity6);

        return list;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
