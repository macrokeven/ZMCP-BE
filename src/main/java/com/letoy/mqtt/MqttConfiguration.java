package com.letoy.mqtt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@Data
public class MqttConfiguration {

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

}
