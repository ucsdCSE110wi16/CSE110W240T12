package cse110.giftX.ui.activeGroupsDetails;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.model.Exchanger;
import cse110.giftX.model.User;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.MainActivity;
import cse110.giftX.ui.adminSettings.AdminSettingsActivity;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

/**
 * Represents the details screen for when selecting an active group
 */
public class ActiveGroupsDetailsActivity extends BaseActivity {
    private Firebase mActiveGroupRef;
    private Firebase mActiveGroupUsersRef;
    private ListView mListView;
    private UserAdapter mUserAdapter;
    private String groupManager;
    private String mGroupId;
    private String mUserEmail;
    private ActiveGroup mActiveGroup;
    private ValueEventListener mActiveGroupRefListener;

    private Set<String> userEmails;
    private ArrayList<User> users;

    private String match;
    private TextView sortingIn;
    private TextView sortingOn;
    private Set<String> blacklist;
    Firebase blacklistRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_group_details);

        /* Get the push ID from the extra passed by ActiveGroupFragment */
        Intent intent = this.getIntent();
        mGroupId = intent.getStringExtra(Constants.KEY_GROUP_ID);
        mUserEmail = intent.getStringExtra(MainActivity.USER_EMAIL);
        if (mGroupId == null) {
            /* No point in continuing if there's no valid ID */
            finish();
            return;
        }

        users = new ArrayList<>();
        blacklist = new HashSet<String>();
        mListView = (ListView)findViewById(R.id.list_view_users);
        mUserAdapter = new UserAdapter(this, users, mGroupId, blacklist);
        mListView.setAdapter(mUserAdapter);
        //match = (TextView)findViewById(R.id.match_email);
        sortingIn = (TextView)findViewById(R.id.title_days_until_sort);
        sortingOn = (TextView)findViewById(R.id.title_sorting_on);




        /*
         * Create Firebase references
         */
        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(mGroupId);
        mActiveGroupUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Save the most recent version of current active group into mActiveGroup instance
         * variable and update the UI to match the current group
         */

        blacklistRef = mActiveGroupRef.child("users").child(mUserEmail);
        blacklistRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Boolean> blMap = (Map<String, Boolean>) dataSnapshot.getValue();
                Set<String> blSet = blMap.keySet();
                for (String u : blSet) {
                    blacklist.add(u);
                }
                mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        mActiveGroupRefListener = mActiveGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * Saving the most recent version of current active group into mActiveGroup
                 * if present finish() the activity if the group is null (group was removed or
                 * unshared by it's owner while current user is in the group details activity)
                 */
                ActiveGroup activeGroup = dataSnapshot.getValue(ActiveGroup.class);

                if (activeGroup == null) {
                    finish();
                    /**
                     * Make sure to call return, otherwise the rest of the method will execute,
                     * even after calling finish.
                     */
                    return;
                }


                // Save to instance variable
                mActiveGroup = activeGroup;
                groupManager = activeGroup.getGroupManager();
                String date = mActiveGroup.getSortDate();
                String time = mActiveGroup.getSortTime();
                Calendar c = Utils.parseDate(date, time);
                int daysLeft = daysUntilSort(c);
                sortingIn.setText(String.format(getString(R.string.title_days_until_sort), daysLeft));
                SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM");
                String dateDisplay = formatter.format(c.getTime());
                sortingOn.setText(dateDisplay);

                //if sorted, set up with match
                if(activeGroup.isSorted()) {
                    match = mActiveGroup.getPairs().get(mUserEmail);
                    startPostSortActivity();
                    return;
                }
                else {
                    if(sortDatePassed(c)) {
                        matchUsers();
                        mActiveGroupRef.child("sorted").setValue(true);
                        return;
                    }
                }

                //michael - getting userEmails and Pojos
                userEmails = mActiveGroup.getUsers().keySet();
                for(String email: userEmails) {
                    mActiveGroupUsersRef.child(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User usr = dataSnapshot.getValue(User.class);

                            if(usr != null) {
                                users.remove(usr);
                                users.add(usr);
                            }
                            if (mUserAdapter != null) {
                                mUserAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }


                // Calling invalidateOptionsMenu causes onCreateOptionsMenu to be called
                invalidateOptionsMenu();
                // Set title appropriately
                setTitle(activeGroup.getGroupName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Calling invalidateOptionsMenu causes onCreateOptionsMenu to be called
        invalidateOptionsMenu();

        /**
         * Set up click listeners for interaction
         */

        // TODO
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if present.
        getMenuInflater().inflate(R.menu.menu_group_details, menu);

        /**
         * Get menu items
         */
        MenuItem remove = menu.findItem(R.id.action_remove_group);
        MenuItem edit = menu.findItem(R.id.action_edit_group_name);
        MenuItem sort = menu.findItem(R.id.action_last_resort_sort);
        MenuItem invite = menu.findItem(R.id.action_invite_users);

        // Only remove & edit options are implemented for now.
        if(groupManager != null && mUserEmail.equals(groupManager)) {
            remove.setVisible(true);
            edit.setVisible(true);
            sort.setVisible(true);
            invite.setVisible(true);
        }
        else {
            remove.setVisible(false);
            edit.setVisible(false);
            sort.setVisible(false);
            invite.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /**
         * Show edit group name dialog when the edit action is selected
         */
        if (id == R.id.action_edit_group_name) {
            Intent intentEditUsers = new Intent(this, AdminSettingsActivity.class);
            intentEditUsers.putExtra(Constants.KEY_GROUP_ID, mGroupId);
            intentEditUsers.putExtra("email", mUserEmail);
            startActivity(intentEditUsers);
        }


        /**
         * removeList() when the remove action is selected
         */
        if (id == R.id.action_remove_group) {
            removeGroup();
            return true;
        }

        if(id == R.id.action_invite_users) {
            InviteDialogFragment inviteDialog = InviteDialogFragment.newInstance(mUserEmail, mGroupId);
            inviteDialog.show(this.getFragmentManager(), "InviteDialogFragment");
        }

        if(id == R.id.action_last_resort_sort) {
            matchUsers();
            mActiveGroupRef.child("sorted").setValue(true);
        }

        return super.onOptionsItemSelected(item);
    }

    // Clean up when activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
        //mActiveGroupRef.removeEventListener(mActiveGroupRefListener);
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        // Common toolbar setup
        setSupportActionBar(toolbar);
        // Add back button to the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Show the edit group name dialog when the user selects "Edit group name" menu item
     */
    public void showEditGroupNameDialog() {
        // Create an instance of the dialog fragment and show it.
        // TODO
        DialogFragment dialogFragment = EditGroupNameDialogFragment.newInstance(mActiveGroup, mGroupId);
        dialogFragment.show(this.getFragmentManager(), "EditGroupNameDialogFragment");
    }
    /**
     * Remove current active group *admin user only*
     */
    public void removeGroup() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialogFragment = RemoveGroupDialogFragment.newInstance(mActiveGroup, mGroupId);
        dialogFragment.show(getFragmentManager(), "RemoveGroupDialogFragment");
    }

    public void onInviteButtonPressed(View view) {
        InviteDialogFragment inviteDialog = InviteDialogFragment.newInstance(mUserEmail, mGroupId);
        inviteDialog.show(this.getFragmentManager(), "InviteDialogFragment");
    }

    public void instantSortButton(View view) {
        matchUsers();
        mActiveGroupRef.child("sorted").setValue(true);
    }

    public void matchUsers() {
        Map<String, Map<String, Boolean>> userPreferences = mActiveGroup.getUsers();
        Map<String, String> pairs = Exchanger.pairUsers(userPreferences);
        mActiveGroupRef.child("pairs").setValue(pairs);
        //wait for listener to pickup data change
    }

    private boolean sortDatePassed(Calendar sortTime) {
        Calendar currTime = Calendar.getInstance();
        return currTime.after(sortTime);
    }

    private int daysUntilSort(Calendar sortTime) {
        int days = 0;
        Calendar currTime = Calendar.getInstance();
        while (currTime.before(sortTime)) {
            currTime.add(Calendar.DAY_OF_MONTH, 1);
            days++;
        }
        return days;
    }

    //changed to clear preferences
    public void onClearBlacklist(View view) {
        blacklist.clear();
        Map<String, Boolean> blk = new HashMap<String, Boolean>();
        blk.put(mUserEmail, true);
        blacklistRef.setValue(blk);
        mUserAdapter.notifyDataSetChanged();
    }

    public final static String USER_EMAIL = "edu.ucsd.cse110wi16.giftexchange.USER_EMAIL1";
    public final static String GROUP_ID = "edu.ucsd.cse110wi16.giftexchange.GROUP_ID1";
    public final static String USER_EMAIL_MATCH = "edu.ucsd.cse110wi16.giftexchange.USER_EMAIL2";
    public void startPostSortActivity() {
        Intent intent = new Intent(this, PostSortActivity.class);
        intent.putExtra(USER_EMAIL, mUserEmail);
        intent.putExtra(GROUP_ID, mGroupId);
        intent.putExtra(USER_EMAIL_MATCH, match);
        startActivity(intent);
        finish();
    }
}
