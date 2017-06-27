package com.minardwu.see.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.entity.Options;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class OptionsAdapter extends ArrayAdapter<Options>{

    int resource_id;
    List<Options> list;

    public OptionsAdapter(Context context, int resource, List<Options> objects) {
        super(context, resource, objects);
        this.resource_id = resource;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Options options = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource_id,null);
        TextView option_title = (TextView) view.findViewById(R.id.tv_item_title);
        TextView option_value = (TextView) view.findViewById(R.id.tv_item_value);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.iv_image);
        if(position == 0||position == 1){
            option_value.setVisibility(View.GONE);
            simpleDraweeView.setImageURI(Uri.parse(options.getAvatarUrl()));
        }else {
            simpleDraweeView.setVisibility(View.GONE);
        }
        option_title.setText(options.getItemTitle());
        option_value.setText(options.getItemVaule());
        return view;
    }

    public void updataItemView(ListView listView,int position, String avatar){
        int index = position - listView.getFirstVisiblePosition();//求出index，index即为要更新的item相对当前可见画面第一项的位置
        if(index>=0 && index<listView.getChildCount()){//判断item是否在可见画面(getChildCount获得当前页面的item数，getCount获得总的item数)
            Options option = list.get(position);
            option.setAvatarUrl(avatar);
            View newItem = listView.getChildAt(index);
            getView(position,newItem,listView);
        }
    }
}
