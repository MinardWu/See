package com.minardwu.see.activity;

import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetUserInfoEvent;
import com.minardwu.see.net.GetUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView tv_info;
    private SimpleDraweeView iv_avatar;
    private TextView tv_username;
    private Button btn_add;
    private LinearLayout ll_result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_search);
        setSupportActionBar(toolbar);
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        tv_info = (TextView) findViewById(R.id.tv_search_info);
        iv_avatar = (SimpleDraweeView) findViewById(R.id.iv_search_image);
        tv_username = (TextView) findViewById(R.id.tv_search_username);
        btn_add = (Button) findViewById(R.id.btn_search_add);
        ll_result = (LinearLayout) findViewById(R.id.ll_search_result);

        tv_info.setText("开始寻找那个人吧!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.equals(Config.me.getUsername())){
                    tv_info.setText("哗啦啦啦，天在下雨。");
                }else {
                    GetUserInfo.getUserInfoByUserName(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserInfoEvent(GetUserInfoEvent event){
        User user = event.getUser();
        if(user.getUserid().equals("0")){
            ll_result.setVisibility(View.GONE);
            tv_info.setVisibility(View.VISIBLE);
            tv_info.setText("找不到该用户");
        }else {
            tv_info.setVisibility(View.GONE);
            ll_result.setVisibility(View.VISIBLE);
            tv_username.setText(user.getUsername());
            iv_avatar.setImageURI(Uri.parse(user.getAvatar()));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                this.finish();
            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
