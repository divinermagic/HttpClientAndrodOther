package com.magic.vulcan.netproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.magic.vulcan.netproject.R;
import com.magic.vulcan.netproject.model.UserInfo;
import com.magic.vulcan.netproject.presenter.UserInfoPresenter;
import com.magic.vulcan.netproject.ui.component.CircleImageView;
import com.magic.vulcan.netproject.ui.component.CustomProgress;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.List;

import static com.magic.vulcan.netproject.app.MyApplication.baseUrl;
import static com.magic.vulcan.netproject.app.MyApplication.mImageLoader;
import static com.magic.vulcan.netproject.app.MyApplication.mUserInfo;

public class UserInfoActivity extends AppCompatActivity implements IUserInfoActivity{

    private CircleImageView mUserIcon;

    private TextView mUserNameTv;

    private TextView mUserIdTv;

    private TextView mUserTapeTv;

    private CustomProgress mCustomProgress;

    private UserInfoPresenter mUserInfoPresenter;

    /*图片显示的配置*/
    private DisplayImageOptions mOptions;

    private static final int REQUEST_PIC_CHOOSED = 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        mUserInfoPresenter = new UserInfoPresenter();
        mUserInfoPresenter.create(this);

        mUserIcon = (CircleImageView) findViewById(R.id.userIcon);
        mUserNameTv = (TextView) findViewById(R.id.userNameTxt);
        mUserIdTv = (TextView) findViewById(R.id.userIDTxt);
        mUserTapeTv = (TextView) findViewById(R.id.userTypeTxt);

        mOptions = new DisplayImageOptions.Builder()
                /*设置下载的图片是否缓存在内存中*/
                .cacheInMemory(true)
                /*设置下载的图片是否缓存在SD卡中*/
                .cacheOnDisk(true)
                .build();
        mImageLoader.displayImage(baseUrl+mUserInfo.getAvatar(),mUserIcon,mOptions);


        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PhotoPickerIntent intent = new PhotoPickerIntent(UserInfoActivity.this);
                intent.setSelectModel(SelectModel.SINGLE);//单选模式
                startActivityForResult(intent,REQUEST_PIC_CHOOSED);
            }
        });
    }

    /*这一步和startActivityForResult(intent,REQUEST_PIC_CHOOSED)相关联*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode==REQUEST_PIC_CHOOSED){
                List<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                if (paths!=null){
                    String path = paths.get(0);//0:第一个
                    mUserInfoPresenter.upLoadIcon(path);
                }
            }
        }
    }

    @Override
    public void onUpLoading() {

        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        /*四个参数：上下文，显示文字，是否可以取消，取消的监听*/
        mCustomProgress = CustomProgress.show(UserInfoActivity.this,"正在上传...",true,null);
    }

    @Override
    public void onLoadSucess(String newUrl) {
        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        /*四个参数：上下文，显示文字，是否可以取消，取消的监听*/
        mCustomProgress = CustomProgress.show(UserInfoActivity.this,"上传成功",true,null);
    }

    @Override
    public void onLoadFailed() {
        if (mCustomProgress!=null){
            mCustomProgress.cancel();
            mCustomProgress = null;
        }
        /*四个参数：上下文，显示文字，是否可以取消，取消的监听*/
        mCustomProgress = CustomProgress.show(UserInfoActivity.this,"上传失败",true,null);
    }
}
