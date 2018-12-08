package com.colossus.common.dao;

import com.colossus.common.model.CategorySecondary;
import com.colossus.common.model.CategorySecondaryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategorySecondaryMapper {
    long countByExample(CategorySecondaryExample example);

    int deleteByExample(CategorySecondaryExample example);

    int deleteByPrimaryKey(String id);

    int insert(CategorySecondary record);

    int insertSelective(CategorySecondary record);

    List<CategorySecondary> selectByExample(CategorySecondaryExample example);

    CategorySecondary selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CategorySecondary record, @Param("example") CategorySecondaryExample example);

    int updateByExample(@Param("record") CategorySecondary record, @Param("example") CategorySecondaryExample example);

    int updateByPrimaryKeySelective(CategorySecondary record);

    int updateByPrimaryKey(CategorySecondary record);
}