package com.letoy.main.entity.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemLogInfo {

    private int id;
    private String username;

    private String uid;
    private String actionName;
    private String createTime;
    private String transactionName;
    private String strSentence;
    private String extraInfo;
    private int type;
    private int moduleType;


    @Override
    public String toString() {
        return createTime + " 用户 " + username + " id:" + uid + "  " + " 进行 " + actionName + " " + transactionName + " 的操作";
    }
}
