package com.letoy.main.service;

import com.letoy.main.entity.system.SystemLogInfo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

public interface SystemLogInfoService {
    void createLog(SystemLogInfo systemLogInfo);
    List<SystemLogInfo> getAllSystemInfo();
    HSSFWorkbook getAllSystemInfoToExcel();
    List<SystemLogInfo> getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(String[] uidList,String startDate,String endDate,int[] logTypeList);
    String getSystemLogInfoByUidListAndStartEndDateAndLogTypeListToString(String[] uidList, String startDate, String endDate, int[] logTypeList);

}
