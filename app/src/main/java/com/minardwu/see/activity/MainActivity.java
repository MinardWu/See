package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.MyFragmentPagerAdapter;
import com.minardwu.see.adapter.NormalItemAdapter;
import com.minardwu.see.base.ActivityController;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.entity.NormalItem;
import com.minardwu.see.entity.User;
import com.minardwu.see.event.GetShowPhotoEvent;
import com.minardwu.see.event.NewPhotoEvent;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.fragment.MyFragment;
import com.minardwu.see.fragment.YourFragment;
import com.minardwu.see.net.Friend;
import com.minardwu.see.net.PhotoService;
import com.minardwu.see.net.UploadPhotoHelper;
import com.minardwu.see.service.LockService;
import com.minardwu.see.util.AlarmHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends FragmentActivity implements  View.OnClickListener {

    private ViewPager viewPager;
    private YourFragment yourFragment;
    private MyFragment myFragment;
    private List<Fragment> fragmentList;
    private Toolbar toolbar;
    private RadioButton rb_your, rb_my;
    private ImageView iv_user, iv_add;
    private View popupView;
    private PopupWindow mPopupWindow;
    private MaterialDialog dialog_add;

    private boolean firstIn = true;
    private boolean readyForExit = false;
    private Timer timer = new Timer(true);
    private int currentItem = 0;
    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmHelper.startService(this,LockService.class,5);
        ActivityController.addActivity(this);//MainActivity没有继承BaseActivity，故要手动添加

        initView();
        initDialog();

        EventBus.getDefault().register(this);

        Friend.getFriendid();
        PhotoService.getPhoto(AVUser.getCurrentUser().getObjectId());

        Config.me = new User();
        Config.you = new User();
        Config.myPhotos = new ArrayList<Photo>();
        Config.yourPhotos = new ArrayList<Photo>();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        rb_your = (RadioButton) findViewById(R.id.rbtn_your);
        rb_my = (RadioButton) findViewById(R.id.rbtn_my);
        iv_user = (ImageView) findViewById(R.id.ibtn_toolbar_user);
        iv_add = (ImageView) findViewById(R.id.ibtn_toolbar_add);

        rb_your.setChecked(true);
        rb_my.setChecked(false);

        rb_your.setOnClickListener(this);
        rb_my.setOnClickListener(this);
        iv_user.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        yourFragment = new YourFragment();
        myFragment = new MyFragment();
        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(yourFragment);
        fragmentList.add(myFragment);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(currentItem);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb_your.setChecked(true);
                        rb_my.setChecked(false);
                        currentItem = 0;
                        break;
                    case 1:
                        rb_your.setChecked(false);
                        rb_my.setChecked(true);
                        currentItem = 1;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initDialog() {
        List<NormalItem> list = new ArrayList<NormalItem>();
        list.add(new NormalItem(R.drawable.camera,"拍照"));
        list.add(new NormalItem(R.drawable.gallery,"从相册选择"));
        NormalItemAdapter adapter = new NormalItemAdapter(this,R.layout.listview_normalitem,list);
        ListView listView = new ListView(MainActivity.this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setPadding(0, 0, 0, -35);
        listView.setDividerHeight(0);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog_add.dismiss();
                if (position == 0) {
                    Intent intent0 = new Intent(MainActivity.this,CameraActivity.class);
                    startActivity(intent0);
                } else if (position == 1) {
                    Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                    intent1.setType("image/*");
                    startActivityForResult(intent1, GALLERY_REQUEST_CODE);
                }
            }
        });
        dialog_add = new MaterialDialog(MainActivity.this).setContentView(listView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn_toolbar_user:
                startActivity(new Intent(MainActivity.this,OptionsActivity.class));
                break;
            case R.id.ibtn_toolbar_add:
                dialog_add.show();
                break;
            case R.id.rbtn_your:
                viewPager.setCurrentItem(0);
                currentItem = 0;
                break;
            case R.id.rbtn_my:
                viewPager.setCurrentItem(1);
                currentItem = 1;
                break;
        }
    }


    //上传图片成功后重新获取自己的照片
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultCodeEvent(ResultCodeEvent event){
        if(event.getResult()==1){
            PhotoService.getPhoto(AVUser.getCurrentUser().getObjectId());
        }
    };

    //获取showPhoto
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetShowPhotoEvent(GetShowPhotoEvent event) {
        Photo showPhoto = event.getPhoto();
        if(Config.yourPhotos.contains(showPhoto)){//如果showPhoto不是新增的，改变state
            Log.v("getShowPhoto","contains");
            for(Photo tempPhoto:Config.yourPhotos){
                if(tempPhoto.getPhotoid().equals(showPhoto.getPhotoid())){
                    tempPhoto.setState(1);
                }else{
                    tempPhoto.setState(0);
                }
            }
        }else {//如果showPhoto是新增的，则添加到Config.yourPhotos中
            Log.v("getShowPhoto","nocontains");
            for(Photo tempPhoto:Config.yourPhotos){
                tempPhoto.setState(0);
            }
            Config.yourPhotos.add(0,showPhoto);
            EventBus.getDefault().post(new NewPhotoEvent(1));//通知YourFragment刷新界面
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UploadPhotoHelper.uploadPhoto(requestCode,resultCode,data,MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByClick();
        }
        return false;
    }

    private void exitByClick() {
        if (readyForExit == false) {
            readyForExit = true;
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            //如果2秒钟内没有按下返回键，readyForExit右变为false则启动定时器取消掉刚才执行的任务
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    readyForExit = false;
                }
            }, 2000);
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}


