package com.magic.vulcan.okhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttp3的简单使用
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.executeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        okHttpExecute();
                    }
                }).start();
            }
        });


        findViewById(R.id.enqueueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "调用线程ID： " + Thread.currentThread().getId());
                okHttpEnqueue();
            }
        });


    }

    /**
     * 同步请求
     */
    private void okHttpExecute() {
        String url = "http://www.jikexueyuan.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            Log.i(TAG, "\n服务器返回结果：  " + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异步请求
     */
    private void okHttpEnqueue() {

        String url = "http://www.jikexueyuan.com";
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "线程ID： " + Thread.currentThread().getId());

                Log.i(TAG, "服务器返回结果：  " + response.body().string());
            }
        });


    }
}
