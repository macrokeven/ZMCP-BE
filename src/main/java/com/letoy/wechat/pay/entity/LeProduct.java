package com.letoy.wechat.pay.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeProduct {
    private String pid;
    private String name;
    private double price;
    private String detail;

}
