package com.minardwu.see.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minardwu.see.R;
import com.minardwu.see.entity.PopupwindowItem;

import java.util.List;

/**
 * Created by MinardWu on 2017/7/10.
 */
public class PopupwindowItemAdapter extends ArrayAdapter<PopupwindowItem> {

    int mresource;
    public PopupwindowItemAdapter(Context context, int resource, List<PopupwindowItem> objects) {
        super(context, resource, objects);
        mresource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopupwindowItem item = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(mresource,null);
        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_popicon);
        TextView tv = (TextView) convertView.findViewById(R.id.tv_poptext);
        iv.setImageResource(item.getIcon());
        tv.setText(item.getItem());
        return convertView;
    }
}
