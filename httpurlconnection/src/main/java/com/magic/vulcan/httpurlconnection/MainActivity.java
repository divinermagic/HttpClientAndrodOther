package com.magic.vulcan.httpurlconnection;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 1、创建一个URL对象
 *      URL url = new URL(http://www.baidu.com);
 * 2、调用URL对象的openConnection( )来获取HttpURLConnection对象实例
 *      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
 * 3、设置HTTP请求使用的方法:GET或者POST,或者其他请求方式 
 *      conn.setRequestMethod("GET");
 * 4、设置连接超时，读取超时的毫秒数，以及服务器希望得到的一些消息头 
 *      conn.setConnectTimeout(6*1000);conn.setReadTimeout(6 * 1000);
 * 5、调用getInputStream()方法获得服务器返回的输入流，然后读取输入流
 *      InputStream in = conn.getInputStream();
 * 6、最后调用disconnect()方法将HTTP连接关掉 
 *      conn.disconnect();
 */
public class MainActivity extends AppCompatActivity {

    private final String baseUrl = "http://35.185.149.228";

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
                        getRequest();
                    }
                }).start();
            }
        });

        findViewById(R.id.postpairsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postPairsRequest();
                    }
                }).start();
            }
        });

        findViewById(R.id.postJsonBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        postJsonRequest();
                    }
                }).start();
            }
        });

    }


    /**
     * 发送GET请求
     * 单词：
     *      connect 连接
     *      disconnect 断开
     */
    public void getRequest() {
        try {
            URL url = new URL(baseUrl + "/user/get-big-direction");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            if (conn.getResponseCode()!=200){
                Log.e(TAG, "getRequest()请求失败");
                return;
            }

            String result = convertStream2String(inputStream);
            Log.i(TAG, "getRequest() result:  " + result);
            if (conn!=null){
                conn.disconnect();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送POST请求，上传键值对
     */
    private void postPairsRequest() {
        try {
            URL url = new URL(baseUrl + "/user/do-login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            byte[] pairsLogin = new String("login_username=jiangwangchaochao3&login_password=12345678").getBytes("UTF-8");
            out.write(pairsLogin);
            out.flush();
            out.close();
            InputStream inputStream = conn.getInputStream();

            if (conn.getResponseCode() != 200) {
                Log.e(TAG, "getRequest()请求失败");
                return;
            }

            String result = convertStream2String(inputStream);

            Log.i(TAG, "getRequest() result:  " + result);

            if (conn != null)
                conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 发送POST请求，上传JSON串
     */
    private void postJsonRequest() {
        Log.i(TAG,"请把postPairsRequest里面的字符串改成json");
    }


    /**
     * 将InputStream转为String
     */
    private String convertStream2String(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
