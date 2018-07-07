package com.magic.vulcan.myokhttputils.net;


import java.util.Map;

/**
 * oKHttpClient接口类
 */
public interface IOKHttpClient {

    /**
     * 发起GET请求
     * @param url 请求的Url
     * @param params 请求的参数 Map集合的方式
     * @param callBack 服务器回调函数类 [响应结果的回调函数]
     */
    void httpGet(String url, Map<String,String> params,NetResultCallBack callBack);

    /**
     * 发起POST请求,上传键值对
     * @param url 请求的Url
     * @param params 请求的参数 (键值对）
     * @param callBack 服务器回调函数类 [响应结果的回调函数]
     */
    void httpPostPairs(String url,Map<String,String> params,NetResultCallBack callBack);

    /**
     * 文件上传（头像上传）
     * @param url 请求的Url
     * @param params 请求的参数 (包含文件）
     * @param callBack 服务器回调函数类 [响应结果的回调函数]
     */
    void upLoadFile(String url,Map<String,Object> params,NetResultCallBack callBack);



}
