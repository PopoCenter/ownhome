package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class OrderCreateDto {


    /**
     * 客户id
     */
    @NotNull(groups = {OrderCreateDto.Add.class}, message = "客户id不能为空")
    private Long customerId;


    /**
     * 地址id
     */
    @NotNull(groups = {OrderCreateDto.Add.class}, message = "客户地址id不能为空")
    private Long customerAddressId;


    /**
     * 预约安装上门时间
     */
    @NotNull(groups = {OrderCreateDto.Add.class}, message = "预约安装上门时间不能为空")
    @NotBlank(groups = {OrderCreateDto.Add.class}, message = "预约安装上门时间不能为空")
    private String visitTime;


    /**
     * 订单备注
     */
    private String demo;

    /**
     * 单据文件ids
     */
    private List<String> fileIdList;

    public interface Add {
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

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
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
