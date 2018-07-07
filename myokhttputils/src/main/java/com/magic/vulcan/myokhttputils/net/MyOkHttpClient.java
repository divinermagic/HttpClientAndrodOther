package com.magic.vulcan.myokhttputils.net;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyOkHttpClient implements IOKHttpClient {

    private static final String TAG = MyOkHttpClient.class.getCanonicalName();

    /*6.建一个全局的OkHttpClient对象*/
    private OkHttpClient mOkHttpClient;

    /*1.把MyOkHttpClient改成一个单例的模式*/
    private static MyOkHttpClient mInstance;

    /*2.把构造方法改成私有的*/
    private MyOkHttpClient(){
        /*7.在构造方法中New一个OkHttpClient cookieJar:Cookie自动管理的方式*/
        mOkHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {

            List<Cookie> cookies;

            /**
             * 当服务器给我回应的时候我可以保持回应Cookie的信息
             * @param url
             * @param cookies
             */
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

                this.cookies = cookies;

            }

            /**
             * 把保持的Cookie信息给添加的每一次请求中
             * @param url
             * @return
             */
            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {

                if (cookies!=null){
                    return cookies;
                }
                return new ArrayList<Cookie>();
            }
        }).build();
    }
    /*3.返回我们的实例*/
    public static synchronized MyOkHttpClient getmInstance(){
        if (mInstance==null){
            mInstance = new MyOkHttpClient();
        }
        return mInstance;
    }

    @Override
    public void httpGet(String url, Map<String, String> params, NetResultCallBack callBack) {
        /*1.*/
        StringBuffer paramsString  = new StringBuffer();
        /*2.*/
        if (params!=null){
            for (String key:params.keySet()){
                String value = params.get(key);
                /*3.Get拼接Url示例：Http://192.168.0.11:45123/get/id=123456&name=jbc [id->key] [value->name]*/
                paramsString.append(key);
                paramsString.append("=");
                paramsString.append(value);
                paramsString.append("&");
            }
        }

        /*4.*/
        String res = null;
        if (paramsString.length()>0){
            res = paramsString.substring(0,paramsString.length()-1);
        }
        /*5.*/
        if (res!=null){
            url = url + res;
        }

        /*8.定义一个Request请求 [url:有参数就拼接，没有参数就用原来的] */
        Request request = new Request.Builder()
                .url(url)
                .build();
        /*9.得到一个Call对象*/
        Call call = mOkHttpClient.newCall(request);
        /*10.实现一个异步的请求*/
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"MyOkHttpClient.httpGet.onFailure.请求失败，异常如下："+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
            }
        });

    }

    @Override
    public void httpPostPairs(String url, Map<String, String> params, NetResultCallBack callBack) {
        /*5.*/
        FormBody.Builder forBuilder = new FormBody.Builder();
        /*6*/
        if (params!=null){
            for (String key:params.keySet()){
                String value = params.get(key);
                forBuilder.add(key,value);
            }
        }

        /*7*/
        RequestBody requestBody = forBuilder.build();

        /*8.定义一个Request请求 [url:有参数就拼接，没有参数就用原来的] */
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        /*9.得到一个Call对象*/
        Call call = mOkHttpClient.newCall(request);
        /*10.实现一个异步的请求*/
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"MyOkHttpClient.httpGet.onFailure.请求失败，异常如下："+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
            }
        });
    }

    @Override
    public void upLoadFile(String url, Map<String, Object> params, NetResultCallBack callBack) {
        /*1.*/
        MultipartBody.Builder builder = new MultipartBody.Builder();
        /*2*/
        builder.setType(MultipartBody.FORM);

        /*3*/
        if (params!=null){
            for (String key:params.keySet()){
                Object value = params.get(key);
                /*4.如果value不是一个文件，我们就已键值对的方式添加进来*/
                if (!(value instanceof File))
                {
                    builder.addFormDataPart(key,value.toString());
                }else {
                    File file = (File) value;
                    //null:指的是不知道文件的具体类型，如果是一张图片的话 就直接制定png就行了
                    builder.addFormDataPart(key,file.getName(),RequestBody.create(null,file));
                }
            }
        }

        /*5*/
        RequestBody requestBody = builder.build();

        /*6.定义一个Request请求 [url:有参数就拼接，没有参数就用原来的] */
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        /*7.得到一个Call对象*/
        Call call = mOkHttpClient.newCall(request);
        /*8.实现一个异步的请求*/
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG,"MyOkHttpClient.httpGet.onFailure.请求失败，异常如下："+e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
            }
        });
    }
}
