package com.minardwu.see.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.minardwu.see.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        //添加Activity
        ActivityController.addActivity(this);
        //设置toolbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView iv_back = (ImageView) findViewById(R.id.iv_toolbar_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mToolbarHelper = new ToolbarHelper(toolbar);
        toolbarSetting(mToolbarHelper);
        //子Activity设置内容区域
        ViewGroup contentView=(ViewGroup) findViewById(R.id.contentview);
        contentView.addView(View.inflate(this, getContentView(), null));
    }

    protected abstract int getContentView();

    public static class ToolbarHelper {
        private Toolbar mToolbar;
        public ToolbarHelper(Toolbar toolbar) {
            this.mToolbar = toolbar;
        }

        public Toolbar getToolbar() {
            return mToolbar;
        }

        public void setTitle(String title) {
            TextView tv_title = (TextView) mToolbar.findViewById(R.id.tv_toolbar_title);
            tv_title.setText(title);
        }
    }

    ToolbarHelper mToolbarHelper;

    protected void toolbarSetting(ToolbarHelper toolbarHelper) {}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
