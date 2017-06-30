package com.minardwu.see.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.minardwu.see.R;
import com.minardwu.see.adapter.NewsAdapter;
import com.minardwu.see.base.BaseActivity;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.NewsEntity;
import com.minardwu.see.event.GetNewsEvent;
import com.minardwu.see.event.ResultCodeEvent;
import com.minardwu.see.net.Friend;
import com.minardwu.see.net.News;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

public class NewsActivity extends BaseActivity {

    private List<NewsEntity> list;
    private ListView listView;
    private View emptyview;
    private NewsAdapter newsAdapter;
    private MaterialDialog dialog_handle_news;

    private String newFriendId;
    private NewsEntity tempNewsEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
        News news = new News();
        news.getNews();
    }

    private void initView() {
        list = new ArrayList<NewsEntity>();
        listView = (ListView) findViewById(R.id.lv_news);
        emptyview = findViewById(R.id.emptyview);
        listView.setEmptyView(emptyview);
        newsAdapter = new NewsAdapter(this,R.layout.listview_news, list);
        listView.setAdapter(newsAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tempNewsEntity = list.get(i);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(NewsActivity.this, android.R.layout.simple_list_item_1);
                arrayAdapter.add("忽略");
                arrayAdapter.add("同意");
                final ListView listView = new ListView(NewsActivity.this);
                listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (8 * scale + 0.5f);
                listView.setPadding(0, dpAsPixels, 0, dpAsPixels - 5);
                listView.setDividerHeight(0);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            News.deleteNews(tempNewsEntity.getNewsid());
                            dialog_handle_news.dismiss();
                        } else if (position == 1) {
                            newFriendId = tempNewsEntity.getUserid();
                            Friend.addFriendWithCheck(AVUser.getCurrentUser().getObjectId(),newFriendId);
                            dialog_handle_news.dismiss();
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
        if(event.getType()==0){
            Log.v("getNews","no news");
        }else {
            NewsEntity news = event.getNews();
            if(news!=null){
                Log.v("getNews","success");
                if(!list.contains(news)){
                    list.add(news);
                }
                newsAdapter.notifyDataSetChanged();
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultCodeEvent(ResultCodeEvent event){
        int result = event.getResult();
        if(result==1){
            Toast.makeText(NewsActivity.this, "添加好友成功"+result, Toast.LENGTH_SHORT).show();
            News.deleteNews(tempNewsEntity.getNewsid());
            Config.me.setFriendid(newFriendId);
            Config.you.setUserid(newFriendId);
        }else {
            Toast.makeText(NewsActivity.this, "出错了，错误代码："+result, Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
