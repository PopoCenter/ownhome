package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderInstallDto {

    @NotNull(groups = {OrderInstallDto.Verify.class}, message = "orderId不能为空")
    private Long orderId;

    public interface Verify {
    }


    private String demo;

    private List<String> fileIdList;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public List<String> getFileIdList() {
        return fileIdList;
    }

    public void setFileIdList(List<String> fileIdList) {
        this.fileIdList = fileIdList;
    }
}
