package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class TeamJoinDto {

    @NotNull(groups = {TeamJoinDto.Add.class}, message = "inviteId不能为空")
    @NotBlank(groups = {TeamJoinDto.Add.class}, message = "inviteId不能为空")
    private String inviteId;


    @NotNull(groups = {TeamJoinDto.Add.class}, message = "teamId不能为空")
    @NotBlank(groups = {TeamJoinDto.Add.class}, message = "teamId不能为空")
    private String teamId;

    public interface Add {
    }


    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
