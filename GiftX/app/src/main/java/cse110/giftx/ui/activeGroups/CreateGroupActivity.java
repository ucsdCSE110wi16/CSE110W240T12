package cse110.giftX.ui.activeGroups;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.MainActivity;
import cse110.giftX.utils.Constants;

public class CreateGroupActivity extends BaseActivity {

    String userEmail;
    String profileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        Button buttonRemoveUsers = (Button)findViewById(R.id.button_remove_users_settings);
        buttonRemoveUsers.setVisibility(View.GONE);

        setTitle("Create group");

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(MainActivity.USER_EMAIL);
        profileURL = intent.getStringExtra("profile_url");

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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
        EditText titleText = (EditText)findViewById(R.id.edit_text_group_title);
        EditText descriptionText = (EditText)findViewById(R.id.edit_text_group_description);
        EditText priceMin = (EditText) findViewById(R.id.edit_text_price_min);
        EditText priceMax = (EditText) findViewById(R.id.edit_text_price_max);
        TextView startDateButton = (TextView) findViewById(R.id.edit_text_start_date);
        TextView endDateButton = (TextView) findViewById(R.id.edit_text_end_date);
        TextView startTimeButton = (TextView) findViewById(R.id.edit_text_start_time);
        TextView endTimeButton = (TextView) findViewById(R.id.edit_text_end_time);

        //getting values from Views
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String startDate = startDateButton.getText().toString();
        String endDate = endDateButton.getText().toString();
        String startTime = startTimeButton.getText().toString();
        String endTime = endTimeButton.getText().toString();
        Double min = Double.parseDouble(priceMin.getText().toString());
        Double max = Double.parseDouble(priceMax.getText().toString());

        if(title.isEmpty() || description.isEmpty()  || startDate.isEmpty()  ||
                endDate.isEmpty()  || startTime.isEmpty()  || endTime.isEmpty() ){
            showErrorToast("Please fill in all fields");
            return;

        }
        if(max < min) {
            Toast.makeText(getApplicationContext(), "Max price must be greater than min price!", Toast.LENGTH_LONG).show();
        } else {
            //creating group Pojo and pushing it to firebase
            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
            ActiveGroup group = new ActiveGroup(title, description, startDate, endDate, startTime, endTime, userEmail, min, max, timestampCreated, profileURL);
            Firebase groupLocation = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS);
            groupLocation = groupLocation.push();
            groupLocation.setValue(group);
            String groupID = groupLocation.getKey().toString();
            groupLocation.child("groupID").setValue(groupID);

            //adding the group to the user
            Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS + "/" + userEmail + "/groups/" + groupID);
            userLocation.setValue(true);
            finish();
        }
    }

    // Show error toast to user
    private void showErrorToast(String message) {
        Toast.makeText(CreateGroupActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
