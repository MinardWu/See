package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.MultipleAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.MultipleView;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.DeleteNewsEvent;
import com.minardwu.see.event.DeletePhotoEvent;
import com.minardwu.see.event.GetUserInfoEvent;
import com.minardwu.see.net.GetUserInfo;
import com.minardwu.see.util.FontUtil;
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
    private TextView tv_logo;
    private TextView tv_versionname;

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
        if(Config.newsList.size()==0){
            mlist.add(new MultipleView(1,"消息","no news",""));
        }else {
            mlist.add(new MultipleView(1,"消息","have news",""));
        }
        mlist.add(new MultipleView(1,"搜索","又在哪呢",""));
        mlist.add(new MultipleView(2,"","",""));
        mlist.add(new MultipleView(1,"版本", "检查更新",""));
        mlist.add(new MultipleView(1,"权限设置","点击前往",""));
        mlist.add(new MultipleView(2,"","",""));
        mlist.add(new MultipleView(1,"意见反馈","有何高见",""));
    }

    private void initView() {
        tv_logo = (TextView) findViewById(R.id.tv_logo);
        tv_versionname = (TextView) findViewById(R.id.tv_versionname);
        tv_logo.setTypeface(FontUtil.getLogoTypeFace());
        tv_versionname.setText("V"+MyApplication.versionName);
        tv_versionname.setTypeface(FontUtil.getLogoTypeFace());

        listView = (ListView) findViewById(R.id.lv_options);
        listView.addFooterView(new ViewStub(this));//添加这个是为了让最后的一个item的分割线显示
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
                }else if(position == 4){
                    Toast.makeText(OptionsActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
                }else if(position == 5){
                    PermissionUtil permissionUtil = new PermissionUtil(OptionsActivity.this);
                    permissionUtil.gotoMiuiPermission();
                }else if(position == 7){
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

    //在消息界面进行操作后更新选项界面的消息条目
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeleteNewsEvent(DeleteNewsEvent event){
        if(event.getResult()==1){
            if(Config.newsList.size()==0){
                multipleAdapter.updataItemValue(listView,1,"no news");
            }else {
                multipleAdapter.updataItemValue(listView,1,"have news");
            }
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
