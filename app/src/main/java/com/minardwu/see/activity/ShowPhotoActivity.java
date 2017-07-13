package com.minardwu.see.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.minardwu.see.R;
import com.minardwu.see.adapter.MyFragmentPagerAdapter;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.PageChangeEvent;
import com.minardwu.see.fragment.ShowPhotoFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class ShowPhotoActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_photo);

        Bundle bundle = getIntent().getExtras();
        int type = bundle.getInt("type");
        int position = bundle.getInt("position");

        fragmentList = new ArrayList<Fragment>();
        if(type==0){
            fragmentList.clear();
            for(Photo photo:Config.yourPhotos)
                fragmentList.add(new ShowPhotoFragment(type,photo));
        }else if(type==1){
            fragmentList.clear();
            for(Photo photo:Config.myPhotos)
                fragmentList.add(new ShowPhotoFragment(type,photo));
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(position);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //页面跳转完后调用
            @Override
            public void onPageSelected(int position) {
                EventBus.getDefault().post(new PageChangeEvent(1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        for(Photo tempphoto:Config.yourPhotos){
            Log.v("dsafhiusdfa",tempphoto.getPhotoid()+"-->"+tempphoto.getState()+"");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
