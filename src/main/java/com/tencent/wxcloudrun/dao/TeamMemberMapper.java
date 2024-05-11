package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.entity.TeamEntity;
import com.tencent.wxcloudrun.entity.TeamMemberEntity;
import com.tencent.wxcloudrun.vo.MemberListVoItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TeamMemberMapper extends BaseMapper<TeamMemberEntity> {


    @Select("select u.phone as phone, u.name as name, m.id as id, m.member_role as role from t_team_member as m left join t_user as u on m.member_id=u.user_id where team_id = #{teamId} order by m.create_time desc ")
    List<MemberListVoItem> findMemberByTeamId(@Param("teamId") Long teamId);

}
