package com.minardwu.see.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.util.FontUtil;

public class PreviewActivity extends AppCompatActivity {

    private int positon;
    private String photoid;
    private SimpleDraweeView simpleDraweeView;
    private TextView tv_time;
    private TextView tv_info;
    private Photo photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        photoid = getIntent().getStringExtra("photoid");
        for(Photo temp:Config.yourPhotos){
            if (temp.getPhotoid().equals(photoid)){
                photo = temp;
            }
        }

        simpleDraweeView = (SimpleDraweeView) findViewById(R.id.iv_lock);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_time.setTypeface(FontUtil.getSegoeUISemilight());
        simpleDraweeView.setImageURI(Uri.parse(photo.getPhotoUrl()));
        if(photo.getPhotoInfo().equals("empty")){
            tv_info.setText("");
        }else {
            tv_info.setText(photo.getPhotoInfo());
        }

    }
}
