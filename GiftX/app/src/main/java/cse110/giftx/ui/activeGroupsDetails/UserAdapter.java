package cse110.giftX.ui.activeGroupsDetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Set;

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

/**
 * Created by Michael Khorram on 3/2/2016.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private Firebase ref = new Firebase(Constants.FIREBASE_URL);
    private String userEmail = Utils.encodeEmail(ref.getAuth().getProviderData().get("email").toString());
    private String groupID;
    private Set<String> blackList;

    public UserAdapter(Context context, ArrayList<User> groups, String groupId, Set<String> bl) {
        super(context, 0, groups);
        groupID = groupId;
        blackList = bl;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user, parent, false);
        }

        TextView textViewUsersName = (TextView) convertView.findViewById(R.id.text_user_name);
        TextView textViewUsersEmail = (TextView) convertView.findViewById(R.id.text_view_user_email);

        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profile_pic_single);

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

        if(user != null) {
            String imgURL = user.getProfileURL();
            new DownloadImageTask(profilePicture).execute(imgURL);

            String clickedEmail = user.getEmail();

            checkBox.setTag(clickedEmail);

            if(blackList.contains(clickedEmail)) {
                checkBox.setChecked(true);
            }
            else {
                checkBox.setChecked(false);
            }

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
                            blackList.add((String) v.getTag());
                        } else {
                            ref.child(Constants.FIREBASE_LOCATION_ACTIVE_GROUPS).child(groupID).child("users")
                                    .child(userEmail).child((String)v.getTag()).setValue(null);
                            blackList.remove((String) v.getTag());
                        }
                    }
                });
                checkBox.setVisibility(View.VISIBLE);
            }
            textViewUsersName.setText(user.getUserName());
            textViewUsersEmail.setText(Utils.decodeEmail(user.getEmail()));

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


