package cse110.giftx.utils;

import cse110.giftx.BuildConfig;

public class Constants {

    /**
     * Constants related to locations in Firebase, such as the name
     * of the node where active groups are stored (ie "activeGroups")
     */

    public static final String FIREBASE_LOCATION_ACTIVE_GROUPS = "activeGroups";
    public static final String FIREBASE_LOCATION_USERS = "users";


    // Constants for Firebase object properties
    public static final String FIREBASE_PROPERTY_GROUP_NAME = "groupName";
    public static final String FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED = "timestampLastChanged";
    public static final String FIREBASE_PROPERTY_TIMESTAMP = "timestamp";


    // Constants for Firebase URL
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_ACTIVE_GROUPS = FIREBASE_URL + "/" + FIREBASE_LOCATION_ACTIVE_GROUPS;
    public static final String FIREBASE_URL_USERS = FIREBASE_URL + "/" + FIREBASE_LOCATION_USERS;



    // Constants for bundles, extras and shared preferences keys
    public static final String KEY_GROUP_NAME = "GROUP_NAME";
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
    public static final String KEY_GROUP_ID = "GROUP_ID";

    /**
     * Constants for Firebase login
     */
    public static final String PASSWORD_PROVIDER = "password";
    public static final String PROVIDER_DATA_DISPLAY_NAME = "displayName";
}