package com.letoy.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MqttSystemMsg {
    private String username;
    private String ts;
    private long sockport;
    private String protocol;
    private long proto_ver;
    private String proto_name;
    private long keepalive;
    private String ipaddress;
    private long expiry_interval;
    private long connected_at;
    private String clientid;
    private boolean clean_start;
}
