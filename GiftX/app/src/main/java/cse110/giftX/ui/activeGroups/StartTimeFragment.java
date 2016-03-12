package cse110.giftX.ui.activeGroups;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
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

        TextView startTime = (TextView) getActivity().findViewById(R.id.edit_text_start_time);
        String AMPM = "AM";
        String min;

        if (minute < 10) {
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }
        if(hourOfDay > 12) {
            hourOfDay = hourOfDay - 12;
            AMPM = "PM";
        }

        Calendar date = Calendar.getInstance();
        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
        date.set(Calendar.MINUTE, minute);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        startTime.setText(sdf.format(date.getTime()) + " " + AMPM);

    }
}
