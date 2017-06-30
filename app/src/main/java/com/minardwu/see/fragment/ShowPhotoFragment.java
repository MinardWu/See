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
    private Animation get_in;
    private Animation get_out;

    private View view;
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
        view = inflater.inflate(R.layout.fragment_showphoto,null);
        initView();
        intAnimation();
        initAction();
        return view;
    }

    private void initView() {
        list = new ArrayList<String>();
        if(type==0){
            list.clear();
            list.add("预览效果");
            list.add("设为壁纸");
            list.add("删除照片");
        }else if(type==1){
            list.clear();
            list.add("预览效果");
        }
        ListTextAdapter listTextAdapter = new ListTextAdapter(MyApplication.getAppContext(),R.layout.listview_photobuttom,list);
        listView = (ListView) view.findViewById(R.id.lv_photo_buttom);
        listView.setAdapter(listTextAdapter);

        tv_photoinfo = (TextView) view.findViewById(R.id.tv_photoinfo);
        tv_photoinfo.setText(photo.getPhotoInfo());
        iv_photo = (SimpleDraweeView) view.findViewById(R.id.iv_photo);
        iv_photo.setImageURI(Uri.parse(photo.getPhotoUrl()));
    }

    private void intAnimation() {
        get_out = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1);
        get_out.setDuration(500);
        get_out.setInterpolator(AnimationUtils.loadInterpolator(MyApplication.getAppContext(),
                android.R.anim.accelerate_decelerate_interpolator));

        get_in = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0);
        get_in.setDuration(500);
        get_in.setInterpolator(AnimationUtils.loadInterpolator(MyApplication.getAppContext(),
                android.R.anim.accelerate_decelerate_interpolator));
    }

    private void initAction() {
        show = false;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        if(type==0){

                        }else if(type==1){

                        }
                        break;
                    case 1:
                        if(type==0){
                            if(photo.getState()==1){
                                Toast.makeText(getContext(), "已设为壁纸", Toast.LENGTH_SHORT).show();
                            }else {
                                PhotoService.setShowPhoto(photo.getPhotoid());
                            }
                        }else if(type==1){

                        }
                        break;
                    case 2:
                        if(type==0){

                        }else if(type==1){

                        }
                        break;
                }
            }
        });

        iv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(show){
                    listView.setVisibility(View.INVISIBLE);
                    listView.startAnimation(get_out);
                    show = false;
                }else {
                    listView.setVisibility(View.VISIBLE);
                    listView.startAnimation(get_in);
                    show = true;
                }

            }
        });
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
            Toast.makeText(getContext(),"设置成功", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new RefreshStatusEvent(1));//设置成功后更新界面图片的显示状态
            listView.setVisibility(View.INVISIBLE);
            listView.setAnimation(get_out);
            show = false;
        }else {
            Toast.makeText(getContext(),"设置失败", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
