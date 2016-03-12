package cse110.giftX;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import cse110.giftX.model.Exchanger;

/**
 * Created by AJ on 3/11/16.
 */
public class BlacklistSortTest {

    @Test
    public void blackListTest(){
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
