package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.MultipleAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.MultipleView;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetUserInfoEvent;
import com.minardwu.see.net.GetUserInfo;
import com.minardwu.see.util.PermissionUtil;

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
        if(!Config.you.getUserid().equals("0")){
            GetUserInfo.getUserInfoByUserId(Config.you.getUserid());
        }
        EventBus.getDefault().register(this);
    }

    private void initData() {
        mlist= new ArrayList<MultipleView>();
        mlist.add(new MultipleView(0,"我的资料","",Config.me.getAvatar()));
        mlist.add(new MultipleView(1,"消息","会有谁呢",""));
        mlist.add(new MultipleView(1,"搜索","又在哪呢",""));
        mlist.add(new MultipleView(1,"版本","1.0.0",""));
        mlist.add(new MultipleView(1,"权限设置","点击前往",""));
        mlist.add(new MultipleView(1,"意见反馈","有何高见",""));
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
                    startActivity(new Intent(OptionsActivity.this,NewsActivity.class));
                }else if(position == 2){
                    startActivity(new Intent(OptionsActivity.this,SearchActivity.class));
                }else if(position == 3){
                    Toast.makeText(OptionsActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                }else if(position == 4){
                    PermissionUtil permissionUtil = new PermissionUtil(OptionsActivity.this);
                    permissionUtil.gotoMiuiPermission();
                }else if(position == 5){
                    startActivity(new Intent(OptionsActivity.this,PostAdviceActivity.class));
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
            multipleAdapter.updataItemImg(listView,0,Config.me.getAvatar());
        }else if(user.getUserid().equals(Config.you.getUserid())){
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

    //为了进行两种操作
    //1.在消息页面添加好友后返回OptionsActivity，重新获取好友信息
    //2.在设置页面设置头像后重新更新头像
    @Override
    protected void onResume() {
        super.onResume();
        multipleAdapter.updataItemImg(listView,0,Config.me.getAvatar());
        if(!Config.you.getUserid().equals("0")){
            GetUserInfo.getUserInfoByUserId(Config.you.getUserid());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
