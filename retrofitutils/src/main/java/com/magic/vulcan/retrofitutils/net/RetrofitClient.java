package com.magic.vulcan.retrofitutils.net;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitClient {

    public static final String BASE_URL = "http://35.185.149.228/";

    /*成员的Retrofit*/
    private static Retrofit retrofit;

    private static RetrofitClient mInstance;

    private BaseApiService apiService;

    /*1.首先是构造函数*/
    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

    }

    /*2.设置成单例模式*/
    public static synchronized RetrofitClient getmInstance(){
        if (mInstance!=null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    /*3.把定义好的Api[BaseApiService]变成实例*/
    private <T> T create(final Class<T> service){
        if (service == null){
            throw new RuntimeException("Api Service is null ");
        }
        return retrofit.create(service);
    }

    /*4.把3号做一层封装*/
    public RetrofitClient createBaseApi(){
        apiService = create(BaseApiService.class);
        return this;
    }
    /**
     * 5.实现Login方法[Subscription:订阅]
     * 此方法和BaseApiService中定义的一致
     * @param username 账号
     * @param password 密码
     * @param subscriber 订阅
     * @return apiService.login
     */
    public Subscription login(String username, String password, Subscriber<ResponseBody> subscriber){
        return apiService.login(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
