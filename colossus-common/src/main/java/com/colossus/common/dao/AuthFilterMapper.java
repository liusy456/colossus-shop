package com.colossus.common.dao;

import com.colossus.common.model.AuthFilter;
import com.colossus.common.model.AuthFilterExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthFilterMapper {
    long countByExample(AuthFilterExample example);

    int deleteByExample(AuthFilterExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthFilter record);

    int insertSelective(AuthFilter record);

    List<AuthFilter> selectByExample(AuthFilterExample example);

    AuthFilter selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthFilter record, @Param("example") AuthFilterExample example);

    int updateByExample(@Param("record") AuthFilter record, @Param("example") AuthFilterExample example);

    int updateByPrimaryKeySelective(AuthFilter record);

    int updateByPrimaryKey(AuthFilter record);
}