package com.hzit.pay.web.mapper;

import com.hzit.pay.web.model.MchInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface MchInfoMapper {
    int deleteByPrimaryKey(String mchId);

    int insert(MchInfo record);

    int insertSelective(MchInfo record);

    MchInfo selectByPrimaryKey(String mchId);

    int updateByPrimaryKeySelective(MchInfo record);

    int updateByPrimaryKey(MchInfo record);
}