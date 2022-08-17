package com.example.demo.service.parse;

public class Average {
    public String average(long kill, long death, long assist) {
        String result = null;
        double aver;
        if (death == 0) {
            result = "Perfect";
        } else {
            aver = (double) (kill + assist) / death;
            result = Double.toString(Math.round(aver * 100) / 100.0);
        }
        return result;
    }
    public String killPer(long all, long my){
        String result = null;
        double aver;
        aver = (double)my/all*100;
        int aver2= (int)Math.round(aver);
        result = aver2+"%";

        return result;
    }

    public String cs(long cs1, long cs2, long minute){
        String result = null;
        double aver;
        aver = (double)(cs1+cs2)/minute;
        result = "("+Double.toString(Math.round(aver*100)/100.0)+")";
        return result;
    }
}
