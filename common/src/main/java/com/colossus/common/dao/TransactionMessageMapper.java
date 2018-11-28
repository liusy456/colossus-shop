package com.colossus.common.dao;

import com.colossus.common.model.TransactionMessage;
import com.colossus.common.model.TransactionMessageExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TransactionMessageMapper {
    long countByExample(TransactionMessageExample example);

    int deleteByExample(TransactionMessageExample example);

    int deleteByPrimaryKey(String id);

    int insert(TransactionMessage record);

    int insertSelective(TransactionMessage record);

    List<TransactionMessage> selectByExampleWithBLOBs(TransactionMessageExample example);

    List<TransactionMessage> selectByExample(TransactionMessageExample example);

    TransactionMessage selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TransactionMessage record, @Param("example") TransactionMessageExample example);

    int updateByExampleWithBLOBs(@Param("record") TransactionMessage record, @Param("example") TransactionMessageExample example);

    int updateByExample(@Param("record") TransactionMessage record, @Param("example") TransactionMessageExample example);

    int updateByPrimaryKeySelective(TransactionMessage record);

    int updateByPrimaryKeyWithBLOBs(TransactionMessage record);

    int updateByPrimaryKey(TransactionMessage record);
}