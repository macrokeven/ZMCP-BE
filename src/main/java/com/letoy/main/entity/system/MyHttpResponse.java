package com.letoy.main.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

import static com.letoy.main.utils.Constants.SUCCESS;

@Data
@AllArgsConstructor
public class MyHttpResponse {

    private int status;
    //返回消息
    private String message;
    //data对象
    private Object data;

    public static HashMap<String, Object> build(int status, String message, Object data) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        if (message != null) {
            hashMap.put("msg", message);
        }
        if (data != null) {
            hashMap.put("data", data);
        }
        return hashMap;
    }

    public static HashMap<String, Object> returnSuccess() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", SUCCESS);
        return hashMap;
    }

}

