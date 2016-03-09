package cse110.giftX.ui.activeGroups;

import android.content.Intent;
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

import java.util.ArrayList;

import cse110.giftX.R;
import cse110.giftX.model.ActiveGroup;
import cse110.giftX.ui.MainActivity;
import cse110.giftX.ui.activeGroupsDetails.ActiveGroupsDetailsActivity;
import cse110.giftX.utils.Constants;

/**
 * A simple Fragment subclass that shows a list of all active groups
 * Use the ShoppingListsFragment#newInstance method to create a
 * create an instance of this fragment.
 */
public class ActiveGroupsFragment extends Fragment {
    private ActiveGroupAdapter mActiveGroupAdapter;
    private ListView mListView;
    private String userEmail;
    private ArrayList<String> groupIDs;
    private ArrayList<ActiveGroup> groups;
    private Firebase groupTracker;

    LayoutInflater inflater;
    ViewGroup container;

    public ActiveGroupsFragment() {
        // Required empty constructor
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * For the moment... no arguments
     */
    public static ActiveGroupsFragment newInstance(String userEmail, ArrayList<String> groups) {
        ActiveGroupsFragment fragment = new ActiveGroupsFragment();
        Bundle args = new Bundle();
        args.putString("email", userEmail);
        args.putStringArrayList("groupIDs", groups);
        fragment.setArguments(args);

        return fragment;
    }

    /* Initialize instance variables  with data from bundles */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        this.userEmail = args.getString("email");
        this.groupIDs = args.getStringArrayList("groupIDs");

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

                    if (mActiveGroupAdapter != null) {
                        mActiveGroupAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

        if (getArguments() != null) {}
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        View rootView = inflater.inflate(R.layout.fragment_group_list, container, false);
        initializeScreen(rootView);
        loadScreen();
        return rootView;
    }

    public void loadScreen() {

        /**
         * Create Firebase references
         */
        //michael - changed from general groups to user groups
        //Firebase activeGroupsRef = new Firebase(Constants.FIREBASE_URL_USERS + "/" + userEmail);

        /**
         * Create the adapter, giving it the activity, model class, layout for each row
         * in the list & finally, a reference to the Firebase location with the list data
         */
        mActiveGroupAdapter = new ActiveGroupAdapter(getActivity(), groups);
        //getActivity(), ActiveGroup.class, R.layout.single_active_group, activeGroupsRef);


        /**
         * Set the adapter to the mListView
         */
        mListView.setAdapter(mActiveGroupAdapter);

        /**
         * Set interactive bits, such as click events and adapters
         */

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActiveGroup selectedGroup = mActiveGroupAdapter.getItem(position);
                if (selectedGroup != null) {
                    Intent intent = new Intent(getActivity(), ActiveGroupsDetailsActivity.class);
                    /* Get the group ID using the adapter's get ref method to get the Firebase
                     * ref and then grab the key.
                     */
                    String groupId = mActiveGroupAdapter.getItem(position).getGroupID(); //getRef(position).getKey();
                    intent.putExtra(Constants.KEY_GROUP_ID, groupId);
                    intent.putExtra(MainActivity.USER_EMAIL, userEmail);

                    // Starts an active showing the details for the selected group
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * Cleanup the adapter when activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActiveGroupAdapter.clear();// cleanup();
    }

    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_groups);
    }
}