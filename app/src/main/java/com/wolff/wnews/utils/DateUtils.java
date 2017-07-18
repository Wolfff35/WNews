package com.wolff.wnews.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by wolff on 25.05.2017.
 */

public class DateUtils {
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATE_FORMAT_VID = "dd-MM-yyyy";
    public static final String DATE_FORMAT_VID_FULL = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_SAVE = "yyyy-MM-dd-HH-mm-ss";

    public Date dateFromString(String strDate, String strFormat){
        //2017-02-02T15:30:00
        if(strDate==null){
            return null;
        }
        if(strDate.equalsIgnoreCase("")|strDate.isEmpty()|strDate==null){
            return null;
        }
        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        try {
            Date date = format.parse(strDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public String dateToString(Date date,String strFormat){
        Date locDate;
        if(date==null){
            locDate = dateFromString("0001-01-01T00:00:00", DATE_FORMAT_STR);
        }else {
            locDate=date;
        }

        DateFormat format = new SimpleDateFormat(strFormat, Locale.ENGLISH);
        String strDate = format.format(locDate);
        return strDate;
    }
    public String addZero(int num){
        if(String.valueOf(num).length()==1){
            return "0"+String.valueOf(num);
        }
        return String.valueOf(num);
    }
    public String calculateInterval(Date pubDate){
        String time_interval;
        Date now = new Date();
        if(pubDate==null){
            Log.e("CALCULATE",""+pubDate);
            return "NULL";
        }
        long sec = (now.getTime()-pubDate.getTime())/1000;
        if(sec<60){
            time_interval=""+sec+" sec";
        }else if(sec<3600){
            time_interval = ""+sec/60+" min";
        }else if(sec<24*3600){
            time_interval = ""+sec/3600+" hours";
        }else {
            time_interval = ""+(sec/(24*3600))+" days";
        }
    return time_interval;
    }
}
