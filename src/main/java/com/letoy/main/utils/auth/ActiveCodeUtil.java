package com.letoy.main.utils.auth;

import com.letoy.main.entity.auth.ActiveCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActiveCodeUtil {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse("2022-10-19 10:45:45");
        Date date2 = sdf.parse("2022-10-19 10:50:45");
        System.out.println((date2.getTime()-date1.getTime()) == 300000);
//        System.out.println((new Date("2022-10-19 10:45:45")).getTime()-(new Date("2022-10-19 09:45:45")).getTime());
    }

    public static boolean checkActiveCode(ActiveCode activeCode) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(activeCode.getCreateTime());
        Date now = new Date();
        return (now.getTime()-date1.getTime()) <= 300000;

    }

    public static long getTime(String targetTime) {
        String[] times = targetTime.split(":");
        int hours = Integer.parseInt(times[0]);
        int min = Integer.parseInt(times[1]);
        int sec = Integer.parseInt(times[2]);
        return hours * 3600000L + min * 60000L + sec * 1000L;
    }
}
