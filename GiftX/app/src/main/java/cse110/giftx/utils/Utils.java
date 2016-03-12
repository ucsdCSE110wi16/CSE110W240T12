package cse110.giftX.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public static Calendar parseDate(String date, String time) {
        int index1 = date.indexOf('/');
        int index2 = date.indexOf('/', index1 + 1);
        int index4 = time.indexOf(':');
        int month = Integer.parseInt(date.substring(0, index1));
        int dayOfMonth = Integer.parseInt(date.substring(index1 + 1, index2));
        int year = Integer.parseInt(date.substring(index2 + 1));
        int hour = Integer.parseInt(time.substring(0, index4));
        if(hour == 12) hour = 0;
        int minute = Integer.parseInt(time.substring(index4 + 1, index4 + 3));
        String ampm = time.substring(time.length() - 2);
        if(ampm.equals("PM")) {
            hour += 12;
        }
        Calendar sortTime = Calendar.getInstance();
        sortTime.set(year, month, dayOfMonth, hour, minute);
        return sortTime;
    }

    public static String getTime(String s) {
        //hh:mmaa
        int hour = Integer.parseInt(s.substring(0, s.indexOf(':')));
        String lowerCaseAMPM = s.substring(5).toLowerCase();
        return "" + hour + s.substring(2, 5) + lowerCaseAMPM;
    }

    public static Drawable loadImageFromWeb(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
