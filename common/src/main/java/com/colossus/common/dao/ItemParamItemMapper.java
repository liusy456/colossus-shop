package com.colossus.common.dao;

import com.colossus.common.model.ItemParamItem;
import com.colossus.common.model.ItemParamItemExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemParamItemMapper {
    long countByExample(ItemParamItemExample example);

    int deleteByExample(ItemParamItemExample example);

    int deleteByPrimaryKey(String id);

    int insert(ItemParamItem record);

    int insertSelective(ItemParamItem record);

    List<ItemParamItem> selectByExampleWithBLOBs(ItemParamItemExample example);

    List<ItemParamItem> selectByExample(ItemParamItemExample example);

    ItemParamItem selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ItemParamItem record, @Param("example") ItemParamItemExample example);

    int updateByExampleWithBLOBs(@Param("record") ItemParamItem record, @Param("example") ItemParamItemExample example);

    int updateByExample(@Param("record") ItemParamItem record, @Param("example") ItemParamItemExample example);

    int updateByPrimaryKeySelective(ItemParamItem record);

    int updateByPrimaryKeyWithBLOBs(ItemParamItem record);

    int updateByPrimaryKey(ItemParamItem record);
}