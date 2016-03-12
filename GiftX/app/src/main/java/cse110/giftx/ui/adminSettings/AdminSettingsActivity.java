package cse110.giftX.ui.adminSettings;

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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.activeGroups.EndDateFragment;
import cse110.giftX.ui.activeGroups.EndTimeFragment;
import cse110.giftX.ui.activeGroups.StartDateFragment;
import cse110.giftX.ui.activeGroups.StartTimeFragment;
import cse110.giftX.utils.Constants;

/**
 * Activity for changing settings of a group such as sort date, blacklist max, and group members
 */
public class AdminSettingsActivity extends BaseActivity {
    String groupID;

    //getting the Views
    EditText groupName;
    EditText descriptionText;
    EditText priceMin;
    EditText priceMax;
    TextView startDateButton;
    TextView endDateButton;
    TextView startTimeButton;
    TextView endTimeButton;

    Firebase groupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        setTitle("Edit group");

        Intent intent = getIntent();
        groupID = intent.getStringExtra(Constants.KEY_GROUP_ID);

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

        groupName = (EditText)findViewById(R.id.edit_text_group_title);
        descriptionText = (EditText)findViewById(R.id.edit_text_group_description);
        priceMin = (EditText) findViewById(R.id.edit_text_price_min);
        priceMax = (EditText) findViewById(R.id.edit_text_price_max);
        startDateButton = (TextView) findViewById(R.id.edit_text_start_date);
        endDateButton = (TextView) findViewById(R.id.edit_text_end_date);
        startTimeButton = (TextView) findViewById(R.id.edit_text_start_time);
        endTimeButton = (TextView) findViewById(R.id.edit_text_end_time);
        Button removeUsersButton = (Button) findViewById(R.id.button_remove_users_settings);
        removeUsersButton.setVisibility(View.VISIBLE);
        removeUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRemove = new Intent(getApplicationContext(), cse110.giftX.ui.adminSettings.AdminEditUsersActivity.class);
                intentRemove.putExtra(Constants.KEY_GROUP_ID, groupID);
                startActivity(intentRemove);
            }
        });

        groupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID);

        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ActiveGroup group = dataSnapshot.getValue(ActiveGroup.class);

                groupName.setText(group.getGroupName());
                descriptionText.setText(group.getGroupDescription());
                startDateButton.setText(group.getSortDate());
                endDateButton.setText(group.getEndDate());
                startTimeButton.setText(group.getSortTime());
                endTimeButton.setText(group.getEndTime());
                priceMin.setText(Double.toString(group.getPriceMin()));
                priceMax.setText(Double.toString(group.getPriceMax()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; This adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_admin_group_settings, menu);

        MenuItem createGroup = menu.findItem(R.id.action_save_settings);

        createGroup.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save_settings) {
            onSaveChangesPressed();

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

    public void onSaveChangesPressed() {
        //getting values from Views
        String title = groupName.getText().toString();
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
            HashMap<String, Object> timestampLastChanged = new HashMap<>();
            timestampLastChanged.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            groupRef.child("groupName").setValue(title);
            groupRef.child("groupDescription").setValue(description);
            groupRef.child("sortDate").setValue(startDate);
            groupRef.child("endDate").setValue(endDate);
            groupRef.child("sortTime").setValue(startTime);
            groupRef.child("endTime").setValue(endTime);
            groupRef.child("priceMin").setValue(min);
            groupRef.child("priceMax").setValue(max);
            groupRef.child("timestampLastChanged").setValue(timestampLastChanged);
        }
        finish();
    }

    // Show error toast to user
    private void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}