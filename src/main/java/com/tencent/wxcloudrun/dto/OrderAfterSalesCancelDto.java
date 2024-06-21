package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderAfterSalesCancelDto {

    @NotNull(groups = {OrderAfterSalesCancelDto.Verify.class}, message = "orderId不能为空")
    private Long orderId;

    public interface Verify {
    }


    private String reason;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
