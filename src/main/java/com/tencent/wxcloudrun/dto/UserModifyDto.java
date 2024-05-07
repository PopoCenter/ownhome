package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class UserModifyDto {

    /**
     * 用户名称
     */
    @NotNull(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    @NotBlank(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    private String name;

    public interface Add {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
