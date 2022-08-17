package com.example.demo.service.parse;

import java.text.SimpleDateFormat;

public class TimeStamp {
    public String timeStamp(long num){
        SimpleDateFormat sdf = new SimpleDateFormat ("MM-dd");
        String result = sdf.format(num).toString();
        return result;
    }
}
