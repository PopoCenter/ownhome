package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dto.OrderCreateDto;
import com.tencent.wxcloudrun.dto.OrderListDto;
import com.tencent.wxcloudrun.entity.OrderEntity;
import com.tencent.wxcloudrun.vo.OrderListVo;

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

    /**
     * 创建订单
     *
     * @param userId
     * @param createDto
     * @throws BusinessDefaultException
     */
    void create(Long userId, OrderCreateDto createDto) throws BusinessDefaultException;


    /**
     * 订单列表
     *
     * @param userId
     * @param listDto
     * @return
     * @throws BusinessDefaultException
     */
    Page<OrderEntity> list(Long userId, OrderListDto listDto) throws BusinessDefaultException;
}
