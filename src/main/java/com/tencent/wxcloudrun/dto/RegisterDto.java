package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class RegisterDto {

    /**
     * 手机号
     */
    @NotNull(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    @NotBlank(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    private String phone;

    /**
     * 用户名称
     */
    @NotNull(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    @NotBlank(groups = {RegisterDto.Add.class}, message = "用户名称不能为空")
    private String name;


    /**
     * 团队id
     */
    private String teamId;

    /**
     * 邀请人id
     */
    private String inviterId;


    public interface Add {
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getInviterId() {
        return inviterId;
    }

    public void setInviterId(String inviterId) {
        this.inviterId = inviterId;
    }
}
