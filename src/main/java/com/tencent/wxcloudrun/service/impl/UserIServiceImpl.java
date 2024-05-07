package com.tencent.wxcloudrun.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
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
import com.tencent.wxcloudrun.enums.YesNoStatus;
import com.tencent.wxcloudrun.service.UserIService;
import com.tencent.wxcloudrun.util.UniqueIdUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author dongdongxie
 * @date 2024/5/7
 */
@Service
public class UserIServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserIService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private UserMapper userMapper;


    @Resource
    private TeamMapper teamMapper;

    @Resource
    private TeamMemberMapper teamMemberMapper;

    @Override
    public IPage<UserEntity> searchPage(Page<UserEntity> pageParam, Map<String, Object> params) {
        return null;
    }

    @Override
    @Transactional
    public void register(RegisterDto registerDto, String openId) throws BusinessDefaultException {
        Long userId = UniqueIdUtils.Millis.uniqueId();
        Date now = new Date();
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setName(registerDto.getName());
        userEntity.setOpenId(openId);
        userEntity.setPhone(registerDto.getPhone());
        userEntity.setCreateTime(now);
        userEntity.setUpdateTime(now);

        // 邀请注册
        String inviterId = registerDto.getInviterId();
        String teamId = registerDto.getTeamId();
        if (StringUtils.isNotEmpty(inviterId) && StringUtils.isNotEmpty(teamId)) {
            // check team
            TeamEntity teamEntity = teamMapper.selectById(Long.parseLong(teamId));
            if (teamEntity == null) {
                throw new BusinessDefaultException("团队不存在");
            }

            // checkout inviter
            UserEntity checkEntity = userMapper.selectById(Long.parseLong(inviterId));
            if (checkEntity == null) {
                throw new BusinessDefaultException("邀请人不存在");
            }
            userEntity.setInviterId(Long.parseLong(inviterId));

            // 被邀请人加入团队逻辑
            TeamMemberEntity memberEntity = new TeamMemberEntity();
            memberEntity.setTeamId(Long.parseLong(teamId));
            memberEntity.setMemberId(userId);
            memberEntity.setMemberRole(MemberRole.COMMON.getValue());
            memberEntity.setCreateTime(now);
            memberEntity.setUpdateTime(now);
            teamMemberMapper.insert(memberEntity);
        }

        userMapper.insert(userEntity);
        logger.info("用户注册成功，phone={}, name={}", registerDto.getPhone(), registerDto.getName());
    }

    @Override
    public UserEntity findByOpenId(String openId) throws BusinessDefaultException {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getOpenId, openId);
        return userMapper.selectOne(queryWrapper);
    }
}
