package com.tencent.wxcloudrun.vo;

import java.util.List;

/**
 * @author dongdongxie
 * @date 2024/5/11
 */
public class OrderRecordVo {

    /**
     * 操作类型
     */
    private String operateType;

    /**
     * 操作人id
     */
    private String operator;

    /**
     * 当前 单据id
     */
    private List<String> currentFileIdList;

    /**
     * 创建时间
     */
    private String operateTime;

    private String demo;

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getCurrentFileIdList() {
        return currentFileIdList;
    }

    public void setCurrentFileIdList(List<String> currentFileIdList) {
        this.currentFileIdList = currentFileIdList;
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
}
