package cse110.giftexchangeapplication.ui;


import android.app.DialogFragment;
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

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.ui.activeGroups.ActiveGroupsFragment;
import cse110.giftexchangeapplication.ui.activeGroups.AddGroupDialogFragment;
import cse110.giftexchangeapplication.ui.login.LoginActivity;
import cse110.giftexchangeapplication.ui.pendingGroups.PendingGroupsFragment;
import cse110.giftexchangeapplication.ui.userProfile.UserProfileActivity;
import cse110.giftexchangeapplication.utils.Constants;

public class MainActivity extends BaseActivity{
    private Firebase mUserRef;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private ValueEventListener mUserRefListener;

//    final Firebase ref = new Firebase(Constants.FIREBASE_URL);
//    AuthData authData = ref.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Firebase ref = new Firebase(Constants.FIREBASE_URL);
        AuthData authData = ref.getAuth();

        if(authData == null)
        {
            startLoginActivity();
        }

        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(mEncodedEmail);

        /**
         * Link layout elements from XML and setup the toolbar
         */



//        else
//        {
//            initializeScreen();
//        }
//        Button mEmailSignInButton = (Button) findViewById(R.id.logout_button);
//        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ref.unauth();
//                startLoginActivity();
//            }
//        });

        initializeScreen();

        mUserRefListener = mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Set the activity's title to current user name if user is not null
                if (user != null) {
                    // Assumes that the first word in the user's name is the first name.
                    String firstName = user.getName().split("\\s+")[0];
                    String title = "Hello there, " + firstName;
                    setTitle(title);
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
//        int id = item.getItemId();

//        /**
//         * logout when logout action is selected
//         */
//        if (id == R.id.action_logout) {
////            ref.unauth();
//            startLoginActivity();
//
//            return true;
//        }
//
//        else if (id == R.id.action_user_profile) {
//            startUserProfileActivity();
//
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void startUserProfileActivity() {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUserRef.removeEventListener(mUserRefListener);
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
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(adapter);

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
        DialogFragment dialog = AddGroupDialogFragment.newInstance(mEncodedEmail);
        dialog.show(MainActivity.this.getFragmentManager(), "AddGroupDialogFragment");
    }

    /**
     * SectionPagerAdapter class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class SectionPagerAdapter extends FragmentStatePagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) { super(fm); }

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
                    fragment = ActiveGroupsFragment.newInstance();
                    break;
                case 1:
                    fragment = PendingGroupsFragment.newInstance();
                    break;
                default:
                    fragment = ActiveGroupsFragment.newInstance();
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
    }

    public void startLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}
