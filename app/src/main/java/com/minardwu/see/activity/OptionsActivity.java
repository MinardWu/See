package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.MultipleAdapter;
import com.minardwu.see.adapter.OptionsAdapter;
import com.minardwu.see.base.ActivityController;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.MultipleView;
import com.minardwu.see.entity.Options;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetUserInfoEvent;
import com.minardwu.see.net.GetUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends BaseActivity {

    private List<MultipleView> mlist;
    private ListView listView;
    private MultipleAdapter multipleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        //获取用户信息
        GetUserInfo.getUserInfoByUserId(AVUser.getCurrentUser().getObjectId());
        //获取好友信息
        if(!Config.me.getFriendid().equals("0")){
            GetUserInfo.getUserInfoByUserId(Config.me.getFriendid());
        }
        EventBus.getDefault().register(this);
    }

    private void initData() {
        mlist= new ArrayList<MultipleView>();
        mlist.add(new MultipleView(0,"我的资料","",Config.myTempAvatarUrl));
        mlist.add(new MultipleView(1,"消息","会有谁呢",""));
        mlist.add(new MultipleView(1,"搜索","又在哪呢",""));
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv_options);
        multipleAdapter = new MultipleAdapter(this,mlist);
        listView.setAdapter(multipleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    startActivity(new Intent(OptionsActivity.this,SettingActivity.class));
                } else if(position == 1){
                    startActivity(new Intent(OptionsActivity.this,NewsActivity.class));
                } else if(position == 2){
                    startActivity(new Intent(OptionsActivity.this,SearchActivity.class));
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserInfoEvent(GetUserInfoEvent event){
        User user = event.getUser();
        if(user.getUserid().equals(AVUser.getCurrentUser().getObjectId())){
            Config.me.setUserid(user.getUserid());
            Config.me.setUsername(user.getUsername());
            Config.me.setSex(user.getSex());
            Config.me.setAvatar(user.getAvatar());
            multipleAdapter.updataItemAvatar(listView,0,Config.me.getAvatar());
        }else if(user.getUserid().equals(Config.me.getFriendid())){
            Config.you.setUserid(user.getUserid());
            Config.you.setUsername(user.getUsername());
            Config.you.setSex(user.getSex());
            Config.you.setAvatar(user.getAvatar());
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_options;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("选项");
    }

    //在消息页面添加好友后返回OptionsActivity，重新获取好友信息
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(OptionsActivity.this, Config.me.getFriendid(), Toast.LENGTH_SHORT).show();
        if(!Config.me.getFriendid().equals("0")){
            GetUserInfo.getUserInfoByUserId(Config.me.getFriendid());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
