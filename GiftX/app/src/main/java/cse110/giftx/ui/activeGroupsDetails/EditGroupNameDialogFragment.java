package cse110.giftx.ui.activeGroupsDetails;

import android.app.Dialog;
import android.os.Bundle;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.HashMap;

import cse110.giftx.R;
import cse110.giftx.model.ActiveGroup;
import cse110.giftx.utils.Constants;

/**
 * Lets user edit the group name for all copies of the current list
 */
public class EditGroupNameDialogFragment extends EditGroupDialogFragment {

    String mGroupName;

    /**
     * Public static constructor that creates fragment and passes a bundle
     * with data into it when adapter is created
     */
    public static EditGroupNameDialogFragment newInstance(ActiveGroup activeGroup, String groupId) {
        EditGroupNameDialogFragment editGroupNameDialogFragment = new EditGroupNameDialogFragment();
        Bundle bundle = EditGroupDialogFragment.newInstanceHelper(activeGroup,
                R.layout.dialog_edit_group, groupId);
        bundle.putString(Constants.KEY_GROUP_NAME, activeGroup.getGroupName());
        editGroupNameDialogFragment.setArguments(bundle);

        return editGroupNameDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupName = getArguments().getString(Constants.KEY_GROUP_NAME);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        /**
         * {@link EditGroupDialogFragment#createDialogHelper(int)} is a
         * superclass method that creates the dialog
         */
        Dialog dialog = super.createDialogHelper(R.string.positive_button_edit_item);

        /**
         * {@link EditGroupDialogFragment#helpSetDefaultValueEditText(String)} is
         * a superclass method that sets the default text of the TextView
         */
        helpSetDefaultValueEditText(mGroupName);
        return dialog;
    }

    /**
     * Changes the group name in all copies of the current group
     */
    protected void doGroupEdit() {
        final String inputGroupName = mEditTextForGroup.getText().toString();

        /**
         * Set input text to be the current group name if it is not empty
         */
        if (!inputGroupName.equals("")) {

            if(mGroupName != null && mGroupId != null) {

                /**
                 * If editText input is not equal to the previous name
                 */
                if (!inputGroupName.equals(mGroupName)) {
                    Firebase activeGroupRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).
                            child(mGroupId);

                    // Make a HashMap for the specific properties you are changing
                    HashMap<String, Object> updatedProperties = new HashMap<String, Object>();
                    updatedProperties.put(Constants.FIREBASE_PROPERTY_GROUP_NAME, inputGroupName);

                    // Add the timestamp for last changed to the updatedProperties HashMap
                    HashMap<String, Object> changedTimestampMap = new HashMap<>();
                    changedTimestampMap.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    // Add the updated timestamp
                    updatedProperties.put(Constants.FIREBASE_PROPERTY_TIMESTAMP_LAST_CHANGED, changedTimestampMap);

                    // Do the update
                    activeGroupRef.updateChildren(updatedProperties);
                }
            }
        }
    }
}
