package com.minardwu.see.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.minardwu.see.R;
import com.minardwu.see.adapter.NewsAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.entity.NewsEntity;
import com.minardwu.see.event.GetNewsEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class NewsActivity extends BaseActivity {

    private List<NewsEntity> list;
    private ListView listView;
    private NewsAdapter newsAdapter;
    private MaterialDialog dialog_handle_news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
        com.minardwu.see.net.News news = new com.minardwu.see.net.News();
        news.getNews();
    }

    private void initView() {
        list = new ArrayList<NewsEntity>();
        listView = (ListView) findViewById(R.id.lv_news);
//        list.add(new News(1,2,3,4));
//        list.add(new News(1,2,3,4));
//        list.add(new News(1,2,3,4));
        newsAdapter = new NewsAdapter(this,R.layout.listview_news, list);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewsActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("同意");
                arrayAdapter.add("拒绝");
                ListView listView = new ListView(NewsActivity.this);
                listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //dialog_handle_news.dismiss();
                        if (position == 0) {

                        } else if (position == 1) {

                        }
                    }
                });
                dialog_handle_news = new MaterialDialog(NewsActivity.this).setContentView(listView);
                dialog_handle_news.show();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetNewsEvent(GetNewsEvent event){
        NewsEntity news = event.getNews();
        if(news!=null){
            Log.v("getNewsend",news.getUsername());
            list.add(news);
            newsAdapter.notifyDataSetChanged();
        }else {
            Log.v("getNewsend","fail");
        }
    };

    @Override
    protected int getContentView() {
        return R.layout.activity_news;
    }

    @Override
    protected void toolbarSetting(ToolbarHelper toolbarHelper) {
        super.toolbarSetting(toolbarHelper);
        toolbarHelper.setTitle("消息");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
