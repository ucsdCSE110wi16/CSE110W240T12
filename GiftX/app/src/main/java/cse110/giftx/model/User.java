package cse110.giftx.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String userID;
    private String aboutMe;
    private String likes;
    private String dislikes;
    private HashMap<String, Object> timestampJoined;

    Map<String, Boolean> groups;  //groupIDs to useless booleans
    Map<String, Boolean> invitations; //groupIDs to useless booleans

    public User() {
        // Required empty constructor
    }

    /**
     * Use this constructor to create new User.
     *
     * @param firstName User's first name
     * @param lastName User's last name
     * @param email User's email
     * @param timestampJoined When user joined
     */
    public User(String firstName, String lastName, String email, HashMap<String, Object> timestampJoined, String uid) {
        this.userID = uid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.aboutMe = "";
        this.likes = "";
        this.dislikes = "";
        this.timestampJoined = timestampJoined;

        //michael - adding dummies to initialize the group and invites locations
        this.groups = new HashMap<String, Boolean>();
        this.invitations = new HashMap<String, Boolean>();
        this.groups.put("dummyGroup", true);
        this.invitations.put("dummyInvitation", true);

    }

    public String getUserID() {return userID; }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getLikes() {
        return likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public Map<String, Boolean> getGroups() {
        return groups;
    }

    public Map<String, Boolean> getInvitations() {
        return invitations;
    }

    public boolean equals(Object other) {
        return this.userID.equals(((User)other).getUserID());
    }
}
