package cse110.giftexchangeapplication.utils;

import cse110.giftexchangeapplication.BuildConfig;

public class Constants {

    /**
     * Constants related to locations in Firebase, such as the name
     * of the node where active groups are stored (ie "activeGroups")
     */


    // Constants for Firebase object properties
    public static final String FIREBASE_LOCATION_ACTIVE_GROUP = "activeGroup";


    // Constants for Firebase URL
    public static final String FIREBASE_URL = BuildConfig.UNIQUE_FIREBASE_ROOT_URL;
    public static final String FIREBASE_URL_ACTIVE_GROUP = FIREBASE_URL + "/" + FIREBASE_LOCATION_ACTIVE_GROUP;


    // Constants for bundles, extras and shared preferences keys
    public static final String KEY_LAYOUT_RESOURCE = "LAYOUT_RESOURCE";
}
