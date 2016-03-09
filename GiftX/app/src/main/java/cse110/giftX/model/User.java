package cse110.giftX.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String userName;
    private String email;
    private String userID;
    private String aboutMe;
    private String likes;
    private String dislikes;
    private HashMap<String, Object> timestampJoined;
    private String profileURL;

    Map<String, Boolean> groups;  //groupIDs to useless booleans
    Map<String, Boolean> invitations; //groupIDs to useless booleans

    public User() {
        // Required empty constructor
    }

    /**
     * Use this constructor to create new User.
     *
     * @param email User's email
     * @param timestampJoined When user joined
     */
    public User(String userName, String email, HashMap<String, Object> timestampJoined) {
        this.userName = userName;
        this.email = email;
        this.aboutMe = "";
        this.likes = "";
        this.dislikes = "";
        this.userID = "";
        this.timestampJoined = timestampJoined;

        //michael - adding dummies to initialize the group and invites locations
        this.groups = new HashMap<String, Boolean>();
        this.invitations = new HashMap<String, Boolean>();
        this.groups.put("dummyGroup", true);
        this.invitations.put("dummyInvitation", true);

    }

    public String getProfileURL(){
        if(profileURL != null)
            return profileURL;
        return null;
    }

    public String getUserID() {return userID; }

    public String getUserName() {
        return userName;
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
        return this.email.equals(((User)other).getEmail());
    }
}
