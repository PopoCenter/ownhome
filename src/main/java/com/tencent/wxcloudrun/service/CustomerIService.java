package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dto.CustomerCreateDto;
import com.tencent.wxcloudrun.dto.CustomerListDto;
import com.tencent.wxcloudrun.dto.RegisterDto;
import com.tencent.wxcloudrun.entity.CustomerAddressEntity;
import com.tencent.wxcloudrun.entity.CustomerEntity;
import com.tencent.wxcloudrun.entity.OrderEntity;
import com.tencent.wxcloudrun.vo.CustomerListVoItem;

import java.util.List;

public interface CustomerIService extends IService<CustomerEntity> {

    /**
     * 创建客户
     *
     * @param userId
     * @param customerCreateDto
     * @throws BusinessDefaultException
     */
    void create(Long userId, CustomerCreateDto customerCreateDto) throws BusinessDefaultException;

    /**
     * 列表
     *
     * @param teamId
     * @return
     * @throws BusinessDefaultException
     */
    List<CustomerListVoItem> findByTeamId(Long teamId, CustomerListDto queryDto) throws BusinessDefaultException;

    /**
     * 查询
     *
     * @param customerId
     * @return
     * @throws BusinessDefaultException
     */
    CustomerEntity findById(Long customerId) throws BusinessDefaultException;

    /**
     * address
     *
     * @param customerId
     * @return
     * @throws BusinessDefaultException
     */
    List<CustomerAddressEntity> findAddressById(Long customerId) throws BusinessDefaultException;
}
