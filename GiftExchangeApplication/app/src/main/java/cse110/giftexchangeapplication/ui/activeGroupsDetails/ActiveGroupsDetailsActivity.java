package cse110.giftexchangeapplication.ui.activeGroupsDetails;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.model.Exchanger;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.ui.MainActivity;
import cse110.giftexchangeapplication.utils.Constants;
import cse110.giftexchangeapplication.utils.Utils;

/**
 * Represents the details screen for when selecting an active group
 */
public class ActiveGroupsDetailsActivity extends BaseActivity {
    private Firebase mActiveGroupRef;
    private Firebase mActiveGroupUsersRef;
    private Firebase mActiveGroupManager;
    private ListView mListView;
    private UserAdapter mUserAdapter;
    private String mGroupId;
    private String mUserEmail;
    private ActiveGroup mActiveGroup;
    private ValueEventListener mActiveGroupRefListener;
    private boolean manager = false;
    private TextView sortingOn;
    private String sortDate;
    private TextView sortDaysLeft;
    private Calendar c;
    private int daysUntilSort;

    private Set<String> userEmails;
    private ArrayList<User> users;

    private TextView match;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_group_details);

        users = new ArrayList<User>();
        mListView = (ListView)findViewById(R.id.list_view_users);
        mUserAdapter = new UserAdapter(this, users);
        mListView.setAdapter(mUserAdapter);


        /* Get the push ID from the extra passed by ActiveGroupFragment */
        Intent intent = this.getIntent();
        mGroupId = intent.getStringExtra(Constants.KEY_GROUP_ID);
        mUserEmail = intent.getStringExtra(MainActivity.USER_EMAIL);
        if (mGroupId == null) {
            /* No point in continuing if there's no valid ID */
            finish();
            return;
        }
        /*
         * Create Firebase references
         */
        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(mGroupId);
        //mactivegroupnotdefined
        mActiveGroupUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);


        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

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

                //if sorted, set up with match
                if(activeGroup.isSorted()) {
                    //match.setText(Utils.decodeEmail(mActiveGroup.getPairs().get(mUserEmail)));

                }

                if (mUserEmail == Utils.decodeEmail(mActiveGroup.getGroupManager())){
                    manager = true;
                }

                /**
                 * Create info based on db
                 */

                sortingOn = (TextView) findViewById(R.id.title_sorting_on);
                sortDate = mActiveGroup.getSortDate();
                sortingOn.setText(String.format(getString(R.string.title_sorting_on), sortDate));

                // Convert date
//        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
//        Date date = format.parse(sortDate);
//
//        while (c.getTime().before(sortDate)) {
//            c.getTime().add(Calendar.DAY_OF_MONTH, 1);
//            daysUntilSort++;
//        }
                daysUntilSort = 3;
                sortDaysLeft = (TextView)findViewById(R.id.title_days_until_sort);
                if(daysUntilSort >= 2){
                    sortDaysLeft.setText(String.format(getString(R.string.title_days_until_sort), daysUntilSort));
                } else {
                    sortDaysLeft.setText(String.format(getString(R.string.title_day_until_sort), daysUntilSort));
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


        if(manager) {
            remove.setVisible(true);
            edit.setVisible(true);
            invite.setVisible(true);
            sort.setVisible(true);
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
            showEditGroupNameDialog();
            return true;
        }


        /**
         * removeList() when the remove action is selected
         */
        if (id == R.id.action_remove_group) {
            removeGroup();
            return true;
        }

        /**
         * Implement the last resort sort
         */
        if (id == R.id.action_invite_users) {
            inviteUsers(null);
        }

        /**
         * Implement the last resort sort
         */
        if(id == R.id.action_last_resort_sort) {
            //Put up the Yes/No message box
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder
                    .setTitle("Instantly sort your group")
                    .setMessage("Are you sure?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // YES
                            instantSort(null);
                        }
                    })
                    .setNegativeButton("No", null)						//Do nothing on no
                    .show();


        }
        return super.onOptionsItemSelected(item);
    }

    // Clean up when activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActiveGroupRef.removeEventListener(mActiveGroupRefListener);
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

    public void inviteUsers(View view) {
        InviteDialogFragment inviteDialog = InviteDialogFragment.newInstance(mUserEmail, mGroupId);
        inviteDialog.show(this.getFragmentManager(), "InviteDialogFragment");
    }

    public void instantSort(View view) {
        Map<String, Map<String, Boolean>> userPreferences = mActiveGroup.getUsers();
        Map<String, String> pairs = Exchanger.pairUsers(userPreferences);
        mActiveGroupRef.child("pairs").setValue(pairs);
        mActiveGroupRef.child("sorted").setValue(true);
        //wait for listener to pickup data change
    }

    public void onSaveBlacklist(View view){
        // save blacklist
    }
}
