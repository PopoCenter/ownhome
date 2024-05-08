package com.tencent.wxcloudrun.vo;


import java.util.List;

public class CustomerListVo {

    private static final long serialVersionUID = 382951780872523489L;

    private List<CustomerListVoItem> customerList;

    public List<CustomerListVoItem> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<CustomerListVoItem> customerList) {
        this.customerList = customerList;
    }
}