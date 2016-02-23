package cse110.giftexchangeapplication.model;


public class ActiveGroup {
    private String groupName;

    // Required public constructor
    public ActiveGroup() {
    }

    /**
     * Use this constructor to create new ActiveGroups.
     * Takes a group name ... for now.
     *
     * @param groupName
     *
     */
    public ActiveGroup(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}

