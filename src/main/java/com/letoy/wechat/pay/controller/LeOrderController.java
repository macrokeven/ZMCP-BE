package com.letoy.wechat.pay.controller;

import com.letoy.main.entity.system.MyHttpResponse;
import com.letoy.mqtt.MqttClientUtil;
import com.letoy.wechat.pay.service.LeOrderService;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.core.notification.RequestParam.Builder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.SUCCESS;

@RestController
@RequestMapping(value = "LeOrder", method = RequestMethod.GET)
public class LeOrderController {

    @Resource
    LeOrderService leOrderService;

    @Resource
    private MqttClientUtil mqttClientUtil;

    @RequestMapping("GetAllLeOrder")
    public HashMap<String, Object> getAllLeOrder() {
        return MyHttpResponse.build(
                SUCCESS,
                null,
                leOrderService.getAllLeOrderInfo());
    }

    @RequestMapping("SendTest")
    public HashMap<String, Object> sendTest(String topic,String message) {
        //0:最多一次 1：最少一次 2：至少一次
        int qos = 2;
        try {
            mqttClientUtil.sendMessage(topic, message, qos);
        } catch (Exception e) {
            System.out.println("mqtt 消息发送异常");
        }
        return MyHttpResponse.build(
                SUCCESS,
                null,
                "");
    }

    @RequestMapping("ReceiveMsg")
    public void receiveMsg(HttpServletRequest request) {

        String merchantId = "1638473710";
        String privateKeyPath = "/cert/apiclient_key.pem";
        String merchantSerialNumber = "57A36EC301F7253920E9CBBB11D950B9E708EBCA";
        String apiV3key = "ChinaLetoy2023ChinaLetoyChinaLet";

        String nonce = request.getParameter("Wechatpay-Signature");
        String signature = request.getParameter("Wechatpay-Signature");
        System.out.println(nonce);
        System.out.println(signature);
    }
}
