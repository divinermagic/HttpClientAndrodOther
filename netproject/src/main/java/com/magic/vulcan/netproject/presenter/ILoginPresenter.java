package com.magic.vulcan.netproject.presenter;

import com.magic.vulcan.netproject.ui.ILoginActivity;

/**
 * P层 业务层
 */
public interface ILoginPresenter {

    /**
     * 用来对ILoginPresenter做个初始化
     * @param view
     */
    void create(ILoginActivity view);

    void register(String userName,String passWord);

    void login(String userName,String passWords);

}
