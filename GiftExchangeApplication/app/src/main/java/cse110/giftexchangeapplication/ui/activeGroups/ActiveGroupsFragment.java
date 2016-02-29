package cse110.giftexchangeapplication.ui.activeGroups;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.Firebase;
import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.ui.activeGroupsDetails.ActiveGroupsDetailsActivity;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * A simple Fragment subclass that shows a list of all active groups
 * Use the ShoppingListsFragment#newInstance method to create a
 * create an instance of this fragment.
 */
public class ActiveGroupsFragment extends Fragment {
    private ActiveGroupAdapter mActiveGroupAdapter;
    private ListView mListView;

    public ActiveGroupsFragment() {
        // Required empty constructor
    }

    /**
     * Create fragment and pass bundle with data as it's arguments
     * For the moment... no arguments
     */
    public static ActiveGroupsFragment newInstance() {
        ActiveGroupsFragment fragment = new ActiveGroupsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /* Initialize instance variables  with data from bundles */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize UI elements
        View rootView = inflater.inflate(R.layout.fragment_group_list, container, false);
        initializeScreen(rootView);

        /**
         * Create Firebase references
         */
        Firebase activeGroupsRef = new Firebase(Constants.FIREBASE_URL_ACTIVE_GROUPS);

        /**
         * Create the adapter, giving it the activity, model class, layout for each row
         * in the list & finally, a reference to the Firebase location with the list data
         */
        mActiveGroupAdapter = new ActiveGroupAdapter(getActivity(), ActiveGroup.class,
                R.layout.single_active_group, activeGroupsRef);


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
                    String groupId = mActiveGroupAdapter.getRef(position).getKey();
                    intent.putExtra(Constants.KEY_GROUP_ID, groupId);

                    // Starts an active showing the details for the selected group
                    startActivity(intent);
                }

            }
        });


        return rootView;
    }

    /**
     * Cleanup the adapter when activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mActiveGroupAdapter.cleanup();
    }

    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_groups);
    }
}