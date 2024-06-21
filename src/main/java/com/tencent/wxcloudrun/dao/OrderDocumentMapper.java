package com.tencent.wxcloudrun.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tencent.wxcloudrun.entity.OrderDocumentEntity;
import com.tencent.wxcloudrun.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderDocumentMapper extends BaseMapper<OrderDocumentEntity> {



    @Update("update  t_order_document set is_delete = 1 where file_id =  #{fileId} ")
    void deleteByFileId(@Param("fileId") String fileId);

}
