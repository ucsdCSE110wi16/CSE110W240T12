package cse110.giftX.ui.adminSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.model.User;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.utils.Constants;

/**
 * Activity class in which the admin of a group can remove users
 */

public class AdminEditUsersActivity extends BaseActivity {
    private String groupID;
    private String userEmail;
    private Set<String> userEmails;
    private ActiveGroup mActiveGroup;
    private Firebase editUsersRef;
    private Firebase mActiveGroupUsersRef;
    private Firebase  mActiveGroupRef;
    private ValueEventListener mActiveGroupRefListener;
    private ArrayList<User> users;
    private cse110.giftX.ui.adminSettings.AdminEditUsersAdapter mEditUserAdapter;
    // ListView in which the group's users will be displayed
    private ListView listViewEditUsers;
    // Button for removing all selected users
    Button buttonRemoveSelected;
    // Button for canceling the edit and exiting the dialog
    Button buttonCancel;


    // Method for when the Activity starts
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_users);

        Intent intent = this.getIntent();
        // Get data from activity
        groupID = intent.getStringExtra(Constants.KEY_GROUP_ID);

        // Set the ListView and Buttons to their appropriate UI pieces
        listViewEditUsers = (ListView) findViewById(R.id.list_view_remove_user);
        buttonRemoveSelected = (Button) findViewById(R.id.btn_remove_users);
        buttonCancel = (Button) findViewById(R.id.btn_cancel);

        // Set the adapter for the list
        users = new ArrayList<User>();
        mEditUserAdapter = new cse110.giftX.ui.adminSettings.AdminEditUsersAdapter(this, users);
        listViewEditUsers.setAdapter(mEditUserAdapter);

        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID);
        editUsersRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID).child("users");
        mActiveGroupUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);
        /**
         * Save the most recent version of current active group into mActiveGroup instance
         * variable and update the UI to match the current group
         */
        mActiveGroupRefListener = mActiveGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * Saving the most recent version of current active group into mActiveGroup
                 * if present finish() the activity if the group is null (group was removed or
                 * unshared by its owner while current user is in the group details activity)
                 */
                ActiveGroup activeGroup = dataSnapshot.getValue(ActiveGroup.class);
                // Save to instance variable
                mActiveGroup = activeGroup;
                userEmails = mActiveGroup.getUsers().keySet();
                for (String email : userEmails) {
                    mActiveGroupUsersRef.child(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User usr = dataSnapshot.getValue(User.class);
                            if (usr != null) {
                                if(usr.getGroups().containsKey(groupID)){
                                    users.remove(usr);
                                    users.add(usr);
                                }
                            }
                            if (mEditUserAdapter != null) {
                                mEditUserAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Give the remove Button functionality
        buttonRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the list of users to remove from the group
                List<User> userRemoveList = mEditUserAdapter.getUserRemoveList();
                if (!userRemoveList.isEmpty()) {
                    for (int i = 0; i < userRemoveList.size(); i++) {
                        User toRemove = userRemoveList.get(i);
                        String email = toRemove.getEmail();
                        Firebase removeUserRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID).child("users").child(email);
                        Firebase removeUserGroupRef = new Firebase(Constants.FIREBASE_URL_USERS).child(email).child("groups").child(groupID);
                        mEditUserAdapter.remove(toRemove);
                        mEditUserAdapter.notifyDataSetChanged();
                        removeUserGroupRef.removeValue();
                        removeUserRef.removeValue();
                        users.remove(toRemove);
                    }
                    listViewEditUsers.invalidateViews();
                }

            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                List<User> userRemoveList = mEditUserAdapter.getUserRemoveList();
                if (!userRemoveList.isEmpty()) {
                    mEditUserAdapter.clearUserRemoveList();
                }
                finish();
            }
        });

    }
    // Clean up when activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
        editUsersRef.removeEventListener(mActiveGroupRefListener);
    }

}
