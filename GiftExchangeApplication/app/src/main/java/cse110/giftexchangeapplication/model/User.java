package cse110.giftexchangeapplication.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String name;
    private String email;
    private String aboutMe;
    private String likes;
    private String dislikes;
    private ArrayList<String> groups;
    private HashMap<String, Object> timestampJoined;

    public User() {
        // Required empty constructor
    }

    public User(String name, String email, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
        this.aboutMe = "";
        this.likes = "";
        this.dislikes = "";
        this.groups = new ArrayList<String>();
    }

    public String getName() {
        return name;
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

    public ArrayList<String> getGroups() {
        return groups;
    }
}
