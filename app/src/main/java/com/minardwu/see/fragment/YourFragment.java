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

import com.minardwu.see.R;
import com.minardwu.see.activity.ShowPhotoActivity;
import com.minardwu.see.adapter.PhotoAdapter;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.GetFriendEvent;
import com.minardwu.see.event.GetUserPhotoEvent;
import com.minardwu.see.event.RefreshStatusEvent;
import com.minardwu.see.net.PhotoService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


public class YourFragment extends Fragment {

    private GridView gridView;
    private List<Photo> list;
    private PhotoAdapter photoAdapter;
    private View emptyview;
    private TextView tv_nofriend;
    private Button btn_load;
    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_your, container, false);
        tv_nofriend = (TextView) view.findViewById(R.id.tv_nofriend);
        btn_load = (Button) view.findViewById(R.id.btn_load);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        btn_load.startAnimation(animation);
        gridView = (GridView) view.findViewById(R.id.gv_your);
        emptyview = view.findViewById(R.id.emptyview);
        gridView.setEmptyView(emptyview);
        photoAdapter = new PhotoAdapter(getContext(),R.layout.gridview_photo,Config.yourPhotos);
        gridView.setAdapter(photoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), ShowPhotoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                bundle.putInt("position",i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        gridView.setVisibility(View.GONE);
        emptyview.setVisibility(View.GONE);
        tv_nofriend.setVisibility(View.GONE);
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshStatusEvent(RefreshStatusEvent event){
        if(event.getResult()==1){
            photoAdapter.notifyDataSetChanged();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFriendEvent(GetFriendEvent event){
        if(event.getResult().equals("0")){//没有好友
            Config.me.setFriendid("0");
            Config.you.setUserid("0");
            btn_load.clearAnimation();
            btn_load.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            emptyview.setVisibility(View.GONE);
            tv_nofriend.setVisibility(View.VISIBLE);
        }else {//有好友则继续获取图片
            Config.me.setFriendid(event.getResult());
            Config.you.setUserid(event.getResult());
            gridView.setVisibility(View.VISIBLE);
            tv_nofriend.setVisibility(View.GONE);
            PhotoService.getPhoto(Config.me.getFriendid());
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetUserPhotoEvent(GetUserPhotoEvent event){
        if(event.getUserid().equals(Config.you.getUserid())){
            if(event.getList().size()!=0){
                btn_load.clearAnimation();
                btn_load.setVisibility(View.GONE);
                Config.yourPhotos.clear();
                Config.yourPhotos.addAll(event.getList());
                Config.yourPhotos.get(0).setState(1);
                photoAdapter.notifyDataSetChanged();
            }else {
                gridView.setVisibility(View.VISIBLE);
                emptyview.setVisibility(View.VISIBLE);
                btn_load.clearAnimation();
                btn_load.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if(Config.you.getUserid()!=null){
            if(Config.you.getUserid().equals("0")){
                gridView.setVisibility(View.GONE);
                emptyview.setVisibility(View.GONE);
                tv_nofriend.setVisibility(View.VISIBLE);
            }else {
                gridView.setVisibility(View.VISIBLE);
                tv_nofriend.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
