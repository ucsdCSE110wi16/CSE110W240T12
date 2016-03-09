package cse110.giftx.ui.activeGroups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cse110.giftx.R;
import cse110.giftx.model.ActiveGroup;
import cse110.giftx.utils.Utils;

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


        if(group != null) {
            //dates formatted as MM/dd/yyyy;w
            //1/7/2020
            String startDateInfo = group.getSortDate();
            String endDateInfo = group.getEndDate();
            int dayOfWeekStart = Integer.parseInt(startDateInfo.substring(startDateInfo.indexOf(';') + 1));
            int dayOfWeekEnd = Integer.parseInt(endDateInfo.substring(endDateInfo.indexOf(';') + 1));
            String dayStart = startDateInfo.substring(startDateInfo.indexOf('/') + 1, startDateInfo.indexOf('/', 3));
            String dayEnd = endDateInfo.substring(endDateInfo.indexOf('/') + 1, endDateInfo.indexOf('/', 3));
            int monthStart = Integer.parseInt(startDateInfo.substring(0, startDateInfo.indexOf('/')));
            int monthEnd = Integer.parseInt(endDateInfo.substring(0, endDateInfo.indexOf('/')));
            String startTime = group.getSortTime();
            String endTime = group.getEndTime();
            String startDateDisplay = Utils.getDayOfWeek(dayOfWeekStart) + ", "
                + dayStart + " " + Utils.getMonth(monthStart) + " " + Utils.getTime(startTime);
            String endDateDisplay = Utils.getDayOfWeek(dayOfWeekEnd) + ", "
                + dayEnd + " " + Utils.getMonth(monthEnd) + " " + Utils.getTime(endTime);

            textViewGroupName.setText(group.getGroupName());
            textViewManagedByUser.setText(Utils.decodeEmail(group.getGroupManager()));
            textViewGroupDescription.setText(group.getGroupDescription());
            textViewStartDate.setText(startDateDisplay);
            textViewEndDate.setText(endDateDisplay);

        }
        return convertView;
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
