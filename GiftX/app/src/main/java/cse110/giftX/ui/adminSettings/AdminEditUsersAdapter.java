package cse110.giftX.ui.adminSettings;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

/**
 * Adapter for the "remove users" fragment.
 */
public class AdminEditUsersAdapter extends ArrayAdapter<User> {
    // Get authenticated user info
    private Firebase ref = new Firebase(Constants.FIREBASE_URL);
    private String userEmail = Utils.encodeEmail(ref.getAuth().getProviderData().get("email").toString());

    private List<User> userRemoveList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    // Constructor for use in conjunction with a list of users
    public AdminEditUsersAdapter(Context context, ArrayList<User> usersList) {
        super(context, 0, usersList);
        layoutInflater = LayoutInflater.from(context);
    }

    // Getter method for removelist
    public List<User> getUserRemoveList(){
        return userRemoveList;
    }

    public void clearUserRemoveList(){
        this.userRemoveList.clear();
    }


    @Override
    public View getView(final int position,View view, final ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.single_user, viewGroup, false);
        }
        // Fill the TextViews with info about the user
        User user = getItem(position);
        String userFullName = user.getUserName();
        TextView textViewName = (TextView) view.findViewById(R.id.text_user_name);
        TextView textViewEmail = (TextView) view.findViewById(R.id.text_view_user_email);
        textViewName.setText(userFullName);
        textViewEmail.setText(user.getEmail());
        // Set the checkbox for each user
        final CheckBox checkBoxEditUsers = (CheckBox) view.findViewById(R.id.checkBox);
        checkBoxEditUsers.setChecked(false);
        view.setBackgroundColor(Color.WHITE);
        if (user != null) {
            String clickedEmail = user.getEmail();

            // Make the checkbox invisible for the current user
            checkBoxEditUsers.setTag(clickedEmail);
            if (userEmail.equals(clickedEmail)) {
                checkBoxEditUsers.setVisibility(View.INVISIBLE);
            }

            final View finalView = view;
            // OnClickListener for the checkbox
            checkBoxEditUsers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBoxEditUsers.isChecked()) {
                        // Change the color of the list item's background
                        finalView.setBackgroundColor(getContext().getResources().getColor(R.color.primary_dark));
                        ListView listViewEditUsersRef = (ListView) viewGroup;
                        // Add the user to the remove list
                        userRemoveList.add((User) listViewEditUsersRef.getItemAtPosition(position));
                    } else {
                        // Change the color of the list item's background
                        finalView.setBackgroundColor(Color.WHITE);
                        ListView listViewEditUsersRef = (ListView) viewGroup;
                        // Remove the user from the remove list
                        userRemoveList.remove(listViewEditUsersRef.getItemAtPosition(position));
                    }
                }
            });
        }
        return view;
    }
}