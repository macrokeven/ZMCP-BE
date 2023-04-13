package com.letoy.mqtt;

import com.letoy.mqtt.MqttSystemMsg;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
@Slf4j
public class MqttClientUtil {
    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.url}")
    private String host;
    @Value("${spring.mqtt.client.id}")
    private String clientId;
    @Value("${spring.mqtt.default.topic}")
    private String topic;
    @Value("${mqtt.connection.timeout}")
    private int timeOut;
    @Value("${mqtt.keep.alive.interval}")
    private int interval;

    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    @PostConstruct
    private void init() {
        connect(host, clientId);
    }

    private void connect(String host, String clientId) {
        try {
            mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
            mqttConnectOptions = getMqttConnectOptions();
            mqttClient.connect(mqttConnectOptions);
            mqttClient.subscribe(topic, 2);
            mqttClient.subscribe("$SYS/brokers/emqx@127.0.0.1/clients/#", 2);
            mqttClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost: " + cause.getMessage());
                }

                public void messageArrived(String topic, MqttMessage message) {
                    if (topic.endsWith("disconnected")) {
                        System.out.println("有设备断开");
                        JSONObject jsonObject = JSON.parseObject(new String(message.getPayload()));
                        MqttSystemMsg msg = JSON.toJavaObject(jsonObject, MqttSystemMsg.class);
                        System.out.println(msg);
                    } else if (topic.endsWith("connected")) {
                        JSONObject jsonObject = JSON.parseObject(new String(message.getPayload()));
                        MqttSystemMsg msg = JSON.toJavaObject(jsonObject, MqttSystemMsg.class);
                        System.out.println("有设备连接");
                        System.out.println(msg);
                    } else {
                        System.out.println("topic: " + topic);
                        System.out.println("Qos: " + message.getQos());
                        System.out.println("message content: " + new String(message.getPayload()));
                    }

                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------" + token.isComplete());
                }
            });

        } catch (Exception e) {
            log.error("mqtt服务链接异常!");
            e.printStackTrace();
        }
    }

    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{host});
        mqttConnectOptions.setKeepAliveInterval(interval);
        mqttConnectOptions.setConnectionTimeout(timeOut);
        mqttConnectOptions.setCleanSession(true);
        return mqttConnectOptions;
    }

    private boolean isConnect() {
        if (Objects.isNull(this.mqttClient)) {
            return false;
        }
        return mqttClient.isConnected();
    }

    private void reConnect() throws Exception {
        if (Objects.nonNull(this.mqttClient)) {
            log.info("mqtt 服务已重新链接...");
            this.mqttClient.connect(this.mqttConnectOptions);
        }
    }

    private void closeConnect() throws Exception {
        if (Objects.nonNull(this.mqttClient)) {
            log.info("mqtt 服务已断开链接...");
            this.mqttClient.disconnect();
        }
    }

    public void sendMessage(String topic, String message, int qos) throws Exception {
        if (Objects.nonNull(this.mqttClient) && this.mqttClient.isConnected()) {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(message.getBytes());
            mqttMessage.setQos(qos);
            MqttTopic mqttTopic = mqttClient.getTopic(topic);
            if (Objects.nonNull(mqttTopic)) {
                try {
                    MqttDeliveryToken publish = mqttTopic.publish(mqttMessage);
                    if (publish.isComplete()) {
                        log.info("消息发送成功---->{}", message);
                    }
                } catch (Exception e) {
                    log.error("消息发送异常", e);
                }
            }
        } else {
            reConnect();
        }
    }
}
