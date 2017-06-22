package com.minardwu.see.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.minardwu.see.R;
import com.minardwu.see.adapter.UserInfoItemAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.entity.UserInfoItem;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends BaseActivity {

    List<UserInfoItem> list;
    ListView listView;
    UserInfoItemAdapter userInfoItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getContentView();
        list = new ArrayList<UserInfoItem>();
        listView = (ListView) findViewById(R.id.lv_userinfo);
        list.add(new UserInfoItem("头像","2","http://minardwu.com/wordpress/wp-content/uploads/2017/03/20150622212916054-1-300x282.png"));
        list.add(new UserInfoItem("用户名","MinardWu","http://minardwu.com/wordpress/wp-content/uploads/2017/03/20150622212916054-1-300x282.png"));
        list.add(new UserInfoItem("密码","不给你看","http://minardwu.com/wordpress/wp-content/uploads/2017/03/20150622212916054-1-300x282.png"));
        list.add(new UserInfoItem("性别","汉子","http://minardwu.com/wordpress/wp-content/uploads/2017/03/20150622212916054-1-300x282.png"));
        userInfoItemAdapter = new UserInfoItemAdapter(this,R.layout.listview_userinfoitem,list);
        listView.setAdapter(userInfoItemAdapter);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_user;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("用户");
    }
}
