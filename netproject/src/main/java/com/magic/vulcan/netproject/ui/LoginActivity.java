package com.magic.vulcan.netproject.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magic.vulcan.netproject.R;
import com.magic.vulcan.netproject.presenter.LoginPresenter;
import com.magic.vulcan.netproject.ui.component.CustomProgress;

public class LoginActivity extends AppCompatActivity implements ILoginActivity{

    private EditText mUserNameEdt;
    private EditText mUserPassWordEdt;

    private Button registerBtn,loginBtn;

    private CustomProgress mCustomProgress;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.create(this);

        mUserNameEdt = (EditText) findViewById(R.id.userNameEdt);
        mUserPassWordEdt = (EditText) findViewById(R.id.passWordEdt);

        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = mUserNameEdt.getText().toString().trim();
                String passWord = mUserPassWordEdt.getText().toString().trim();
                if (userName!=null&&!userName.equals("")&&passWord!=null&&!passWord.equals("")){
                    mLoginPresenter.login(userName,passWord);
                }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mUserNameEdt.getText().toString().trim();
                String passWord = mUserPassWordEdt.getText().toString().trim();

                if (userName!=null&&!userName.equals("")&&passWord!=null&&!passWord.equals("")){
                    mLoginPresenter.register(userName,passWord);
                }
            }
        });

    }

    @Override
    public void onLogining() {

        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        /*四个参数：上下文，显示文字，是否可以取消，取消的监听*/
        mCustomProgress = CustomProgress.show(LoginActivity.this,"正在登录...",true,null);
    }

    @Override
    public void onLoginSucess() {

        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this,UserInfoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginFailed() {

        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistering() {
        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        /*四个参数：上下文，显示文字，是否可以取消，取消的监听*/
        mCustomProgress = CustomProgress.show(LoginActivity.this,"正在注册...",true,null);
    }

    @Override
    public void onRegisterSucess() {
        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegisterFailed() {
        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
    }
}
