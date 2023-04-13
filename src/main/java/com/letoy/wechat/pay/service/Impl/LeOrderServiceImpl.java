package com.letoy.wechat.pay.service.Impl;

import com.letoy.wechat.pay.dao.LeOrderMapper;
import com.letoy.wechat.pay.entity.LeOrder;
import com.letoy.wechat.pay.service.LeOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LeOrderServiceImpl implements LeOrderService {

    @Resource
    LeOrderMapper leOrderMapper;
    @Override
    public List<LeOrder> getAllLeOrderInfo() {
        System.out.println(leOrderMapper);
        return leOrderMapper.getAllLeOrderInfo();
    }
}
