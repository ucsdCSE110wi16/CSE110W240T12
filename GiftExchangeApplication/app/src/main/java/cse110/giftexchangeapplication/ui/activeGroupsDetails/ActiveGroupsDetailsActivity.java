package cse110.giftexchangeapplication.ui.activeGroupsDetails;

import android.os.Bundle;

import com.firebase.client.Firebase;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Represents the details screen for when selecting an active group
 */
public class ActiveGroupsDetailsActivity extends BaseActivity {
    private ActiveGroup mActiveGroup;
    private Firebase mActiveGroupRef;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_active_group_details);

        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUP);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen();
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    /**
     * Link layout elements from XML and setup the toolbar
     */
    private void initializeScreen() {

    }
}
