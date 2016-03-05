package cse110.giftexchangeapplication.ui.activeGroups;

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

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Adds a new active group
 */
public class AddGroupDialogFragment extends DialogFragment {
    String mEncodedEmail;
    EditText mEditTextGroupName;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static AddGroupDialogFragment newInstance(String encodedEmail) {
        AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ENCODED_EMAIL, encodedEmail);
        addGroupDialogFragment.setArguments(bundle);

        return addGroupDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEncodedEmail = getArguments().getString(Constants.KEY_ENCODED_EMAIL);
    }

    /**
     * Open keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_add_group, null);
        mEditTextGroupName = (EditText) rootView.findViewById(R.id.edit_text_group_name);

        /**
         * Call addActiveGroup() when user taps "Done" keyboard action
         */
        mEditTextGroupName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addActiveGroup();
                }

                return true;
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addActiveGroup();
                    }
                });

        return builder.create();
    }

    /**
     * Add new ActiveGroup
     */
    public void addActiveGroup() {
        // Get the string that the user entered into the EditText and make an object with it
        String userEnteredName = mEditTextGroupName.getText().toString();

        /**
         * If EditText input is not empty
         */
        if (!userEnteredName.equals("")) {

            // Create Firebase references
            Firebase groupsRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS);
            Firebase newGroupRef = groupsRef.push();

            // Save groupsRef.push() to maintain same random ID
            final String groupID = newGroupRef.getKey();

            /*
             * Set raw version of date to the ServerValue.TIMESTAMP value and save into
             * timestampCreateMap
             */
            HashMap<String, Object> timestampCreated = new HashMap<>();
            timestampCreated.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

            // Build the active group
            ActiveGroup newActiveGroup = new ActiveGroup(userEnteredName, mEncodedEmail, timestampCreated);

            // Add the active group
            newGroupRef.setValue(newActiveGroup);

            // Close the dialog fragment
            AddGroupDialogFragment.this.getDialog().cancel();

        }
    }

}
