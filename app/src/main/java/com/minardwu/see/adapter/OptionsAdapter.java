package com.minardwu.see.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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

    public OptionsAdapter(Context context, int resource, List<Options> objects) {
        super(context, resource, objects);
        this.resource_id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(resource_id,null);
            viewHolder.option_title = (TextView) convertView.findViewById(R.id.tv_item_title);
            viewHolder.option_value = (TextView) convertView.findViewById(R.id.tv_item_value);
            //viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Options options = getItem(position);
        if(position == 0||position == 1){
            viewHolder.option_value.setVisibility(View.GONE);
            viewHolder.simpleDraweeView.setImageURI(Uri.parse(options.getAvatarUrl()));
        }else {
            viewHolder.simpleDraweeView.setVisibility(View.GONE);
        }
        if(position==2||position==3){
            viewHolder.simpleDraweeView.setVisibility(View.GONE);
        }
        viewHolder.option_title.setText(options.getItemTitle());
        viewHolder.option_value.setText(options.getItemVaule());

        return convertView;
    }

    public void updataItemView(ListView listView,int position, String avatar){
        int index = position - listView.getFirstVisiblePosition();//求出index，index即为要更新的item相对当前可见画面第一项的位置
        if(index>=0 && index<listView.getChildCount()){//判断item是否在可见画面(getChildCount获得当前页面的item数，getCount获得总的item数)
            Options option = getItem(position);
            option.setAvatarUrl(avatar);
            View newItem = listView.getChildAt(index);
            getView(position,newItem,listView);
        }
    }

    class ViewHolder{
        TextView option_title;
        TextView option_value;
        SimpleDraweeView simpleDraweeView;
    }
}
