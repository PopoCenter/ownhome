package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dao.CustomerAddressMapper;
import com.tencent.wxcloudrun.dao.CustomerMapper;
import com.tencent.wxcloudrun.dto.CustomerCreateAddressDto;
import com.tencent.wxcloudrun.dto.CustomerCreateDto;
import com.tencent.wxcloudrun.dto.CustomerListDto;
import com.tencent.wxcloudrun.entity.CustomerAddressEntity;
import com.tencent.wxcloudrun.entity.CustomerEntity;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.enums.YesNoStatus;
import com.tencent.wxcloudrun.service.CustomerIService;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.util.CoreStringUtils;
import com.tencent.wxcloudrun.util.UniqueIdUtils;
import com.tencent.wxcloudrun.vo.CustomerAddressVo;
import com.tencent.wxcloudrun.vo.CustomerListVoItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class CustomerIServiceImpl extends ServiceImpl<CustomerMapper, CustomerEntity> implements CustomerIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private TeamIService teamIService;

    @Override
    @Transactional
    public void create(Long userId, CustomerCreateDto customerCreateDto) throws BusinessDefaultException {
        if (!CoreStringUtils.checkChinaMobilePhone(customerCreateDto.getPhone())) {
            throw new BusinessDefaultException("手机号格式不正确");
        }

        List<CustomerCreateAddressDto> addressList = customerCreateDto.getAddressList();
        if (addressList.isEmpty()) {
            throw new BusinessDefaultException("客户地址不能为空");
        }

        TeamEntity teamEntity = teamIService.findByUserId(userId);
        if (teamEntity == null) {
            throw new BusinessDefaultException("团队信息不能为空，请加入团队后再创建客户");
        }


        CustomerEntity customerEntity = new CustomerEntity();
        Long customerId = UniqueIdUtils.Millis.uniqueId();
        Date now = new Date();
        customerEntity.setCustomerId(customerId);
        customerEntity.setOwnerId(userId);
        customerEntity.setTeamId(teamEntity.getTeamId());
        customerEntity.setPhone(customerCreateDto.getPhone());
        customerEntity.setName(customerCreateDto.getName());
        customerEntity.setAgeRange(customerCreateDto.getAgeRange());
        customerEntity.setGenderType(customerCreateDto.getGenderType());
        customerEntity.setCreateTime(now);
        customerEntity.setUpdateTime(now);
        customerMapper.insert(customerEntity);

        addressList.forEach(it -> {
            CustomerAddressEntity addressEntity = new CustomerAddressEntity();
            addressEntity.setAddress(it.getAddress());
            addressEntity.setCustomerId(customerId);
            addressEntity.setLatitude(it.getLatitude());
            addressEntity.setLongitude(it.getLongitude());
            addressEntity.setCreateTime(now);
            addressEntity.setUpdateTime(now);
            customerAddressMapper.insert(addressEntity);
        });

        logger.info("创建客户成功，userId={}, customerId={}", userId, customerId);
    }

    @Override
    public List<CustomerListVoItem> findByTeamId(Long teamId, CustomerListDto queryDto) throws BusinessDefaultException {
        LambdaQueryWrapper<CustomerEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerEntity::getTeamId, teamId);

        if (queryDto != null) {

            if (queryDto.getPhone() != null && !queryDto.getPhone().isEmpty()) {
                queryWrapper.like(CustomerEntity::getPhone, queryDto.getPhone());
            }

            if (queryDto.getName() != null && !queryDto.getName().isEmpty()) {
                queryWrapper.like(CustomerEntity::getName, queryDto.getName());
            }

            if (queryDto.getAddress() != null && !queryDto.getAddress().isEmpty()) {
                LambdaQueryWrapper<CustomerAddressEntity> queryAddressWrapper = new LambdaQueryWrapper<>();
                queryAddressWrapper.like(CustomerAddressEntity::getAddress, queryDto.getAddress());

                List<CustomerAddressEntity> addressList = customerAddressMapper.selectList(queryAddressWrapper);
                if (!addressList.isEmpty()) {
                    List<Long> customerIds = addressList.stream().map(CustomerAddressEntity::getId).collect(Collectors.toList());
                    queryWrapper.in(CustomerEntity::getCustomerId, customerIds);
                }
            }
        }

        List<CustomerListVoItem> customerList = Lists.newArrayList();
        List<CustomerEntity> dbList = customerMapper.selectList(queryWrapper);
        if (dbList.isEmpty()) {
            return customerList;
        }


        for (CustomerEntity customer : dbList) {
            CustomerListVoItem item = new CustomerListVoItem();
            item.setCustomerId(customer.getCustomerId());
            item.setName(customer.getName());
            item.setPhone(customer.getPhone());
            customerList.add(item);
        }

        return customerList;
    }

    @Override
    public CustomerEntity findById(Long customerId) throws BusinessDefaultException {
        return customerMapper.selectById(customerId);
    }

    @Override
    public List<CustomerAddressEntity> findAddressById(Long customerId) throws BusinessDefaultException {
        LambdaQueryWrapper<CustomerAddressEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CustomerAddressEntity::getCustomerId, customerId);
        return customerAddressMapper.selectList(queryWrapper);
    }
}
