package cse110.giftX.ui.pendingGroups;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

public class PendingGroupsFragment extends Fragment {

    private InvitesAdapter mInvitesAdapter;
    private ListView mListView;
    private String userEmail;
    private ArrayList<String> groupIDs;
    private Firebase groupTracker;
    private ArrayList<ActiveGroup> groups;

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static PendingGroupsFragment newInstance(String userEmail, ArrayList<String> groups) {
        PendingGroupsFragment fragment = new PendingGroupsFragment();
        Bundle args = new Bundle();
        args.putString("email", userEmail);
        args.putStringArrayList("groups", groups);
        fragment.setArguments(args);

        return fragment;
    }

    public PendingGroupsFragment() {
        // Required public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) { super.onSaveInstanceState(outState); }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        this.userEmail = args.getString("email"); //michael
        this.groupIDs = args.getStringArrayList("groups");

        groupTracker = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS);
        groups = new ArrayList<ActiveGroup>();

        for(String gID: groupIDs) {
            groupTracker.child(gID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ActiveGroup grp = dataSnapshot.getValue(ActiveGroup.class);
                    if(grp != null) {
                        groups.remove(grp);
                        groups.add(grp);
                    }

                    if (mInvitesAdapter != null) {
                        mInvitesAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pending_groups_list, container, false);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen(rootView);

        mInvitesAdapter = new InvitesAdapter(getActivity(), groups);
        mListView.setAdapter(mInvitesAdapter);

        /**
         * Set interactive bits, such as click events/adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //for now, just accept the invite
                selectedGroup = mInvitesAdapter.getItem(position);
                String uEmail = Utils.decodeEmail(selectedGroup.getGroupManager());
                if (selectedGroup != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                            .setTitle(getActivity().getResources().getString(R.string.invitation_title))
                            .setMessage(getString(R.string.invitation_mesage) +" " + uEmail + "?")
                            .setPositiveButton(R.string.accept_invite, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    acceptInvite();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.decline_invite, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    declineInvite();
                                    dialog.dismiss();
                                }
                            })
                            .setNeutralButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    builder.create().show();
                } else {
                    Toast.makeText(getContext(), "The group no longer exists", Toast.LENGTH_LONG).show();
                }

            }
        });

        return rootView;
    }
    ActiveGroup selectedGroup;
    View rootView;

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_pending_groups);
        //causes bugs:
        //View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_empty, null);
        //mListView.addFooterView(footer);
    }

    private void acceptInvite() {
        groupTracker.child(selectedGroup.getGroupID()).child("users").child(userEmail).child(userEmail).setValue(true);
        Firebase userGroups = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail).child("groups");
        userGroups.child(selectedGroup.getGroupID()).setValue(true);
        //delete invite
        Firebase invite = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail).child("invitations");
        groups.remove(selectedGroup);
        invite.child(selectedGroup.getGroupID()).setValue(null);
        mInvitesAdapter.notifyDataSetChanged();
    }
    private void declineInvite() {
        Firebase invite = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail).child("invitations");
        groups.remove(selectedGroup);
        invite.child(selectedGroup.getGroupID()).setValue(null);
        mInvitesAdapter.notifyDataSetChanged();
    }
}
