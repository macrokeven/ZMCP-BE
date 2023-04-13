package com.letoy.main.service.Impl;

import com.letoy.main.dao.SystemLogInfoMapper;
import com.letoy.main.dao.UserInfoMapper;
import com.letoy.main.entity.auth.UserInfo;
import com.letoy.main.entity.system.SystemLogInfo;
import com.letoy.main.service.SystemLogInfoService;
import com.letoy.main.utils.Constants;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SystemLogInfoServiceImpl implements SystemLogInfoService {

    @Resource
    SystemLogInfoMapper systemLogInfoMapper;

    @Resource
    UserInfoMapper userInfoMapper;

    @Override
    public void createLog(SystemLogInfo systemLogInfo) {
        Date now = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        systemLogInfo.setCreateTime(simpleDateFormat.format(now));
        if (systemLogInfo.getUsername().equals("") && (!systemLogInfo.getUid().equals(""))) {
            UserInfo userInfo = userInfoMapper.getUserInfoByUid(systemLogInfo.getUid());
            systemLogInfo.setUsername(userInfo.getUsername());
        }
        if (systemLogInfo.getStrSentence().equals("")) {
            systemLogInfo.setStrSentence(systemLogInfo + systemLogInfo.getExtraInfo());
        }
        systemLogInfo.setStrSentence("<div class='log'><div class='log-time'>" +
                simpleDateFormat.format(now) + "</div>" + Constants.getLogType(systemLogInfo.getType()) +
                "<div class='log-content'>" + systemLogInfo.getStrSentence() + "</div></div>");
        systemLogInfoMapper.createSystemLoginInfoWithSystemLogInfo(systemLogInfo);
    }

    @Override
    public List<SystemLogInfo> getAllSystemInfo() {
        return systemLogInfoMapper.getAllSystemLogInfo();
    }

    @Override
    public HSSFWorkbook getAllSystemInfoToExcel() {
        List<SystemLogInfo> systemLogInfos = systemLogInfoMapper.getAllSystemLogInfo();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("系统日志");
        String[] titles = new String[]{"用户名", "用户id", "操作名称", "事物名称", "执行时间"};

        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        hssfSheet.setDefaultColumnStyle(0, style);
        hssfSheet.setDefaultColumnStyle(1, style);
        hssfSheet.setDefaultColumnStyle(2, style);
        hssfSheet.setDefaultColumnStyle(3, style);
        hssfSheet.setDefaultColumnStyle(4, style);
        hssfSheet.setColumnWidth(0, 5000);
        hssfSheet.setColumnWidth(1, 12000);
        hssfSheet.setColumnWidth(2, 4000);
        hssfSheet.setColumnWidth(3, 14000);
        hssfSheet.setColumnWidth(4, 10000);
        //创建标题行
        HSSFRow headRow = hssfSheet.createRow(0);
        int i = 0;
        for (String s : titles) {
            headRow.createCell(i).setCellValue(s);
            i++;
        }

        int row = 1;

        //创建数据
        for (SystemLogInfo systemLogInfo : systemLogInfos) {
            HSSFRow hssfRow = hssfSheet.createRow(row);
            hssfRow.createCell(0).setCellValue(systemLogInfo.getUsername());
            hssfRow.createCell(1).setCellValue(systemLogInfo.getUid());
            hssfRow.createCell(2).setCellValue(systemLogInfo.getActionName());
            hssfRow.createCell(3).setCellValue(systemLogInfo.getTransactionName());
            hssfRow.createCell(4).setCellValue(systemLogInfo.getCreateTime());
            row++;
        }

        return hssfWorkbook;

    }


    @Override
    public List<SystemLogInfo> getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(String[] uidList, String startDate, String endDate, int[] logTypeList) {
        if (logTypeList.length > 0) {
            return systemLogInfoMapper.getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(
                    uidList, startDate.equals("") ? "2000-01-01" : startDate + " 00:00:00",
                    endDate.equals("") ? "3000-01-01" : endDate + " 23:59:59", logTypeList);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getSystemLogInfoByUidListAndStartEndDateAndLogTypeListToString(String[] uidList, String startDate, String endDate, int[] logTypeList) {
        StringBuilder result = new StringBuilder();
        if (logTypeList.length > 0) {
            List<SystemLogInfo> resultArr =  systemLogInfoMapper.getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(
                    uidList, startDate.equals("") ? "2000-01-01" : startDate + " 00:00:00",
                    endDate.equals("") ? "3000-01-01" : endDate + " 23:59:59", logTypeList);
            Pattern pattern = Pattern.compile("<[^>]+>");

            for(SystemLogInfo systemLogInfo : resultArr){
                Matcher matcher = pattern.matcher(systemLogInfo.getStrSentence());
                result.append(matcher.replaceAll(" ")).append("\n");
            }
            return result.toString();
        } else {
            return null;
        }
    }
}
