package cse110.giftX.ui.activeGroups;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

import cse110.giftX.R;

/**
 * Created by miguelvargas on 3/8/16.
 */
public class StartTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hourOfDay = c.get(c.HOUR_OF_DAY);
        int minute = c.get(c.MINUTE);

        return new TimePickerDialog(getActivity(), this, hourOfDay, minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Button startTime = (Button) getActivity().findViewById(R.id.edit_text_start_time);
        String AMPM = "AM";

        if(hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            AMPM = "PM";
        }

        startTime.setText(hourOfDay + ":" + minute + " " + AMPM);

    }
}
