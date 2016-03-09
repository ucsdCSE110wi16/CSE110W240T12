package cse110.giftexchangeapplication.ui.activeGroupsDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.utils.Utils;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {

    public UserAdapter(Context context, ArrayList<User> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user, parent, false);
        }

        TextView textViewUsersName = (TextView) convertView.findViewById(R.id.text_user_name);
        TextView textViewUsersEmail = (TextView) convertView.findViewById(R.id.text_view_user_email);

        if(user != null) {
            textViewUsersName.setText(user.getFirstName() + " " + user.getLastName());
            textViewUsersEmail.setText(Utils.decodeEmail(user.getEmail()));
        }
        return convertView;
    }
}
