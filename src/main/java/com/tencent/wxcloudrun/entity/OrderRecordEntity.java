package com.tencent.wxcloudrun.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 订单操作记录
 *
 * @author dongdongxie
 * @date 2024/05/07
 */
@TableName("t_order_record")
public class OrderRecordEntity {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 团队id
     */
    private Long orderId;

    /**
     * 操作类型
     */
    private Integer operateType;

    /**
     * 操作人id
     */
    private Long operatorId;

    /**
     * 当前 单据id
     */
    private String currentDocumentIds;

    /**
     * 操作后 单据id
     */
    private String afterDocumentIds;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getCurrentDocumentIds() {
        return currentDocumentIds;
    }

    public void setCurrentDocumentIds(String currentDocumentIds) {
        this.currentDocumentIds = currentDocumentIds;
    }

    public String getAfterDocumentIds() {
        return afterDocumentIds;
    }

    public void setAfterDocumentIds(String afterDocumentIds) {
        this.afterDocumentIds = afterDocumentIds;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}