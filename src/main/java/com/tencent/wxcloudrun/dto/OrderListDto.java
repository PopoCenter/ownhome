package com.tencent.wxcloudrun.dto;


import javax.validation.constraints.NotNull;

/**
 * 订单列表
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
public class OrderListDto extends PageDTO {

    /**
     * 客户姓名模糊搜索
     */
    private String name;

    /**
     * -1 全部 10 待处理 20 处理完成 30 已取消
     */
    @NotNull(groups = {OrderListDto.List.class}, message = "queryStatus不能为空")
    private Integer queryStatus;


    public interface List {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(Integer queryStatus) {
        this.queryStatus = queryStatus;
    }
}
