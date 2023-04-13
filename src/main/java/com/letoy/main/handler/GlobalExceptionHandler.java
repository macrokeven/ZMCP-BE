package com.letoy.main.handler;

import com.letoy.main.exception.RequestLimitException;
import com.letoy.main.exception.SystemException;
import com.letoy.main.utils.LogUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

import static com.letoy.main.utils.Constants.REQUEST_TOO_MANY_TIMES;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Log log = LogFactory.getLog(GlobalExceptionHandler.class);

    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseBody
    private HashMap<String, Object> runtimeExceptionHandler(HttpServletRequest req, RequestLimitException e) {
        HashMap<String, Object> modelMap = new HashMap<String, Object>();
        LogUtils logUtils = new LogUtils();
        logUtils.writeToFile(e.getMessage());
        modelMap.put("status", REQUEST_TOO_MANY_TIMES);
        modelMap.put("msg", "网络错误，请稍后再试！");
        return modelMap;
    }
    @ExceptionHandler(value = SystemException.class)
    @ResponseBody
    private HashMap<String, Object> systemExceptionHandler(HttpServletRequest req, SystemException e) {
        HashMap<String, Object> modelMap = new HashMap<String, Object>();
        LogUtils logUtils = new LogUtils();
        log.info(e.getMessage());
        logUtils.writeToFile(e.getMessage());
        modelMap.put("status", REQUEST_TOO_MANY_TIMES);
        modelMap.put("msg", "网络错误，请稍后再试！");
        return modelMap;
    }
}

