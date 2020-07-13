package com.hzit.pay.web.mapper;

import com.hzit.pay.web.model.PaySerialNo;
import org.springframework.stereotype.Repository;

@Repository
public interface PaySerialNoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PaySerialNo record);

    int insertSelective(PaySerialNo record);

    PaySerialNo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaySerialNo record);

    int updateByPrimaryKey(PaySerialNo record);
}