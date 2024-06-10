package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.config.ResponseMessage;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.OrderEntity;
import com.tencent.wxcloudrun.entity.UserEntity;
import com.tencent.wxcloudrun.enums.OrderStatus;
import com.tencent.wxcloudrun.enums.ResponseEnum;
import com.tencent.wxcloudrun.service.OrderIService;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.service.UserIService;
import com.tencent.wxcloudrun.util.CoreDateUtils;
import com.tencent.wxcloudrun.vo.OrderDetailVo;
import com.tencent.wxcloudrun.vo.OrderListItemVo;
import com.tencent.wxcloudrun.vo.OrderListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * counter控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

    @Autowired
    private UserIService userIService;

    @Autowired
    private OrderIService orderIService;

    /**
     * 创建订单
     */
    @PostMapping(value = "/create")
    ResponseMessage<?> create(@RequestBody @Validated(OrderCreateDto.Add.class) OrderCreateDto createDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            orderIService.create(userEntity.getUserId(), createDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 编辑订单
     */
    @PostMapping(value = "/edit")
    ResponseMessage<?> edit(@RequestBody @Validated(OrderEditDto.Add.class) OrderEditDto editDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            orderIService.edit(userEntity.getUserId(), editDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 订单详情
     */
    @PostMapping(value = "/detail")
    ResponseMessage<OrderDetailVo> detail(@RequestBody @Validated(OrderDetailDto.Verify.class) OrderDetailDto detailDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }
            OrderDetailVo detailVo = orderIService.detail(detailDto.getOrderId());
            return ResponseMessage.success(detailVo);
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 订单列表
     */
    @PostMapping(value = "/list")
    ResponseMessage<OrderListVo> list(@RequestBody @Validated(OrderListDto.List.class) OrderListDto listDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            OrderListVo listVo = new OrderListVo();
            List<OrderListItemVo> orderList = Lists.newArrayList();

            Page<OrderEntity> orderPage = orderIService.list(userEntity.getUserId(), listDto);
            for (OrderEntity order : orderPage.getRecords()) {
                OrderListItemVo itemVo = new OrderListItemVo();
                itemVo.setOrderId(order.getOrderId());
                itemVo.setCreator(order.getOwnerName());
                OrderStatus orderStatus = OrderStatus.get(order.getStatus());
                if (orderStatus == null) {
                    continue;
                }
                switch (orderStatus) {
                    case PENDING:
                        itemVo.setOrderStatusName("待安装");
                        break;
                    case AFTER_SALES_PENDING:
                        itemVo.setOrderStatusName("售后中");
                        break;
                    case CANCEL:
                        itemVo.setOrderStatusName("已取消");
                        break;
                    case AFTER_SALES_INSTALL:
                    case INSTALL:
                        itemVo.setOrderStatusName("处理完成");
                        break;
                }
                itemVo.setCreateTime(CoreDateUtils.formatDateTime(order.getCreateTime()));
                itemVo.setCustomer(order.getCustomerName());
                itemVo.setCustomerPhone(order.getCustomerPhone());
                itemVo.setAddress(order.getAddress());
                itemVo.setInviteTime(CoreDateUtils.formatDate(order.getVisitTime()));
                itemVo.setAfterSalesTime(order.getAfterSalesTime() == null ? StringUtils.EMPTY : CoreDateUtils.formatDateTime(order.getAfterSalesTime()));
                orderList.add(itemVo);
            }

            listVo.setOrderList(orderList);
            return ResponseMessage.success(listVo, orderPage.getTotal(), orderPage.getCurrent(), orderPage.getSize());
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 安装订单
     */
    @PostMapping(value = "/install")
    ResponseMessage<?> install(@RequestBody @Validated(OrderInstallDto.Verify.class) OrderInstallDto installDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            orderIService.install(userEntity.getUserId(), installDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }

    /**
     * 取消订单
     */
    @PostMapping(value = "/cancel")
    ResponseMessage<?> cancel(@RequestBody @Validated(OrderCancelDto.Verify.class) OrderCancelDto cancelDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            orderIService.cancel(userEntity.getUserId(), cancelDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 售后
     */
    @PostMapping(value = "/afterSales")
    ResponseMessage<?> afterSales(@RequestBody @Validated(OrderAfterSalesDto.Verify.class) OrderAfterSalesDto salesDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            orderIService.afterSales(userEntity.getUserId(), salesDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 售后编辑
     */
    @PostMapping(value = "/afterSales/edit")
    ResponseMessage<?> salesEdit(@RequestBody @Validated(OrderAfterSalesEditDto.Verify.class) OrderAfterSalesEditDto editDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            orderIService.afterSalesEdit(userEntity.getUserId(), editDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }

    /**
     * 售后编辑
     */
    @PostMapping(value = "/afterSales/finish")
    ResponseMessage<?> finish(@RequestBody @Validated(OrderAfterSalesFinishDto.Verify.class) OrderAfterSalesFinishDto finishDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            orderIService.afterSalesFinish(userEntity.getUserId(), finishDto);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


}