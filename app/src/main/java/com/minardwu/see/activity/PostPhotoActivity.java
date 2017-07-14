package com.minardwu.see.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.net.UploadPhotoHelper;
import com.minardwu.see.util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;

public class PostPhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private SimpleDraweeView simpleDraweeView;
    private TextView tv_cancle;
    private TextView tv_post;
    private EditText et_photoinfo;
    private MaterialDialog dialog_exit;

    private byte[] bytes;
    private boolean rotate;
    private Bitmap bitmap ;
    private Bitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_photo);

        EventBus.getDefault().register(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
        tv_post = (TextView) findViewById(R.id.tv_post);

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_previewphoto);
        et_photoinfo = (EditText) findViewById(R.id.et_photoinfo);

        dialog_exit = new MaterialDialog(PostPhotoActivity.this);
        dialog_exit.setTitle("是否退出编辑");
        dialog_exit.setMessage("");
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

        bytes = getIntent().getByteArrayExtra("bytes");
        rotate = getIntent().getBooleanExtra("rotate",true);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if(rotate){
            finalBitmap = FileUtil.getRotateBitmap(bitmap, 90.0f);
        }else {
            finalBitmap = bitmap;
        }

        simpleDraweeView.setImageBitmap(finalBitmap);
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
                UploadPhotoHelper.savePhotoAndUpload(finalBitmap,et_photoinfo.getText().toString());
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dialog_exit.show();
        }
        return false;
    }

    //发布图片结果
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultCodeEvent(ResultCodeEvent event) {
        if(event.getResult()==1){
            Toast.makeText(PostPhotoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(PostPhotoActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
