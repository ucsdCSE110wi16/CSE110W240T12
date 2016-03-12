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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.model.User;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

public class ActiveGroupAdapter extends ArrayAdapter<ActiveGroup> {

    public ActiveGroupAdapter(Context context, ArrayList<ActiveGroup> groups) {
        super(context, 0, groups);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ActiveGroup group = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_active_group, parent, false);
        }

        final TextView textViewGroupName = (TextView) convertView.findViewById(R.id.text_view_group_name);
        final TextView textViewManagedByUser = (TextView) convertView.findViewById(R.id.text_view_managed_by_user);
        final TextView textViewGroupDescription = (TextView) convertView.findViewById(R.id.text_view_group_description);
        final TextView textViewStartDate = (TextView) convertView.findViewById(R.id.group_date_start);
        final TextView textViewEndDate = (TextView) convertView.findViewById(R.id.group_date_end);

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


            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM");
            SimpleDateFormat formatter1 = new SimpleDateFormat("hh:mmaa");
            String startTimeDisplay = Utils.getTime(formatter1.format(sortCal.getTime()));
            String endTimeDisplay = Utils.getTime(formatter1.format(endCal.getTime()));
            String startDateDisplay = formatter.format(sortCal.getTime()) + " " + startTimeDisplay;
            String endDateDisplay = formatter.format(endCal.getTime()) + " " + endTimeDisplay;

            textViewGroupName.setText(group.getGroupName());
            textViewGroupDescription.setText(group.getGroupDescription());
            textViewStartDate.setText(startDateDisplay);
            textViewEndDate.setText(endDateDisplay);

            // Call to match the info of the user with the given group's manager (email).
            Firebase ref = new Firebase(Constants.FIREBASE_URL_USERS).child(group.getGroupManager());

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);

                    textViewManagedByUser.setText(user.getUserName());
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

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

}
