package com.minardwu.see.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.minardwu.see.R;
import com.minardwu.see.base.BaseActivity;

public class SearchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_search;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("搜索");
    }
}
