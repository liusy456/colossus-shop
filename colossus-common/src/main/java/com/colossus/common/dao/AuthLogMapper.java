package com.colossus.common.dao;

import com.colossus.common.model.AuthLog;
import com.colossus.common.model.AuthLogExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AuthLogMapper {
    long countByExample(AuthLogExample example);

    int deleteByExample(AuthLogExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthLog record);

    int insertSelective(AuthLog record);

    List<AuthLog> selectByExampleWithBLOBs(AuthLogExample example);

    List<AuthLog> selectByExample(AuthLogExample example);

    AuthLog selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthLog record, @Param("example") AuthLogExample example);

    int updateByExampleWithBLOBs(@Param("record") AuthLog record, @Param("example") AuthLogExample example);

    int updateByExample(@Param("record") AuthLog record, @Param("example") AuthLogExample example);

    int updateByPrimaryKeySelective(AuthLog record);

    int updateByPrimaryKeyWithBLOBs(AuthLog record);

    int updateByPrimaryKey(AuthLog record);
}