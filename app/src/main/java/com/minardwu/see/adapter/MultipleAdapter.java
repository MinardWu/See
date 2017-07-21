package com.minardwu.see.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.minardwu.see.R;
import com.minardwu.see.base.Config;
import com.minardwu.see.base.MyApplication;
import com.minardwu.see.entity.MultipleView;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */
public class MultipleAdapter extends BaseAdapter {

    Context context;
    List<MultipleView> multipleViewList;

    public MultipleAdapter(Context c, List<MultipleView> list){
        this.context = c;
        this.multipleViewList = list;
    }

    //返回类型
    @Override
    public int getItemViewType(int position) {
        return  multipleViewList.get(position).getType();
    }

    //返回不同布局的数量
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return multipleViewList.size();
    }

    @Override
    public Object getItem(int position) {
        return multipleViewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextViewHolder textViewHolder = null;
        ImgViewHolder imgViewHolder = null;
        int type = getItemViewType(position);
        switch (type){
            case 0:
                if(convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_imgitem,null);
                    imgViewHolder = new ImgViewHolder();
                    imgViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_title);
                    imgViewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.iv_avatar);
                    convertView.setTag(imgViewHolder);
                }else {
                    imgViewHolder = (ImgViewHolder) convertView.getTag();
                }
                imgViewHolder.tv_title.setText(multipleViewList.get(position).getItemTitle());
                //这里为设置页面的好友项做特殊处理
                if(position==1 && Config.you.getUserid().equals("0")){
                    imgViewHolder.simpleDraweeView.setImageURI(Uri.parse("res://"+ MyApplication.getAppContext().getPackageName()+"/" + R.drawable.icon_nofriendavatar));
                }else {
                    imgViewHolder.simpleDraweeView.setImageURI(Uri.parse(multipleViewList.get(position).getAvatarUrl()+""));
                }
                break;
            case 1:
                if(convertView == null){
                    convertView = LayoutInflater.from(context).inflate(R.layout.listview_textitem,null);
                    textViewHolder = new TextViewHolder();
                    textViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_item_title);
                    textViewHolder.tv_value = (TextView) convertView.findViewById(R.id.tv_item_value);
                    convertView.setTag(textViewHolder);
                }else {
                    textViewHolder = (TextViewHolder) convertView.getTag();
                }
                textViewHolder.tv_title.setText(multipleViewList.get(position).getItemTitle());
                //这里为选项页面的消息项做特殊处理
                if(position==1 && multipleViewList.get(position).getItemVaule().equals("have news")){
                    textViewHolder.tv_value.setText(Config.newsList.size()+"条消息");
                    textViewHolder.tv_value.setTextColor(0xFFFF7474);
                }else if(position==1 && multipleViewList.get(position).getItemVaule().equals("no news")){
                    textViewHolder.tv_value.setText("暂无消息");
                    textViewHolder.tv_value.setTextColor(0xFF969292);
                }else {
                    textViewHolder.tv_value.setText(multipleViewList.get(position).getItemVaule());
                }
                break;
            case 2:
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_emptyitem,null);
                break;
        }

        return convertView;
    }

    public void updataItemImg(ListView listView, int position, String url){
        int index = position - listView.getFirstVisiblePosition();//求出index，index即为要更新的item相对当前可见画面第一项的位置
        if(index>=0 && index<listView.getChildCount()){//判断item是否在可见画面(getChildCount获得当前页面的item数，getCount获得总的item数)
            MultipleView multipleView = (MultipleView) getItem(position);
            multipleView.setAvatarUrl(url);
            View newItem = listView.getChildAt(index);
            getView(position,newItem,listView);
        }
    }

    public void updataItemTitle(ListView listView, int position, String title){
        int index = position - listView.getFirstVisiblePosition();//求出index，index即为要更新的item相对当前可见画面第一项的位置
        if(index>=0 && index<listView.getChildCount()){//判断item是否在可见画面(getChildCount获得当前页面的item数，getCount获得总的item数)
            MultipleView multipleView = (MultipleView) getItem(position);
            multipleView.setItemTitle(title);
            View newItem = listView.getChildAt(index);
            getView(position,newItem,listView);
        }
    }

    public void updataItemValue(ListView listView, int position, String value){
        int index = position - listView.getFirstVisiblePosition();//求出index，index即为要更新的item相对当前可见画面第一项的位置
        if(index>=0 && index<listView.getChildCount()){//判断item是否在可见画面(getChildCount获得当前页面的item数，getCount获得总的item数)
            MultipleView multipleView = (MultipleView) getItem(position);
            multipleView.setItemVaule(value);
            View newItem = listView.getChildAt(index);
            getView(position,newItem,listView);
        }
    }

    public class TextViewHolder{
        TextView tv_title;
        TextView tv_value;
    }

    public class ImgViewHolder{
        TextView tv_title;
        SimpleDraweeView simpleDraweeView;
    }

}
