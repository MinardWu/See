package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.OptionsAdapter;
import com.minardwu.see.base.ActivityController;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.News;
import com.minardwu.see.entity.Options;

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
        list = new ArrayList<Options>();
        listView = (ListView) findViewById(R.id.lv_options);
        list.add(new Options("我的资料","MinardWu", Config.tempUrl));
        list.add(new Options("他的资料","Ming",Config.tempUrl));
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

    @Override
    protected int getContentView() {
        return R.layout.activity_options;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("选项");
    }
}
