package com.magic.vulcan.retrofit;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getCanonicalName();

    private String baseUrl = "http://35.185.149.228";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.getBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getRequest();
            }
        });

        findViewById(R.id.postPairsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPairs();
            }
        });

        findViewById(R.id.uploadBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoad();
            }
        });
    }


    /**
     * 发起GET请求 第一步
     */
    private void getRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IGetInfos getInfos = retrofit.create(IGetInfos.class);
        Call<RespCategory> call = getInfos.getCategories();
        call.enqueue(new Callback<RespCategory>() {
            @Override
            public void onResponse(Call<RespCategory> call, Response<RespCategory> response) {
                RespCategory respCategory = response.body();
                Log.i(TAG,"onResponse()");
            }

            @Override
            public void onFailure(Call<RespCategory> call, Throwable t) {
                Log.e(TAG,"发起GET请求失败"+t);
            }
        });
    }

    /**
     * 发起GET请求 第二步
     */
    private class RespCategory{
        public int status;
        public List<CategoryModel> data;
        public class CategoryModel{
            public int id;
            public String name;
        }
    }

    /**
     * 发起GET请求 第三步
     */
    private interface IGetInfos{
        @GET("user/get-big-direction")
        Call<RespCategory> getCategories();
    }

    /**
     * 发起POST上传键值对（登录）第一步
     */
    private void postPairs() {
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUserLogin userLogin  = retrofit.create(IUserLogin.class);
        Call<RespLoginModel> login = userLogin.login("jikexueyuanjgc", "123456");
        login.enqueue(new Callback<RespLoginModel>() {
            @Override
            public void onResponse(Call<RespLoginModel> call, Response<RespLoginModel> response) {
                RespLoginModel body = response.body();
                Log.i(TAG,"onResponse()");
            }

            @Override
            public void onFailure(Call<RespLoginModel> call, Throwable t) {
                Log.e(TAG,"发起POST上传键值对（登录）失败"+t);

            }
        });
    }

    /**
     * 发起POST上传键值对（登录）第二步
     */
    public class RespLoginModel{
        public int status;
        public User date;
        public class User{
            private String id;
            private String usename;
            private String avatar;
        }
    }
    /**
     * 发起POST上传键值对（登录）第三步
     */
    public interface IUserLogin{
        @POST("user/do-login")
        @FormUrlEncoded
        Call<RespLoginModel>login(@Field("login_username")String username,@Field("login_password")String password);
    }


    /**
     * 上传文件 第一步
     */
    private void upLoad() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IUpLoadIcon upLoadIcon = retrofit.create(IUpLoadIcon.class);

        File file = new File(Environment.getExternalStorageDirectory()+"/ic_launcher.png");

        RequestBody photoBody = RequestBody.create(MediaType.parse("image/png"),file);

        MultipartBody.Part photo = MultipartBody.Part.createFormData("uploadImg[file]",file.getName(),photoBody);

        Call<RespUpLoadModel> call = upLoadIcon.upLoadIcon(photo.toString());

        call.enqueue(new Callback<RespUpLoadModel>() {
            @Override
            public void onResponse(Call<RespUpLoadModel> call, Response<RespUpLoadModel> response) {
                RespUpLoadModel respUpLoadModel = response.body();
                Log.i(TAG,"onResponse");
            }

            @Override
            public void onFailure(Call<RespUpLoadModel> call, Throwable t) {
                Log.e(TAG,"上传文件失败"+t);
            }
        });

    }

    /**
     * 上传文件 第二步
     */
    public class RespUpLoadModel{
        private int status;
        private Img date;
        public class Img{
            private String img_url;
        }
    }


    /**
     * 上传文件 第三步
     */
    public interface IUpLoadIcon{
        @POST("file/upload-img")
        @Multipart
        Call<RespUpLoadModel>upLoadIcon(@Part String photo);
    }

}
