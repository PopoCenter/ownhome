package com.tencent.wxcloudrun.vo;



public class VerifyCodeVo {

    private static final long serialVersionUID = 382951780872523489L;

    /**
     * 是否验证通过
     */
    private boolean isVerify;


    public boolean isVerify() {
        return isVerify;
    }

    public void setVerify(boolean verify) {
        isVerify = verify;
    }
}