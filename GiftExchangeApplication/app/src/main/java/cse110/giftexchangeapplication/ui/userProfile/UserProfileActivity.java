package cse110.giftexchangeapplication.ui.userProfile;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.firebase.client.Firebase;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.ui.activeGroups.ActiveGroupsFragment;
import cse110.giftexchangeapplication.ui.pendingGroups.PendingGroupsFragment;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Created by AJ on 2/29/16.
 */
public class UserProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        logout.setVisible(true);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

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
                    fragment = AboutMeFragment.newInstance();
                    break;
                case 1:
                    fragment = AboutMeFragment.newInstance();
                    break;
                case 2:
                    fragment = AboutMeFragment.newInstance();
                    break;
                default:
                    fragment = AboutMeFragment.newInstance();
                    break;
            }
            return fragment;
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
