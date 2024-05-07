package com.tencent.wxcloudrun.vo;


import java.util.List;

public class MemberListVo {

    private static final long serialVersionUID = 382951780872523489L;


   private List<MemberListVoItem> memberList;

    public List<MemberListVoItem> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberListVoItem> memberList) {
        this.memberList = memberList;
    }
}