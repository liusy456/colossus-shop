package com.colossus.common.dao;

import com.colossus.common.model.AuthOrganization;
import com.colossus.common.model.AuthOrganizationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthOrganizationMapper {
    long countByExample(AuthOrganizationExample example);

    int deleteByExample(AuthOrganizationExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthOrganization record);

    int insertSelective(AuthOrganization record);

    List<AuthOrganization> selectByExample(AuthOrganizationExample example);

    AuthOrganization selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthOrganization record, @Param("example") AuthOrganizationExample example);

    int updateByExample(@Param("record") AuthOrganization record, @Param("example") AuthOrganizationExample example);

    int updateByPrimaryKeySelective(AuthOrganization record);

    int updateByPrimaryKey(AuthOrganization record);
}