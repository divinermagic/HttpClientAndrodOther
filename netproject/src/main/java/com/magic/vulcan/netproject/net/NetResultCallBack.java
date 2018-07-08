package com.magic.vulcan.netproject.net;


/**
 * 服务器回调函数类（okhttp3）
 */
public interface NetResultCallBack {

    /**
     * 服务器成功响应的回调函数
     * @param body 内容
     */
    void onResult(String body);

    /**
     * 网络请求失败回调函数
     * @param body 内容
     */
    void onError(String body);



}
