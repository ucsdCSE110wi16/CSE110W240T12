package cse110.giftexchangeapplication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import cse110.giftexchangeapplication.utils.Constants;

public class ActiveGroup {
    private String groupName;
    private String groupManager;
    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timeStampCreated;

    // Required public constructor
    public ActiveGroup() {
    }

    /**
     * Use this constructor to create new ActiveGroups.
     * Takes a group name ... for now.
     *
     * @param name
     *
     */
    public ActiveGroup(String name, String manager, HashMap<String, Object> timeStampCreated) {
        this.groupName = name;
        this.groupManager = manager;
        this.timeStampCreated = timeStampCreated;

        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampNowObject;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupManager() {
        return groupManager;
    }

    public HashMap<String, Object> getTimestampLastChanged() {
        return timestampLastChanged;
    }

    public HashMap<String, Object> getTimeStampCreated() {
        return timeStampCreated;
    }

    @JsonIgnore
    public long getTimestampLastChangedLong() {

        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }

    @JsonIgnore
    public long getTimestampCreatedLong() {
        return (long) timestampLastChanged.get(Constants.FIREBASE_PROPERTY_TIMESTAMP);
    }
}

