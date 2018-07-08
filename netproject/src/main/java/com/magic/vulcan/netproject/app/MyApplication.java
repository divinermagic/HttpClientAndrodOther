package com.magic.vulcan.netproject.app;

import android.app.Application;

import com.magic.vulcan.netproject.model.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApplication extends Application {

    public static final String TAG = "NetProject";

    public static final String baseUrl = "http://35.185.149.228";

    public static final UserInfo mUserInfo = new UserInfo();

    public static ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(this));
    }
}
