package com.minardwu.see.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.minardwu.see.R;

/**
 * Created by MinardWu on 2016/3/8.
 */
public class ProgressDialog {

    public static Dialog createLoadingDialog(Context context) {

        View view =  LayoutInflater.from(context).inflate(R.layout.progress_dialog, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_progressDialog);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_progressDialog);

        //设置控件信息
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        iv.startAnimation(animation);

        Dialog loadingDialog = new Dialog(context, R.style.progress_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false);//不可以用“返回键”取消
        loadingDialog.setContentView(ll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

        return loadingDialog;

    }

}
