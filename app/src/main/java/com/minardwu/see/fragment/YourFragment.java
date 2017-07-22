package com.minardwu.see.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.minardwu.see.R;
import com.minardwu.see.activity.SearchActivity;
import com.minardwu.see.activity.ShowPhotoActivity;
import com.minardwu.see.adapter.PhotoAdapter;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.event.GetFriendEvent;
import com.minardwu.see.event.GetUserPhotoEvent;
import com.minardwu.see.event.NewPhotoEvent;
import com.minardwu.see.event.RefreshStatusEvent;
import com.minardwu.see.net.Friend;
import com.minardwu.see.net.PhotoService;
import com.minardwu.see.service.GetShowPhotoService;
import com.minardwu.see.util.AlarmHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class YourFragment extends Fragment {

    private GridView gridView;
    private List<Photo> list;
    private PhotoAdapter photoAdapter;
    private View view;
    private View emptyview;
    private RelativeLayout rl_nofriend;
    private TextView tv_nofriend;
    private ImageView iv_nofriend;
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
        view = inflater.inflate(R.layout.fragment_your, container, false);
        rl_nofriend = (RelativeLayout) view.findViewById(R.id.rl_nofriend);
        iv_nofriend = (ImageView) view.findViewById(R.id.iv_nofriend);
        tv_nofriend = (TextView) view.findViewById(R.id.tv_nofriend);
        tv_nonet = (TextView) view.findViewById(R.id.tv_nonet);

        iv_nofriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
        btn_load = (Button) view.findViewById(R.id.btn_load);
        btn_load.setEnabled(false);
        btn_load.startAnimation(animation);
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
                        Friend.getFriendid();
                    }
                },1400);
            }
        });

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
        rl_nofriend.setVisibility(View.GONE);
        tv_nonet.setVisibility(View.GONE);
        return view;
    }

    //自己设置新的showPhoto后刷新UI
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshStatusEvent(RefreshStatusEvent event){
        if(event.getResult()==1){
            photoAdapter.notifyDataSetChanged();
        }
    };

    //好友发布新的照片后刷新UI
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewPhotoEvent(NewPhotoEvent event){
        if(event.getResult()==1){
            photoAdapter.notifyDataSetChanged();
        }
    };

    //查询用户是否有好友
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetFriendEvent(GetFriendEvent event){
        if(event.getResult().equals("0")){//没有好友
            Config.you.setUserid("0");
            btn_load.clearAnimation();
            btn_load.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            emptyview.setVisibility(View.GONE);
            rl_nofriend.setVisibility(View.VISIBLE);
        }else if(event.getResult().equals("error")){//获取出错，如没网
            //Toast.makeText(getContext(), "网络出错了", Toast.LENGTH_SHORT).show();
            tv_nonet.setVisibility(View.VISIBLE);
            btn_load.clearAnimation();
            btn_load.setEnabled(true);
        }else {//有好友则继续获取图片
            Config.you.setUserid(event.getResult());
            gridView.setVisibility(View.VISIBLE);
            rl_nofriend.setVisibility(View.GONE);
            PhotoService.getPhoto(Config.you.getUserid());
            AlarmHelper.startService(getContext(),GetShowPhotoService.class,5);
        }
    };

    //获取好友照片
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
        }else if(event.getUserid().equals("error")){
            //Toast.makeText(getContext(), "网络出错了", Toast.LENGTH_SHORT).show();
            tv_nonet.setVisibility(View.VISIBLE);
            btn_load.clearAnimation();
            btn_load.setEnabled(true);
        }
    };


    //在更换、添加或删除好友后修改YourFragment内容
    //在删除好友照片后修改YourFragment内容
    @Override
    public void onResume() {
        super.onResume();
        if(Config.changeFriend){
            if(Config.you.getUserid()!=null){
                if(Config.you.getUserid().equals("0")){
                    //删除原好友
                    gridView.setVisibility(View.GONE);
                    emptyview.setVisibility(View.GONE);
                    rl_nofriend.setVisibility(View.VISIBLE);
                }else {
                    //更换或添加新好友，把之前好友图片隐藏，只显示加载按钮
                    Config.yourPhotos.clear();
                    gridView.setVisibility(View.GONE);
                    rl_nofriend.setVisibility(View.GONE);
                    btn_load.setVisibility(View.VISIBLE);
                    btn_load.startAnimation(animation);
                    PhotoService.getPhoto(Config.you.getUserid());
                }
            }
        }
        if(Config.deletePhoto){
            photoAdapter.notifyDataSetChanged();
        }
        //更新界面过后都重置为false
        Config.changeFriend = false;
        Config.deletePhoto = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
