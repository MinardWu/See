package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.UserForQQ;
import com.minardwu.see.event.LoginEvent;
import com.minardwu.see.event.LoginWithQQEvent;
import com.minardwu.see.net.Login;
import com.minardwu.see.net.LoginWithQQ;
import com.minardwu.see.util.FontUtil;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private MaterialEditText et_username;
    private MaterialEditText et_password;
    private Button btn_login;
    private TextView tv_logo;
    private TextView tv_noaccount;
    private TextView tv_forgetPassword;
    private ImageView iv_qq;

    private LoginListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        EventBus.getDefault().register(this);

        listener = new LoginListener();
    }

    private void initView() {
        et_username = (MaterialEditText) findViewById(R.id.et_username);
        et_password = (MaterialEditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_logo = (TextView) findViewById(R.id.tv_logo);
        tv_noaccount = (TextView) findViewById(R.id.tv_noaccount);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);

        btn_login.setOnClickListener(this);
        tv_noaccount.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        iv_qq.setOnClickListener(this);

        tv_logo.setTypeface(FontUtil.getLogoTypeFace());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if(et_username.getText().toString().length() == 0){
                    et_username.setError("请输入用户名");
                }else if(et_password.getText().toString().length() == 0){
                    et_password.setError("请输入密码");
                }else {
                    btn_login.setText("登录中...");
                    btn_login.setEnabled(false);
                    Login.login(et_username.getText().toString(),et_password.getText().toString());
                }
                break;
            case R.id.tv_noaccount:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.tv_forgetPassword:

                break;
            case R.id.iv_qq:
                if (!MyApplication.tencent.isSessionValid()) {
                    MyApplication.tencent.login(LoginActivity.this, "all", listener);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event){
        if(event.getResult()==1){
            Config.loginWithQQ= false;
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            this.finish();
        }else{
            btn_login.setText("登录");
            btn_login.setEnabled(true);
            if(event.getResult()==-1){
                et_password.setError("密码错误");
            }else if(event.getResult()==-2){
                et_username.setError("该用户不存在");
            }else if(event.getResult()==-3){
                Toast.makeText(this,"登陆出错了",Toast.LENGTH_LONG).show();
            }
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginWithQQEvent(LoginWithQQEvent event){
        if(event.getResult()==1||event.getResult()==2){
            Config.loginWithQQ= true;
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            this.finish();
        }else{
            Toast.makeText(this,"QQ授权出错了",Toast.LENGTH_LONG).show();
        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private class LoginListener implements IUiListener {
        @Override
        public void onComplete(Object o) {
            Toast.makeText(LoginActivity.this, "授权成功！", Toast.LENGTH_LONG).show();
            JSONObject jsonObject = (JSONObject) o;
            getUserInfo(jsonObject);
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, "授权出错！", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this, "授权取消！", Toast.LENGTH_LONG).show();
        }
    }


    private void getUserInfo(JSONObject jsonObject) {
        final UserForQQ userForQQ = new UserForQQ();
        //第一步设置openid和token，否则获取不到下面的信息
        try {
            userForQQ.setQqID(jsonObject.getString("openid"));
            Log.v("LoginWithQQ",jsonObject.getString("openid"));
            String openid = jsonObject.getString("openid");
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");
            MyApplication.tencent.setAccessToken(token, expires);
            MyApplication.tencent.setOpenId(openid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //第二步获取QQ用户的各信息,sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息
        QQToken mQQToken = MyApplication.tencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(final Object o) {
                JSONObject userInfoJson = (JSONObject) o;
                //"ret": 0,
                //"msg": "",
                //"is_lost": 0,
                //"nickname": "High!",
                //"gender": "男",
                //"province": "天津",
                //"city": "河西",
                //"figureurl": "http://qzapp.qlogo.cn/qzapp/1105547524/19E8D43EB75ED256CAC70C02953F188A/30",
                //"figureurl_1": "http://qzapp.qlogo.cn/qzapp/1105547524/19E8D43EB75ED256CAC70C02953F188A/50",
                //"figureurl_2": "http://qzapp.qlogo.cn/qzapp/1105547524/19E8D43EB75ED256CAC70C02953F188A/100",
                //"figureurl_qq_1": "http://q.qlogo.cn/qqapp/1105547524/19E8D43EB75ED256CAC70C02953F188A/40",
                //"figureurl_qq_2": "http://q.qlogo.cn/qqapp/1105547524/19E8D43EB75ED256CAC70C02953F188A/100",
                //"is_yellow_vip": "0",
                //"vip": "0",
                //"yellow_vip_level": "0",
                //"level": "0",
                //"is_yellow_year_vip": "0"
                try {
                    Log.v("LoginWithQQ",userInfoJson.getString("nickname"));
                    Log.v("LoginWithQQ",userInfoJson.getString("figureurl_qq_2"));
                    Log.v("LoginWithQQ",userInfoJson.getString("gender"));
                    userForQQ.setUsername(userInfoJson.getString("nickname"));
                    userForQQ.setAvatarUrl(userInfoJson.getString("figureurl_qq_2"));
                    userForQQ.setSex(userInfoJson.getString("gender"));
                    LoginWithQQ.login(userForQQ);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(UiError uiError) {
                Log.e("LoginWithQQ", "获取qq用户信息错误");
                Toast.makeText(LoginActivity.this, "获取qq用户信息错误"+uiError.errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.e("LoginWithQQ", "获取qq用户信息取消");
                Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }
}
