package cse110.giftexchangeapplication.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Defines the data structure for User objects.
 */
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String aboutMe;
    private String likes;
    private String dislikes;
    private ArrayList<String> groups;
    private HashMap<String, Object> timestampJoined;

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
    public User(String firstName, String lastName, String email, HashMap<String, Object> timestampJoined) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.aboutMe = "";
        this.likes = "";
        this.dislikes = "";
        this.groups = new ArrayList<String>();
        this.timestampJoined = timestampJoined;

    }

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

    public ArrayList<String> getGroups() {
        return groups;
    }
}
