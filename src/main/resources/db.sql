CREATE TABLE `Counters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `count` int(11) NOT NULL DEFAULT '1',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;







DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `user_id`          BIGINT(20)    NOT NULL COMMENT '用户id',
    `name`             VARCHAR(32)   NOT NULL COMMENT '用户名称',
    `phone`            VARCHAR(32)   NOT NULL COMMENT '手机号',
    `open_id`          VARCHAR(64)   NOT NULL COMMENT 'openId',
    `inviter_id`       BIGINT(20)    NULL DEFAULT NULL COMMENT '邀请人id',
    `create_time`      DATETIME      NOT NULL COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`user_id`),
    UNIQUE KEY `uk_openid` (`open_id`)  USING BTREE
) ENGINE = InnoDB comment = '用户表';


DROP TABLE IF EXISTS `t_team`;
CREATE TABLE `t_team`
(
    `team_id`          BIGINT(20)    NOT NULL COMMENT '团队id',
    `name`             VARCHAR(64)   NOT NULL COMMENT '名称',
    `owner_id`         BIGINT(20)   NOT NULL COMMENT '团队id',
    `create_time`      DATETIME      NOT NULL COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`team_id`),
    UNIQUE KEY `uk_ownerid` (`owner_id`)  USING BTREE
) ENGINE = InnoDB comment = '团队表';


DROP TABLE IF EXISTS `t_team_member`;
CREATE TABLE `t_team_member`
(
    `id`               BIGINT(20)    auto_increment NOT NULL COMMENT 'id',
    `team_id`          BIGINT(20)    NOT NULL COMMENT '团队id',
    `member_id`        BIGINT(20)    NOT NULL COMMENT '成员id',
    `member_role`      int           NOT NULL COMMENT '成员角色',
    `create_time`      DATETIME      NOT NULL COMMENT '创建时间',
    `update_time`      DATETIME      NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_teamid_memberid` (`team_id`, `member_id`)  USING BTREE
) ENGINE = InnoDB comment = '团队成员表';







