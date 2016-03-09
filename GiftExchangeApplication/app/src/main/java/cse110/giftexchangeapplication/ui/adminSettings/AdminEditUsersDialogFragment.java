package cse110.giftexchangeapplication.ui.adminSettings;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
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

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Set;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.ui.activeGroupsDetails.InviteDialogFragment;
import cse110.giftexchangeapplication.utils.Constants;
import cse110.giftexchangeapplication.utils.Utils;

/**
 * DialogFragment class for a dialog in which the admin of a group can add/remove users
 */

public class AdminEditUsersDialogFragment extends DialogFragment {
    private String groupID;
    private String userEmail;
    private Set<String> userEmails;
    private ActiveGroup mActiveGroup;
    private Firebase editUsersRef;
    private Firebase mActiveGroupUsersRef;
    private Firebase  mActiveGroupRef;
    private ValueEventListener mActiveGroupRefListener;
    private ArrayList<User> users;
    private AdminEditUsersAdapter mEditUserAdapter;
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Instantiate a builder, inflater, and view for the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootViewEditUsers = inflater.inflate(R.layout.dialog_edit_users, null);
        builder.setView(rootViewEditUsers);

        // Get data from activity
        Bundle bundle = this.getArguments();
        groupID = bundle.getString(Constants.KEY_GROUP_ID);

        // Set the ListView and Buttons to their appropriate UI pieces
        listViewEditUsers = (ListView) rootViewEditUsers.findViewById(R.id.list_view_group_members);
        buttonRemoveSelected = (Button) rootViewEditUsers.findViewById(R.id.button_remove_selected);
        buttonAddUsers = (Button) rootViewEditUsers.findViewById(R.id.button_add_users);
        buttonCancel = (Button) rootViewEditUsers.findViewById(R.id.button_cancel);

        // Set the adapter for the list
        users = new ArrayList<User>();
        mEditUserAdapter = new AdminEditUsersAdapter(getActivity(), users);
        listViewEditUsers.setAdapter(mEditUserAdapter);
        // Give the cancel Button functionality
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mActiveGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID);
        editUsersRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID).child("users");
        mActiveGroupUsersRef = new Firebase(Constants.FIREBASE_URL_USERS);
        /**
         * Save the most recent version of current active group into mActiveGroup instance
         * variable and update the UI to match the current group
         */
        mActiveGroupRefListener = mActiveGroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /**
                 * Saving the most recent version of current active group into mActiveGroup
                 * if present finish() the activity if the group is null (group was removed or
                 * unshared by its owner while current user is in the group details activity)
                 */
                ActiveGroup activeGroup = dataSnapshot.getValue(ActiveGroup.class);
                // Save to instance variable
                mActiveGroup = activeGroup;
                userEmails = mActiveGroup.getUsers().keySet();
                for (String email : userEmails) {
                    mActiveGroupUsersRef.child(email).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User usr = dataSnapshot.getValue(User.class);
                            if (usr != null) {
                                if(usr.getGroups().containsKey(groupID)){
                                    users.remove(usr);
                                    users.add(usr);
                                }
                            }
                            if (mEditUserAdapter != null) {
                                mEditUserAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Give the remove Button functionality
        buttonRemoveSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the list of users to remove from the group
                List<User> userRemoveList = mEditUserAdapter.getUserRemoveList();
                if (!userRemoveList.isEmpty()) {
                    for (int i = 0; i < userRemoveList.size(); i++) {
                        User toRemove = userRemoveList.get(i);
                        String email = toRemove.getEmail();
                        Firebase removeUserRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(groupID).child("users").child(email);
                        Firebase removeUserGroupRef = new Firebase(Constants.FIREBASE_URL_USERS).child(email).child("groups").child(groupID);
                        removeUserGroupRef.removeValue();
                        removeUserRef.removeValue();
                        users.remove(toRemove);
                    }
                }

            }
        });
        buttonAddUsers.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InviteDialogFragment inviteDialog = InviteDialogFragment.newInstance(userEmail, groupID);
                inviteDialog.show(getActivity().getFragmentManager(), "InviteDialogFragment");
            }
        });


        return builder.create();
    }
    // Clean up when activity is destroyed.
    @Override
    public void onDestroy() {
        super.onDestroy();
        editUsersRef.removeEventListener(mActiveGroupRefListener);
    }

}
