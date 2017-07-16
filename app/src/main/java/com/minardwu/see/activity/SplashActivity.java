package com.minardwu.see.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.event.GetFriendEvent;
import com.minardwu.see.event.LoginEvent;
import com.minardwu.see.net.Friend;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    private SimpleDraweeView splashPic;
    private Timer timer;
    private AVUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        EventBus.getDefault().register(this);

        timer = new Timer(true);

        //1秒之后开始获取数据（如果有的话），这一秒固定用来展示
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentUser = AVUser.getCurrentUser();
                if (currentUser != null) {

                }
            }
        },1000);

        //3秒之后不论如何跳离SplashActivity
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentUser = AVUser.getCurrentUser();
                if (currentUser != null) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            }
        },3000);

    }

    private void initView() {
        splashPic = (SimpleDraweeView) findViewById(R.id.iv_splash);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) splashPic.getLayoutParams();
        params.width = Config.screenWidth;
        params.height = Config.screenHeight;
        splashPic.setLayoutParams(params);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFriendEvent(GetFriendEvent event){
        if(event.getResult()!=null){
            Config.you.setUserid(event.getResult());
        }else {
            Config.you.setUserid("0");
        }
        //获取完数据马上跳转
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
