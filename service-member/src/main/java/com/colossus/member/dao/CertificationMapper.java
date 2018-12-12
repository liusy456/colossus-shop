package com.colossus.member.dao;

import com.colossus.member.model.Certification;

public interface CertificationMapper {

    int deleteByPrimaryKey(String id);

    int insert(Certification record);

    int insertSelective(Certification record);

    Certification selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Certification record);

    int updateByPrimaryKey(Certification record);
}