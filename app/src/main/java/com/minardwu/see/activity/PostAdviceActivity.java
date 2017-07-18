package com.minardwu.see.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minardwu.see.R;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.event.PostAdviceEvent;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.net.Advice;
import com.minardwu.see.net.UploadPhotoHelper;
import com.minardwu.see.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;

public class PostAdviceActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView tv_cancle;
    private TextView tv_post;
    private EditText et_advice;
    private MaterialDialog dialog_exit;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_advice);

        EventBus.getDefault().register(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_post = (TextView) findViewById(R.id.tv_post);
        et_advice = (EditText) findViewById(R.id.et_photoinfo);

        dialog = ProgressDialog.createLoadingDialog(this);

        dialog_exit = new MaterialDialog(PostAdviceActivity.this);
        dialog_exit.setMessage("是否退出编辑");
        dialog_exit.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
            }
        });
        dialog_exit.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_cancle.setOnClickListener(this);
        tv_post.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancle:
                dialog_exit.show();
                break;
            case R.id.tv_post:
                if(et_advice.getText().toString().length()==0){
                    Toast.makeText(PostAdviceActivity.this, "内容为空哦", Toast.LENGTH_SHORT).show();
                }else {
                    Advice.postAdvice(et_advice.getText().toString());
                    dialog.show();
                }
                break;
        }
    }

    //发送建议结果
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPostAdviceEvent(PostAdviceEvent event) {
        dialog.dismiss();
        if(event.getResult()==1){
            Toast.makeText(MyApplication.getAppContext(), "感谢您，您的意见已发送", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(MyApplication.getAppContext(), "发送出了点问题╮(╯▽╰)╭", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog_exit.show();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
