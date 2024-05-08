package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.entity.OrderEntity;
import com.tencent.wxcloudrun.service.OrderIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class OrderIServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDocumentMapper orderDocumentMapper;

    @Resource
    private OrderRecordMapper orderRecordMapper;


    @Override
    public List<OrderEntity> findByCustomerId(Long customerId) throws BusinessDefaultException {
        return null;
    }
}
