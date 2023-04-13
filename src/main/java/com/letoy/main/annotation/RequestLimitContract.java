package com.letoy.main.annotation;


import com.letoy.main.exception.RequestLimitException;
import com.letoy.main.utils.RedisUtil;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Aspect
@Component
public class RequestLimitContract {

    private static final Logger logger = LoggerFactory.getLogger(RequestLimitContract.class);

    @Resource
    private RedisUtil redis;

    private final ScheduledExecutorService executorService;

    public RequestLimitContract(){
        executorService = new ScheduledThreadPoolExecutor(8,
                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
    }

    @Before("within(@org.springframework.web.bind.annotation.RestController *) && @annotation(limit)")
    public void requestLimit(final JoinPoint joinPoint, RequestLimit limit) throws Exception {
        try {
            Object[] args = joinPoint.getArgs();
            HttpServletRequest request = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                    break;
                }
            }
            if (request == null) {
                throw new RequestLimitException("方法中缺失HttpServletRequest参数");
            }
            String ip = request.getLocalAddr();
            String url = request.getRequestURL().toString();
            String key = "req_limit_".concat(url).concat(ip);
            if (!redis.hasKey(key)) {
                redis.set(key, String.valueOf(1));
            } else {
                Integer getValue = Integer.parseInt((String) redis.get(key)) + 1;
                redis.set(key, String.valueOf(getValue));
            }
            int count = Integer.parseInt((String) redis.get(key));
            if (count > 0) {
                //创建一个定时器
//                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        redis.del(key);
                    }
                };
                //这个定时器设定在time规定的时间之后会执行上面的remove方法，也就是说在这个时间后它可以重新访问
//                timer.schedule(timerTask, limit.time());
                // 利用线程池
                executorService.schedule(timerTask,limit.time() / 1000,TimeUnit.SECONDS);
            }
            if (count > limit.count()) {
                logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + limit.count() + "]");
                throw new RequestLimitException("非法请求！");
            }
        } catch (RequestLimitException e) {
            throw new RequestLimitException("非法请求！");
        } catch (Exception e) {
            logger.error("发生异常", e);
        }
    }
}
