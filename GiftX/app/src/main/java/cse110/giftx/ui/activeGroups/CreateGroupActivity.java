package cse110.giftx.ui.activeGroups;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.Calendar;
import java.util.HashMap;

import cse110.giftx.R;
import cse110.giftx.model.ActiveGroup;
import cse110.giftx.ui.BaseActivity;
import cse110.giftx.ui.MainActivity;
import cse110.giftx.utils.Constants;

public class CreateGroupActivity extends BaseActivity {

    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Intent intent = getIntent();
        userEmail = intent.getStringExtra(MainActivity.USER_EMAIL);
    }

    public void onCreateGroupPressed(View view) {
        //getting the Views
        EditText titleText = (EditText)findViewById(R.id.edit_text_group_title);
        EditText descriptionText = (EditText)findViewById(R.id.edit_text_group_description);
        DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        DatePicker datePicker1 = (DatePicker) findViewById(R.id.datePicker1);
        TimePicker timePicker1 = (TimePicker) findViewById(R.id.timePicker1);

        //getting values from Views
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String startDate = "" + datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/"
                + datePicker.getYear() + ";";
        Calendar c = Calendar.getInstance();
        c.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        startDate += dayOfWeek;
        String startTime;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            startTime = "" + timePicker.getHour() + ":" + timePicker.getMinute();
        else
            startTime = "" + timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();

        String endDate = "" + datePicker1.getMonth() + "/" + datePicker1.getDayOfMonth() + "/"
                + datePicker1.getYear() + ";";
        Calendar cal = Calendar.getInstance();
        cal.set(datePicker1.getYear(), datePicker1.getMonth(), datePicker1.getDayOfMonth());
        int dayOfWeek1 = cal.get(Calendar.DAY_OF_WEEK);
        endDate += dayOfWeek1;
        String endTime;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            endTime = "" + timePicker1.getHour() + ":" + timePicker1.getMinute();
        else
            endTime = "" + timePicker1.getCurrentHour() + ":" + timePicker1.getCurrentMinute();

        //creating group Pojo and pushing it to firebase
        HashMap<String, Object> timestampCreated = new HashMap<>();
        timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);
        ActiveGroup group = new ActiveGroup(title, description, startDate, endDate, startTime, endTime, userEmail, timestampCreated);
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
