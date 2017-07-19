package com.minardwu.see.activity;

import android.app.Dialog;
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
import com.minardwu.see.util.FontUtil;
import com.minardwu.see.widget.CustomTextView;
import com.minardwu.see.widget.ProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.drakeet.materialdialog.MaterialDialog;

public class PostPhotoActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private SimpleDraweeView simpleDraweeView;
//    private TextView tv_cancle;
//    private TextView tv_post;
    private CustomTextView tv_send;
    private EditText et_photoinfo;
    private MaterialDialog dialog_exit;
    private Dialog dialog;
    private TextView tv_time;

    private byte[] bytes;
    private String from;
    private Bitmap bitmap ;
    private Bitmap finalBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_photo);

        EventBus.getDefault().register(this);

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        tv_cancle = (TextView) findViewById(R.id.tv_cancle);
//        tv_post = (TextView) findViewById(R.id.tv_post);

        tv_time = (TextView) findViewById(R.id.tv_time);
        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_previewphoto);
        et_photoinfo = (EditText) findViewById(R.id.et_photoinfo);
        tv_send = (CustomTextView) findViewById(R.id.tv_send);

        tv_time.setTypeface(FontUtil.getSegoeUISemilight());

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_send.setEnabled(false);
                if(et_photoinfo.getText().toString().length()==0){
                    UploadPhotoHelper.savePhotoAndUpload(finalBitmap,"empty");
                }else {
                    UploadPhotoHelper.savePhotoAndUpload(finalBitmap,et_photoinfo.getText().toString());
                }
                dialog.show();
            }
        });

        dialog = ProgressDialog.createLoadingDialog(this);

        dialog_exit = new MaterialDialog(PostPhotoActivity.this);
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

        from = getIntent().getStringExtra("from");
        if(from.equals("gallery")){
            bytes = FileUtil.bytes;
        }else if(from.equals("camera")){
            bytes = getIntent().getByteArrayExtra("bytes");
        }
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        if(from.equals("gallery")){
            finalBitmap = bitmap;
        }else if(from.equals("camera")){
            finalBitmap = FileUtil.getRotateBitmap(bitmap, 90.0f);
        }


        simpleDraweeView.setImageBitmap(finalBitmap);
//        tv_cancle.setOnClickListener(this);
//        tv_post.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.tv_cancle:
//                dialog_exit.show();
//                break;
//            case R.id.tv_post:
//                if(et_photoinfo.getText().toString().length()==0){
//                    UploadPhotoHelper.savePhotoAndUpload(finalBitmap,"empty");
//                }else {
//                    UploadPhotoHelper.savePhotoAndUpload(finalBitmap,et_photoinfo.getText().toString());
//                }
//                dialog.show();
//                break;
//        }
//    }

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
        dialog.dismiss();
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
