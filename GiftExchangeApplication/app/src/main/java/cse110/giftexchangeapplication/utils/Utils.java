package cse110.giftexchangeapplication.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Utility classes
 */
public class Utils {

    /*
     * Format the timestamp with SimpleDateFormat
     */
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private Context mContext = null;


    /**
     * Public constructor that takes mContext for later use
     */
    public Utils(Context con) {
        mContext = con;
    }

    /**
     * Encode user email to use it as a Firebase key
     */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    public static String getDayOfWeek(int i) {
        switch(i) {
            case 1: return "Sun";
            case 2: return "Mon";
            case 3: return "Tue";
            case 4: return "Wed";
            case 5: return "Thu";
            case 6: return "Fri";
            case 7: return "Sat";
            default: return "";
        }
    }

    public static String getMonth(int i) {
        switch(i) {
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11: return "Dec";
            default: return "";
        }
    }

    public static String getTime(String s) {
        int hour = Integer.parseInt(s.substring(0, s.indexOf(':')));
        String newHour = "";
        String newMinute = s.substring(s.indexOf(':') + 1);
        if(newMinute.length() < 2)
            newMinute = "0" + newMinute;
        String ampm = "";
        if(hour >= 12) {
            int nHour = hour - 12;
            if(nHour == 0) nHour = 12;
            newHour += nHour;
            ampm = "pm";
        }
        else {
            if(hour == 0) {
                newHour = "12";
            }
            else {
                newHour += hour;
            }
            ampm = "am";
        }
        return newHour + ":" + newMinute + ampm;
    }
}
