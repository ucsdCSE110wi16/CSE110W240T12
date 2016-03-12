package cse110.giftX.ui.pendingGroups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.model.User;
import cse110.giftX.utils.Constants;
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
        final TextView textViewInvitedBy = (TextView) convertView.findViewById(R.id.text_view_inviter);
        final TextView invitedByEmail = (TextView) convertView.findViewById(R.id.text_view_email_invite);

        if(group != null) {
            textViewGroupTitle.setText(group.getGroupName());
            invitedByEmail.setText(Utils.decodeEmail(group.getGroupManager()));

            Firebase groupRef = new Firebase(Constants.FIREBASE_URL_USERS).child(group.getGroupManager());

            groupRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    textViewInvitedBy.setText(user.getUserName());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        return convertView;
    }
}
