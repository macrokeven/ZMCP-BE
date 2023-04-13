package com.letoy.main.exception;

import com.letoy.main.annotation.RequestLimitContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemSQLException extends RuntimeException{

    private static final Logger logger = LoggerFactory.getLogger(RequestLimitContract.class);

    private static final long serialVersionUID = 1555967171104727461L;

    public SystemSQLException(){
        super("数据更新失败！");
    }

    public SystemSQLException(String message){
        super("数据更新失败！");
        logger.error(message);
    }
}
