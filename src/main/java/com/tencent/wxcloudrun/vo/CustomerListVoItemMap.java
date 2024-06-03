package com.tencent.wxcloudrun.vo;

import java.util.List;

/**
 * @author dongdongxie
 * @date 2024/6/3
 */
public class CustomerListVoItemMap {



    private String index;

    private List<CustomerListVoItem> children;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public List<CustomerListVoItem> getChildren() {
        return children;
    }

    public void setChildren(List<CustomerListVoItem> children) {
        this.children = children;
    }
}
