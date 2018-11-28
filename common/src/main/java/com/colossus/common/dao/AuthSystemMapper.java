package com.colossus.common.dao;

import com.colossus.common.model.AuthSystem;
import com.colossus.common.model.AuthSystemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthSystemMapper {
    long countByExample(AuthSystemExample example);

    int deleteByExample(AuthSystemExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthSystem record);

    int insertSelective(AuthSystem record);

    List<AuthSystem> selectByExample(AuthSystemExample example);

    AuthSystem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthSystem record, @Param("example") AuthSystemExample example);

    int updateByExample(@Param("record") AuthSystem record, @Param("example") AuthSystemExample example);

    int updateByPrimaryKeySelective(AuthSystem record);

    int updateByPrimaryKey(AuthSystem record);
}