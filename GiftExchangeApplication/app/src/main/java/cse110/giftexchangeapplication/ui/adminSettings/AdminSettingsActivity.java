package cse110.giftexchangeapplication.ui.adminSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.ArrayList;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.ui.MainActivity;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Activity for changing settings of a group such as sort date, blacklist max, and group members
 */
public class AdminSettingsActivity extends BaseActivity {
    private Firebase activeGroupRef;
    private Firebase mActiveGroupUsersRef;
    private String userEmail;
    private String userID;
    private String groupID;
    private ActiveGroup activeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        /* Get data  from from previous activity */
        Intent intent = getIntent();
        groupID = intent.getStringExtra(Constants.KEY_GROUP_ID);
        userEmail = intent.getStringExtra("email");
        if (groupID == null) {
            /* End if there is no valid group */
            finish();
            return;
        }
        activeGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID);


        Button buttonEditUsers = (Button)findViewById(R.id.button_edit_participants);
        buttonEditUsers.setOnClickListener(onclick);
    }
    View.OnClickListener onclick= new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.KEY_GROUP_ID, groupID);
                    bundle.putString("email", userEmail);
                    AdminEditUsersDialogFragment editUsersDialogFragment = new AdminEditUsersDialogFragment();
                    editUsersDialogFragment.setArguments(bundle);
                    editUsersDialogFragment.show(getFragmentManager(), "dialog");
                }
    };

}