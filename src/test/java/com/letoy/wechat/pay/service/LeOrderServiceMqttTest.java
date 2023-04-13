package com.letoy.wechat.pay.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
class LeOrderServiceMqttTest {

    @Resource
    LeOrderService leOrderService;
    @Test
    void getAllLeOrderInfo() {
        System.out.println(leOrderService.getAllLeOrderInfo());
    }
}