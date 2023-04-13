package com.letoy.main.dao;

import com.letoy.main.entity.system.SystemLogInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SystemLogInfoMapper {
    void createSystemLoginInfoWithSystemLogInfo(SystemLogInfo systemLogInfo);
    List<SystemLogInfo> getAllSystemLogInfo();
    List<SystemLogInfo> getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(String[] uidList,String startDate,String endDate,int[] logTypeList);
}
