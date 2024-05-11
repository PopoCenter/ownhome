package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;


public class OrderDetailDto {


    @NotNull(groups = {OrderDetailDto.Verify.class}, message = "orderId不能为空")
    private Long orderId;


    public interface Verify {
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
