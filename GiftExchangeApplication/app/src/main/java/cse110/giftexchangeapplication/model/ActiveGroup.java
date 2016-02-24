package cse110.giftexchangeapplication.model;

import java.util.ArrayList;

public class ActiveGroup {
    private String groupName;
    private String groupID;
    private String groupDescription;
    private String userManager;
    private String sortDate, endDate;
    private ArrayList<ArrayList <String>> users;
    private ArrayList<ArrayList <String>> blacklistFlags;
    private double priceMin, priceMax;
    private int blacklistMax;
    private boolean isSorted;

    // Required public constructor
    public ActiveGroup() {
    }

    /**
     * Use this constructor to create new ActiveGroups.
     *
     * @param groupName Group title
     * @param sortDate Date of group sort
     * @param endDate Date which gifts are given
     * @param priceMin Minimum price for gifts
     * @param priceMax Maximum price for gifts
     * @param blacklistMax Maximum number of people to be blacklisted
     *
     */
    public ActiveGroup(String groupName, String sortDate, String endDate,
                       double priceMin, double priceMax, int blacklistMax) {
        this.groupName = groupName;
        this.sortDate = sortDate;
        this.endDate = endDate;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.blacklistMax = blacklistMax;

        //make firebase reference then...
        // use push.
        // return uid and store it like that

    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getUserManager() {
        return userManager;
    }

    public void setUserManager(String userManager) {
        this.userManager = userManager;
    }

    public String getSortDate() {
        return sortDate;
    }

    public void setSortDate(String sortDate) {
        this.sortDate = sortDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<ArrayList<String>> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<ArrayList<String>> users) {
        this.users = users;
    }

    public ArrayList<ArrayList<String>> getBlacklistFlags() {
        return blacklistFlags;
    }

    public void setBlacklistFlags(ArrayList<ArrayList<String>> blacklistFlags) {
        this.blacklistFlags = blacklistFlags;
    }

    public double getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(double priceMin) {
        this.priceMin = priceMin;
    }

    public double getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(double priceMax) {
        this.priceMax = priceMax;
    }

    public int getBlacklistMax() {
        return blacklistMax;
    }

    public void setBlacklistMax(int blacklistMax) {
        this.blacklistMax = blacklistMax;
    }

    public boolean isSorted() {
        return isSorted;
    }

    public void setIsSorted(boolean isSorted) {
        this.isSorted = isSorted;
    }
}