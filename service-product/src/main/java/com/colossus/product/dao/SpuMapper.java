package com.colossus.product.dao;

import com.colossus.product.model.Spu;

public interface SpuMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    int insert(Spu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    int insertSelective(Spu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    Spu selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(Spu record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table spu
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Spu record);
}