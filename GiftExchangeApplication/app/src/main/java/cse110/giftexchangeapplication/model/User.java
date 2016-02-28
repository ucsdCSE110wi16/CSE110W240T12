package cse110.giftexchangeapplication.model;

import java.util.HashMap;

/**
 * Defines the data structure for User objects.
 */
public class User {
    //TODO add the rest of the fields being stored in the User POJO
    private String firstName;
    private String lastName;
    private String email;
    private HashMap<String, Object> timestampJoined;

    public User() {
        // Required empty constructor
    }

    /**
     * Use this constructor to create new User.
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param timestampJoined
     */
    public User(String firstName, String lastName, String email, HashMap<String, Object> timestampJoined) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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
}
