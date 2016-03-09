package cse110.giftx.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class Exchanger {
    public static Map<String, String> pairUsers(Map<String, Map<String, Boolean>> userPreferences) { //map of UIDs to UID/Boolean pairs
        Map<String, String> pairs = new HashMap<String, String>();

        Set<String> giverSet = userPreferences.keySet();
        Set<String> receiverSet = new HashSet<String>(giverSet);

        Set<String> replaceGiver = new HashSet<String>();
        Set<String> replaceReceiver = new HashSet<String>();
        for(String user: giverSet) {
            boolean pairFound = false;
            String receiver = null;
            for(String u: receiverSet) {
                if(!user.equals(u) && !userPreferences.get(user).containsKey(u) && !userPreferences.get(u).containsKey(user)) {
                    pairs.put(user, u);
                    receiver = u;
                    pairFound = true;
                    break;
                }
            }
            if(!pairFound)
                replaceGiver.add(user);
            else
                receiverSet.remove(receiver);
        }

        giverSet = replaceGiver;
        replaceGiver = new HashSet<String>();
        for(String user: giverSet) {
            boolean pairFound = false;
            String receiver = null;
            for(String u: receiverSet) {
                if(!user.equals(u)) {
                    pairs.put(user, u);
                    pairFound = true;
                    receiver = u;
                    break;
                }
            }
            if(!pairFound)
                replaceGiver.add(user);
            else
                receiverSet.remove(receiver);
        }

        giverSet = replaceGiver;
        for(String user: giverSet) {
            String receiver = null;
            for(String u: receiverSet) {
                pairs.put(user, u);
                receiver = u;
                break;
            }
            receiverSet.remove(receiver);
        }

        return pairs;
    }
}