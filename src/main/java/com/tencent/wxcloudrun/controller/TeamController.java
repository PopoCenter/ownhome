package com.tencent.wxcloudrun.controller;

import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.config.ResponseMessage;
import com.tencent.wxcloudrun.dto.TeamCreateDto;
import com.tencent.wxcloudrun.dto.TeamJoinDto;
import com.tencent.wxcloudrun.dto.TeamModifyDto;
import com.tencent.wxcloudrun.dto.UserModifyDto;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.entity.TeamMemberEntity;
import com.tencent.wxcloudrun.entity.UserEntity;
import com.tencent.wxcloudrun.enums.ResponseEnum;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.service.UserIService;
import com.tencent.wxcloudrun.vo.MemberListVo;
import com.tencent.wxcloudrun.vo.MemberListVoItem;
import com.tencent.wxcloudrun.vo.UserInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * counter控制器
 */
@RestController
@RequestMapping("/api/team")
public class TeamController extends BaseController {

    @Autowired
    private UserIService userIService;

    @Autowired
    private TeamIService teamIService;

    /**
     * 创建团队
     */
    @PostMapping(value = "/create")
    ResponseMessage<?> create(@RequestBody @Validated(TeamCreateDto.Add.class) TeamCreateDto createDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            // check team

            teamIService.create(userEntity.getUserId(), createDto.getName());
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 团队成员列表
     */
    @PostMapping(value = "/memberList")
    ResponseMessage<MemberListVo> memberList(@RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            MemberListVo listVo = new MemberListVo();
            List<MemberListVoItem> memberList = teamIService.findMemberListByUserId(userEntity.getUserId());
            listVo.setMemberList(memberList);
            return ResponseMessage.success(listVo);
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 接受邀请过 加入团队
     */
    @PostMapping(value = "/join")
    ResponseMessage<?> join(@RequestBody @Validated(TeamJoinDto.Add.class) TeamJoinDto joinDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            teamIService.join(userEntity.getUserId(), Long.parseLong(joinDto.getTeamId()), Long.parseLong(joinDto.getInviteId()));
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 修改团队
     */
    @PostMapping(value = "/modify")
    ResponseMessage<?> modify(@RequestBody @Validated(TeamModifyDto.Add.class) TeamModifyDto modifyDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            TeamEntity teamEntity = teamIService.findByUserId(userEntity.getUserId());
            if (teamEntity == null) {
                throw new BusinessDefaultException("团队不存在");
            }

            teamIService.modify(userEntity.getUserId(), modifyDto.getName());
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 退出团队
     */
    @PostMapping(value = "/exit")
    ResponseMessage<?> exit(@RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            teamIService.exit(userEntity.getUserId());
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 解散团队
     */
    @PostMapping(value = "/dismiss")
    ResponseMessage<?> dismiss(@RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            teamIService.dismiss(userEntity.getUserId());
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }
}