package cse110.giftX.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.ui.activeGroups.ActiveGroupsFragment;
import cse110.giftX.ui.activeGroups.CreateGroupActivity;
import cse110.giftX.ui.login.LoginActivity;
import cse110.giftX.ui.pendingGroups.PendingGroupsFragment;
import cse110.giftX.ui.userProfile.UserProfileActivity;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

public class MainActivity extends BaseActivity{

    public final static String USER_EMAIL = "edu.ucsd.cse110wi16.giftexchange.USER_EMAIL";
//    final Firebase ref = new Firebase(Constants.FIREBASE_URL);
//    Firebase userRef;
//    AuthData authData = ref.getAuth();
    String userEmail;
    String imageURL;
//    String userID;
//    User user;
//    ArrayList<String> groupIDs;
//    ArrayList<String> groupInvitations;
//    SectionPagerAdapter adapter;
//    boolean uiInitialized;
//

//    final Firebase mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

    final Firebase ref = new Firebase(Constants.FIREBASE_URL);

    private Firebase mUserRef;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mUserRefListener;

    ArrayList<String> groupIDs;
    ArrayList<String> groupInvitations;

    boolean uiInitialized;

    SectionPagerAdapter sectionPagerAdapter;

    AuthData authData = ref.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(authData == null)
        {
            startLoginActivity();
            finish();
        }

        else {
            userEmail = Utils.encodeEmail(authData.getProviderData().get("email").toString());
            mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail);

            //profile pictures
            imageURL = authData.getProviderData().get("profileImageURL").toString();
            mUserRef.child("profileURL").setValue(imageURL);

            uiInitialized = false;

            groupIDs = new ArrayList<>();
            groupInvitations = new ArrayList<>();

            /**
             * Link layout elements from XML and setup the toolbar
             */
            initializeScreen();

            mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    // Assumes that the first word in the user's name is the first name.
                    String firstName = user.getUserName().split("\\s+")[0];
                    String title = "Hello there, " + firstName;
                    setTitle(title);
                    groupIDs = new ArrayList<String>(); //needed
                    for (String gID: user.getGroups().keySet()) {
                        if(!gID.equals("dummyGroup")) {
                            groupIDs.add(gID);
                        }
                    }
                    groupInvitations = new ArrayList<String>(); //needed
                    for (String invite: user.getInvitations().keySet()) {
                        if( !invite.equals("dummyInvitation")) {
                            groupInvitations.add(invite);
                        }
                    }

                    if (!uiInitialized) {
                        initializeScreen();
                        uiInitialized = true;
                    }

                    else {
                        sectionPagerAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.e(LOG_TAG,
                            getString(R.string.log_error_the_read_failed) +
                                    firebaseError.getMessage());
                }
            });
        }
    }

    /**
     * Override onOptionsItemSelected to use main_menu instead of BaseActivity menu
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; This adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem userProfile = menu.findItem(R.id.action_user_profile);

        // Login menu item
        MenuItem logout = menu.findItem(R.id.action_logout);

        // Set the visibility
        logout.setVisible(true);
        userProfile.setVisible(true);

        return true;
    }

    /**
     * Override onOptionsItemSelected to add action_setting only to the MainActivity
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /**
         * logout when logout action is selected
         */
        if (id == R.id.action_logout) {
            ref.unauth();
            startLoginActivity();

            return true;
        }
        else if (id == R.id.action_user_profile) {
            startUserProfileActivity();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("email123", userEmail);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mUserRef.removeEventListener(mUserRefListener);
    }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        /**
         * Create SectionPagerAdapter, set is as adapter to viewPager with setOffscreenPageLimt(2)
         */
        sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(sectionPagerAdapter);

        /**
         * Setup mTabLayout with view pager
         */
        tabLayout.setupWithViewPager(viewPager);

    }

    /**
     * Create an instance of the AddList dialog fragment and show it
     */
    public void showAddGroupDialog(View view) {
        // Create an instance of the dialog fragment and show it
//        DialogFragment dialog = AddGroupDialogFragment.newInstance(mEncodedEmail);
//        dialog.show(MainActivity.this.getFragmentManager(), "AddGroupDialogFragment");

        //dialog.show(MainActivity.this.getFragmentManager(), "AddGroupDialogFragment");
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra(USER_EMAIL, userEmail);
        intent.putExtra("profile_url", imageURL);
        startActivity(intent);
    }

    /**
     * SectionPagerAdapter class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class SectionPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager fragmentManager;

        public SectionPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        /**
         * Use positions (0 and 1) to find and instantiate fragments with newInstance()
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            /**
             * Set fragment to different fragments depending on position in ViewPage
             */
            switch (position) {
                case 0:
                    fragment = ActiveGroupsFragment.newInstance(userEmail, groupIDs);
                    break;
                case 1:
                    fragment = PendingGroupsFragment.newInstance(userEmail, groupInvitations);
                    break;
                default:
                    fragment = ActiveGroupsFragment.newInstance(userEmail, groupIDs);
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.pager_title_active_groups);
                case 1:
                default:
                    return getString(R.string.pager_title_pending_groups);
            }
        }

        @Override
        public int getItemPosition(Object object) {
            if(fragmentManager.getFragments().contains(object))
                return POSITION_NONE;
            else
                return POSITION_UNCHANGED;
        }
    }

    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
