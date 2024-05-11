package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class TeamMemberFireDto {

    @NotNull(groups = {TeamMemberFireDto.Add.class}, message = "name不能为空")
    private Long memberId;

    public interface Add {
    }


    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
