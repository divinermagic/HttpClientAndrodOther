package com.magic.vulcan.retrofitrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private String baseUrl = "http://35.185.149.228";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

    }

    /**
     * 发起POST请求实现登录
     */
    private void login() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        IUserLogin userLogin = retrofit.create(IUserLogin.class);
        userLogin.login("jikexueyuanjgc","123456")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RespLoginModel>() {
                    @Override
                    public void onCompleted() {
                        Log.i("MainActivity","事件接收已经完成");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("MainActivity","连接不到服务器："+e);
                    }

                    @Override
                    public void onNext(RespLoginModel respLoginModel) {
                        String body = UtilsJson.createJsonString(respLoginModel);
                        Log.i("MainActivity","查询结果："+body);
                        Toast.makeText(MainActivity.this, "onNext+收到事件", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class RespLoginModel{
        public int status;
        public User date;
    }

    private class User{
        /**
         * 'id' : 用户id
         * 'username': 用户名
         * 'avatar'  : 用户默认头像路径
         **/
        private String id;
        private String username;
        private String avatar;
    }

    private interface IUserLogin{
        @POST("/user/do-login")
        @FormUrlEncoded
        Observable<RespLoginModel> login(@Field("login_username")String username, @Field("login_password")String password);
    }
}
