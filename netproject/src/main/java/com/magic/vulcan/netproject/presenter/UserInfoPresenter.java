package com.magic.vulcan.netproject.presenter;

import com.magic.vulcan.netproject.model.RespUpLoad;
import com.magic.vulcan.netproject.net.MyOkHttpClient;
import com.magic.vulcan.netproject.net.NetResultCallBack;
import com.magic.vulcan.netproject.tools.UtilsJson;
import com.magic.vulcan.netproject.ui.IUserInfoActivity;

import java.io.File;
import java.util.HashMap;

import static com.magic.vulcan.netproject.app.MyApplication.baseUrl;
import static com.magic.vulcan.netproject.app.MyApplication.mUserInfo;

/**
 * 用户信息的实现类
 */
public class UserInfoPresenter implements IUserInfoPresenter {

    private IUserInfoActivity mView;

    private MyOkHttpClient myOkHttpClient = MyOkHttpClient.getmInstance();

    @Override
    public void create(IUserInfoActivity view) {
        this.mView = view;
    }

    @Override
    public void upLoadIcon(String path) {

        File uploadFile = new File(path);

        HashMap<String,Object> params = new HashMap<>();
        params.put("UploadForm[file]",uploadFile);

        mView.onUpLoading();

        myOkHttpClient.upLoadFile(baseUrl + "/file/upload-img", params, new NetResultCallBack() {
            @Override
            public void onResult(String body) {

                RespUpLoad respUpLoad = UtilsJson.getBean(body,RespUpLoad.class);
                mUserInfo.setAvatar(respUpLoad.data.img_url);
                mView.onLoadSucess(respUpLoad.data.img_url);
            }

            @Override
            public void onError(String body) {
                mView.onLoadFailed();
            }
        });
    }
}
