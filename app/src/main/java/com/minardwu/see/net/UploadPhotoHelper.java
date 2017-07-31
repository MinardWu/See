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

import com.minardwu.see.activity.CameraActivity;
import com.minardwu.see.activity.MainActivity;
import com.minardwu.see.activity.PostPhotoActivity;
import com.minardwu.see.activity.SettingActivity;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by MinardWu on 2017/7/4.
 */
public class UploadPhotoHelper {

    private static long currentTimeMillis = System.currentTimeMillis();
    private static String dirPath = Environment.getExternalStorageDirectory() + "/See/img/";
    private static String filePath = Environment.getExternalStorageDirectory() + "/See/img/" + currentTimeMillis + ".jpg";
    private static Uri imageUri = Uri.parse("file://" + filePath);//裁剪图片时使用，传送裁剪得到的bitmap的位置

    private static Context mcontext;
    private static int CAMERA_REQUEST_CODE = 1;
    private static int GALLERY_REQUEST_CODE = 2;
    private static int IMAGEZOOM_REQUEST_CODE = 3;


    public static void uploadPhoto(int requestCode, int resultCode, Intent data,Context context){
        mcontext = context;
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                } else {
                    Log.v("uploadPhoto","开始保存上传");
                    Bitmap bitmap = extras.getParcelable("data");
                    savePhotoAndUpload(bitmap,"不会再被用到这个方法了");
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE) {
            if (data == null) {
                Log.v("uploadPhoto","选择照片出错了");
                return;
            } else {
                Log.v("uploadPhoto","选择照片正常");
                Log.v("uploadPhotsadasdo","1");
                Uri uri = data.getData();
                cutImage(uri);
//                Bitmap bitmap = null;
//                try {
//                    bitmap = BitmapFactory.decodeStream(MyApplication.getAppContext().getContentResolver().openInputStream(uri));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//                //将bitmap转化为byte[]
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
//                FileUtil.bytes = baos.toByteArray();
//                //跳转到发布界面
//                Intent intent = new Intent(mcontext, PostPhotoActivity.class);
//                intent.putExtra("from", "gallery");
//                ((MainActivity)mcontext).startActivity(intent);
            }
        } else if (requestCode == IMAGEZOOM_REQUEST_CODE) {
            if (imageUri!=null) {
                Log.v("uploadPhoto","imageUri!=null");
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(MyApplication.getAppContext().getContentResolver().openInputStream(imageUri));
                    //将bitmap转化为byte[]
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    FileUtil.bytes = baos.toByteArray();
                    //跳转到发布界面
                    Intent intent = new Intent(mcontext, PostPhotoActivity.class);
                    intent.putExtra("from", "gallery");
                    ((MainActivity)mcontext).startActivity(intent);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else {
                Toast.makeText(MyApplication.getAppContext(), "出错了", Toast.LENGTH_SHORT).show();
                Log.v("uploadPhoto","imageUri==null");
            }

        }
    }

    //保存bitmap并上传，无论是拍照还是裁剪图片最后得到的都是一个bitmap
    public static Uri savePhotoAndUpload(Bitmap bitmap,String info) {
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
            PhotoService.uploadPhoto(filePath,info);
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.v("uploadPhoto",e.getMessage().toString());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("uploadPhoto",e.getMessage().toString());
            return null;
        }
    }

    //打开图片裁剪页面
    public static void cutImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //宽高比例
        intent.putExtra("aspectX", 6);
        intent.putExtra("aspectX", 10);
        //图片宽高
        intent.putExtra("outputX", 1080);
        intent.putExtra("outputY", 1920);
        //发送图片位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        ((MainActivity)mcontext).startActivityForResult(intent, IMAGEZOOM_REQUEST_CODE);
    }


}
