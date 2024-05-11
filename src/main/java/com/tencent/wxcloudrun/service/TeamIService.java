package com.tencent.wxcloudrun.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.entity.TeamMemberEntity;
import com.tencent.wxcloudrun.vo.MemberListVoItem;

import java.util.List;

public interface TeamIService extends IService<TeamEntity> {

    /**
     * 查询团队
     *
     * @param userId
     * @return
     * @throws BusinessDefaultException
     */
    TeamEntity findByUserId(Long userId) throws BusinessDefaultException;

    /**
     * 创建团队
     *
     * @param userId
     * @param name
     * @throws BusinessDefaultException
     */
    void create(Long userId, String name) throws BusinessDefaultException;


    /**
     * 被邀请人加入团队
     *
     * @param userId
     * @param teamId
     * @param inviteId
     * @throws BusinessDefaultException
     */
    void join(Long userId, Long teamId, Long inviteId) throws BusinessDefaultException;

    /**
     * 查询团队成员
     *
     * @param userId
     * @return
     * @throws BusinessDefaultException
     */
    List<MemberListVoItem> findMemberListByUserId(Long userId) throws BusinessDefaultException;


    /**
     * 修改团队名称
     *
     * @param userId
     * @param name
     * @throws BusinessDefaultException
     */
    void modify(Long userId, String name) throws BusinessDefaultException;

    /**
     * 退出团队
     *
     * @param userId
     * @throws BusinessDefaultException
     */
    void exit(Long userId) throws BusinessDefaultException;

    /**
     * 解散团队
     *
     * @param userId
     * @throws BusinessDefaultException
     */
    void dismiss(Long userId) throws BusinessDefaultException;

    /**
     * 移除团队
     *
     * @param userId
     * @throws BusinessDefaultException
     */
    void fire(Long userId, Long memberId) throws BusinessDefaultException;

    /**
     * query
     *
     * @param userId
     * @return
     * @throws BusinessDefaultException
     */
    TeamMemberEntity findMemberByUserId(Long userId) throws BusinessDefaultException;
}
