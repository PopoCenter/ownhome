package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dao.*;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.*;
import com.tencent.wxcloudrun.enums.OrderOperateType;
import com.tencent.wxcloudrun.enums.OrderStatus;
import com.tencent.wxcloudrun.enums.YesNoStatus;
import com.tencent.wxcloudrun.service.OrderIService;
import com.tencent.wxcloudrun.util.CoreDateUtils;
import com.tencent.wxcloudrun.util.UniqueIdUtils;
import com.tencent.wxcloudrun.vo.OrderDetailVo;
import com.tencent.wxcloudrun.vo.OrderRecordVo;
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
        LambdaQueryWrapper<OrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderEntity::getCustomerId, customerId);
        queryWrapper.orderByDesc(OrderEntity::getCreateTime);
        return orderMapper.selectList(queryWrapper);
    }


    private void orderDocumentInit(Long orderId, OrderOperateType operateType, List<String> fileIdList) {
        Date now = new Date();
        // order document save
        for (String fileId : fileIdList) {
            OrderDocumentEntity document = new OrderDocumentEntity();
            document.setOrderId(orderId);
            document.setFileId(fileId);
            document.setOperateType(operateType.getValue());
            document.setIsDelete(YesNoStatus.NO.getValue());
            document.setCreateTime(now);
            document.setUpdateTime(now);
            orderDocumentMapper.insert(document);
        }
    }


    private void orderRecordInit(Long orderId, OrderOperateType operateType, List<String> fileIdList, Long operatorId) {
        Date now = new Date();
        // order record save
        OrderRecordEntity record = new OrderRecordEntity();
        record.setOperateType(operateType.getValue());
        String currentIds = StringUtils.join(fileIdList, ",");
        record.setCurrentDocumentIds(currentIds);
        record.setOrderId(orderId);
        record.setOperatorId(operatorId);
        UserEntity userEntity = userMapper.selectById(operatorId);
        record.setOperator(userEntity.getName());
        record.setCreateTime(now);
        record.setUpdateTime(now);
        orderRecordMapper.insert(record);
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

        Date visitTime = CoreDateUtils.parseDate(createDto.getVisitTime());
        if (visitTime == null) {
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
        order.setAddressId(address.getId());
        order.setCustomerName(customer.getName());
        order.setAddress(address.getAddress());
        order.setCustomerPhone(customer.getPhone());
        order.setLatitude(address.getLatitude());
        order.setLongitude(address.getLongitude());
        order.setStatus(OrderStatus.PENDING.getValue());
        order.setVisitTime(visitTime);
        order.setDemo(createDto.getDemo());
        order.setCreateTime(now);
        order.setUpdateTime(now);
        orderMapper.insert(order);

        // order document save
        orderDocumentInit(orderId, OrderOperateType.CREATE, fileIdList);

        // order record save
        orderRecordInit(orderId, OrderOperateType.CREATE, fileIdList, userId);
        logger.info("订单创建成功，orderId={}, userId={}", orderId, userId);
    }

    @Override
    @Transactional
    public void edit(Long userId, OrderEditDto editDto) throws BusinessDefaultException {
        OrderEntity orderEntity = orderMapper.selectById(editDto.getOrderId());
        if (orderEntity == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.PENDING.getValue() != orderEntity.getStatus()) {
            throw new BusinessDefaultException("非待安装订单状态，操作失败");
        }

        Date visitTime = CoreDateUtils.parseDateTime(editDto.getVisitTime());
        if (visitTime == null) {
            throw new BusinessDefaultException("预约时间格式不正确");
        }

        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(orderEntity.getOrderId());


        if (editDto.getCustomerId().equals(orderEntity.getCustomerId())) {
            orderUpdate.setCustomerId(editDto.getCustomerId());

            CustomerEntity customer = customerMapper.selectById(editDto.getCustomerId());
            if (customer == null) {
                throw new BusinessDefaultException("客户不存在");
            }

            orderUpdate.setCustomerName(customer.getName());
            orderUpdate.setCustomerPhone(customer.getPhone());
        }

        if (editDto.getCustomerAddressId().equals(orderEntity.getAddressId())) {
            orderUpdate.setAddressId(editDto.getCustomerAddressId());
            CustomerAddressEntity address = customerAddressMapper.selectById(editDto.getCustomerAddressId());
            if (address == null) {
                throw new BusinessDefaultException("客户地址不存在");
            }

            orderUpdate.setAddress(address.getAddress());
            orderUpdate.setLatitude(address.getLatitude());
            orderUpdate.setLongitude(address.getLongitude());
        }


        orderUpdate.setUpdateTime(now);
        orderUpdate.setVisitTime(visitTime);
        orderUpdate.setDemo(editDto.getDemo());

        orderMapper.updateById(orderUpdate);

        List<String> fileIdList = editDto.getAddFileIdList();
        // old file
        List<String> oldFileList = findOrderFileIds(editDto.getOrderId());
        // order document save
        orderDocumentInit(orderEntity.getOrderId(), OrderOperateType.EDIT, fileIdList);

        fileIdList.addAll(oldFileList);

        // order record save
        orderRecordInit(orderEntity.getOrderId(), OrderOperateType.EDIT, fileIdList, userId);
        logger.info("编辑订单成功，orderId={}", editDto.getOrderId());
    }


    private List<String> findOrderFileIds(Long orderId) {
        List<String> fileIdList = Lists.newArrayList();
        LambdaQueryWrapper<OrderDocumentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDocumentEntity::getOrderId, orderId);
        queryWrapper.eq(OrderDocumentEntity::getIsDelete, YesNoStatus.NO.getValue());
        List<OrderDocumentEntity> documentEntityList = orderDocumentMapper.selectList(queryWrapper);
        if (!documentEntityList.isEmpty()) {
            documentEntityList.forEach(it -> fileIdList.add(it.getFileId()));
        }
        return fileIdList;
    }


    @Override
    public Page<OrderEntity> list(Long userId, OrderListDto listDto) throws BusinessDefaultException {
        Integer queryStatus = listDto.getQueryStatus();
        List<Integer> checkList = Lists.newArrayList(-1, 10, 20, 30);
        if (!checkList.contains(queryStatus)) {
            throw new BusinessDefaultException("订单状态码错误");
        }

        Page<OrderEntity> page = Page.of(listDto.getPageNum(), listDto.getPageSize());

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

        return orderMapper.selectPage(page, queryWrapper);

    }

    @Override
    @Transactional
    public void install(Long userId, OrderInstallDto installDto) throws BusinessDefaultException {
        OrderEntity orderEntity = orderMapper.selectById(installDto.getOrderId());
        if (orderEntity == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.PENDING.getValue() != orderEntity.getStatus()) {
            throw new BusinessDefaultException("非待安装订单状态，操作失败");
        }

        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(orderEntity.getOrderId());
        orderUpdate.setStatus(OrderStatus.INSTALL.getValue());
        orderUpdate.setUpdateTime(now);
        orderUpdate.setInstallTime(now);
        orderMapper.updateById(orderUpdate);

        List<String> fileIdList = installDto.getFileIdList();
        // old file
        List<String> oldFileList = findOrderFileIds(installDto.getOrderId());

        // order document save
        orderDocumentInit(orderEntity.getOrderId(), OrderOperateType.INSTALL, fileIdList);


        fileIdList.addAll(oldFileList);
        // order record save
        orderRecordInit(orderEntity.getOrderId(), OrderOperateType.INSTALL, fileIdList, userId);
        logger.info("订单安装完成，orderId={}, userId={}", orderEntity.getOrderId(), userId);
    }

    @Override
    @Transactional
    public void cancel(Long userId, OrderCancelDto cancelDto) throws BusinessDefaultException {
        OrderEntity order = orderMapper.selectById(cancelDto.getOrderId());
        if (order == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.PENDING.getValue() != order.getStatus()) {
            throw new BusinessDefaultException("非待安装订单状态，操作失败");
        }


        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(order.getOrderId());
        orderUpdate.setCancelTime(now);
        orderUpdate.setCancelReason(cancelDto.getReason());
        orderUpdate.setUpdateTime(now);
        orderUpdate.setStatus(OrderStatus.CANCEL.getValue());
        orderMapper.updateById(orderUpdate);
        logger.info("取消订单成功，orderId={}", cancelDto.getOrderId());
    }

    @Override
    @Transactional
    public void afterSales(Long userId, OrderAfterSalesDto salesDto) throws BusinessDefaultException {
        OrderEntity order = orderMapper.selectById(salesDto.getOrderId());
        if (order == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.INSTALL.getValue() != order.getStatus()) {
            throw new BusinessDefaultException("非已完成订单状态，操作失败");
        }


        Date visitTime = CoreDateUtils.parseDateTime(salesDto.getSalesVisitTime());
        if (visitTime == null) {
            throw new BusinessDefaultException("预约时间格式不正确");
        }


        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(order.getOrderId());
        orderUpdate.setStatus(OrderStatus.AFTER_SALES_PENDING.getValue());
        orderUpdate.setUpdateTime(now);
        orderUpdate.setAfterSalesTime(visitTime);
        orderUpdate.setAfterSalesReason(salesDto.getReason());
        orderMapper.updateById(orderUpdate);

        // old file
        List<String> oldFileList = findOrderFileIds(order.getOrderId());

        List<String> fileIdList = salesDto.getAddFileIdList();

        // order document save
        orderDocumentInit(order.getOrderId(), OrderOperateType.AFTER_SALES_CREATE, fileIdList);

        fileIdList.addAll(oldFileList);
        // order record save
        orderRecordInit(order.getOrderId(), OrderOperateType.AFTER_SALES_CREATE, fileIdList, userId);
        logger.info("创建售后完成，orderId={}, userId={}", order.getOrderId(), userId);

    }

    @Override
    @Transactional
    public void afterSalesEdit(Long userId, OrderAfterSalesEditDto editDto) throws BusinessDefaultException {
        OrderEntity order = orderMapper.selectById(editDto.getOrderId());
        if (order == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.AFTER_SALES_PENDING.getValue() != order.getStatus()) {
            throw new BusinessDefaultException("非售后中订单状态，操作失败");
        }


        Date visitTime = CoreDateUtils.parseDateTime(editDto.getSalesVisitTime());
        if (visitTime == null) {
            throw new BusinessDefaultException("预约时间格式不正确");
        }


        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(order.getOrderId());
        orderUpdate.setUpdateTime(now);
        orderUpdate.setAfterSalesTime(visitTime);
        orderUpdate.setAfterSalesReason(editDto.getReason());
        orderMapper.updateById(orderUpdate);

        // old file
        List<String> oldFileList = findOrderFileIds(order.getOrderId());

        List<String> fileIdList = editDto.getAddFileIdList();

        // order document save
        orderDocumentInit(order.getOrderId(), OrderOperateType.AFTER_SALES_EDIT, fileIdList);

        fileIdList.addAll(oldFileList);
        // order record save
        orderRecordInit(order.getOrderId(), OrderOperateType.AFTER_SALES_EDIT, fileIdList, userId);
        logger.info("编辑售后订单完成，orderId={}, userId={}", order.getOrderId(), userId);
    }

    @Override
    @Transactional
    public void afterSalesFinish(Long userId, OrderAfterSalesFinishDto finishDto) throws BusinessDefaultException {
        OrderEntity order = orderMapper.selectById(finishDto.getOrderId());
        if (order == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        if (OrderStatus.AFTER_SALES_PENDING.getValue() != order.getStatus()) {
            throw new BusinessDefaultException("非售后中订单状态，操作失败");
        }


        Date now = new Date();
        OrderEntity orderUpdate = new OrderEntity();
        orderUpdate.setOrderId(order.getOrderId());
        orderUpdate.setUpdateTime(now);
        orderUpdate.setAfterSalesReason(finishDto.getReason());
        orderMapper.updateById(orderUpdate);

        // old file
        List<String> oldFileList = findOrderFileIds(order.getOrderId());

        List<String> fileIdList = finishDto.getAddFileIdList();

        // order document save
        orderDocumentInit(order.getOrderId(), OrderOperateType.AFTER_SALES_FINISH, fileIdList);

        fileIdList.addAll(oldFileList);
        // order record save
        orderRecordInit(order.getOrderId(), OrderOperateType.AFTER_SALES_FINISH, fileIdList, userId);
        logger.info("售后订单处理完成，orderId={}, userId={}", order.getOrderId(), userId);
    }

    @Override
    public OrderDetailVo detail(Long orderId) throws BusinessDefaultException {
        OrderEntity order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessDefaultException("订单不存在");
        }

        OrderDetailVo detailVo = new OrderDetailVo();

        detailVo.setOrderId(order.getOrderId());
        detailVo.setCreator(order.getOwnerName());
        OrderStatus orderStatus = OrderStatus.get(order.getStatus());
        if (orderStatus == null) {
            return null;
        }
        switch (orderStatus) {
            case PENDING:
                detailVo.setOrderStatusName("待安装");
                break;
            case AFTER_SALES_PENDING:
                detailVo.setOrderStatusName("售后中");
                break;
            case CANCEL:
                detailVo.setOrderStatusName("已取消");
                break;
            case AFTER_SALES_INSTALL:
            case INSTALL:
                detailVo.setOrderStatusName("处理完成");
                break;
        }
        detailVo.setCreateTime(CoreDateUtils.formatDateTime(order.getCreateTime()));
        detailVo.setCustomer(order.getCustomerName());
        detailVo.setCustomerPhone(order.getCustomerPhone());
        detailVo.setCustomerId(order.getCustomerId());
        detailVo.setAddress(order.getAddress());
        detailVo.setLatitude(order.getLatitude());
        detailVo.setLongitude(order.getLongitude());
        detailVo.setOrderDemo(order.getDemo());
        detailVo.setInviteTime(CoreDateUtils.formatDate(order.getVisitTime()));
        detailVo.setAfterSalesTime(order.getAfterSalesTime() == null ? StringUtils.EMPTY : CoreDateUtils.formatDateTime(order.getAfterSalesTime()));
        detailVo.setCustomerAddressId(order.getAddressId());

        CustomerEntity customerEntity = customerMapper.selectById(order.getCustomerId());
        detailVo.setGenderType(customerEntity.getGenderType());


        List<String> fileIdList = Lists.newArrayList();
        LambdaQueryWrapper<OrderDocumentEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDocumentEntity::getOrderId, orderId);
        queryWrapper.eq(OrderDocumentEntity::getIsDelete, YesNoStatus.NO.getValue());
        List<OrderDocumentEntity> documentEntityList = orderDocumentMapper.selectList(queryWrapper);
        if (!documentEntityList.isEmpty()) {
            documentEntityList.forEach(it -> fileIdList.add(it.getFileId()));
        }
        detailVo.setFileIdList(fileIdList);

        List<OrderRecordVo> recordList = Lists.newArrayList();
        LambdaQueryWrapper<OrderRecordEntity> queryRecordWrapper = new LambdaQueryWrapper<>();
        queryRecordWrapper.eq(OrderRecordEntity::getOrderId, orderId);
        List<OrderRecordEntity> recordEntityList = orderRecordMapper.selectList(queryRecordWrapper);
        if (!recordEntityList.isEmpty()) {
            for (OrderRecordEntity record : recordEntityList) {
                OrderRecordVo recordVo = new OrderRecordVo();
                recordVo.setOperator(record.getOperator());
                recordVo.setOperateTime(CoreDateUtils.formatDateTime(record.getCreateTime()));
                recordVo.setOperateType(OrderOperateType.get(record.getOperateType()).getName());
                String[] currentIdList = StringUtils.split(record.getCurrentDocumentIds(), ",");
                recordVo.setCurrentFileIdList(Lists.newArrayList(currentIdList));
                recordList.add(recordVo);
            }
        }
        detailVo.setRecordList(recordList);
        return detailVo;
    }
}
