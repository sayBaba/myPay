package com.hzit.pay.web.mapper;

import com.hzit.pay.web.model.NoticeInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(NoticeInfo record);

    int insertSelective(NoticeInfo record);

    NoticeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(NoticeInfo record);

    int updateByPrimaryKey(NoticeInfo record);

    /**
     * 根据流水号查询
     * @param reqSerialNo
     * @return
     */
    NoticeInfo queryByReqSerial(@Param("reqSerialNo") String reqSerialNo);

}