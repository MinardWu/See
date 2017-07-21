package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.MultipleAdapter;
import com.minardwu.see.base.ActivityController;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.MultipleView;
import com.minardwu.see.event.SetUserInfoEvent;
import com.minardwu.see.net.Friend;
import com.minardwu.see.net.SetUserInfo;
import com.minardwu.see.net.UploadAvatarHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends BaseActivity {

    private List<MultipleView> list;
    private ListView listView;
    private MultipleAdapter multipleAdapter;

    private MaterialDialog dialog_edit_avatar;
    private MaterialDialog dialog_friend;
    private MaterialDialog dialog_edit_username;
    private MaterialDialog dialog_edit_psd;
    private MaterialDialog dialog_edit_sex;
    private View view_edit_username;
    private View view_edit_password;
    private MaterialEditText et_newname;
    private MaterialEditText et_oldPassword;
    private MaterialEditText et_newPassword;
    private Button btn_logout;


    private boolean isHaveFriend;
    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        if(Config.you.getUserid().equals("0")){
            isHaveFriend = false;
        }else {
            isHaveFriend = true;
        }
        list = new ArrayList<MultipleView>();
        list.add(new MultipleView(0,AVUser.getCurrentUser().getUsername(),"",Config.me.getAvatar()));
        if(isHaveFriend){
            list.add(new MultipleView(0,Config.you.getUsername(),"",Config.you.getAvatar()));
        }else {
            list.add(new MultipleView(0,"无好友","",Config.you.getAvatar()));
        }
        list.add(new MultipleView(2,"","",""));
        list.add(new MultipleView(1,"昵称",Config.me.getUsername(),""));
        list.add(new MultipleView(1,"密码","勿视",""));
        if(Config.me.getSex()==0){
            list.add(new MultipleView(1,"性别","女",""));
        }else if(Config.me.getSex()==1){
            list.add(new MultipleView(1,"性别","男",""));
        }else if(Config.me.getSex()==-1){
            list.add(new MultipleView(1,"性别","未知",""));
        }
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.lv_userinfo);
        listView.addFooterView(new ViewStub(this));//添加这个是为了让最后的一个item的分割线显示
        multipleAdapter = new MultipleAdapter(this,list);
        listView.setAdapter(multipleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    ListView listView = new ListView(SettingActivity.this);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("拍照");
                    arrayAdapter.add("相册");
                    listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    float scale = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (8 * scale + 0.5f);
                    listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                    listView.setDividerHeight(0);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                dialog_edit_avatar.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            } else if (position == 1) {
                                dialog_edit_avatar.dismiss();
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                            }

                        }
                    });
                    dialog_edit_avatar = new MaterialDialog(SettingActivity.this).setTitle("更换头像").setContentView(listView);
                    dialog_edit_avatar.show();
                } else if (position == 1){
                    if(isHaveFriend){
                        dialog_friend = new MaterialDialog(SettingActivity.this);
                        dialog_friend.setTitle("删除好友？");
                        dialog_friend.setMessage(Config.you.getUsername());
                        dialog_friend.setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Friend.deleteFriend(AVUser.getCurrentUser().getObjectId());
                            }
                        });
                        dialog_friend.setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog_friend.dismiss();
                            }
                        });
                        dialog_friend.show();
                    }else {
                        Toast.makeText(SettingActivity.this, "快去寻找吧", Toast.LENGTH_SHORT).show();
                    }
                } else if (position == 3) {
                    view_edit_username = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_editname, null);
                    et_newname = (MaterialEditText) view_edit_username.findViewById(R.id.et_newUsername);
                    dialog_edit_username = new MaterialDialog(SettingActivity.this);
                    dialog_edit_username.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_newname.getText().toString().length() == 0) {
                                et_newname.setError("用户名不能为空");
                            } else if (et_newname.getText().toString().equals(Config.me.getUsername())) {
                                et_newname.setError("未做出修改");
                            } else {
                                dialog_edit_username.dismiss();
                                SetUserInfo.setUsername(et_newname.getText().toString());
                            }
                        }
                    });
                    dialog_edit_username.setView(view_edit_username).show();
                } else if (position == 4) {
                    view_edit_password = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_editpsw, null);
                    et_oldPassword = (MaterialEditText) view_edit_password.findViewById(R.id.et_oldPassword);
                    et_newPassword = (MaterialEditText) view_edit_password.findViewById(R.id.et_newPassword);
                    dialog_edit_psd = new MaterialDialog(SettingActivity.this);
                    dialog_edit_psd.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_oldPassword.getText().toString().length() == 0) {
                                et_oldPassword.setError("不能为空");
                            } else if (et_newPassword.getText().toString().length() == 0) {
                                et_newPassword.setError("不能为空");
                            }else if (et_newPassword.getText().toString().equals(et_oldPassword.getText().toString())) {
                                et_newPassword.setError("新旧密码相同");
                            } else {
                                SetUserInfo.setPassword(et_oldPassword.getText().toString(),et_newPassword.getText().toString());
                            }
                        }
                    });
                    dialog_edit_psd.setView(view_edit_password).show();
                } else if (position == 5) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("女");
                    arrayAdapter.add("男");
                    ListView listView = new ListView(SettingActivity.this);
                    listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    float scale = getResources().getDisplayMetrics().density;
                    int dpAsPixels = (int) (8 * scale + 0.5f);
                    listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                    listView.setDividerHeight(0);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            dialog_edit_sex.dismiss();
                            if (position == 0) {
                                if(Config.me.getSex()==0){
                                    Toast.makeText(SettingActivity.this, "未作出改变", Toast.LENGTH_SHORT).show();
                                }else {
                                    SetUserInfo.setSex(0);
                                }
                            } else if (position == 1) {
                                if(Config.me.getSex()==1){
                                    Toast.makeText(SettingActivity.this, "未作出改变", Toast.LENGTH_SHORT).show();
                                }else {
                                    SetUserInfo.setSex(1);
                                }
                            }
                        }
                    });
                    dialog_edit_sex = new MaterialDialog(SettingActivity.this).setContentView(listView);
                    dialog_edit_sex.show();
                }
            }
        });

        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AVUser.logOut();
                Config.me = null;
                Config.you = null;
                Config.myPhotos.clear();
                Config.yourPhotos.clear();
                Config.newsList.clear();
                ActivityController.finishAllActivity();
                finish();
                startActivity(new Intent(SettingActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("设置");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSetUserInfoEvent(SetUserInfoEvent event){
        switch (event.getType()){
            case 0:
                if(event.getResult()==1){
                    Toast.makeText(MyApplication.getAppContext(), "头像上传成功", Toast.LENGTH_SHORT).show();
                    multipleAdapter.updataItemImg(listView,0,Config.me.getAvatar());
                }else if(event.getResult()==-1){
                    Toast.makeText(MyApplication.getAppContext(), "头像上传失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if(event.getResult()==1){
                    Toast.makeText(MyApplication.getAppContext(), "修改昵称成功", Toast.LENGTH_SHORT).show();
                    multipleAdapter.updataItemTitle(listView,0,Config.me.getUsername());
                    multipleAdapter.updataItemValue(listView,3,Config.me.getUsername());
                }else if(event.getResult()==-1){
                    Toast.makeText(MyApplication.getAppContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                }else if(event.getResult()==-2){
                    Toast.makeText(MyApplication.getAppContext(), "修改昵称失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(event.getResult()==1){
                    dialog_edit_psd.dismiss();
                    Toast.makeText(MyApplication.getAppContext(), "修改密码成功,请重新登陆", Toast.LENGTH_SHORT).show();
                    AVUser.logOut();
                    ActivityController.finishAllActivity();
                    startActivity(new Intent(SettingActivity.this,LoginActivity.class));
                }else if(event.getResult()==-1){
                    et_oldPassword.setError("初始密码错误");
                }else if(event.getResult()==-2){
                    Toast.makeText(MyApplication.getAppContext(), "修改密码失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                Log.d("setSex","result:"+event.getResult());
                Log.d("setSex","config:"+Config.me.getSex());
                if(event.getResult()==1){
                    Toast.makeText(MyApplication.getAppContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    if(Config.me.getSex()==0){
                        multipleAdapter.updataItemValue(listView,4,"女");
                    }else if(Config.me.getSex()==1){
                        multipleAdapter.updataItemValue(listView,4,"男");
                    }
                }else if(event.getResult()==-1){
                    Toast.makeText(MyApplication.getAppContext(), "修改出错了", Toast.LENGTH_SHORT).show();
                }
                break;
            case 4:
                Log.d("removeFriend","result:"+event.getResult());
                if(event.getResult()==1){
                    dialog_friend.dismiss();
                    Toast.makeText(MyApplication.getAppContext(), "删除好友成功", Toast.LENGTH_SHORT).show();
                    Config.resetFriend();
                    Config.changeFriend = true;
                    multipleAdapter.updataItemTitle(listView,1,"无好友");
                    multipleAdapter.updataItemImg(listView,1,"");
                    isHaveFriend = false;
                }else{
                    dialog_friend.dismiss();
                    Toast.makeText(MyApplication.getAppContext(), "操作失败了", Toast.LENGTH_SHORT).show();
                }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        UploadAvatarHelper.uploadAvatar(requestCode,resultCode,data,SettingActivity.this);
    }
}
