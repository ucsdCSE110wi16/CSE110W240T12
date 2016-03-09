package cse110.giftX.ui.activeGroups;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

//    Button startDate, endDate;
//    TextView date;
//    int year_x, month_x, day_x;
//    static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(MainActivity.USER_EMAIL);
        profileURL = intent.getStringExtra("profile_url");
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

    public void onCreateGroupPressed(View view) {
        //getting the Views
        EditText titleText = (EditText)findViewById(R.id.edit_text_group_title);
        EditText descriptionText = (EditText)findViewById(R.id.edit_text_group_description);
        Button startDateButton = (Button) findViewById(R.id.edit_text_start_date);
        Button endDateButton = (Button) findViewById(R.id.edit_text_end_date);
        Button startTimeButton = (Button) findViewById(R.id.edit_text_start_time);
        Button endTimeButton = (Button) findViewById(R.id.edit_text_end_time);
//        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
//        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
//        DatePicker datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
//        TimePicker timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

//        //getting values from Views
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String startDate = startDateButton.getText().toString();
        String endDate = endDateButton.getText().toString();
        String startTime = startTimeButton.getText().toString();
        String endTime = endTimeButton.getText().toString();

//        String startDate = "" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/"
//                + datePicker.getYear() + ";";
//        Calendar c = Calendar.getInstance();
//        c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
//        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
//        startDate += dayOfWeek;
//        String startTime;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            startTime = "" + timePicker.getHour() + ":" + timePicker.getMinute();
//        else
//            startTime = "" + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
//
//        String endDate = "" + datePicker1.getMonth() + "/" + datePicker1.getDayOfMonth() + "/"
//                + datePicker1.getYear() + ";";
//        Calendar cal = Calendar.getInstance();
//        cal.set(datePicker1.getYear(), datePicker1.getMonth(), datePicker1.getDayOfMonth());
//        int dayOfWeek1 = cal.get(Calendar.DAY_OF_WEEK);
//        endDate += dayOfWeek1;
//        String endTime;
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            endTime = "" + timePicker1.getHour() + ":" + timePicker1.getMinute();
//        else
//            endTime = "" + timePicker1.getCurrentHour() + ":" + timePicker1.getCurrentMinute();

        //creating group Pojo and pushing it to firebase
        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        ActiveGroup group = new ActiveGroup(title, description, startDate, endDate, startTime, endTime, userEmail, timestampCreated, profileURL);
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
