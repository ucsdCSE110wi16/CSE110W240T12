package cse110.giftexchangeapplication.utils;

import cse110.giftexchangeapplication.BuildConfig;

public class Constants {

    /**
     * Constants related to locations in Firebase, such as the name
     * of the node where active groups are stored (ie "activeGroups")
     */

    public static final String FIREBASE_LOCATION_ACTIVE_GROUPS = "activeGroups";

    // Constants for Firebase object properties
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "timestampLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";


    // Constants for Firebase URL
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_ACTIVE_GROUPS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ACTIVE_GROUPS;



    // Constants for bundles, extras and shared preferences keys
    public static final String KEY_GROUP_NAME = "GROUP_NAME";
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_GROUP_ID = "GROUP_ID";
}
