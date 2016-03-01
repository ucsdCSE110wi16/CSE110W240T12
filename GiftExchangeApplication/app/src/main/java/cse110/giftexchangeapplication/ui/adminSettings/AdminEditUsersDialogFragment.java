package cse110.giftexchangeapplication.ui.adminSettings;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import cse110.giftexchangeapplication.R;

/**
 * DialogFragment class for a dialog in which the admin of a group can add/remove users
 */

public class AdminEditUsersDialogFragment extends DialogFragment {
    // Button for removing all selected users
    Button buttonRemoveSelected;
    // Button for adding users
    Button buttonAddUsers;
    // Button for canceling the edit and exiting the dialog
    Button buttonCancel;
    // ListView in which the group's users will be displayed
    ListView listViewEditUsers;
    // Adapter for the ListView
    ArrayAdapter<String> adapterUserList;
    // Hardcoded array of user names for now
    String[] arrayUsers = {"User 1", "User 2", "User 3", "User 4", "User 5", "User 6", "User 7",
            "User 8"};

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
        // Set the adapter for the list
        adapterUserList = new ArrayAdapter<>(getActivity(), R.layout.single_group_member,
                R.id.text_view_member_name, arrayUsers);
        listViewEditUsers.setAdapter(adapterUserList);
        // Give the cancel Button functionality
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }



}
