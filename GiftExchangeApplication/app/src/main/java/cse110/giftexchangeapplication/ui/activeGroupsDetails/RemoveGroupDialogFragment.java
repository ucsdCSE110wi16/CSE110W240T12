package cse110.giftexchangeapplication.ui.activeGroupsDetails;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.firebase.client.Firebase;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * Gives user the ability to delete an Active Group
 */
public class RemoveGroupDialogFragment extends DialogFragment {
    String mGroupId;

    /**
     * Public static constructor that creates fragment and passes a bundle with data
     * into it when the adapter is created
     */
    public static RemoveGroupDialogFragment newInstance(ActiveGroup activeGroup, String groupId) {
        RemoveGroupDialogFragment removeGroupDialogFragment = new RemoveGroupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_GROUP_ID, groupId);
        removeGroupDialogFragment.setArguments(bundle);

        return removeGroupDialogFragment;
    }

    /**
     * Initialize instance variables with data from the bundle created
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroupId = getArguments().getString(Constants.KEY_GROUP_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_group))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_group))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeGroup();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeGroup() {
        // Get the location to remove from
        Firebase groupToRemoveRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS).child(mGroupId);

        //TODO: need to remove the group in all its users too

        // Remove the value
        groupToRemoveRef.removeValue();
    }

}
