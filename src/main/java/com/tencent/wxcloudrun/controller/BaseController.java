package com.tencent.wxcloudrun.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 抽象Controller
 *
 * @author dongdongxie
 * @date 2024/04/13
 */
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());


    public String getOpenId(HttpHeaders headers) {
        return headers.getFirst("X_WX_OPENID");
    }

}
