package com.tencent.wxcloudrun.vo;


import java.util.List;

public class OrderListVo {

    private static final long serialVersionUID = 382951780872523489L;

    private List<OrderListItemVo> orderList;


    public List<OrderListItemVo> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderListItemVo> orderList) {
        this.orderList = orderList;
    }
}