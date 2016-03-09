package cse110.giftX.ui.activeGroups;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.utils.Utils;

public class ActiveGroupAdapter extends ArrayAdapter<ActiveGroup> {//FirebaseListAdapter<ActiveGroup> {

    public ActiveGroupAdapter(Context context, ArrayList<ActiveGroup> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActiveGroup group = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_active_group, parent, false);
        }

        TextView textViewGroupName = (TextView) convertView.findViewById(R.id.text_view_group_name);
        TextView textViewManagedByUser = (TextView) convertView.findViewById(R.id.text_view_managed_by_user);
        //TODO add more fields-needs end date
        TextView textViewGroupDescription = (TextView) convertView.findViewById(R.id.text_view_group_description);
        TextView textViewStartDate = (TextView) convertView.findViewById(R.id.group_date_start);
        TextView textViewEndDate = (TextView) convertView.findViewById(R.id.group_date_end);

        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profile_pic);

        if(group != null) {
            //dates formatted as MM/dd/yyyy;w
            //1/7/2020
            String startDateInfo = group.getSortDate();
            String endDateInfo = group.getEndDate();
            String startTime = group.getSortTime();
            String endTime = group.getEndTime();
            Calendar sortCal = Utils.parseDate(startDateInfo, startTime);
            Calendar endCal = Utils.parseDate(endDateInfo, endTime);


            String imgURL = group.getManagerURL();
            new DownloadImageTask(profilePicture).execute(imgURL);

            //Drawable profile = Utils.loadImageFromWeb(imgURL);
            //if(profile != null) {
            //    profile.setVisible(true, true);
            //    profilePicture.setImageDrawable(profile);
            //}

            //int dayOfWeekStart = Integer.parseInt(startDateInfo.substring(startDateInfo.indexOf(';') + 1));
            //int dayOfWeekEnd = Integer.parseInt(endDateInfo.substring(endDateInfo.indexOf(';') + 1));
            //String dayStart = startDateInfo.substring(startDateInfo.indexOf('/') + 1, startDateInfo.indexOf('/', 3));
            //String dayEnd = endDateInfo.substring(endDateInfo.indexOf('/') + 1, endDateInfo.indexOf('/', 3));
            //int monthStart = Integer.parseInt(startDateInfo.substring(0, startDateInfo.indexOf('/')));
            //int monthEnd = Integer.parseInt(endDateInfo.substring(0, endDateInfo.indexOf('/')));
            //String startTime = group.getSortTime();
            //String endTime = group.getEndTime();

            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM");
            SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mmaa");
            String startTimeDisplay = Utils.getTime(formatter1.format(sortCal.getTime()));
            String endTimeDisplay = Utils.getTime(formatter1.format(endCal.getTime()));
            String startDateDisplay = formatter.format(sortCal.getTime()) + " " + startTimeDisplay;
            String endDateDisplay = formatter.format(endCal.getTime()) + " " + endTimeDisplay;

            //String startDateDisplay = Utils.getDayOfWeek(dayOfWeekStart) + ", "
            //    + dayStart + " " + Utils.getMonth(monthStart) + " " + Utils.getTime(startTime);
            //String endDateDisplay = Utils.getDayOfWeek(dayOfWeekEnd) + ", "
            //    + dayEnd + " " + Utils.getMonth(monthEnd) + " " + Utils.getTime(endTime);

            textViewGroupName.setText(group.getGroupName());
            textViewManagedByUser.setText(Utils.decodeEmail(group.getGroupManager()));
            textViewGroupDescription.setText(group.getGroupDescription());
            textViewStartDate.setText(startDateDisplay);
            textViewEndDate.setText(endDateDisplay);

        }
        return convertView;
    }

    private class DownloadImageTask extends AsyncTask<Object, Integer, Drawable> {

        ImageView imgView;
        public DownloadImageTask(ImageView i) {
            imgView = i;
        }

        protected Drawable doInBackground(Object... args) {
            Drawable d = Utils.loadImageFromWeb((String) args[0]);
            return d;
        }

        protected void onPostExecute(Drawable result) {
            if(result != null && imgView != null) {
                imgView.setImageDrawable(result);
            }
        }
    }

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
    /*
    public ActiveGroupAdapter(Activity activity, Class<ActiveGroup> modelClass, int modelLayout,
                              Query ref) {
        super(activity, modelClass, modelLayout, ref);
        this.mActivity = activity;
    }

    /**
     * Protected method that populates the view attached to the adapter (list_view_active_groups)
     * with items inflated from single_active_group.xml
     * populateView also handles data changes and updates the listView accordingly
     */

    /*
    @Override
    protected void populateView(View view, ActiveGroup group) {

        /**
         * Grab the needed TextViews and strings
         *
        TextView textViewGroupName = (TextView) view.findViewById(R.id.text_view_group_name);
        TextView textViewManagedByUser = (TextView) view.findViewById(R.id.text_view_managed_by_user);

        // Set the group name & owner
        textViewGroupName.setText(group.getGroupName());
        textViewManagedByUser.setText(group.getGroupManager());
    }*/

}
