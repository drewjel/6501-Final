package edu.virginia.cs.mooncake.wada.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateTimeUtil {

    public static long getRelativeMilisFromTime(int hour, int min, int sec, long anotherTime) {
        /* Given another time, this function adds one day to the hour, min, sec if this time is less than
		 * the another time.
		 */
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(anotherTime);
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH);
        int d = cal.get(Calendar.DAY_OF_MONTH);
        Calendar cal2 = new GregorianCalendar(y, m, d);
        long startDayTime = cal2.getTimeInMillis();
        long time = startDayTime + getMilsFromTime(hour, min, sec);

        if (time < anotherTime)
            return time + getMilsFromTime(24, 0, 0);

        return time;

    }

    public static long getMilsFromTime(int hour, int min, int sec) {
        return (hour * 60 * 60 + min * 60 + sec) * 1000;
    }

    public static long getTimeInMilis(int year, int month, int day, int hour, int minute, int second) {

        Calendar cal = new GregorianCalendar(year, month - 1, day, hour, minute, second);
        return cal.getTimeInMillis();
		/*
		SimpleDateFormat format= new SimpleDateFormat("MM-dd-yyyy HH:mm:ss:SSS");
		String str="";
		long t=0;
		if(month<10)
			str+="0";
		str+=month+"-";
		if(day<10)
			str+="0"+"-";
		str+=day+"-"+year+" ";
		
				
		if(hour<10)
			str+="0";
		str+=hour+":";
		
		if(minute<10)
			str+="0";
		str+=minute;	
		
		if(second<10)
			str+="0";
		str+=second;
		if(mils<10)
			str+="00";
		else if(mils<100)
			str+="0";
		str+=mils;
				
		try{
			Date d= format.parse(str);
			t = d.getTime();
		}catch(Exception ex)
		{
			Log.e("date","date String can not be parsed: "+str);
		}
		
		return t;*/
    }

    public static String getDateTimeString(long timeInMilis, int flag) {
        Date date = new Date(timeInMilis);
        String formatStr = "yyyy-MM-dd";

        if (flag == 1)
            formatStr += " hh:mm aa";
        else if (flag == 2)
            formatStr += " hh:mm:ss aa";
        else if (flag == 3)
            formatStr += " HH-mm-ss";
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        return format.format(date);
    }

    public static String getDateString(long timeInMilis) {
        return getDateTimeString(timeInMilis, 0);
    }

    public static String getDateString(int year, int month, int day) {
        return getDateString(new GregorianCalendar(year, month, day).getTimeInMillis());
    }

    public static String getTimeString(long timeInMilis, int flag) {
        if (flag < 1 || flag > 3)
            return "";
        String str = getDateTimeString(timeInMilis, flag);
        return str.substring(11);
    }

    public static String timeStringFormat(int hour, int min) {
        String minStr = "" + min;
        String str = "";
        if (min < 10)
            minStr = "0" + min;
        if (hour == 0)
            str = "12" + ":" + minStr + " AM";
        else if (hour < 10)
            str = "0" + hour + ":" + minStr + " AM";
        else if (hour < 12)
            str = hour + ":" + minStr + " AM";
        else if (hour == 12)
            str = hour + ":" + minStr + " PM";
        else {
            hour -= 12;
            if (hour < 10)
                str = "0" + hour + ":" + minStr + " PM";
            else
                str = hour + ":" + minStr + " PM";
        }
        return str;
    }

}
