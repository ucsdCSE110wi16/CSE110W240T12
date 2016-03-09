package cse110.giftx.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import cse110.giftx.R;
import cse110.giftx.model.User;
import cse110.giftx.ui.activeGroups.ActiveGroupsFragment;
import cse110.giftx.ui.activeGroups.CreateGroupActivity;
import cse110.giftx.ui.login.LoginActivity;
import cse110.giftx.ui.pendingGroups.PendingGroupsFragment;
import cse110.giftx.ui.userProfile.UserProfileActivity;
import cse110.giftx.utils.Constants;
import cse110.giftx.utils.Utils;

public class MainActivity extends BaseActivity{

    public final static String USER_EMAIL = "edu.ucsd.cse110wi16.giftexchange.USER_EMAIL";
    final Firebase ref = new Firebase(Constants.FIREBASE_URL);
    Firebase userRef;
    AuthData authData = ref.getAuth();
    String userEmail;
    String userID;
    User user;
    ArrayList<String> groupIDs;
    ArrayList<String> groupInvitations;
    SectionPagerAdapter adapter;
    boolean uiInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(authData == null)
        {
            startLoginActivity();
            finish();
        }
        else
        {
            //michael
            //getting the userEmail from the userID
            //possibly not needed if we can just save the email that the user enters at login
            uiInitialized = false;
            groupIDs = new ArrayList<String>();
            groupInvitations = new ArrayList<String>();
            userID = authData.getUid().toString();
            userEmail = Utils.encodeEmail(authData.getProviderData().get("email").toString());

            //USER Listener
            userRef = ref.child(Constants.FIREBASE_LOCATION_USERS).child(userEmail);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //whenever user changes, update arraylist, then initialize screen
                    user = dataSnapshot.getValue(User.class);
                    groupIDs = new ArrayList<String>();
                    for(String gID: user.getGroups().keySet()) {
                        if(!gID.equals("dummyGroup")) {
                            groupIDs.add(gID);
                        }
                    }
                    groupInvitations = new ArrayList<String>();
                    for(String invite: user.getInvitations().keySet()) {
                        if(!invite.equals("dummyInvite")) {
                            groupInvitations.add(invite);
                        }
                    }
                    if(!uiInitialized) {
                        initializeScreen();
                        uiInitialized = true;
                    }
                    else {
                        adapter.notifyDataSetChanged();
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                }
            });

        }
//        Button mEmailSignInButton = (Button) findViewById(R.id.logout_button);
//        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ref.unauth();
//                startLoginActivity();
//            }
//        });
        //initializeScreen();
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
        adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter); //TODO

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

        //michael - replacing dialog with activity, but probably shouldnt open directly to new Activity
        //DialogFragment dialog = AddGroupDialogFragment.newInstance();
        //dialog.show(MainActivity.this.getFragmentManager(), "AddGroupDialogFragment");
        Intent intent = new Intent(this, CreateGroupActivity.class);
        intent.putExtra(USER_EMAIL, userEmail);
        startActivity(intent);
    }

    /**
     * SectionPagerAdapter class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class SectionPagerAdapter extends FragmentStatePagerAdapter {
        FragmentManager frgm;

        public SectionPagerAdapter(FragmentManager fm) { super(fm); frgm = fm;}

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

            //michael - passed in userEmail to newInstances and added userEmail variable to each fragment class
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

        //michael1
        @Override
        public int getItemPosition(Object object) {
            if(frgm.getFragments().contains(object))
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
