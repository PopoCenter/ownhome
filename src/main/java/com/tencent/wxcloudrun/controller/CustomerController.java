package com.tencent.wxcloudrun.controller;

import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.config.ResponseMessage;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.*;
import com.tencent.wxcloudrun.enums.OrderStatus;
import com.tencent.wxcloudrun.enums.ResponseEnum;
import com.tencent.wxcloudrun.service.*;
import com.tencent.wxcloudrun.util.ChangeChinesePinyinUtil;
import com.tencent.wxcloudrun.util.CoreDateUtils;
import com.tencent.wxcloudrun.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * counter控制器
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController extends BaseController {

    @Autowired
    private UserIService userIService;

    @Autowired
    private CustomerIService customerIService;

    @Autowired
    private CustomerAddressIService customerAddressIService;

    @Autowired
    private TeamIService teamIService;

    @Autowired
    private OrderIService orderIService;

    /**
     * 创建客户
     */
    @PostMapping(value = "/create")
    ResponseMessage<?> create(@RequestBody @Validated(CustomerCreateDto.Add.class) CustomerCreateDto createDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            customerIService.create(userEntity.getUserId(), createDto);
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
     * 客户列表
     */
    @PostMapping(value = "/list")
    ResponseMessage<CustomerListVo> list(@RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }


            List<CustomerListVoItemMap> mapList = Lists.newArrayList();

            CustomerListVo listVo = new CustomerListVo();
            List<CustomerListVoItem> customerList = Lists.newArrayList();
            TeamEntity teamEntity = teamIService.findByUserId(userEntity.getUserId());
            if (teamEntity != null) {
                customerList = customerIService.findByTeamId(teamEntity.getTeamId());
            }

            if (customerList.isEmpty()) {
                return ResponseMessage.success(listVo);
            }


            Map<String, List<CustomerListVoItem>> map = ChangeChinesePinyinUtil.getUserCodeGroup(customerList);

            List<String> indexList = Lists.newArrayList("A", "B","C","D","E","F","G","H","I","G","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");

            for (String index : indexList) {
                CustomerListVoItemMap itemMap = new CustomerListVoItemMap();
                itemMap.setIndex(index);
                List<CustomerListVoItem> voItemList = map.get(index);
                if (voItemList.isEmpty()) {
                    continue;
                }
                itemMap.setChildren(voItemList);
                mapList.add(itemMap);
            }

            listVo.setCustomerList(mapList);

            return ResponseMessage.success(listVo);
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }

    /**
     * 客户详情
     */
    @PostMapping(value = "/detail")
    ResponseMessage<CustomerDetailVo> detail(@RequestBody @Validated(CustomerDetailDto.Add.class) CustomerDetailDto detailDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            CustomerDetailVo detailVo = new CustomerDetailVo();
            Long customerId = Long.parseLong(detailDto.getCustomerId());
            CustomerEntity customerEntity = customerIService.findById(customerId);
            if (customerEntity == null) {
                throw new BusinessDefaultException("客户不存在");
            }

            detailVo.setName(customerEntity.getName());
            detailVo.setPhone(customerEntity.getPhone());
            detailVo.setAgeRange(customerEntity.getAgeRange());
            detailVo.setGenderType(customerEntity.getGenderType());
            UserEntity dbEntity = userIService.getById(customerEntity.getOwnerId());
            detailVo.setCreator(dbEntity.getName());
            detailVo.setCreateTime(CoreDateUtils.formatDateTime(customerEntity.getCreateTime()));


            List<CustomerAddressVo> addressVoList = Lists.newArrayList();
            List<CustomerAddressEntity> addressList = customerIService.findAddressById(customerId);
            if (!addressList.isEmpty()) {
                addressList.forEach(address -> {
                    CustomerAddressVo addressVo = new CustomerAddressVo();
                    addressVo.setAddress(address.getAddress());
                    addressVo.setId(address.getId());
                    addressVo.setLatitude(address.getLatitude());
                    addressVo.setLongitude(address.getLongitude());
                    addressVoList.add(addressVo);
                });

            }

            detailVo.setAddressList(addressVoList);

            List<CustomerOrderVo> orderVoList = Lists.newArrayList();

            List<OrderEntity> orderList = orderIService.findByCustomerId(customerId);
            if (!orderList.isEmpty()) {
                for (OrderEntity order : orderList) {
                    CustomerOrderVo orderVo = new CustomerOrderVo();

                    orderVo.setOrderId(order.getOrderId());
                    orderVo.setCreator(order.getOwnerName());
                    OrderStatus orderStatus = OrderStatus.get(order.getStatus());
                    if (orderStatus == null) {
                        return null;
                    }
                    switch (orderStatus) {
                        case PENDING:
                            orderVo.setOrderStatusName("待安装");
                            break;
                        case AFTER_SALES_PENDING:
                            orderVo.setOrderStatusName("售后中");
                            break;
                        case CANCEL:
                            orderVo.setOrderStatusName("已取消");
                            break;
                        case AFTER_SALES_INSTALL:
                        case INSTALL:
                            orderVo.setOrderStatusName("处理完成");
                            break;
                    }
                    orderVo.setCreateTime(CoreDateUtils.formatDateTime(order.getCreateTime()));

                    orderVoList.add(orderVo);
                }
            }

            detailVo.setOrderList(orderVoList);

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
     * 删除客户地址
     */
    @PostMapping(value = "/address/delete")
    ResponseMessage<?> delete(@RequestBody @Validated(CustomerAddressDeleteDto.Modify.class) CustomerAddressDeleteDto modifyDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            CustomerAddressEntity dbEntity = customerAddressIService.getById(modifyDto.getId());
            if (dbEntity == null) {
                return ResponseMessage.fail(ResponseEnum.FAILURE, "地址不存在");
            }

            customerAddressIService.removeById(modifyDto.getId());
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
     * 修改客户地址
     */
    @PostMapping(value = "/address/modify")
    ResponseMessage<?> modify(@RequestBody @Validated(CustomerAddressModifyDto.Modify.class) CustomerAddressModifyDto modifyDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            CustomerAddressEntity dbEntity = customerAddressIService.getById(modifyDto.getId());
            if (dbEntity == null) {
                return ResponseMessage.fail(ResponseEnum.FAILURE, "地址不存在");
            }

            CustomerAddressEntity addressEntity = new CustomerAddressEntity();
            addressEntity.setId(modifyDto.getId());
            addressEntity.setLongitude(modifyDto.getLongitude());
            addressEntity.setLatitude(modifyDto.getLatitude());
            addressEntity.setUpdateTime(new Date());
            customerAddressIService.updateById(addressEntity);
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
     * 新增客户地址
     */
    @PostMapping(value = "/address/add")
    ResponseMessage<?> addressAdd(@RequestBody @Validated(CustomerAddressAddDto.Add.class) CustomerAddressAddDto addDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = getOpenId(headers);
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                return ResponseMessage.fail(ResponseEnum.USER_NOT_EXIST, "用户不存在");
            }

            CustomerEntity dbEntity = customerIService.getById(addDto.getCustomerId());
            if (dbEntity == null) {
                return ResponseMessage.fail(ResponseEnum.FAILURE, "客户不存在");
            }

            CustomerAddressEntity addressEntity = new CustomerAddressEntity();
            addressEntity.setCustomerId(addDto.getCustomerId());
            addressEntity.setAddress(addDto.getAddress());
            addressEntity.setLongitude(addDto.getLongitude());
            addressEntity.setLatitude(addDto.getLatitude());
            Date now = new Date();
            addressEntity.setCreateTime(now);
            addressEntity.setUpdateTime(now);
            customerAddressIService.save(addressEntity);
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