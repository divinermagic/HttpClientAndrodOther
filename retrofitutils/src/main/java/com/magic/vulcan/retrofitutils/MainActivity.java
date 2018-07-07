package com.magic.vulcan.retrofitutils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.magic.vulcan.retrofitutils.net.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitClient.getmInstance().createBaseApi().login("jikexueyuanjgc", "123456", new Subscriber<ResponseBody>() {

                    @Override
                    public void onCompleted() {
                        Log.e(TAG, "onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError()");
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        //请求成功
                        try {
                            Log.e(TAG, "onNext:  " + responseBody.string().toString() + "");   //response.body（）已经是RespDirectModel的数据结构了

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
