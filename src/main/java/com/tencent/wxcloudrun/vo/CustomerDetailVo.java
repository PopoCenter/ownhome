package com.tencent.wxcloudrun.vo;

import java.util.List;

/**
 * @author dongdongxie
 * @date 2024/5/8
 */
public class CustomerDetailVo {

    /**
     * 创建人
     */
    private String creator;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户手机号
     */
    private String phone;

    /**
     * 性别
     */
    private Integer genderType;

    /**
     * 年龄区间
     */
    private String ageRange;

    /**
     * 创建时间
     */
    private String createTime;

    private List<CustomerAddressVo> addressList;

    private List<CustomerOrderVo> orderList;


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<CustomerAddressVo> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<CustomerAddressVo> addressList) {
        this.addressList = addressList;
    }

    public List<CustomerOrderVo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<CustomerOrderVo> orderList) {
        this.orderList = orderList;
    }
}
