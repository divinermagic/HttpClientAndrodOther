package com.magic.vulcan.retrofitutils.net;


import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;
/**
 * 网络请求的接口类
 */
public interface BaseApiService {

    @POST("/user/do-login")
    @FormUrlEncoded
    Observable<ResponseBody> login(@Field("login_username") String username, @Field("login_password") String password);

    //TODO 以下同理定义其他接口

}
