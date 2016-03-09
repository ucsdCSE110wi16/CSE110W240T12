package cse110.giftX.ui.activeGroupsDetails;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.model.User;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.userProfile.UserProfileActivity;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

/**
 * Represents the details screen for when selecting an active group
 */
public class PostSortActivity extends BaseActivity {
    private Firebase mActiveGroupRef;
    private Firebase matchedUserRef;
    private String mGroupId;
    private String mUserEmail;
    private ActiveGroup mActiveGroup;
    private ValueEventListener mActiveGroupRefListener;
    private String groupManager;
    private TextView match_text;
    private User matchedUser;
    private ImageView profileView;

    private String match;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sort);

        /* Get the push ID from the extra passed by ActiveGroupFragment */
        Intent intent = this.getIntent();
        mUserEmail = intent.getStringExtra(ActiveGroupsDetailsActivity.USER_EMAIL);
        mGroupId = intent.getStringExtra(ActiveGroupsDetailsActivity.GROUP_ID);
        match = intent.getStringExtra(ActiveGroupsDetailsActivity.USER_EMAIL_MATCH);

        //initialize views
        match_text = (TextView) findViewById(R.id.txt_match);
        profileView = (ImageView) findViewById(R.id.imageView2);

        // TODO - Being called from presort, not main. (ARTHUR)
        // will pass the group id as well as the user email and
        // matched user
        // mGroupId = intent.getStringExtra(Constants.KEY_GROUP_ID);
        // mUserEmail = intent.getStringExtra(P.USER_EMAIL);

        if (mGroupId == null) {
            /* No point in continuing if there's no valid ID */
            finish();
            return;
        }
        /*
         * Create Firebase references
         */
        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(mGroupId);
        matchedUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(match);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();

        /**
         * Save the most recent version of current active group into mActiveGroup instance
         * variable and update the UI to match the current group
         */
        mActiveGroupRef.addValueEventListener(new ValueEventListener() {
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

                // Calling invalidateOptionsMenu causes onCreateOptionsMenu to be called
                invalidateOptionsMenu();
                // Set title appropriately
                setTitle(activeGroup.getGroupName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        matchedUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                matchedUser = dataSnapshot.getValue(User.class);
                String displayName = matchedUser.getUserName();
                match_text.setText(displayName);
                String profileURL = matchedUser.getProfileURL();
                new DownloadImageTask().execute(profileURL);
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


        sort.setVisible(false);
        invite.setVisible(false);
        if(groupManager != null && mUserEmail.equals(groupManager)) {
            remove.setVisible(true);
            edit.setVisible(true);
        }
        else {
            remove.setVisible(false);
            edit.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_group_name){
            // call edit group page TODO
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
     * Remove current active group *admin user only*
     */
    public void removeGroup() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialogFragment = RemoveGroupDialogFragment.newInstance(mActiveGroup, mGroupId);
        dialogFragment.show(getFragmentManager(), "RemoveGroupDialogFragment");
    }

    public void onViewProfile(View view){

        //intent to open profile activity with correct user
        // Passes a String with the user's email (ARTHUR)

        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("email123", match);
        startActivity(intent);

    }

    private class DownloadImageTask extends AsyncTask<Object, Integer, Drawable> {

        protected Drawable doInBackground(Object... args) {
            Drawable d = Utils.loadImageFromWeb((String) args[0]);
            return d;
        }

        protected void onPostExecute(Drawable result) {
            if(result != null && profileView != null) {
                profileView.setImageDrawable(result);
            }
        }
    }
}
