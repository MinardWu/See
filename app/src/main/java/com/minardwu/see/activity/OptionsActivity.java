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
import com.minardwu.see.adapter.OptionsAdapter;
import com.minardwu.see.base.ActivityController;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.News;
import com.minardwu.see.entity.Options;
import com.minardwu.see.entity.User;
import com.minardwu.see.entity.UserInfoItem;
import com.minardwu.see.event.GetUserInfoEvent;
import com.minardwu.see.event.LoginEvent;
import com.minardwu.see.net.GetUserInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends BaseActivity {

    private List<Options> list;
    private ListView listView;
    private OptionsAdapter optionsAdapter;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        Log.d("getUserInfoByUserId",AVUser.getCurrentUser().getObjectId());
        Log.d("getUserInfoByUserId",Config.me.getFriendid()+"=======");
//        GetUserInfo.getUserInfoByUserId(AVUser.getCurrentUser().getObjectId());
        GetUserInfo.getUserInfoByUserId(Config.me.getFriendid());
        EventBus.getDefault().register(this);
    }

    private void initList() {
        list = new ArrayList<Options>();
        listView = (ListView) findViewById(R.id.lv_options);
        list.add(new Options("我的资料","me",Config.tempUrl));
        list.add(new Options("他的资料","you",Config.tempUrl));
        list.add(new Options("消息","会有谁呢","null"));
        list.add(new Options("搜索","又在哪呢","null"));
        optionsAdapter = new OptionsAdapter(this,R.layout.listview_normalitem,list);
        listView.setAdapter(optionsAdapter);
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
            }else if(user.getUserid().equals(Config.me.getFriendid())){
                Config.you.setUserid(user.getUserid());
                Config.you.setUsername(user.getUsername());
                Config.you.setSex(user.getSex());
                Config.you.setAvatar(user.getAvatar());
                Log.d("getUserInfoByUserId",Config.you.getAvatar()+"/////////");
                optionsAdapter.updataItemView(listView,0,Config.you.getAvatar());
            }
//            list.clear();
//            list.add(new Options("我的资料","me",Config.tempUrl));
//            list.add(new Options("他的资料","you",Config.you.getAvatar()));
//            list.add(new Options("消息","会有谁呢","null"));
//            list.add(new Options("搜索","又在哪呢","null"));
//            optionsAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(this,"huoqushibai",Toast.LENGTH_LONG).show();
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
