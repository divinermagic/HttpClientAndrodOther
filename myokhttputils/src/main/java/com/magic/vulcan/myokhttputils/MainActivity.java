package com.magic.vulcan.myokhttputils;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.magic.vulcan.myokhttputils.net.MyOkHttpClient;
import com.magic.vulcan.myokhttputils.net.NetResultCallBack;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String baseUrl = "http://35.185.149.228";

    private static final String TAG = MainActivity.class.getCanonicalName();

    private Button getBtn,postPairsBtn,upLoadFileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getBtn = findViewById(R.id.getBtn);
        postPairsBtn = findViewById(R.id.postPairsBtn);
        upLoadFileBtn = findViewById(R.id.upLoadFileBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOkHttpClient.getmInstance().httpGet(baseUrl + "user/get-big-direction", null, new NetResultCallBack() {
                    @Override
                    public void onResult(String body) {
                        Log.i(TAG,"onResult:"+body);
                    }

                    @Override
                    public void onError(String body) {

                    }
                });
            }
        });

        postPairsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> params = new HashMap<String, String>();
                params.put("login_username","jgc");
                params.put("login_password","123456");
                MyOkHttpClient.getmInstance().httpPostPairs(baseUrl + "user/do_login", params, new NetResultCallBack() {
                    @Override
                    public void onResult(String body) {

                    }

                    @Override
                    public void onError(String body) {

                    }
                });
            }
        });

        upLoadFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,Object> params = new HashMap<>();

                File file= new File(Environment.getExternalStorageDirectory()+"ic_launcher_round.png");

                params.put("upload[file]",file);

                MyOkHttpClient.getmInstance().upLoadFile(baseUrl + "file/upload-img", params, new NetResultCallBack() {
                    @Override
                    public void onResult(String body) {

                    }

                    @Override
                    public void onError(String body) {

                    }
                });
            }
        });


    }
}
