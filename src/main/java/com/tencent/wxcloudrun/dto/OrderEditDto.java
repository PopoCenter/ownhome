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
public class OrderEditDto {


    @NotNull(groups = {OrderEditDto.Add.class}, message = "客户id不能为空")
    private Long orderId;


    /**
     * 客户id
     */
    @NotNull(groups = {OrderEditDto.Add.class}, message = "客户id不能为空")
    private Long customerId;


    /**
     * 地址id
     */
    @NotNull(groups = {OrderCreateDto.Add.class}, message = "客户地址id不能为空")
    private Long customerAddressId;


    /**
     * 预约安装上门时间
     */
    @NotNull(groups = {OrderEditDto.Add.class}, message = "预约安装上门时间不能为空")
    @NotBlank(groups = {OrderEditDto.Add.class}, message = "预约安装上门时间不能为空")
    private String visitTime;


    /**
     * 订单备注
     */
    @NotNull(groups = {OrderEditDto.Add.class}, message = "名称不能为空")
    @NotBlank(groups = {OrderEditDto.Add.class}, message = "名称不能为空")
    private String demo;

    /**
     * 新增单据文件ids
     */
    private List<String> addFileIdList;

    public interface Add {
    }


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public List<String> getAddFileIdList() {
        return addFileIdList;
    }

    public void setAddFileIdList(List<String> addFileIdList) {
        this.addFileIdList = addFileIdList;
    }
}
