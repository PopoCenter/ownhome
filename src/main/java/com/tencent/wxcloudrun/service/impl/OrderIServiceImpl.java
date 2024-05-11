package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.dto.OrderCreateDto;
import com.tencent.wxcloudrun.dto.OrderListDto;
import com.tencent.wxcloudrun.entity.*;
import com.tencent.wxcloudrun.enums.OrderOperateType;
import com.tencent.wxcloudrun.enums.OrderStatus;
import com.tencent.wxcloudrun.enums.YesNoStatus;
import com.tencent.wxcloudrun.service.OrderIService;
import com.tencent.wxcloudrun.util.CoreDateUtils;
import com.tencent.wxcloudrun.util.UniqueIdUtils;
import com.tencent.wxcloudrun.vo.OrderListItemVo;
import com.tencent.wxcloudrun.vo.OrderListVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class OrderIServiceImpl extends ServiceImpl<OrderMapper, OrderEntity> implements OrderIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDocumentMapper orderDocumentMapper;

    @Resource
    private OrderRecordMapper orderRecordMapper;

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerAddressMapper customerAddressMapper;


    @Override
    public List<OrderEntity> findByCustomerId(Long customerId) throws BusinessDefaultException {
        return null;
    }

    @Override
    @Transactional
    public void create(Long userId, OrderCreateDto createDto) throws BusinessDefaultException {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessDefaultException("用户不存在");
        }
        CustomerEntity customer = customerMapper.selectById(createDto.getCustomerId());
        if (customer == null) {
            throw new BusinessDefaultException("客户不存在");
        }

        CustomerAddressEntity address = customerAddressMapper.selectById(createDto.getCustomerAddressId());
        if (address == null) {
            throw new BusinessDefaultException("客户地址不存在");
        }

        Date installTime = CoreDateUtils.parseDateTime(createDto.getInstallTime());
        if (installTime == null) {
            throw new BusinessDefaultException("预约时间格式不正确");
        }

        List<String> fileIdList = createDto.getFileIdList();
        if (fileIdList.isEmpty()) {
            throw new BusinessDefaultException("单据信息不存在");
        }

        // order save
        Date now = new Date();
        OrderEntity order = new OrderEntity();
        Long orderId = UniqueIdUtils.Millis.uniqueId();
        order.setOrderId(orderId);
        order.setOwnerId(userId);
        order.setOwnerName(user.getName());
        order.setCustomerId(customer.getCustomerId());
        order.setCustomerName(customer.getName());
        order.setCustomerPhone(customer.getPhone());
        order.setAddress(address.getAddress());
        order.setLatitude(address.getLatitude());
        order.setLongitude(address.getLongitude());
        order.setStatus(OrderStatus.PENDING.getValue());
        order.setVisitTime(installTime);
        order.setDemo(createDto.getDemo());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderMapper.insert(order);

        // order document save
        for (String fileId : fileIdList) {
            OrderDocumentEntity document = new OrderDocumentEntity();
            document.setOrderId(orderId);
            document.setFileId(fileId);
            document.setOperateType(OrderOperateType.CREATE.getValue());
            document.setIsDelete(YesNoStatus.NO.getValue());
            document.setCreateTime(now);
            document.setUpdateTime(now);
            orderDocumentMapper.insert(document);
        }

        // order record save
        OrderRecordEntity record = new OrderRecordEntity();
        record.setOperateType(OrderOperateType.CREATE.getValue());
        String currentIds = StringUtils.join(fileIdList, ",");
        record.setCurrentDocumentIds(currentIds);
        record.setOrderId(orderId);
        record.setOperatorId(userId);
        record.setCreateTime(now);
        record.setUpdateTime(now);
        orderRecordMapper.insert(record);
        logger.info("订单创建成功，orderId={}, userId={}", orderId, userId);
    }

    @Override
    public Page<OrderEntity> list(Long userId, OrderListDto listDto) throws BusinessDefaultException {
        Integer queryStatus = listDto.getQueryStatus();
        List<Integer> checkList = Lists.newArrayList(-1, 10, 20, 30);
        if (!checkList.contains(queryStatus)) {
            throw new BusinessDefaultException("订单状态码错误");
        }

        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 客户名称模糊搜索
        String name = listDto.getName();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(OrderEntity::getCustomerName, name);
        }

        List<Integer> dbStatusList = Lists.newArrayList();
        switch (queryStatus) {
            case 10:
                dbStatusList.add(OrderStatus.PENDING.getValue());
                dbStatusList.add(OrderStatus.AFTER_SALES_PENDING.getValue());
                break;
            case 20:
                dbStatusList.add(OrderStatus.INSTALL.getValue());
                dbStatusList.add(OrderStatus.AFTER_SALES_INSTALL.getValue());
                break;
            case 30:
                dbStatusList.add(OrderStatus.CANCEL.getValue());
                break;
        }
        if (!dbStatusList.isEmpty()) {
            queryWrapper.in(OrderEntity::getStatus, dbStatusList);
        }
        Page<OrderEntity> page = Page.of(listDto.getPageNum(), listDto.getPageSize());
        return orderMapper.selectPage(page, queryWrapper);

    }
}
