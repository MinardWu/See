package com.minardwu.see.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.adapter.ListTextAdapter;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class ShowPhotoFragment extends Fragment {

    private boolean show;
    private Photo photo;
    private int type;
    private Animation myAnimation_Translate;

    private List<String> list;
    private ListView listView;

    private SimpleDraweeView iv_photo;
    private TextView tv_photoinfo;

    public ShowPhotoFragment(int type, Photo photo) {
        this.type = type;
        this.photo = photo;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showphoto,null);
        show = false;
        list = new ArrayList<String>();
        if(type==0){
            list.clear();
            list.add("现在就用");
            list.add("预览效果");
        }else if(type==1){
            list.clear();
            list.add("发送");
            list.add("预览效果");
            list.add("删除照片");
        }
        ListTextAdapter listTextAdapter = new ListTextAdapter(MyApplication.getAppContext(),R.layout.listview_photobuttom,list);
        listView = (ListView) view.findViewById(R.id.lv_photo_buttom);
        listView.setAdapter(listTextAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getContext(),list.get(i),Toast.LENGTH_SHORT).show();
            }
        });
        tv_photoinfo = (TextView) view.findViewById(R.id.tv_photoinfo);
        tv_photoinfo.setText(photo.getPhotoInfo());
        iv_photo = (SimpleDraweeView) view.findViewById(R.id.iv_photo);
        iv_photo.setImageURI(Uri.parse(photo.getPhotoUrl()));
        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show){
                    listView.setVisibility(View.INVISIBLE);
                    myAnimation_Translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 1);
                    myAnimation_Translate.setDuration(500);
                    myAnimation_Translate.setInterpolator(AnimationUtils.loadInterpolator(MyApplication.getAppContext(), android.R.anim.accelerate_decelerate_interpolator));
                    listView.startAnimation(myAnimation_Translate);
                    show = false;
                }else {
                    listView.setVisibility(View.VISIBLE);
                    myAnimation_Translate = new TranslateAnimation(
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 1,
                            Animation.RELATIVE_TO_PARENT, 0);
                    myAnimation_Translate.setDuration(500);
                    myAnimation_Translate.setInterpolator(AnimationUtils.loadInterpolator(MyApplication.getAppContext(), android.R.anim.accelerate_decelerate_interpolator));
                    listView.startAnimation(myAnimation_Translate);
                    show = true;
                }

            }
        });

        return view;
    }
}
