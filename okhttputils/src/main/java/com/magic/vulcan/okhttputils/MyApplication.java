package com.magic.vulcan.okhttputils;

import android.app.Application;

import com.lzy.okhttputils.OkHttpUtils;

/**
 * 使用okHttpUtils
 * 需要建立一个MyApplication的基本类
 * 并重写onCreate()方法进行全局配置
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*进行全局配置*/
        OkHttpUtils.init(this);
        /*然后在清单文件里面application节点下指向我们定义的[android:name=".MyApplication"]*/

    }
}
