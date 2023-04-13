package com.letoy.main.controller;

import com.letoy.main.entity.system.MyHttpResponse;
import com.letoy.main.entity.system.SystemLogInfoQueryCondition;
import com.letoy.main.service.SystemLogInfoService;
import com.letoy.main.utils.RandomUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.SUCCESS;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "SystemLogInfo", method = POST)
public class SystemLogInfoController {

    @Resource
    SystemLogInfoService systemLogInfoService;

    @RequestMapping("GetAllSystemInfo")
    public HashMap<String, Object> getAllSystemInfo() {
        return MyHttpResponse.build(
                SUCCESS,
                null,
                systemLogInfoService.getAllSystemInfo());
    }


    @RequestMapping(value = "/GetSystemLogToExcel", method = RequestMethod.GET)
    @ApiOperation(value = "下载信息", httpMethod = "GET", notes = "下载符合条件的Excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void fileDownLoad(HttpServletResponse response) {
        HSSFWorkbook hssfWorkbook = systemLogInfoService.getAllSystemInfoToExcel();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String id = RandomUtil.getRandomId();
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    java.net.URLEncoder.encode("system-log-" + simpleDateFormat.format(date) + ".xls", String.valueOf(StandardCharsets.UTF_8)));
            OutputStream outputStream = response.getOutputStream();
            hssfWorkbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("GetSystemLogInfoByUidListAndStartEndDateAndLogTypeList")
    @ResponseBody
    public HashMap<String, Object> getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(@RequestBody SystemLogInfoQueryCondition systemLogInfoQueryCondition) {
        return MyHttpResponse.build(
                SUCCESS,
                null,
                systemLogInfoService.getSystemLogInfoByUidListAndStartEndDateAndLogTypeList(systemLogInfoQueryCondition.getUidList(),
                        systemLogInfoQueryCondition.getStartDate(),
                        systemLogInfoQueryCondition.getEndDate(),
                        systemLogInfoQueryCondition.getLogTypeList()));
    }

    @RequestMapping("GetSystemLogInfoByUidListAndStartEndDateAndLogTypeListToString")
    @ResponseBody
    public void getSystemLogInfoByUidListAndStartEndDateAndLogTypeListToString(
            HttpServletResponse response, @RequestBody SystemLogInfoQueryCondition systemLogInfoQueryCondition) {
        try (ServletOutputStream outStr = response.getOutputStream(); BufferedOutputStream buff = new BufferedOutputStream(outStr)) {
            response.setCharacterEncoding("utf-8");
            //设置响应的内容类型
            response.setContentType("text/plain");
            //设置文件的名称和格式
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    java.net.URLEncoder.encode("system-log-" + simpleDateFormat.format(date) + ".log", String.valueOf(StandardCharsets.UTF_8)));
            String result = systemLogInfoService.getSystemLogInfoByUidListAndStartEndDateAndLogTypeListToString(systemLogInfoQueryCondition.getUidList(),
                    systemLogInfoQueryCondition.getStartDate(),
                    systemLogInfoQueryCondition.getEndDate(),
                    systemLogInfoQueryCondition.getLogTypeList());
            buff.write(result.getBytes(StandardCharsets.UTF_8));
            buff.flush();
        } catch (Exception e) {
            //LOGGER.error("导出文件文件出错:{}",e);
        }

    }
}
