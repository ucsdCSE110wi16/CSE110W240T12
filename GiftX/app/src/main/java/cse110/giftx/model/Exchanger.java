package cse110.giftX.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Michael Khorram on 3/2/2016.
 * Improved by Kevin Chu on 3/8/2016
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

        // if we run the algorithm too much, just ignore the blacklist
        // happens if too many people listed
        int counter = 0;


        // one user edge case pairs user with userself
        if(userList.size() == 1) {
            curr_user = userList.get(0);
            final_pairs.put(curr_user, curr_user);
        }

        // looping through nested hash map
        for (Map.Entry<String, Map<String, Boolean>> usePrefs : userPreferences.entrySet()) {

            // gets current user
            curr_user = usePrefs.getKey();
            //System.out.println("current user: " + curr_user);

            // gets shitlist users per user
            for (Map.Entry<String, Boolean> bList_map : usePrefs.getValue().entrySet()) {
                // shit list users per user
                bList_user = bList_map.getKey();
                curr_blacklist.add(bList_user);
                //System.out.println("blacklist: " + curr_blacklist);
            }

            // get first random user
            rand_user = userList.get(pick_rand_user.nextInt(userList.size()));
            //System.out.println("rand user 1: " + rand_user);

            // check is user in shitlist
            if(curr_blacklist.contains(rand_user) && !((curr_user).equals(rand_user))) {
                is_shit = true;
            }

            else if((curr_user).equals(rand_user) && (userList.size() > 1)) {
                while((curr_user).equals(rand_user)) {
                    rand_user = userList.get(pick_rand_user.nextInt(userList.size()));
                }
            }
            //is_shit = curr_blacklist.contains(rand_user);

            if(curr_blacklist.contains(rand_user)) {
                is_shit = true;
            }

            // generate random user until one found not in shit list
            while (is_shit && !((curr_user).equals(rand_user)) && (userList.size() > 1)) {
                // if run more than 10 times, give up
                if(counter > 10) {
                    break;
                }
                if(curr_blacklist.contains(rand_user) && !((curr_user).equals(rand_user))) {
                    is_shit = true;
                    rand_user = userList.get(pick_rand_user.nextInt(userList.size()));
                }
                else if((curr_user).equals(rand_user)){
                    rand_user = userList.get(pick_rand_user.nextInt(userList.size()));
                }
                else {
                    is_shit = false;
                }
                counter = counter + 1;
                //System.out.println("curr user - rand user " + curr_user + " - " + rand_user);
            }

            // insert final pairs in hashmap
            final_pairs.put(curr_user, rand_user);

            // remove user from user list because user has been assigned
            if(!(curr_user).equals(rand_user)) {
                userList.remove(rand_user);
            }

            // reset assignment loop counter
            counter = 0;

            //System.out.println("userList: " + userList);
            // clear blacklist for new user
            curr_blacklist.clear();
        }
        //System.out.println("userList size: " + userList.size());
        // if there are still unsigned users in userlist return error
        if (!(userList.isEmpty())) {
            //System.out.println("return error");
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
        Map<String, Boolean> shitList7 = new HashMap<>();
        Map<String, Boolean> shitList8 = new HashMap<>();
        Map<String, Boolean> shitList9 = new HashMap<>();

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

        // shit 5
        shitList5.put("Luke", false);
        shitList5.put("Jerry", false);
        shitList5.put("Maria", false);
        shitList5.put("Joe", false);

        // shit 6
        shitList6.put("John", false);

        // shit 7
        shitList7.put("Jerry", false);
        shitList7.put("Luke", false);
        shitList7.put("Mary", false);

        // shit 8
        shitList8.put("John", false);
        shitList8.put("Joe", false);
        shitList8.put("Mary", false);
        shitList8.put("Stacy", false);
        shitList8.put("Howard", false);
        shitList8.put("George", false);
        shitList8.put("Robert", false);
        shitList8.put("Luke", false);
        shitList8.put("Jerry", false);
        shitList8.put("Maria", false);
        shitList8.put("Fred", false);
        shitList8.put("Larry", false);


        // test list
        userList.put("John", shitList8);
        userList.put("Joe", shitList8);
        userList.put("Mary", shitList8);
        userList.put("Stacy", shitList8);
        userList.put("Howard", shitList8);
        userList.put("George", shitList8);
        userList.put("Robert", shitList8);
        userList.put("Luke", shitList8);
        userList.put("Jerry", shitList8);
        userList.put("Maria", shitList8);
        userList.put("Fred", shitList8);
        userList.put("Larry", shitList8);

        ret_list = exchanger.pairUsers(userList);

        long startTime = System.currentTimeMillis();
        //System.out.println("return list size " + ret_list.size());
        while(ret_list.isEmpty()) {
            //System.out.println(ret_list.isEmpty());
            ret_list = exchanger.pairUsers(userList);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) + "ms");


        // print contents of list
        for(Map.Entry<String, String> entry : ret_list.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue();
            System.out.println("user: " + key + " " + "paired with: " + value);
        }

    }
}