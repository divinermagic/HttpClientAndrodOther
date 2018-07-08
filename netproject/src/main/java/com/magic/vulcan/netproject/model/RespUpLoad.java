package com.magic.vulcan.netproject.model;

/**
 * 服务器上传图片之后给的一个数据结构
 */
public class RespUpLoad {

    public int status;

    public ImgUpload data;

    public class ImgUpload {
        public String img_url;
    }

}
