package com.magic.vulcan.okhttputils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.cache.CacheMode;
import com.lzy.okhttputils.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okHttpUtilsGET();
            }
        });

        findViewById(R.id.postBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                okHttpUtilsPOST();
            }
        });
    }

    /**
     * GET请求
     * .execute()调用回调就是异步，不调用就是同步
     */
    private void okHttpUtilsGET(){

        OkHttpUtils.get("http://www.jikexueyuan.com")
                .tag("GET")
                .cacheKey("GetCacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG,s+"\n");
                    }
                });
    }


    /**
     * POST请求
     */
    private void okHttpUtilsPOST(){
        OkHttpUtils.post("http://www.jikexueyuan.com")
                .tag("GET")
                .cacheKey("GetCacheKey")
                .cacheMode(CacheMode.DEFAULT)
                .params("","")
                .params("fileName",new File(""))
                .upJson("")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i(TAG,s);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("GET");
    }

}
