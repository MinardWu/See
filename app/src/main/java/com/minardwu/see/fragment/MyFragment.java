package com.minardwu.see.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.activity.ShowPhotoActivity;
import com.minardwu.see.adapter.PhotoAdapter;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.GetUserPhotoEvent;
import com.minardwu.see.net.Friend;
import com.minardwu.see.net.PhotoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MyFragment extends Fragment {

    private GridView gridView;
    private List<Photo> list;
    private PhotoAdapter photoAdapter;
    private View view;
    private View emptyview;
    private Button btn_load;
    private TextView tv_nonet;
    private Animation animation;
    private Timer loadDataTimer = new Timer(true);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my, container, false);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        btn_load = (Button) view.findViewById(R.id.btn_load);
        tv_nonet = (TextView) view.findViewById(R.id.tv_nonet);
        gridView = (GridView) view.findViewById(R.id.gv_my);
        emptyview = view.findViewById(R.id.emptyview);
        gridView.setEmptyView(emptyview);
        photoAdapter = new PhotoAdapter(getContext(),R.layout.gridview_photo,Config.myPhotos);
        gridView.setAdapter(photoAdapter);

        tv_nonet.setVisibility(View.GONE);
        btn_load.startAnimation(animation);
        btn_load.setEnabled(false);
        gridView.setVisibility(View.GONE);
        emptyview.setVisibility(View.GONE);

        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_nonet.setVisibility(View.GONE);
                btn_load.setEnabled(false);
                btn_load.startAnimation(animation);
                //一秒之后才开始重新获取数据（好让尝试重新加载动画可以显示一会╮(╯▽╰)╭）
                loadDataTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        PhotoService.getPhoto(AVUser.getCurrentUser().getObjectId());
                    }
                },1400);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowPhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                bundle.putInt("position",i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserPhotoEvent(GetUserPhotoEvent event){
        if(event.getUserid().equals(AVUser.getCurrentUser().getObjectId())){
            if(event.getList().size()!=0){
                btn_load.clearAnimation();
                btn_load.setVisibility(View.GONE);
                Config.myPhotos.clear();
                Config.myPhotos.addAll(event.getList());
                Config.myPhotos.get(0).setState(1);
                photoAdapter.notifyDataSetChanged();
            }else {
                btn_load.clearAnimation();
                btn_load.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.VISIBLE);
            }
        }else if(event.getUserid().equals("error")){
            btn_load.clearAnimation();
            btn_load.setEnabled(true);
            tv_nonet.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
