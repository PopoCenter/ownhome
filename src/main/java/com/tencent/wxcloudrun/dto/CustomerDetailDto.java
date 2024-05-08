package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class CustomerDetailDto {


    @NotNull(groups = {CustomerDetailDto.Add.class}, message = "customerId不能为空")
    @NotBlank(groups = {CustomerDetailDto.Add.class}, message = "customerId不能为空")
    private String customerId;

    public interface Add {
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
