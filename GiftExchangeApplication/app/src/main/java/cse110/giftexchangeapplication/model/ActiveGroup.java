package cse110.giftexchangeapplication.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.firebase.client.ServerValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cse110.giftexchangeapplication.utils.Constants;

public class ActiveGroup {
    private String groupName;
    private String groupID;
    private String groupDescription;
    private String manager;
    private Date sortDate, endDate;
    private ArrayList<ArrayList<String>> users;
    private ArrayList<ArrayList <String>> blacklistFlags;
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
    public ActiveGroup(String name, String manager, HashMap<String, Object> timeStampCreated) {
        this.groupName = name;
        this.groupID = "";
        this.groupDescription = "";
        this.manager = manager;
        this.sortDate = null;
        this.endDate = null;
        this.users = new ArrayList<>();
        this.blacklistFlags = new ArrayList<> ();
        this.priceMin = 0;
        this.priceMax = 0;
        this.blacklistMax = 0;
        this.sorted = false;

        this.timeStampCreated = timeStampCreated;

        HashMap<String, Object> timestampNowObject = new HashMap<String, Object>();
        timestampNowObject.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        this.timestampLastChanged = timestampNowObject;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getManager() {
        return manager;
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

    public Date getSortDate() {
        return sortDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public ArrayList<ArrayList<String>> getUsers() {
        return users;
    }

    public ArrayList<ArrayList<String>> getBlacklistFlags() {
        return blacklistFlags;
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
}

