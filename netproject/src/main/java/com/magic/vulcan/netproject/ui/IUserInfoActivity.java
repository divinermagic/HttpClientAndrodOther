package com.magic.vulcan.netproject.ui;

/**
 * 用户信息界面的接口
 */
public interface IUserInfoActivity {

    /**
     * 用户头像正在上传
     */
    void onUpLoading();

    /**
     * 用户头像上传成功
     */
    void onLoadSucess(String newUrl);

    /**
     *  用户头像上传失败
     */
    void onLoadFailed();

}
