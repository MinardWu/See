package com.minardwu.see.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.minardwu.see.R;
import com.minardwu.see.adapter.MyFragmentPagerAdapter;
import com.minardwu.see.fragment.MyFragment;
import com.minardwu.see.fragment.YourFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements YourFragment.OnFragmentInteractionListener,MyFragment.OnFragmentInteractionListener, View.OnClickListener {

    private ViewPager viewPager;
    private YourFragment yourFragment;
    private MyFragment myFragment;
    private List<Fragment> fragmentList;
    private RadioButton rb_your, rb_my;
    private ImageButton ibtn_user, ibtn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        rb_your = (RadioButton) findViewById(R.id.rbtn_your);
        rb_my = (RadioButton) findViewById(R.id.rbtn_my);
        ibtn_user = (ImageButton) findViewById(R.id.ibtn_toolbar_user);
        ibtn_add = (ImageButton) findViewById(R.id.ibtn_toolbar_add);

        rb_your.setChecked(true);
        rb_my.setChecked(false);

        rb_your.setOnClickListener(this);
        rb_my.setOnClickListener(this);
        ibtn_user.setOnClickListener(this);
        ibtn_add.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        yourFragment = new YourFragment();
        myFragment = new MyFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(yourFragment);
        fragmentList.add(myFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_your.setChecked(true);
                        rb_my.setChecked(false);
                        break;
                    case 1:
                        rb_your.setChecked(false);
                        rb_my.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn_toolbar_user:

                break;
            case R.id.ibtn_toolbar_add:

                break;
            case R.id.rbtn_your:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rbtn_my:
                viewPager.setCurrentItem(1);
                break;
        }
    }
}


