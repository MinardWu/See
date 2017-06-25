package com.minardwu.see.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;

import com.minardwu.see.R;
import com.minardwu.see.adapter.MyFragmentPagerAdapter;
import com.minardwu.see.fragment.MyFragment;
import com.minardwu.see.fragment.YourFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements  View.OnClickListener {

    private ViewPager viewPager;
    private YourFragment yourFragment;
    private MyFragment myFragment;
    private List<Fragment> fragmentList;
    private RadioButton rb_your, rb_my;
    private ImageView iv_user, iv_add;
    private View popupView;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initPopupWindow();
    }

    private void initView() {
        rb_your = (RadioButton) findViewById(R.id.rbtn_your);
        rb_my = (RadioButton) findViewById(R.id.rbtn_my);
        iv_user = (ImageView) findViewById(R.id.ibtn_toolbar_user);
        iv_add = (ImageView) findViewById(R.id.ibtn_toolbar_add);

        rb_your.setChecked(true);
        rb_my.setChecked(false);

        rb_your.setOnClickListener(this);
        rb_my.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        iv_add.setOnClickListener(this);

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

    private void initPopupWindow() {
        popupView = getLayoutInflater().inflate(R.layout.popupwindow, null);
        ListView listView = (ListView) popupView.findViewById(R.id.lv_popup);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{"拍照","相册"});
        listView.setAdapter(arrayAdapter);
        mPopupWindow = new PopupWindow(popupView, 130, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.popupwindow_9);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn_toolbar_user:
                startActivity(new Intent(MainActivity.this,OptionsActivity.class));
                break;
            case R.id.ibtn_toolbar_add:
                mPopupWindow.showAsDropDown(view);
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


