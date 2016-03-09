package cse110.giftx.ui.activeGroupsDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

import cse110.giftx.R;
import cse110.giftx.model.User;
import cse110.giftx.utils.Constants;
import cse110.giftx.utils.Utils;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Firebase ref = new Firebase(Constants.FIREBASE_URL);
    private String userEmail = Utils.encodeEmail(ref.getAuth().getProviderData().get("email").toString());
    private String groupID;

    public UserAdapter(Context context, ArrayList<User> groups, String groupId) {
        super(context, 0, groups);
        groupID = groupId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user, parent, false);
        }

        TextView textViewUsersName = (TextView) convertView.findViewById(R.id.text_user_name);
        TextView textViewUsersEmail = (TextView) convertView.findViewById(R.id.text_view_user_email);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        if(user != null) {
            String clickedEmail = user.getEmail();


            checkBox.setTag(clickedEmail);
            if(userEmail.equals(clickedEmail)) {
                checkBox.setVisibility(View.INVISIBLE);
            }
            else {
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox box = (CheckBox) v;
                        if (box.isChecked()) {
                            ref.child(Constants.FIREBASE_LOCATION_ACTIVE_GROUPS).child(groupID).child("users")
                                    .child(userEmail).child((String)v.getTag()).setValue(true);
                        } else {
                            ref.child(Constants.FIREBASE_LOCATION_ACTIVE_GROUPS).child(groupID).child("users")
                                    .child(userEmail).child((String)v.getTag()).setValue(null);
                        }
                    }
                });
                checkBox.setVisibility(View.VISIBLE);
            }
            textViewUsersName.setText(user.getFirstName() + " " + user.getLastName());
            textViewUsersEmail.setText(Utils.decodeEmail(user.getEmail()));

        }
        return convertView;
    }
}
