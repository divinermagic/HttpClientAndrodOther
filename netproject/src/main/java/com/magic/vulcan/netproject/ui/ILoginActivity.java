package com.magic.vulcan.netproject.ui;

/**
 * 登陆界面接口（MVP）
 */
public interface ILoginActivity {

    /**
     * 登录中
     */
    void onLogining();

    /**
     * 登录成功
     */
    void onLoginSucess();

    /**
     * 登录失败
     */
    void onLoginFailed();

    /**
     * 正在注册
     */
    void onRegistering();

    /**
     * 注册成功
     */
    void onRegisterSucess();

    /**
     * 注册失败
     */
    void onRegisterFailed();

}
