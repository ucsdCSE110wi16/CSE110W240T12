package cse110.giftexchangeapplication.ui.adminSettings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.BaseActivity;

/**
 * Activity for changing settings of a group such as sort date, blacklist max, and group members
 */
public class AdminSettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        Button buttonEditUsers = (Button)findViewById(R.id.button_edit_participants);
        buttonEditUsers.setOnClickListener(onclick);
    }
    View.OnClickListener onclick= new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    AdminEditUsersDialogFragment editUsersDialogFragment = new AdminEditUsersDialogFragment();
                    editUsersDialogFragment.show(getFragmentManager(), "dialog");
                }
            };

}