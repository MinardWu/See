package com.minardwu.see.net;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.minardwu.see.activity.SettingActivity;
import com.minardwu.see.base.MyApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by MinardWu on 2017/7/4.
 */
public class UploadAvatarHelper {

    private static long currentTimeMillis = System.currentTimeMillis();
    private static String dirPath = Environment.getExternalStorageDirectory() + "/light/img/";
    private static String filePath = Environment.getExternalStorageDirectory() + "/light/img/" + currentTimeMillis + ".jpg";
    private static Uri imageUri = Uri.parse("file://" + filePath);//裁剪图片时使用，传送裁剪得到的bitmap的位置

    private static Context mcontext;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int IMAGEZOOM_REQUEST_CODE = 3;


    public static void uploadAvatar(int requestCode, int resultCode, Intent data,Context context){
        mcontext = context;
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                } else {
                    Log.v("uploadAvatar","开始保存上传");
                    Bitmap bitmap = extras.getParcelable("data");
                    savePhotoAndUpload(bitmap);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                Log.v("uploadAvatar","选择照片出错了");
                return;
            } else {
                Log.v("uploadAvatar","选择照片正常");
                Uri uri = data.getData();
                cutImage(uri);
            }
        } else if (requestCode == IMAGEZOOM_REQUEST_CODE) {
            if (imageUri!=null) {
                Log.v("uploadAvatar","imageUri!=null");
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(MyApplication.getAppContext().getContentResolver().openInputStream(imageUri));
                    savePhotoAndUpload(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(MyApplication.getAppContext(), "出错了", Toast.LENGTH_SHORT).show();
                Log.v("uploadAvatar","imageUri==null");
            }

        }
    }

    //保存bitmap并上传，无论是拍照还是裁剪图片最后得到的都是一个bitmap
    private static Uri savePhotoAndUpload(Bitmap bitmap) {
        File fileDir = new File(dirPath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        File img = new File(filePath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            SetUserInfo.setAvatar(filePath);
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("uploadAvatar",e.getMessage().toString());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("uploadAvatar",e.getMessage().toString());
            return null;
        }
    }

    //打开图片裁剪页面
    private static void cutImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectX", 1);
        //图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        //发送图片位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        ((SettingActivity)mcontext).startActivityForResult(intent, IMAGEZOOM_REQUEST_CODE);
    }


}
