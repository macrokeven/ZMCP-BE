package com.letoy.wechat.pay.dao;

import com.letoy.wechat.pay.entity.LeOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LeOrderMapper {
    List<LeOrder> getAllLeOrderInfo();
}
