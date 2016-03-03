package cse110.giftexchangeapplication.ui.pendingGroups;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.ui.activeGroups.ActiveGroupAdapter;
import cse110.giftexchangeapplication.utils.Constants;

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
        View rootView = inflater.inflate(R.layout.fragment_pending_groups_list, container, false);

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
                ActiveGroup selectedGroup = mInvitesAdapter.getItem(position);
                if (selectedGroup != null) {
                    //add self to group
                    groupTracker.child(selectedGroup.getGroupID()).child("users").child(userEmail).child(userEmail).setValue(true);
                    Firebase userGroups = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail).child("groups");
                            userGroups.child(selectedGroup.getGroupID()).setValue(true);
                    //delete invite
                    Firebase invite = new Firebase(Constants.FIREBASE_URL_USERS).child(userEmail).child("invitations");
                    groups.remove(selectedGroup);
                    invite.child(selectedGroup.getGroupID()).setValue(null);
                    mInvitesAdapter.notifyDataSetChanged();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() { super.onResume(); }

    @Override
    public void onPause() { super.onPause(); }

    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_pending_groups);
        View footer = getActivity().getLayoutInflater().inflate(R.layout.footer_empty, null);
        mListView.addFooterView(footer);
    }

}
