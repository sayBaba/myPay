package com.hzit.pay.web.mapper;

import com.hzit.pay.web.model.MchPayChannel;
import org.springframework.stereotype.Repository;

@Repository
public interface MchPayChannelMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(MchPayChannel record);

    int insertSelective(MchPayChannel record);

    MchPayChannel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MchPayChannel record);

    int updateByPrimaryKey(MchPayChannel record);
}