package cse110.giftexchangeapplication.ui.activeGroups;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.utils.Utils;

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

        if(group != null) {
            textViewGroupName.setText(group.getGroupName());
            textViewManagedByUser.setText(Utils.decodeEmail(group.getGroupManager()));
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
