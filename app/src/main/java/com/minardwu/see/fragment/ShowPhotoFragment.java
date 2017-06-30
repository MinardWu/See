package com.minardwu.see.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.RefreshStatusEvent;
import com.minardwu.see.event.SetShowPhotoEvent;
import com.minardwu.see.net.PhotoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
                switch (i){
                    case 0:
                        if(type==0){
                            PhotoService.setShowPhoto(photo.getPhotoid());
                            Log.v("idontknowyetpp","send");
                        }else if(type==1){

                        }
                        break;
                    case 1:
                        if(type==0){

                        }else if(type==1){

                        }
                        break;
                    case 2:
                        if(type==0){

                        }else if(type==1){

                        }
                        break;
                }
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

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onSetShowPhotoEvent(SetShowPhotoEvent event){
        if(event.getResult()==1){
            for(Photo tempphoto:Config.yourPhotos){
                if(tempphoto.getPhotoid().equals(event.getPhotoid())){
                    tempphoto.setState(1);
                }else {
                    tempphoto.setState(0);
                }
            }
            Toast.makeText(getContext(), "设置成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new RefreshStatusEvent(1));
        }else {
            Toast.makeText(getContext(), "设置失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
