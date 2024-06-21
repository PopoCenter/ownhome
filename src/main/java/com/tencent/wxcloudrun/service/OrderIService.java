package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.OrderEntity;
import com.tencent.wxcloudrun.vo.OrderDetailVo;

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
     * 编辑订单
     *
     * @param userId
     * @param editDto
     * @throws BusinessDefaultException
     */
    void edit(Long userId, OrderEditDto editDto) throws BusinessDefaultException;


    /**
     * 订单列表
     *
     * @param userId
     * @param listDto
     * @return
     * @throws BusinessDefaultException
     */
    Page<OrderEntity> list(Long userId, OrderListDto listDto) throws BusinessDefaultException;


    /**
     * install
     *
     * @param userId
     * @param installDto
     * @throws BusinessDefaultException
     */
    void install(Long userId, OrderInstallDto installDto) throws BusinessDefaultException;



    /**
     * install
     *
     * @param userId
     * @param cancelDto
     * @throws BusinessDefaultException
     */
    void cancel(Long userId, OrderCancelDto cancelDto) throws BusinessDefaultException;




    /**
     * afterSales
     *
     * @param userId
     * @param salesDto
     * @throws BusinessDefaultException
     */
    void afterSales(Long userId, OrderAfterSalesDto salesDto) throws BusinessDefaultException;


    /**
     * afterSales
     *
     * @param userId
     * @param editDto
     * @throws BusinessDefaultException
     */
    void afterSalesEdit(Long userId, OrderAfterSalesEditDto editDto) throws BusinessDefaultException;


    /**
     * afterSales
     *
     * @param userId
     * @param finishDto
     * @throws BusinessDefaultException
     */
    void afterSalesFinish(Long userId, OrderAfterSalesFinishDto finishDto) throws BusinessDefaultException;



    /**
     * afterSales
     *
     * @param userId
     * @param cancelDto
     * @throws BusinessDefaultException
     */
    void afterSalesCancel(Long userId, OrderAfterSalesCancelDto cancelDto) throws BusinessDefaultException;


    /**
     * detail
     *
     * @param orderId
     * @return
     * @throws BusinessDefaultException
     */
    OrderDetailVo detail(Long orderId) throws BusinessDefaultException;

}
