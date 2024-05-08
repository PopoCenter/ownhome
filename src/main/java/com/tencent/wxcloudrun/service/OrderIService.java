package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.entity.OrderEntity;

import java.util.List;

public interface OrderIService extends IService<OrderEntity> {


    /**
     * 查询客户订单
     *
     * @param customerId
     * @return
     * @throws BusinessDefaultException
     */
    List<OrderEntity> findByCustomerId(Long customerId) throws BusinessDefaultException;

}
