package com.colossus.common.dao;

import com.colossus.common.model.AuthUserOrganization;
import com.colossus.common.model.AuthUserOrganizationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AuthUserOrganizationMapper {
    long countByExample(AuthUserOrganizationExample example);

    int deleteByExample(AuthUserOrganizationExample example);

    int deleteByPrimaryKey(String id);

    int insert(AuthUserOrganization record);

    int insertSelective(AuthUserOrganization record);

    List<AuthUserOrganization> selectByExample(AuthUserOrganizationExample example);

    AuthUserOrganization selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AuthUserOrganization record, @Param("example") AuthUserOrganizationExample example);

    int updateByExample(@Param("record") AuthUserOrganization record, @Param("example") AuthUserOrganizationExample example);

    int updateByPrimaryKeySelective(AuthUserOrganization record);

    int updateByPrimaryKey(AuthUserOrganization record);
}