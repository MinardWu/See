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
import com.minardwu.see.entity.NewsEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */
public class NewsAdapter extends ArrayAdapter<NewsEntity> {
    int resource_id;
    public NewsAdapter(Context context, int resource, List<NewsEntity> objects) {
        super(context, resource, objects);
        this.resource_id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsEntity news = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resource_id,null);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.iv_news_image);
        TextView news_user = (TextView) view.findViewById(R.id.tv_news_user);
        TextView news_msg = (TextView) view.findViewById(R.id.tv_news_msg);
        simpleDraweeView.setImageURI(Uri.parse(news.getUseravatar()));
        news_user.setText(news.getUsername());
        news_msg.setText("请求加你为好友");
        return view;
    }
}
