package com.tencent.wxcloudrun.vo;


import java.util.List;

public class OrderDetailVo {

    private static final long serialVersionUID = 382951780872523489L;


    private Long orderId;

    private String creator;




    private String createTime;

    private String orderStatusName;



    private String customer;

    private String customerPhone;

    private String address;


    private Long customerId;

    private Long customerAddressId;



    private String inviteTime;

    private String afterSalesTime;





    /**
     * 经度
     */
    private String latitude;

    /**
     * 纬度
     */
    private String longitude;

    private Integer genderType;







    private String orderDemo;

    private List<String> fileIdList;



    private List<OrderRecordVo> recordList;


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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Integer getGenderType() {
        return genderType;
    }

    public void setGenderType(Integer genderType) {
        this.genderType = genderType;
    }

    public String getOrderDemo() {
        return orderDemo;
    }

    public void setOrderDemo(String orderDemo) {
        this.orderDemo = orderDemo;
    }

    public List<String> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<String> fileIdList) {
        this.fileIdList = fileIdList;
    }

    public List<OrderRecordVo> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<OrderRecordVo> recordList) {
        this.recordList = recordList;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }


    public Long getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(Long customerAddressId) {
        this.customerAddressId = customerAddressId;
    }
}