package com.bazukaa.secured.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFromTimeStamp {
    public static String formatTime(long timeMillis){
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aaa MMM dd, yyyy");
        Date resultDate = new Date(timeMillis);
        String timeStamp = "Created on ";
        timeStamp = timeStamp + String.valueOf(sdf.format(resultDate));
        return timeStamp;
    }
}
