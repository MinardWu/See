package com.minardwu.see.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.minardwu.see.R;
import com.minardwu.see.adapter.PhotoAdapter;
import com.minardwu.see.base.Config;
import com.minardwu.see.entity.Photo;

import java.util.ArrayList;
import java.util.List;


public class MyFragment extends Fragment {

    private GridView gridView;
    private List<Photo> list;
    private PhotoAdapter photoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        gridView = (GridView) view.findViewById(R.id.gv_my);
        list = new ArrayList<Photo>();
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        list.add(new Photo(1,1, Config.tempAvatarUrl,"",1));
        photoAdapter = new PhotoAdapter(getContext(),R.layout.gridview_photo,list);
        gridView.setAdapter(photoAdapter);
        return view;
    }


}
