package com.minardwu.see.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.entity.UserInfoItem;

import java.util.List;

/**
 * Created by MinardWu on 2017/6/22.
 */
public class UserInfoItemAdapter extends ArrayAdapter<UserInfoItem> {

    private int resource_id;

    public UserInfoItemAdapter(Context context, int resource, List<UserInfoItem> objects) {
        super(context, resource, objects);
        this.resource_id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfoItem userInfoItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource_id,null);
        TextView userinfo_title = (TextView) view.findViewById(R.id.tv_item_title);
        TextView userinfo_value = (TextView) view.findViewById(R.id.tv_item_value);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.userinfo_value_portrait);
        if(position == 0){
            userinfo_value.setVisibility(View.GONE);
            simpleDraweeView.setImageURI(Uri.parse("http://minardwu.com/wordpress/wp-content/uploads/2017/03/20150622212916054-1-300x282.png"));
        }else {
            simpleDraweeView.setVisibility(View.GONE);
        }
        userinfo_title.setText(userInfoItem.getItemName());
        userinfo_value.setText(userInfoItem.getItemVaule());
        return view;
    }


}
