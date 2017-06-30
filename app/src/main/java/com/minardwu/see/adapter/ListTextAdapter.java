package com.minardwu.see.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.minardwu.see.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */
public class ListTextAdapter extends ArrayAdapter<String> {

    int resourceid;

    public ListTextAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        resourceid = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(resourceid,null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_buttom);
        textView.setText(getItem(position));
        return convertView;
    }
}
