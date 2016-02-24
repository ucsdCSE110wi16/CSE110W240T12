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

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Adds a new active group
 */
public class AddGroupDialogFragment extends DialogFragment {
    EditText mEditTextGroupName;

    /**
     * Public static constructor that creates fragment and
     * passes a bundle with data into it when adapter is created
     */
    public static AddGroupDialogFragment newInstance() {
        AddGroupDialogFragment addGroupDialogFragment = new AddGroupDialogFragment();
        Bundle bundle = new Bundle();
        addGroupDialogFragment.setArguments(bundle);

        return addGroupDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

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
        // Get the reference to the root node in Firebase
        Firebase ref = new Firebase(Constants.FIREBASE_URL);
        // Get the string that the user entered into the EditText and make an object with it
        String userEnteredName = mEditTextGroupName.getText().toString();
        //ActiveGroup activeGroup = new ActiveGroup(userEnteredName);

        // Go to the "activeGroup" child node of the root node.
        // This will create the node for you if it doesn't already exist.
        // Then using the setValue menu it will serialize the ActiveGroup POJO
        //ref.child("activeGroup").setValue(activeGroup);
    }

}
