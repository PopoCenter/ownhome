package com.tencent.wxcloudrun.vo;



public class CustomerListVoItem {

    private static final long serialVersionUID = 382951780872523489L;

    private Long customerId;

    private String phone;

    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}