package com.minardwu.see.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.minardwu.see.R;
import com.minardwu.see.adapter.UserInfoItemAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.UserInfoItem;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class SettingActivity extends BaseActivity {

    private List<UserInfoItem> list;
    private ListView listView;
    private UserInfoItemAdapter userInfoItemAdapter;

    private MaterialDialog dialog_edit_avatar;
    private MaterialDialog dialog_edit_username;
    private MaterialDialog dialog_edit_psd;
    private MaterialDialog dialog_edit_sex;
    private View view_edit_username;
    private View view_edit_password;
    private MaterialEditText et_newname;
    private MaterialEditText et_oldPassword;
    private MaterialEditText et_newPassword;

    public static int CAMERA_REQUEST_CODE = 1;
    public static int GALLERY_REQUEST_CODE = 2;
    public static int IMAGEZOOM_REQUEST_CODE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        list = new ArrayList<UserInfoItem>();
        listView = (ListView) findViewById(R.id.lv_userinfo);
        list.add(new UserInfoItem("头像","MinardWu", Config.tempUrl));
        list.add(new UserInfoItem("昵称","MinardWu",Config.tempUrl));
        list.add(new UserInfoItem("密码","不给你看",Config.tempUrl));
        list.add(new UserInfoItem("性别","汉子",Config.tempUrl));
        userInfoItemAdapter = new UserInfoItemAdapter(this,R.layout.listview_normalitem,list);
        listView.setAdapter(userInfoItemAdapter);
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
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, CAMERA_REQUEST_CODE);
                            } else if (position == 1) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, GALLERY_REQUEST_CODE);
                            }

                        }
                    });
                    dialog_edit_avatar = new MaterialDialog(SettingActivity.this).setTitle("更换头像").setContentView(listView);
                    dialog_edit_avatar.show();
                } else if (position == 1) {
                    view_edit_username = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_editusername, null);
                    et_newname = (MaterialEditText) view_edit_username.findViewById(R.id.et_newUsername);
                    dialog_edit_username = new MaterialDialog(SettingActivity.this);
                    dialog_edit_username.setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (et_newname.getText().toString().length() == 0) {
                                et_newname.setError("用户名不能为空");
                            } else {
                                dialog_edit_username.dismiss();
                            }
                        }
                    });
                    dialog_edit_username.setView(view_edit_username).show();
                } else if (position == 2) {
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
                            } else {
                                dialog_edit_psd.dismiss();
                            }
                        }
                    });
                    dialog_edit_psd.setView(view_edit_password).show();
                } else if (position == 3) {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SettingActivity.this, android.R.layout.simple_list_item_1);
                    arrayAdapter.add("汉子");
                    arrayAdapter.add("妹子");
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

                            } else if (position == 1) {

                            }
                        }
                    });
                    dialog_edit_sex = new MaterialDialog(SettingActivity.this).setTitle("选一个吧").setContentView(listView);
                    dialog_edit_sex.show();
                }
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
}
