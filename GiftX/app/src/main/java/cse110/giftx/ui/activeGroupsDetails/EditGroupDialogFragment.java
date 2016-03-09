package cse110.giftx.ui.activeGroupsDetails;

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

import cse110.giftx.R;
import cse110.giftx.model.ActiveGroup;
import cse110.giftx.utils.Constants;

/**
 * Base class for {@link DialogFragment}s involved with editing an active group
 */
public abstract class EditGroupDialogFragment extends DialogFragment {
    String mGroupId;
    EditText mEditTextForGroup;
    int mResource;

    /**
     * Helper method that creates a basic bundle of all of the information
     * needed to change values in an active group.
     *
     * @param activeGroup
     * @param resource
     * @return
     */
    protected static Bundle newInstanceHelper(ActiveGroup activeGroup, int resource, String groupId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_GROUP_ID, groupId);
        bundle.putInt(Constants.KEY_LAYOUT_RESOURCE, resource);

        return bundle;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupId = getArguments().getString(Constants.KEY_GROUP_ID);
        mResource = getArguments().getInt(Constants.KEY_LAYOUT_RESOURCE);
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    protected Dialog createDialogHelper (int stringResourceForPositiveButton) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflate the layout, set root ViewGroup to null
        View rootView = inflater.inflate(mResource, null);
        mEditTextForGroup = (EditText) rootView.findViewById(R.id.edit_text_group_dialog);

        /**
         * Call doGroupEdit() when the user taps 'Done' keyboard action
         */
        mEditTextForGroup.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    doGroupEdit();

                    /**
                     * Close the dialog fragment when done
                     */
                    EditGroupDialogFragment.this.getDialog().cancel();
                }
                return true;
            }
        });

        // Inflate & set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(stringResourceForPositiveButton, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doGroupEdit();

                        /**
                         * Close the dialog fragment
                         */
                    }
                })
                .setNegativeButton(R.string.negative_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Close the dialog fragment
                        EditGroupDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    /**
     * Set the EditText text to be the inputted text
     * and put the pointer at the end of the input
     *
     * @param defaultText
     */
    protected void helpSetDefaultValueEditText(String defaultText) {
        mEditTextForGroup.setText(defaultText);
        mEditTextForGroup.setSelection(defaultText.length());
    }

    /**
     * Mehtod to be overwritten with whatever edit is supposed to happen to the list
     */
    protected abstract void doGroupEdit();
}
