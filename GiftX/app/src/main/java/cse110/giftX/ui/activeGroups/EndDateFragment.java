package cse110.giftX.ui.activeGroups;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import cse110.giftX.R;

/**
 * Created by miguelvargas on 3/8/16.
 */
public class EndDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Button button = (Button) getActivity().findViewById(R.id.edit_text_end_date);

        Calendar date = Calendar.getInstance();

        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        button.setText(sdf.format(date.getTime()));
    }
}
