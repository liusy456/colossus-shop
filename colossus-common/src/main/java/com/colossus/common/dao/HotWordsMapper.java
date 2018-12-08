package com.colossus.common.dao;

import com.colossus.common.model.HotWords;
import com.colossus.common.model.HotWordsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface HotWordsMapper {
    long countByExample(HotWordsExample example);

    int deleteByExample(HotWordsExample example);

    int deleteByPrimaryKey(String id);

    int insert(HotWords record);

    int insertSelective(HotWords record);

    List<HotWords> selectByExample(HotWordsExample example);

    HotWords selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") HotWords record, @Param("example") HotWordsExample example);

    int updateByExample(@Param("record") HotWords record, @Param("example") HotWordsExample example);

    int updateByPrimaryKeySelective(HotWords record);

    int updateByPrimaryKey(HotWords record);
}