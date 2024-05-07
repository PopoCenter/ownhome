package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dto.RegisterDto;
import com.tencent.wxcloudrun.entity.UserEntity;

import java.util.Map;

public interface UserIService extends IService<UserEntity> {

    /**
     * 分页查询
     * @param pageParam
     * @param params
     * @return
     */
    IPage<UserEntity> searchPage(Page<UserEntity> pageParam, Map<String, Object> params);


    /**
     * 注册
     * @param registerDto
     * @param openId
     * @throws BusinessDefaultException
     */
    void register(RegisterDto registerDto, String openId) throws BusinessDefaultException;

    /**
     * 查询用户
     * @param openId
     * @return
     * @throws BusinessDefaultException
     */
    UserEntity findByOpenId(String openId) throws BusinessDefaultException;

}
