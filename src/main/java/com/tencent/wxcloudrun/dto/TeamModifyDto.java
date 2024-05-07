package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class TeamModifyDto {

    @NotNull(groups = {TeamModifyDto.Add.class}, message = "name不能为空")
    @NotBlank(groups = {TeamModifyDto.Add.class}, message = "name不能为空")
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
