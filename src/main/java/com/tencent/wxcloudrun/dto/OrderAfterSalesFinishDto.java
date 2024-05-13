package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderAfterSalesFinishDto {

    @NotNull(groups = {OrderAfterSalesFinishDto.Verify.class}, message = "orderId不能为空")
    private Long orderId;

    public interface Verify {
    }


    private String reason;

    private List<String> addFileIdList;

    public List<String> getAddFileIdList() {
        return addFileIdList;
    }

    public void setAddFileIdList(List<String> addFileIdList) {
        this.addFileIdList = addFileIdList;
    }

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
