package cse110.giftX.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.HashMap;
import java.util.Map;

import cse110.giftX.utils.Constants;

public class ActiveGroup {
    private String groupName;
    private String groupID;
    private String groupDescription;
    private String groupManager;
    private String managerURL;
    private String sortDate;
    private String sortTime;
    private String endDate;
    private String endTime;

    Map<String, Map<String, Boolean>> users; //with blacklist
    Map<String, String> pairs; //will instantiate when sorting happens

    private double priceMin, priceMax;
    private int blacklistMax;
    private boolean sorted;

    private HashMap<String, Object> timestampLastChanged;
    private HashMap<String, Object> timeStampCreated;

    // Required public constructor
    public ActiveGroup() {
    }

    /**

     * Use this constructor to create new ActiveGroups.
     * Takes a group name ... for now.
     *
     * @param name Group name
     * @param manager Group manager
     * @param timeStampCreated When group was created
     *
     */
    public ActiveGroup(String name, String description, String sortDate, String endDate, String sortTime,
                       String endTime, String manager, double priceMin, double priceMax,
                       HashMap<String, Object> timeStampCreated, String url) {
        this.groupName = name;
        this.groupID = groupID;
        this.groupDescription = description;
        this.groupManager = manager;
        this.sortDate = sortDate;
        this.sortTime = sortTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.users = new HashMap<String, Map<String, Boolean>>();
        this.pairs = new HashMap<String, String>();
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.blacklistMax = 0;
        this.sorted = false;
        this.timeStampCreated = timeStampCreated;
        this.managerURL = url;

        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampNowObject;

        Map<String, Boolean> blackList = new HashMap<String, Boolean>();
        blackList.put(manager, true);
        this.users.put(manager, blackList);
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupManager() {
        return groupManager;
    }

    public String getManagerURL(){
        if(managerURL != null)
            return managerURL;
        return null;
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

    public String getGroupID() {
        return groupID;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public String getSortDate() {
        return sortDate;
    }
    public String getSortTime() {return sortTime;}

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() { return endTime; }

    public Map<String, Map<String, Boolean>> getUsers() {
        return users;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public int getBlacklistMax() {
        return blacklistMax;
    }

    public boolean isSorted() {
        return sorted;
    }

    public Map<String, String> getPairs() { return pairs; }

    public boolean equals(Object other) { return this.groupID.equals(((ActiveGroup)other).getGroupID()); }
}

