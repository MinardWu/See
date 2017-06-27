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

public class SplashActivity extends AppCompatActivity {

    SimpleDraweeView splashPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        EventBus.getDefault().register(this);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//                AVUser currentUser = AVUser.getCurrentUser();
//                if (currentUser != null) {
//                    Friend.getFriendid();
//                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                    finish();
//                } else {
//                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
//                    finish();
//                }
//
//            }
//        }).start();

        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            Friend.getFriendid();
        } else {
            startActivity(new Intent(SplashActivity.this,LoginActivity.class));
            finish();
        }
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
            Config.me.setFriendid(event.getResult());
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
