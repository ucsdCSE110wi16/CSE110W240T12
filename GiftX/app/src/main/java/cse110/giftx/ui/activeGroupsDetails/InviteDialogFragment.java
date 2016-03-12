package cse110.giftX.ui.activeGroupsDetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftX.R;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

public class InviteDialogFragment extends DialogFragment {
    EditText mEditTextInviteEmail;
    String groupID;
    String userEmail;
    Firebase usersRef;
    String userEnteredEmail;
    Activity context;

    public static InviteDialogFragment newInstance(String userEmail, String groupID) {
        InviteDialogFragment inviteDialog = new InviteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userEmail", userEmail);
        bundle.putString("groupID", groupID);
        inviteDialog.setArguments(bundle);

        return inviteDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        userEmail = b.getString("userEmail");
        groupID = b.getString("groupID");
        usersRef = new Firebase(Constants.FIREBASE_URL_USERS);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_invite_user, null);
        mEditTextInviteEmail = (EditText) rootView.findViewById(R.id.edit_text_invite_name);

        mEditTextInviteEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    inviteUser();
                }

                return true;
            }
        });

        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton("Invite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inviteUser();
                    }
                });

        return builder.create();
    }

    public void inviteUser() {
        userEnteredEmail = Utils.encodeEmail(mEditTextInviteEmail.getText().toString());

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userEnteredEmail) && !userEnteredEmail.equals(userEmail)
                        && !userEnteredEmail.equals("")) {
                    usersRef.child(userEnteredEmail).child("invitations").child(groupID).setValue(true);
                    showToast("Invite Sent!");
                } else {
                    showToast("Invalid Email!");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        InviteDialogFragment.this.getDialog().cancel();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}