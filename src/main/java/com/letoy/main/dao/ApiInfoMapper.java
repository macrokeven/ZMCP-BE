package com.letoy.main.dao;

import com.letoy.main.entity.auth.ApiInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ApiInfoMapper {
    List<ApiInfo> getApiInfoListByUid(String uid);
    int createApiInfo(ApiInfo apiInfo);

}

