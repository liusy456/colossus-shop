package com.colossus.common.dao;

import com.colossus.common.model.CategoryImage;
import com.colossus.common.model.CategoryImageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CategoryImageMapper {
    long countByExample(CategoryImageExample example);

    int deleteByExample(CategoryImageExample example);

    int deleteByPrimaryKey(String id);

    int insert(CategoryImage record);

    int insertSelective(CategoryImage record);

    List<CategoryImage> selectByExample(CategoryImageExample example);

    CategoryImage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CategoryImage record, @Param("example") CategoryImageExample example);

    int updateByExample(@Param("record") CategoryImage record, @Param("example") CategoryImageExample example);

    int updateByPrimaryKeySelective(CategoryImage record);

    int updateByPrimaryKey(CategoryImage record);
}