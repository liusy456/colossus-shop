package com.colossus.common.dao;

import com.colossus.common.model.IndexSlideAd;
import com.colossus.common.model.IndexSlideAdExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IndexSlideAdMapper {
    long countByExample(IndexSlideAdExample example);

    int deleteByExample(IndexSlideAdExample example);

    int deleteByPrimaryKey(String id);

    int insert(IndexSlideAd record);

    int insertSelective(IndexSlideAd record);

    List<IndexSlideAd> selectByExample(IndexSlideAdExample example);

    IndexSlideAd selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") IndexSlideAd record, @Param("example") IndexSlideAdExample example);

    int updateByExample(@Param("record") IndexSlideAd record, @Param("example") IndexSlideAdExample example);

    int updateByPrimaryKeySelective(IndexSlideAd record);

    int updateByPrimaryKey(IndexSlideAd record);
}