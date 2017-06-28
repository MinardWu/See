package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

    private List<Options> list;
    private List<MultipleView> mlist;
    private ListView listView;
    private OptionsAdapter optionsAdapter;
    private MultipleAdapter multipleAdapter;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        Log.d("getUserInfoByUserId",AVUser.getCurrentUser().getObjectId());
        Log.d("getUserInfoByUserId",Config.me.getFriendid());
        if(Config.me.getAvatar()!=null){
            Config.myTempAvatarUrl=Config.me.getAvatar();
        }
        if(Config.you.getAvatar()!=null){
            Config.yourTempAvatarUrl=Config.you.getAvatar();
        }
        GetUserInfo.getUserInfoByUserId(AVUser.getCurrentUser().getObjectId());
        GetUserInfo.getUserInfoByUserId(Config.me.getFriendid());
        EventBus.getDefault().register(this);
    }

    private void initData() {
        mlist= new ArrayList<MultipleView>();
        mlist.add(new MultipleView(0,"我的资料","me",Config.myTempAvatarUrl));
        mlist.add(new MultipleView(0,"他的资料","you",Config.yourTempAvatarUrl));
        mlist.add(new MultipleView(1,"消息","会有谁呢","null"));
        mlist.add(new MultipleView(1,"搜索","又在哪呢","null"));
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
                }else if(position == 1){

                }else if(position == 2){
                    startActivity(new Intent(OptionsActivity.this,NewsActivity.class));
                }else if(position == 3){
                    startActivity(new Intent(OptionsActivity.this,SearchActivity.class));
                }
            }
        });

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                ActivityController.finishAllActivity();
                startActivity(new Intent(OptionsActivity.this,LoginActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserInfoEvent(GetUserInfoEvent event){
        User user = event.getUser();
        if(user!=null){
            if(user.getUserid().equals(AVUser.getCurrentUser().getObjectId())){
                Config.me.setUserid(user.getUserid());
                Config.me.setUsername(user.getUsername());
                Config.me.setSex(user.getSex());
                Config.me.setAvatar(user.getAvatar());
                multipleAdapter.updataItemView(listView,0,Config.me.getAvatar());
                Log.d("getUserInfoByUserId",Config.me.getAvatar());
            }else if(user.getUserid().equals(Config.me.getFriendid())){
                Config.you.setUserid(user.getUserid());
                Config.you.setUsername(user.getUsername());
                Config.you.setSex(user.getSex());
                Config.you.setAvatar(user.getAvatar());
                Log.d("getUserInfoByUserId",Config.you.getAvatar());
                multipleAdapter.updataItemView(listView,1,Config.you.getAvatar());
            }
        }else {
            Log.d("getUserInfoByUserId","fail");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
