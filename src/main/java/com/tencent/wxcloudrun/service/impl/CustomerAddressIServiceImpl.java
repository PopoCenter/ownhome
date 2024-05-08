package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.dao.CustomerAddressMapper;
import com.tencent.wxcloudrun.dao.CustomerMapper;
import com.tencent.wxcloudrun.entity.CustomerAddressEntity;
import com.tencent.wxcloudrun.service.CustomerAddressIService;
import com.tencent.wxcloudrun.service.TeamIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class CustomerAddressIServiceImpl extends ServiceImpl<CustomerAddressMapper, CustomerAddressEntity> implements CustomerAddressIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private TeamIService teamIService;

}
