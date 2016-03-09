package cse110.giftexchangeapplication.ui.adminSettings;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.User;

/**
 * Created by Kyle on 3/7/2016.
 */
public class AdminEditUsersAdapter extends ArrayAdapter<User> {
    private List<User> userRemoveList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public AdminEditUsersAdapter(Context context, ArrayList<User> usersList) {
        super(context, 0, usersList);
        layoutInflater = LayoutInflater.from(context);
    }
    public List<User> getUserRemoveList(){
        return userRemoveList;
    }

    @Override
    public View getView(final int position,View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.single_group_member, viewGroup, false);
        }
        final User user = getItem(position);
        String userFullName = user.getFirstName() + " " + user.getLastName();
        TextView textViewName = (TextView)view.findViewById(R.id.text_view_member_name);
        textViewName.setText(userFullName);
        final CheckBox checkBoxEditUsers = (CheckBox)view.findViewById(R.id.check_box_select_user);
        final View finalView = view;
        checkBoxEditUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxEditUsers.isChecked()) {
                    finalView.setBackgroundColor(Color.RED);
                    ListView listViewEditUsersRef = (ListView) viewGroup;
                    userRemoveList.add((User) listViewEditUsersRef.getItemAtPosition(position));
                } else {
                    finalView.setBackgroundColor(Color.WHITE);
                    ListView listViewEditUsersRef = (ListView) viewGroup;
                    userRemoveList.remove(listViewEditUsersRef.getItemAtPosition(position));
                }
            }
        });


        return view;
    }

}