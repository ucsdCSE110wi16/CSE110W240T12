package cse110.giftexchangeapplication.ui;


import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.activeGroups.ActiveGroupsFragment;
import cse110.giftexchangeapplication.ui.activeGroups.AddGroupDialogFragment;

public class MainActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Link layout elements from XML and setup the toolbar
         */
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    public void initializeScreen() {
        // Setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ActiveGroupsFragment fragment = new ActiveGroupsFragment();
        fragmentTransaction.add(R.id.active_groups, fragment);
        fragmentTransaction.commit();

    }

    /**
     * Create an instance of the AddList dialog fragment and show it
     */
    public void showAddGroupDialog(View view) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = AddGroupDialogFragment.newInstance();
        dialog.show(MainActivity.this.getFragmentManager(), "AddGroupDialogFragment");
    }
}
