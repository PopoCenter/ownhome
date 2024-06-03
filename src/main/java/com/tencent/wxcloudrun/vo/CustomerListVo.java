package com.tencent.wxcloudrun.vo;


import java.util.List;

public class CustomerListVo {

    private static final long serialVersionUID = 382951780872523489L;

    private List<CustomerListVoItemMap> customerList;


    public List<CustomerListVoItemMap> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<CustomerListVoItemMap> customerList) {
        this.customerList = customerList;
    }
}