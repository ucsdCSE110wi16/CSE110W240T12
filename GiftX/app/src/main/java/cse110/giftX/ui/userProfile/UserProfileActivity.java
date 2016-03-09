package cse110.giftX.ui.userProfile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.utils.Constants;

/**
 * Created by AJ on 2/29/16.
 */
public class UserProfileActivity extends BaseActivity {
    private static final String ARG_PARAM1 = "email123";
    private String email;
    Firebase mUserRef;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        email = b.getString(ARG_PARAM1);

        setContentView(R.layout.activity_user_profile);

        mUserRef = new Firebase(Constants.FIREBASE_URL_USERS).child(email);

        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                String name  = user.getUserName();

                userName = (TextView) findViewById(R.id.text_view_user_name_profile);

                userName.setText(name);


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        setTitle(R.string.user_profile_title);

        // Link layout elements
        initializeScreen();

    }

    /**
     * Override onOptionsItemSelected to use main_menu instead of BaseActivity menu
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; This adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);

        // Login menu item
        MenuItem logout = menu.findItem(R.id.action_logout);

        // Set the visibility
        logout.setVisible(false);

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
            // TODO: fill in

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeScreen() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        /**
         * Create SectionPagerAdapter, set is as adapter to viewPager with setOffscreenPageLimt(2)
         */
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

        /**
         * Setup mTabLayout with view pager
         */
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {

            switch (i) {
                case 1:
                    tabLayout.getTabAt(1).setIcon(R.drawable.likes);
                    break;
                case 2:
                    tabLayout.getTabAt(2).setIcon(R.drawable.dislikes);
                    break;
                case 0:
                default:
                    tabLayout.getTabAt(0).setIcon(R.drawable.aboutme);
                    break;
            }

        }
    }

    /**
     * SectionPagerAdapter class that extends FragmentStatePagerAdapter to save fragments state
     */
    public class SectionPagerAdapter extends FragmentPagerAdapter {

        public SectionPagerAdapter(FragmentManager fm) { super(fm); }
        Firebase ref = new Firebase(Constants.FIREBASE_URL);

        /**
         * Use positions (0 and 1) to find and instantiate fragments with newInstance()
         *
         * @param position
         */
        @Override
        public Fragment getItem(int position) {
            return AboutFragment.newInstance(email, position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.pager_title_about_me);
                case 1:
                    return getString(R.string.pager_title_likes);
                case 2:
                    return getString(R.string.pager_title_dislikes);
                default:
                    return getString(R.string.pager_title_about_me);
            }
        }
    }

}