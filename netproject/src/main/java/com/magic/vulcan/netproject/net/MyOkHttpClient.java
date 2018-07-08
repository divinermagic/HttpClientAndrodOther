package com.magic.vulcan.netproject.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    /*16.[成功][失败][handler的消息类型(网络请求的消息类型)]的常量类型*/
    private final static int NET_RESPONSE = 0;
    private final static int NET_FAILURE = 1;
    private final static int MSG_TYPE_NET = 1001;

    /*6.建一个全局的OkHttpClient对象*/
    private OkHttpClient mOkHttpClient;

    /*1.把MyOkHttpClient改成一个单例的模式*/
    private static MyOkHttpClient mInstance;

    /*12.把我们的callBack做一个本地的存储，再在外面的handler里面找到具体的某一个CallBack*/
    private Map<String,NetResultCallBack> callBackMap = new HashMap<String,NetResultCallBack>();

    /*11.把我们的OkHttpClient的回调转到我们的UI线程里面来*/
    private Handler mHandler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            /*18.判断下消息的类型*/
            if(msg.what==MSG_TYPE_NET){
                String key = msg.getData().getString("key");
                int type = msg.getData().getInt("type");
                /*19.从callBackMap里取出我们的callback，然后再删除下，防止我们的callBackMap越来越大*/
                NetResultCallBack callBack = callBackMap.get(key);
                callBackMap.remove(key);
                /*20.然后根据我们的callback类型，判断下并做不同的处理*/
                if (type == NET_RESPONSE){
                    String body = (String) msg.obj;
                    callBack.onResult(body);
                    msg.obj = null;
                }else if(type == NET_FAILURE) {
                    String res = "Failed";
                    if (msg.getData()!=null){
                        res = msg.getData().getString("error");
                    }
                    callBack.onError(res);
                }
            }
        }
    };

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
    public void httpGet(String url, Map<String, String> params, final NetResultCallBack callBack) {

        /*13.当我们发起网络请求的时候，首先第一步就要把我们的callback放进我们的callBackMap里面*/
        callBackMap.put(callBack.toString(),callBack);

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
                sendFailureMsg2Handler(callBack,e);
//                /*14.首先判断我们的Callback是不是为空，为空不处理，不为空就发送Message [GET和POST都可以用，可提取成公用的方法] */
//                if (callBack!=null){
//                    Bundle data = new Bundle();
//                    Message msg = mHandler.obtainMessage();
//                    /*15.设置类型，是判断[成功]还是[失败] 在全局中设置两个常量*/
//                    data.putInt("type",NET_FAILURE);
//                    /*17.放置我们callback的Key,用于找到我们的callback*/
//                    data.putString("key",callBack.toString());
//                    //把错误异常传递出去
//                    StringBuffer res = new StringBuffer();
//                    StackTraceElement[] ete = e.getStackTrace();
//                    if (ete!=null){
//                        for (StackTraceElement ee :ete){
//                            res.append(ee.toString());
//                            res.append("\n");
//                        }
//                    }
//                    data.putString("error",res.toString());
//                    msg.setData(data);
//                    msg.what = MSG_TYPE_NET;
//                    msg.sendToTarget();
//                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
//                /*14.首先判断我们的Callback是不是为空，为空不处理，不为空就发送Message[GET和POST都可以用，可提取成公用的方法]*/
//                if (callBack!=null){
//                    Bundle data = new Bundle();
//                    Message msg = mHandler.obtainMessage();
//                    /*15.设置类型，是判断[成功]还是[失败] 在全局中设置两个常量*/
//                    data.putInt("type",NET_RESPONSE);
//                    /*17.放置我们callback的Key,用于找到我们的callback*/
//                    data.putString("key",callBack.toString());
//                    msg.setData(data);
//                    msg.what = MSG_TYPE_NET;
//                    //一定要调用String的方法，不能是toString
//                    msg.obj = response.body().string();
//                    msg.sendToTarget();
//                }
                sendResponseMsg2Handler(callBack,response);

            }
        });

    }

    @Override
    public void httpPostPairs(String url, Map<String, String> params, final NetResultCallBack callBack) {
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
                sendFailureMsg2Handler(callBack,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendResponseMsg2Handler(callBack,response);
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
            }
        });
    }

    @Override
    public void upLoadFile(String url, Map<String, Object> params, final NetResultCallBack callBack) {
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
                sendFailureMsg2Handler(callBack,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendResponseMsg2Handler(callBack,response);
                Log.i(TAG,"MyOkHttpClient.httpGet.onResponse.请求成功，内容如下："+response.body().string());
            }
        });
    }

    /**
     * 21.[onFailure和onResponse]共用的方法
     * 向handler发送请求成功的消息
     * @param callBack
     * @param response
     * @throws IOException
     */
    private void sendResponseMsg2Handler(NetResultCallBack callBack,Response response) throws IOException{
        /*14.首先判断我们的Callback是不是为空，为空不处理，不为空就发送Message[GET和POST都可以用，可提取成公用的方法]*/
        if (callBack!=null){
            Bundle data = new Bundle();
            Message msg = mHandler.obtainMessage();
            /*15.设置类型，是判断[成功]还是[失败] 在全局中设置两个常量*/
            data.putInt("type",NET_RESPONSE);
            /*17.放置我们callback的Key,用于找到我们的callback*/
            data.putString("key",callBack.toString());
            msg.setData(data);
            msg.what = MSG_TYPE_NET;
            //一定要调用String的方法，不能是toString
            msg.obj = response.body().string();
            msg.sendToTarget();
        }
    }

    /**
     * 21.[onFailure和onResponse]共用的方法
     * 向handler发送网络请求失败的消息
     * @param callBack
     * @param e
     */
    private void sendFailureMsg2Handler(NetResultCallBack callBack,IOException e){
        /*14.首先判断我们的Callback是不是为空，为空不处理，不为空就发送Message [GET和POST都可以用，可提取成公用的方法] */
        if (callBack!=null){
            Bundle data = new Bundle();
            Message msg = mHandler.obtainMessage();
            /*15.设置类型，是判断[成功]还是[失败] 在全局中设置两个常量*/
            data.putInt("type",NET_FAILURE);
            /*17.放置我们callback的Key,用于找到我们的callback*/
            data.putString("key",callBack.toString());
            //把错误异常传递出去
            StringBuffer res = new StringBuffer();
            StackTraceElement[] ete = e.getStackTrace();
            if (ete!=null){
                for (StackTraceElement ee :ete){
                    res.append(ee.toString());
                    res.append("\n");
                }
            }
            data.putString("error",res.toString());
            msg.setData(data);
            msg.what = MSG_TYPE_NET;
            msg.sendToTarget();
        }
    }
}
