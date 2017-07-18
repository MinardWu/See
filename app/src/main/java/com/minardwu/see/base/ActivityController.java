package com.minardwu.see.base;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MinardWu on 2017/6/21.
 */
public class ActivityController {

    public static List<Activity> activityList = new ArrayList<Activity>();

    public static void addActivity(Activity activity){
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity){
        activityList.remove(activity);
    }

    public static void finishAllActivity(){
        for(Activity activity:activityList)
            if(!activity.isFinishing())
                activity.finish();
    }

    public static boolean lockActivityIsActive = false;
    public static Activity lockActivity;

    public static void finishLockActivity(){
        lockActivity.finish();
        lockActivityIsActive = false;
    }

}
