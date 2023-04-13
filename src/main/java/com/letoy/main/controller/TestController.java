package com.letoy.main.controller;

import com.letoy.main.dao.ApiInfoMapper;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.entity.system.MyHttpResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@RestController
@RequestMapping(value = "Test",method = RequestMethod.POST)
public class TestController {

    @Resource
    ApiInfoMapper apiInfoMapper;
    @RequestMapping("GetValueTest")
    @ApiOperation(value = "数据获取测试接口", notes = "测试是否能从后端获取数据")
    public HashMap<String, Object> getValueTest() {
        var user = new UserInfo();
        return MyHttpResponse.build(0, "", apiInfoMapper.getApiInfoListByUid("1"));
    }
}
