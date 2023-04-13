package com.letoy.wechat.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeOrder {
    private String id;
    private String createTime;
    private String payTime;
    private String createUid;
    private boolean pay;
    private int status;


}
