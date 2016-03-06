package cse110.giftexchangeapplication.ui.activeGroups;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.utils.Constants;

public class ActiveGroupAdapter extends FirebaseListAdapter<ActiveGroup> {

    /**
     * Public constructor that initializes private instance variables when adapter is created
     */
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
    @Override
    protected void populateView(View view, ActiveGroup group) {

        /**
         * Grab the needed TextViews and strings
         */
        final TextView textViewGroupName = (TextView) view.findViewById(R.id.text_view_group_name);
        final TextView textViewManagedByUser = (TextView) view.findViewById(R.id.text_view_managed_by_user);

        Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS).child(group.getManager());
        ValueEventListener refListener = ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                textViewManagedByUser.setText(user.getName());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        // Set the group name & owner
        textViewGroupName.setText(group.getGroupName());
    }
}
