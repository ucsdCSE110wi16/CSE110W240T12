package cse110.giftX.ui.pendingGroups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.utils.Utils;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class InvitesAdapter extends ArrayAdapter<ActiveGroup> {

    public InvitesAdapter(Context context, ArrayList<ActiveGroup> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActiveGroup group = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_invite, parent, false);
        }

        TextView textViewGroupTitle = (TextView) convertView.findViewById(R.id.invite_text_view_group_title);
        TextView textViewInvitedBy = (TextView) convertView.findViewById(R.id.invited_by);

        if(group != null) {
            textViewGroupTitle.setText(group.getGroupName());
            textViewInvitedBy.setText(Utils.decodeEmail(group.getGroupManager()));
        }
        return convertView;
    }
}
