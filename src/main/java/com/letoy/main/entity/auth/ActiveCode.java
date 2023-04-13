package com.letoy.main.entity.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveCode {
    private String code;
    private String createTime;
    private String phoneNumber;
    private int type;
}
