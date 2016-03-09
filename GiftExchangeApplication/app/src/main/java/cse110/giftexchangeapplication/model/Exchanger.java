package cse110.giftexchangeapplication.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class Exchanger {
    public static Map<String, String> pairUsers(Map<String, Map<String, Boolean>> userPreferences) { //map of UIDs to UID/Boolean pairs

        // map with pairs to return
        Map<String, String> final_pairs = new HashMap<String, String>();

        // empty map to return for error
        Map<String, String> error = new HashMap<String, String>();

        // map of all users with names (column of names)
        Set<String> userSet = userPreferences.keySet();

        // 2 lists to randomize users
        List<String> userList = new ArrayList<>(userSet); // list to assign users from

        // string to hold user while iterating thru hashmap
        String curr_user;
        // string for blacklist user
        String bList_user;
        // boolean to check if rand user in shitlist
        Boolean is_shit = false;

        // map to hold blacklist
        Set<String> curr_blacklist = new HashSet<String>();

        // randomizer object to look for random user pair
        Random pick_rand_user = new Random();
        // string to hold random user
        String rand_user;


        // one user edge case pairs user with userself
        if(userList.size() == 1) {
            curr_user = userList.get(0);
            final_pairs.put(curr_user, curr_user);
        }

        // looping through nested hash map
        for (Map.Entry<String, Map<String, Boolean>> usePrefs : userPreferences.entrySet()) {

            // gets current user
            curr_user = usePrefs.getKey();

            // gets shitlist users per user
            for (Map.Entry<String, Boolean> bList_map : usePrefs.getValue().entrySet()) {
                // shit list users per user
                bList_user = bList_map.getKey();
                curr_blacklist.add(bList_user);
            }

            // get random user
            rand_user = userList.get(pick_rand_user.nextInt(userList.size()));

            // check is user in shitlist
            is_shit = curr_blacklist.contains(rand_user);

            // generate random user until one found not in shit list
            while (is_shit && !(curr_user.equals(rand_user))) {
                rand_user = userList.get(pick_rand_user.nextInt(userList.size()));
                is_shit = curr_blacklist.contains(rand_user);
            }

            final_pairs.put(curr_user, rand_user);
            userList.remove(rand_user);

            // clear blacklist for new user
            curr_blacklist.clear();
        }

        // if there are still unsigned users in userlist return error
        if (userList.size() > 0) {
            return error;
        }
        else {
            return final_pairs;
        }
    }


    public static void main(String[] args) {
        int max = 3;
        Map<String, Boolean> shitList1 = new HashMap<>();
        Map<String, Boolean> shitList2 = new HashMap<>();
        Map<String, Boolean> shitList3 = new HashMap<>();
        Map<String, Boolean> shitList4 = new HashMap<>();
        Map<String, Boolean> shitList5 = new HashMap<>();
        Map<String, Boolean> shitList6 = new HashMap<>();

        Map<String, Map<String, Boolean>> userList = new HashMap<>();
        Map<String, String> ret_list = new HashMap<>();

        Exchanger exchanger = new Exchanger();

        // testing values
        // shit 1
        shitList1.put("John", false);
        shitList1.put("Stacy", false);
        shitList1.put("Howard", false);

        // shit 2
        shitList2.put("Mary", false);
        shitList2.put("Stacy", false);


        // shit 3
        shitList3.put("John", false);
        shitList3.put("Joe", false);

        // shit 4
        shitList4.put("Mary", false);
        shitList4.put("John", false);
        shitList4.put("George", false);


        // test list
        userList.put("John", shitList1);
        userList.put("Joe", shitList2);
        userList.put("Mary", shitList6);
        userList.put("Stacy", shitList3);
        userList.put("Howard", shitList5);
        userList.put("George", shitList3);
        userList.put("Robert", shitList4);

        ret_list = exchanger.pairUsers(userList);

        while(ret_list.isEmpty()) {
            ret_list = exchanger.pairUsers(userList);
        }

        // print contents of list
        for(Map.Entry<String, String> entry : ret_list.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue();
            System.out.println("user: " + key + " " + "paired with: " + value);
        }

    }
}