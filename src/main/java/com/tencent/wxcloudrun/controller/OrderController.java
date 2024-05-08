package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.Exception.BusinessDefaultException;
import com.tencent.wxcloudrun.config.ResponseMessage;
import com.tencent.wxcloudrun.dto.TeamCreateDto;
import com.tencent.wxcloudrun.entity.UserEntity;
import com.tencent.wxcloudrun.enums.ResponseEnum;
import com.tencent.wxcloudrun.service.TeamIService;
import com.tencent.wxcloudrun.service.UserIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * counter控制器
 */
@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

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
            String openId = getOpenId(headers);
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
}