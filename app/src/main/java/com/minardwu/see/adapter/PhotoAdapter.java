package com.minardwu.see.adapter;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;
import com.minardwu.see.fragment.MyFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class PhotoAdapter extends ArrayAdapter<Photo>{

    int resource_id;

    public PhotoAdapter(Context context, int resource, List<Photo> objects) {
        super(context, resource, objects);
        resource_id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Photo photo = getItem(position);
        View view;
        if(convertView!=null){
            view = convertView;
        }else {
            view = LayoutInflater.from(getContext()).inflate(resource_id,null);
        }
        SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.iv_gridview);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = Config.screenWidth/3;
        layoutParams.height = Config.screenHeight/3;
        imageView.setLayoutParams(layoutParams);
        imageView.setImageURI(Uri.parse(photo.getPhotoUrl()));
        if(photo.getState()==0){
            ColorMatrix colorMatrix = new ColorMatrix();
            colorMatrix.setSaturation(0f); // 设置饱和度:0为纯黑白，饱和度为0；1为饱和度为100，即原图；
            ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
            imageView.setColorFilter(colorMatrixColorFilter);
        }
        return  view;

    }
}
