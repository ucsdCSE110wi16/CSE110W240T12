package cse110.giftexchangeapplication.utils;

import android.content.Context;

import java.text.SimpleDateFormat;

import cse110.giftexchangeapplication.model.ActiveGroup;

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
     * Return true if current userEmail equals to ActiveGroup.manager();
     * return false otherwise
     */
    public static boolean checkIfManager(ActiveGroup activeGroup, String currentUserEmail) {
        return (activeGroup.getManager() != null &&
                activeGroup.getManager().equals(currentUserEmail));
    }

    /**
     * Encode user email to use it as a Firebase key
     */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }
}
