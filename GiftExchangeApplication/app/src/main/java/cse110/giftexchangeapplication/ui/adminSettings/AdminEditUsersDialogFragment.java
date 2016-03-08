package cse110.giftexchangeapplication.ui.adminSettings;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.List;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.User;

/**
 * DialogFragment class for a dialog in which the admin of a group can add/remove users
 */

public class AdminEditUsersDialogFragment extends DialogFragment {
    Firebase editUsersRef = new Firebase("https://burning-heat-3076.firebaseio.com/users");
    // Button for removing all selected users
    Button buttonRemoveSelected;
    // Button for adding users
    Button buttonAddUsers;
    // Button for canceling the edit and exiting the dialog
    Button buttonCancel;
    // ListView in which the group's users will be displayed
    ListView listViewEditUsers;

    // Method for when the dialog is created
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Instantiate a builder, inflater, and view for the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootViewEditUsers = inflater.inflate(R.layout.dialog_edit_users, null );
        builder.setView(rootViewEditUsers);
        // Set the ListView and Buttons to their appropriate UI pieces
        listViewEditUsers = (ListView)rootViewEditUsers.findViewById(R.id.list_view_group_members);
        buttonRemoveSelected = (Button)rootViewEditUsers.findViewById(R.id.button_remove_selected);
        buttonAddUsers = (Button)rootViewEditUsers.findViewById(R.id.button_add_users);
        buttonCancel = (Button)rootViewEditUsers.findViewById(R.id.button_cancel);
        final List<Object> userRemoveList = new ArrayList<>();
        // Set the adapter for the list
        final FirebaseListAdapter<User> mEditUserAdapter = new FirebaseListAdapter<User>(getActivity(), User.class,
                R.layout.single_group_member, editUsersRef){
            @Override
            protected void populateView(View view, User object){
                ((TextView)view.findViewById(R.id.text_view_member_first_name)).setText(object.getFirstName());
                ((TextView)view.findViewById(R.id.text_view_member_last_name)).setText(object.getLastName());
            }
            @Override
            public View getView(final int position, final View view, final ViewGroup viewGroup) {
                final View tempView = super.getView(position, view, viewGroup);
                final CheckBox checkBoxEditUsers = (CheckBox)tempView.findViewById(R.id.check_box_select_user);
                checkBoxEditUsers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(checkBoxEditUsers.isChecked()){
                            view.setBackgroundColor(Color.RED);
                            ListView listViewEditUsersRef = (ListView) viewGroup;
                            userRemoveList.add(listViewEditUsersRef.getItemAtPosition(position));
                        }
                        else{
                            view.setBackgroundColor(Color.BLUE);
                            ListView listViewEditUsersRef = (ListView) viewGroup;
                            userRemoveList.remove(listViewEditUsersRef.getItemAtPosition(position));
                        }
                    }
                });

                return tempView;
            }

        };
        listViewEditUsers.setAdapter(mEditUserAdapter);
        // Give the cancel Button functionality
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        buttonRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userRemoveList.isEmpty()) {
                    for (int i = 0; i < userRemoveList.size(); i++) {
                        Firebase getUserItemRef = mEditUserAdapter.getRef((Integer) userRemoveList.get(i));
                        getUserItemRef.removeValue();
                    }
                }

            }
        });
        return builder.create();
    }



}
