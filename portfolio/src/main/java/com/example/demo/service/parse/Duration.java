package com.example.demo.service.parse;

public class Duration {
    public String duration(long num){
        String duration;
        String min = Long.toString(num/60);
        String sec = Long.toString(num%60);
        duration = min+"분 "+sec+"초";
        return duration;
    }
}
