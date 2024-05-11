package com.tencent.wxcloudrun.dto;

import lombok.Data;

import javax.validation.constraints.Min;


/**
 * 分页
 *
 * @author dongdongxie
 * @date 2023/04/13
 */
@Data
public class PageDTO {

    @Min(value = 1, message = "页码必须大于零")
    private Integer pageNum;

    @Min(value = 1, message = "每页数据最少一条")
    private Integer pageSize;


}
