package com.letoy.main.entity.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserInfo对象",description="用户对象UserUserInfo")
public class UserInfo {
    @ApiModelProperty(value="用户唯一编号",name="userId",example="C3ED61C8B4F8432CAF43341105D2C86A")
    private String uid;
    @ApiModelProperty(value="用户手机号",name="phoneNumber",example="13011111111")
    private String phoneNumber;
    @ApiModelProperty(value="用户名",name="username",example="macro")
    private String username;
    private String password;
    @ApiModelProperty(value="用户是否激活",name="active",example="false")
    private boolean active;
    @ApiModelProperty(value="用户角色",name="role",example="66")
    private int role;
    @ApiModelProperty(value="用户的Token",name="token",example="eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MzUwMTM5MjgsInN1YiI6IkMzRUQ2MUM4QjRGODQzMkNBRjQzMzQxMTA1RDJDODZBIiwiaWF0IjoxNjM0NDA5MTI4NDg3fQ.-M3gLH8hPYzcXgLpseRnhA9MVDnnwT3DutrUqMMagptjC77GUAK2_O7sNf8qpvgcls8dfi6ZEquWW3z7Dku6DQ")
    private String token;
    private String avatar;
}
