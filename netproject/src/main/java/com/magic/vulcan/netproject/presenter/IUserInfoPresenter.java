package com.magic.vulcan.netproject.presenter;

import com.magic.vulcan.netproject.ui.IUserInfoActivity;

/**
 * P层 用户信息界面的接口
 */
public interface IUserInfoPresenter {

    /**
     * 用来对IUserInfoPresenter做个初始化
     * @param view
     */
    void create(IUserInfoActivity view);

    /**
     * 用来上传用户的头像
     */
    void upLoadIcon(String path);

}
