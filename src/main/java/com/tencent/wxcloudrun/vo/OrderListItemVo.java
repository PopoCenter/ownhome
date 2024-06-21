package com.tencent.wxcloudrun.vo;

/**
 * @author dongdongxie
 * @date 2024/5/10
 */
public class OrderListItemVo {


    private Long  orderId;

    private String creator;

    private String createTime;

    private String orderStatusName;

    private String customer;

    /**
     * 性别
     */
    private Integer genderType;

    private String customerPhone;

    private String address;

    private String inviteTime;

    private String afterSalesTime;

    private String demo;



    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInviteTime() {
        return inviteTime;
    }

    public void setInviteTime(String inviteTime) {
        this.inviteTime = inviteTime;
    }

    public String getAfterSalesTime() {
        return afterSalesTime;
    }

    public void setAfterSalesTime(String afterSalesTime) {
        this.afterSalesTime = afterSalesTime;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }
}
