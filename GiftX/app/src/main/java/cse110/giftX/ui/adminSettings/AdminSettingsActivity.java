package cse110.giftX.ui.adminSettings;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.Calendar;
import java.util.HashMap;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.activeGroups.CreateGroupActivity;
import cse110.giftX.ui.activeGroups.EndDateFragment;
import cse110.giftX.ui.activeGroups.EndTimeFragment;
import cse110.giftX.ui.activeGroups.StartDateFragment;
import cse110.giftX.ui.activeGroups.StartTimeFragment;
import cse110.giftX.utils.Constants;

/**
 * Activity for changing settings of a group such as sort date, blacklist max, and group members
 */
public class AdminSettingsActivity extends BaseActivity {
    private Firebase activeGroupRef;
    private String userEmail;
    private String userID;
    private String groupID;
    private ActiveGroup activeGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_settings);

        /* Get data  from from previous activity */
        Intent intent = getIntent();
        groupID = intent.getStringExtra(Constants.KEY_GROUP_ID);
        userEmail = intent.getStringExtra("email");
        if (groupID == null) {
            /* End if there is no valid group */
            finish();
            return;
        }
        // Make a reference to the group
        activeGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID);

        // Set the button for removing users
        Button buttonEditUsers = (Button)findViewById(R.id.button_remove_users_settings);
        buttonEditUsers.setOnClickListener(onRemoveClick);

        initializeScreen();
    }

    private void initializeScreen() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_group_bar);
        // Common toolbar setup
        setSupportActionBar(toolbar);

        // Add back button to the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; This adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_create_group, menu);

        MenuItem createGroup = menu.findItem(R.id.action_create_group);

        createGroup.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create_group) {
            onCreateGroupPressed();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStartDateClicked(View view) {
        DialogFragment dialogFragment = new StartDateFragment();
        dialogFragment.show(getFragmentManager(), "Date Picker");
    }

    public void onEndDateClicked(View view) {
        DialogFragment dialogFragment = new EndDateFragment();
        dialogFragment.show(getFragmentManager(), "Date Picker");
    }

    public void onStartTimeClicked(View view) {
        DialogFragment dialogFragment = new StartTimeFragment();
        dialogFragment.show(getFragmentManager(), "Time Picker");
    }

    public void onEndTimeClicked(View view) {
        DialogFragment dialogFragment = new EndTimeFragment();
        dialogFragment.show(getFragmentManager(), "Time Picker");
    }

    public void onCreateGroupPressed() {
        //getting the Views
        EditText titleText = (EditText) findViewById(R.id.edit_text_group_title_settings);
        EditText descriptionText = (EditText) findViewById(R.id.edit_text_group_description_settings);
        EditText priceMin = (EditText) findViewById(R.id.edit_text_price_min_settings);
        EditText priceMax = (EditText) findViewById(R.id.edit_text_price_max_settings);
        Button startDateButton = (Button) findViewById(R.id.edit_text_start_date);
        Button endDateButton = (Button) findViewById(R.id.edit_text_end_date);
        Button startTimeButton = (Button) findViewById(R.id.edit_text_start_time);
        Button endTimeButton = (Button) findViewById(R.id.edit_text_end_time);


        //getting values from Views
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String startDate = startDateButton.getText().toString();
        String endDate = endDateButton.getText().toString();
        String startTime = startTimeButton.getText().toString();
        String endTime = endTimeButton.getText().toString();
        Double min = 0.00;
        Double max = 0.00;
        if(!priceMax.getText().toString().equals("")) {
            try {
                max = Double.parseDouble(priceMax.getText().toString());
                activeGroupRef.child("priceMax").setValue(max);
            }
            catch (NumberFormatException e) {
            }
        }
        if(!priceMin.getText().toString().equals("")) {
            try {
                min = Double.parseDouble(priceMin.getText().toString());
                activeGroupRef.child("priceMin").setValue(min);
            }
            catch (NumberFormatException e) {
            }
        }


        if (title.isEmpty() || description.isEmpty() || startDate.isEmpty() ||
                endDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            showErrorToast("Please fill in all fields");
            return;

        }
        if (max < min) {
            Toast.makeText(getApplicationContext(), "Max price must be greater than min price!", Toast.LENGTH_LONG).show();
        } else {
            //creating group Pojo and pushing it to firebase
            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            activeGroupRef.child("priceMin").setValue(min);
            activeGroupRef.child("endDate").setValue(endDate);
            activeGroupRef.child("endTime").setValue(endTime);
            activeGroupRef.child("groupDescription").setValue(description);
            activeGroupRef.child("groupName").setValue(title);
            activeGroupRef.child("sortDate").setValue(startDate);
            activeGroupRef.child("sortTime").setValue(startTime);
        }

        finish();
    }

    // Show error toast to user
    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    // Set the listener for the "Remove Users" button
    View.OnClickListener onRemoveClick= new View.OnClickListener(){
        @Override
        public void onClick(View view){
            // Set up the new activity and display it
            Intent intent = new Intent(getApplicationContext(), cse110.giftX.ui.adminSettings.AdminEditUsersActivity.class);
            intent.putExtra(Constants.KEY_GROUP_ID, groupID);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        }
    };


}