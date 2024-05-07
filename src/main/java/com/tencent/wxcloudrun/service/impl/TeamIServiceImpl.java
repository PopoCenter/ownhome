package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.dao.TeamMapper;
import com.tencent.wxcloudrun.dao.TeamMemberMapper;
import com.tencent.wxcloudrun.dao.UserMapper;
import com.tencent.wxcloudrun.dto.RegisterDto;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.entity.TeamMemberEntity;
import com.tencent.wxcloudrun.entity.UserEntity;
import com.tencent.wxcloudrun.enums.MemberRole;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.service.UserIService;
import com.tencent.wxcloudrun.util.UniqueIdUtils;
import com.tencent.wxcloudrun.vo.MemberListVoItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class TeamIServiceImpl extends ServiceImpl<TeamMapper, TeamEntity> implements TeamIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;

    @Resource
    private TeamMapper teamMapper;

    @Resource
    private TeamMemberMapper teamMemberMapper;

    @Override
    public TeamEntity findByUserId(Long userId) throws BusinessDefaultException {
        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getMemberId, userId);
        TeamMemberEntity memberEntity = teamMemberMapper.selectOne(queryWrapper);
        if (memberEntity == null) {
            return null;
        }

        return teamMapper.selectById(memberEntity.getTeamId());
    }

    @Override
    @Transactional
    public void create(Long userId, String name) throws BusinessDefaultException {
        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getMemberId, userId);
        TeamMemberEntity checkEntity = teamMemberMapper.selectOne(queryWrapper);
        if (checkEntity != null) {
            throw new BusinessDefaultException("您已拥有团队，不可重复创建");
        }

        TeamEntity teamEntity = new TeamEntity();
        Long teamId = UniqueIdUtils.Millis.uniqueId();
        Date now = new Date();
        teamEntity.setTeamId(teamId);
        teamEntity.setName(name);
        teamEntity.setOwnerId(userId);
        teamEntity.setCreateTime(now);
        teamEntity.setUpdateTime(now);
        teamMapper.insert(teamEntity);

        TeamMemberEntity memberEntity = new TeamMemberEntity();
        memberEntity.setTeamId(teamId);
        memberEntity.setMemberId(userId);
        memberEntity.setMemberRole(MemberRole.MANAGER.getValue());
        memberEntity.setCreateTime(now);
        memberEntity.setUpdateTime(now);
        teamMemberMapper.insert(memberEntity);
        logger.info("团队创建成功，userId={}, teamId={}, name={}", userId, teamId, name);
    }

    @Override
    public void join(Long userId, Long teamId, Long inviteId) throws BusinessDefaultException {
        TeamEntity teamEntity = teamMapper.selectById(teamId);
        if (teamEntity == null) {
            throw new BusinessDefaultException("团队不存在");
        }

        UserEntity userEntity = userMapper.selectById(inviteId);
        if (userEntity == null) {
            throw new BusinessDefaultException("邀请人不存在");
        }

        // 被邀请人加入团队逻辑
        TeamMemberEntity memberEntity = new TeamMemberEntity();
        memberEntity.setTeamId(teamId);
        memberEntity.setMemberId(userId);
        memberEntity.setMemberRole(MemberRole.COMMON.getValue());
        Date now = new Date();
        memberEntity.setCreateTime(now);
        memberEntity.setUpdateTime(now);
        teamMemberMapper.insert(memberEntity);

        //更新该用户邀请人信息
        UserEntity updateEntity = new UserEntity();
        updateEntity.setUserId(userId);
        updateEntity.setInviterId(inviteId);
        updateEntity.setUpdateTime(now);
        userMapper.updateById(updateEntity);
        logger.info("被邀请人加入团队成功，userId={}, teamId={}, inviteId={}", userId, teamId, inviteId);
    }

    @Override
    public List<MemberListVoItem> findMemberListByUserId(Long userId) throws BusinessDefaultException {
        TeamEntity teamEntity = this.findByUserId(userId);
        if (teamEntity == null) {
            return null;
        }

//        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(TeamMemberEntity::getTeamId, teamEntity.getTeamId());

        return teamMemberMapper.findMemberByTeamId(teamEntity.getTeamId());
    }

    @Override
    public void modify(Long userId, String name) throws BusinessDefaultException {
        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getMemberId, userId);
        TeamMemberEntity memberEntity = teamMemberMapper.selectOne(queryWrapper);
        if (memberEntity == null) {
            throw new BusinessDefaultException("团队信息不存在");
        }

        if (MemberRole.MANAGER.getValue() != memberEntity.getMemberRole()) {
            throw new BusinessDefaultException("非管理员角色，修改失败");
        }

        TeamEntity updateEntity = new TeamEntity();
        updateEntity.setTeamId(memberEntity.getTeamId());
        updateEntity.setName(name);
        updateEntity.setUpdateTime(new Date());
        teamMapper.updateById(updateEntity);
        logger.info("修改团队成功，userId={}, name={}", userId, name);
    }

    @Override
    @Transactional
    public void exit(Long userId) throws BusinessDefaultException {
        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getMemberId, userId);
        TeamMemberEntity memberEntity = teamMemberMapper.selectOne(queryWrapper);
        if (memberEntity == null) {
            return;
        }

        if (MemberRole.MANAGER.getValue() == memberEntity.getMemberRole()) {
            throw new BusinessDefaultException("管理员不可退出团队");
        }

        teamMemberMapper.deleteById(memberEntity.getId());
        logger.info("退出团队成功，userId={}", userId);
    }

    @Override
    public void dismiss(Long userId) throws BusinessDefaultException {
        LambdaQueryWrapper<TeamMemberEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getMemberId, userId);
        TeamMemberEntity memberEntity = teamMemberMapper.selectOne(queryWrapper);
        if (memberEntity == null) {
            return;
        }

        if (MemberRole.COMMON.getValue() == memberEntity.getMemberRole()) {
            throw new BusinessDefaultException("普通成员不可解散团队");
        }

        teamMapper.deleteById(memberEntity.getTeamId());

        LambdaQueryWrapper<TeamMemberEntity> updateWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeamMemberEntity::getTeamId, memberEntity.getTeamId());
        teamMemberMapper.delete(updateWrapper);

        logger.info("解散团队成功，userId={}", userId);
    }
}
