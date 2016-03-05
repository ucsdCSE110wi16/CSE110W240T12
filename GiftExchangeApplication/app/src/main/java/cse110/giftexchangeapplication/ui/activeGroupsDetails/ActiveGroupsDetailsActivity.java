package cse110.giftexchangeapplication.ui.activeGroupsDetails;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.utils.Constants;
import cse110.giftexchangeapplication.utils.Utils;

/**
 * Represents the details screen for when selecting an active group
 */
public class ActiveGroupsDetailsActivity extends BaseActivity {
    private Firebase mActiveGroupRef;
    private ListView mListView;
    private String mGroupId;
    private ActiveGroup mActiveGroup;
    private ValueEventListener mActiveGroupRefListener;

    // Stores whether the current user is the owner
    private boolean mCurrentuserIsManager = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_group_details);

        /* Get the push ID from the extra passed by ActiveGroupFragment */
        Intent intent = this.getIntent();
        mGroupId = intent.getStringExtra(Constants.KEY_GROUP_ID);
        if (mGroupId == null) {
            /* No point in continuing if there's no valid ID */
            finish();
            return;
        }
        /*
         * Create Firebase references
         */
        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(mGroupId);

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

                // Check if current user is manager
                mCurrentuserIsManager = Utils.checkIfManager(activeGroup, mEncodedEmail);

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
//        MenuItem share = menu.findItem(R.id.action_share_group);
//        MenuItem archive = menu.findItem(R.id.action_archive);

        // Only remove & edit options are implemented for now.
        remove.setVisible(mCurrentuserIsManager);
        edit.setVisible(mCurrentuserIsManager);

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
}
