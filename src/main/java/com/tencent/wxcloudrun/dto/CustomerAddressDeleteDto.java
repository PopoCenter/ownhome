package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;

/**
 * 注册
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class CustomerAddressDeleteDto {

    @NotNull(groups = {CustomerAddressDeleteDto.Modify.class}, message = "id不能为空")
    private Long id;

    public interface Modify {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
