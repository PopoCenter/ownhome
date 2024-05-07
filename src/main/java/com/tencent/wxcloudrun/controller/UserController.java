package com.tencent.wxcloudrun.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.config.ApiResponse;
import com.tencent.wxcloudrun.config.ResponseMessage;
import com.tencent.wxcloudrun.dto.*;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.entity.UserEntity;
import com.tencent.wxcloudrun.enums.ResponseEnum;
import com.tencent.wxcloudrun.model.Counter;
import com.tencent.wxcloudrun.service.CounterService;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.service.UserIService;
import com.tencent.wxcloudrun.util.SendSmsUtil;
import com.tencent.wxcloudrun.vo.UserInfoVo;
import com.tencent.wxcloudrun.vo.VerifyCodeVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * counter控制器
 */
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {

    @Autowired
    private UserIService userIService;

    @Autowired
    private TeamIService teamIService;


    private Cache<String, String> codeCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    /**
     * 注册
     */
    @PostMapping(value = "/sendCode")
    ResponseMessage<?> sendCode(@RequestBody @Validated(SendMessageDto.Send.class) SendMessageDto sendMessageDto) {
        try {

            String phone = sendMessageDto.getPhone();

            SendSmsUtil.SendSmsRequest request = new SendSmsUtil.SendSmsRequest();
            request.setPhone(phone);

            request.setSmsSdkAppId("1400788564");

            request.setSecretId("");
            request.setSecretKey("");

            request.setSignName("蓝图数字化工具");
            request.setTemplateId("1662625");
            // 这个值，要看你的模板中是否预留了占位符，如果没有则不需要设置
            request.setTemplateParamSet(new String[]{"模板中的参数值，如果没有则为空"});

            //SendSmsUtil.sendSms(request);

            Random random = new Random();
            int code = random.nextInt(999999) + 100000; // 生成一个六位数的随机数，从100000到999999

            //缓存5分钟 5分钟后失效

            codeCache.put(phone, String.valueOf(code));


            logger.info("generate code success, code={}", code);
            return ResponseMessage.success();
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    @PostMapping(value = "/verify")
    ResponseMessage<?> verify(@RequestBody @Validated(VerifyCodeDto.Verify.class) VerifyCodeDto verifyCodeDto) {
        try {
            String phone = verifyCodeDto.getPhone();
            String code = verifyCodeDto.getCode();

            String cacheCode = codeCache.getIfPresent(phone);
            if (cacheCode == null || StringUtils.isEmpty(cacheCode)) {
                logger.info("验证码失效");
                return ResponseMessage.fail(ResponseEnum.CODE_NOT_EXIST, "验证码失效");
            }

            if (cacheCode.equals(code)) {
                return ResponseMessage.success("验证通过");
            } else {
                logger.info("验证码不正确");
                return ResponseMessage.fail(ResponseEnum.CODE_WRONG, "验证码不正确");
            }
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }


    /**
     * 注册
     */
    @PostMapping(value = "/register")
    ResponseMessage<?> register(@RequestBody @Validated(RegisterDto.Add.class) RegisterDto registerDto, @RequestHeader HttpHeaders headers) {
        try {

            String phone = registerDto.getPhone();
            // TODO: 2024/5/7 check


            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            userIService.register(registerDto, openId);
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
     * 用户详情
     */
    @PostMapping(value = "/userInfo")
    ResponseMessage<UserInfoVo> info(@RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");
            UserInfoVo userInfoVo = new UserInfoVo();

            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                logger.info("用户不存在, openId={}", openId);
                throw new BusinessDefaultException("用户不存在");
            }

            userInfoVo.setUserId(userEntity.getUserId());
            userInfoVo.setName(userEntity.getName());
            userInfoVo.setPhone(userEntity.getPhone());

            userInfoVo.setTeamName(StringUtils.EMPTY);
            userInfoVo.setTeamId(StringUtils.EMPTY);
            TeamEntity teamEntity = teamIService.findByUserId(userEntity.getUserId());
            if (teamEntity != null) {
                userInfoVo.setTeamName(teamEntity.getName());
                userInfoVo.setTeamId(teamEntity.getTeamId().toString());
            }

            return ResponseMessage.success(userInfoVo);
        } catch (BusinessDefaultException ue) {
            logger.error(ue.getMessage(), ue);
            return ResponseMessage.fail(ResponseEnum.FAILURE, ue.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseMessage.fail(ResponseEnum.SYSTEM_CODE);
        }
    }

    /**
     * 用户修改
     */
    @PostMapping(value = "/modify")
    ResponseMessage<?> modify(@RequestBody @Validated(UserModifyDto.Add.class) UserModifyDto modifyDto, @RequestHeader HttpHeaders headers) {
        try {
            String openId = headers.getFirst("HTTP_X_WX_OPENID");

            UserEntity userEntity = userIService.findByOpenId(openId);
            if (userEntity == null) {
                throw new BusinessDefaultException("用户不存在");
            }

            UserEntity updateEntity = new UserEntity();
            updateEntity.setUserId(userEntity.getUserId());
            updateEntity.setName(modifyDto.getName());
            updateEntity.setUpdateTime(new Date());
            userIService.updateById(updateEntity);

            logger.info("用户修改成功, userId={}", userEntity.getUserId());

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