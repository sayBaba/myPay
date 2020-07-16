package com.hzit.pay.web.mapper;

import com.hzit.pay.web.model.PaySerialNo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaySerialNoMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PaySerialNo record);

    int insertSelective(PaySerialNo record);

    PaySerialNo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PaySerialNo record);

    int updateByPrimaryKey(PaySerialNo record);

    /**
     * 根据流水号查询
     * @param reqSerialNo
     * @return
     */
    PaySerialNo queryBySerialNo(@Param("reqSerialNo")String reqSerialNo);


}