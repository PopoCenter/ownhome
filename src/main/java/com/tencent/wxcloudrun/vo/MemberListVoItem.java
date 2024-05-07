package com.tencent.wxcloudrun.vo;



public class MemberListVoItem {

    private static final long serialVersionUID = 382951780872523489L;


    private String phone;

    private String name;

    private Integer role;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}