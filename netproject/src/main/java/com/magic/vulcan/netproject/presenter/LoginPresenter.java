package com.magic.vulcan.netproject.presenter;

import com.magic.vulcan.netproject.net.MyOkHttpClient;
import com.magic.vulcan.netproject.net.NetResultCallBack;
import com.magic.vulcan.netproject.ui.ILoginActivity;

import java.util.HashMap;

import static com.magic.vulcan.netproject.app.MyApplication.baseUrl;

/**
 * 登录接口的实现类
 */
public class LoginPresenter implements ILoginPresenter {

    private ILoginActivity mView;

    private MyOkHttpClient myOkHttpClient = MyOkHttpClient.getmInstance();

    @Override
    public void create(ILoginActivity view) {
        this.mView = view;
    }

    /**
     * - signup_username："用户名"
     * - type："用户类型"(1学生，2老师)
     * - big_direction："学习/研究大方向id"
     * - signup_password："用户密码"
     */
    @Override
    public void register(String userName, String passWord) {
        HashMap<String,String> params = new HashMap<>();
        params.put("signup_username",userName);
        params.put("type",""+1);
        params.put("big_direction",""+1);
        params.put("signup_password",passWord);

        mView.onRegistering();

        myOkHttpClient.httpPostPairs(baseUrl + "/user/do-signup", params, new NetResultCallBack() {
            @Override
            public void onResult(String body) {
                mView.onRegisterSucess();
            }

            @Override
            public void onError(String body) {
                mView.onRegisterFailed();
            }
        });

    }

    @Override
    public void login(String userName, String passWords) {
        HashMap<String,String> params = new HashMap<>();
        params.put("login_username",userName);
        params.put("login_password",passWords);

        mView.onLogining();

        myOkHttpClient.httpPostPairs(baseUrl + "/user/do-login", params, new NetResultCallBack() {
            @Override
            public void onResult(String body) {
                mView.onLoginSucess();
            }

            @Override
            public void onError(String body) {
                mView.onLoginFailed();
            }
        });

    }
}
