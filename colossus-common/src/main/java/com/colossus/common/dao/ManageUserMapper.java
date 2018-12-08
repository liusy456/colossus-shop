package com.colossus.common.dao;

import com.colossus.common.model.ManageUser;
import com.colossus.common.model.ManageUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ManageUserMapper {
    long countByExample(ManageUserExample example);

    int deleteByExample(ManageUserExample example);

    int deleteByPrimaryKey(String id);

    int insert(ManageUser record);

    int insertSelective(ManageUser record);

    List<ManageUser> selectByExample(ManageUserExample example);

    ManageUser selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ManageUser record, @Param("example") ManageUserExample example);

    int updateByExample(@Param("record") ManageUser record, @Param("example") ManageUserExample example);

    int updateByPrimaryKeySelective(ManageUser record);

    int updateByPrimaryKey(ManageUser record);
}