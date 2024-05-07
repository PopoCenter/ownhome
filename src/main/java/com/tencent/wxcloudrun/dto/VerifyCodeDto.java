package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class VerifyCodeDto {

    /**
     * code
     */
    @NotNull(groups = {VerifyCodeDto.Verify.class}, message = "code不能为空")
    @NotBlank(groups = {VerifyCodeDto.Verify.class}, message = "code不能为空")
    private String phone;


    /**
     * code
     */
    @NotNull(groups = {VerifyCodeDto.Verify.class}, message = "code不能为空")
    @NotBlank(groups = {VerifyCodeDto.Verify.class}, message = "code不能为空")
    private String code;


    public interface Verify {
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
