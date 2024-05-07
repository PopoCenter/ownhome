package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class SendMessageDto {

    /**
     * 手机号
     */
    @NotNull(groups = {SendMessageDto.Send.class}, message = "用户名称不能为空")
    @NotBlank(groups = {SendMessageDto.Send.class}, message = "用户名称不能为空")
    private String phone;


    public interface Send {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
