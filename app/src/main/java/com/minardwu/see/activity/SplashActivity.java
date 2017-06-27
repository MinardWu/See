package com.minardwu.see.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.Config;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SimpleDraweeView splashPic = (SimpleDraweeView) findViewById(R.id.iv_splash);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) splashPic.getLayoutParams();
        params.width = Config.screenWidth;
        params.height = Config.screenHeight;
        splashPic.setLayoutParams(params);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                AVUser currentUser = AVUser.getCurrentUser();
                if (currentUser != null) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }

            }
        }).start();
    }
}
