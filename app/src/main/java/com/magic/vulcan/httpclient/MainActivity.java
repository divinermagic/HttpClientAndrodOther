package com.magic.vulcan.httpclient;

import android.net.http.AndroidHttpClient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * HttpClient的学习
 * 6.0后废弃
 * 在build.gradle里添加
 * useLibrary 'org.apache.http.legacy'
 */
public class MainActivity extends AppCompatActivity {

    private Button getBtn,psotBtn;

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpClientGet();
                    }
                }).start();
            }
        });

        findViewById(R.id.postBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpClientPost();
                    }
                }).start();
            }
        });
    }

    /**
     * HttpClient执行GET请求
     * 1.创建HttpClient对象。
     * 2.创建对应的发送请求的对象 如果发送GET请求，则创建HttpGet对象，如果发送POST请求，则创建HttpPost对象
     * 3.设置发送请求的参数
     * 4.调用execute方法发送GET或POST请求，并返回HttpResponse对象
     * 5.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理
     */
    private void httpClientGet() {
        try {
            HttpClient getClient = AndroidHttpClient.newInstance("");
            String url = "http://35.185.149.228/user/get-big-direction";
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = getClient.execute(getRequest);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                InputStream inputStream = response.getEntity().getContent();
                String results = convertStream2String(inputStream);
                Log.i(TAG,"GET success :  "+ results );
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * HttpClient执行POST请求
     * 1.创建HttpClient对象。
     * 2.创建对应的发送请求的对象 如果发送GET请求，则创建HttpGet对象，如果发送POST请求，则创建HttpPost对象
     * 3.设置发送请求的参数
     * 4.调用execute方法发送GET或POST请求，并返回HttpResponse对象
     * 5.通过HttpResponse接口的getEntity方法返回响应信息，并进行相应的处理
     */
    private void httpClientPost() {
        HttpClient postClient = AndroidHttpClient.newInstance("");

        /**
         * 1. - signup_username："用户名"
         * 2. - type："用户类型"(1学生，2老师)
         * 3. - big_direction："学习/研究大方向id"
         * 4. - signup_password："用户密码"
         */

        try{
            String url = "http://35.185.149.228/user/do-signup";
            HttpPost request = new HttpPost(url);
            //使用NameValuePairs:名称值对
            List<NameValuePair> postParamters = new ArrayList<NameValuePair>();
            postParamters.add(new BasicNameValuePair("signup_username","jikexueyuan10086"));
            postParamters.add(new BasicNameValuePair("type","!"));
            postParamters.add(new BasicNameValuePair("big_direction","1"));
            postParamters.add(new BasicNameValuePair("signup_password","112233"));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParamters);
            HttpResponse response = postClient.execute(request);
            if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
                InputStream result = response.getEntity().getContent();
                Log.i(TAG,"post success : "+result);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 把InputStream转换成String [convertStream2String:流转换为字符串]
     * @param inputStream 网络获取的 字符串输入流
     * @return sb.toString()
     */
    private String convertStream2String(InputStream inputStream){

        StringBuilder sb = new StringBuilder();

        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            String line = null;
            while ((line = bufferedReader.readLine())!=null){
                sb.append(line+"\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                inputStream.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
