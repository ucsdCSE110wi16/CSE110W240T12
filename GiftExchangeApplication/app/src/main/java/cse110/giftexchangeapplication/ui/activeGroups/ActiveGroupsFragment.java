package cse110.giftexchangeapplication.ui.activeGroups;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.ActiveGroup;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * A simple Fragment subclass that shows a list of all active groups
 * Use the ShoppingListsFragment#newInstance method to create a
 * create an instance of this fragment.
 */
public class ActiveGroupsFragment extends Fragment{
    private ListView mListView;
    private TextView mTextViewGroupName;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        Firebase refGroupName = new Firebase(Constants.FIREBASE_URL).child("activeGroup");

        /**
         * Add ValueEventListeners to Firebase references
         * to control get data and control behavior and visibility of elements
         */
        refGroupName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // You can use getValue to deserialize the data at dataSnapshot
                // into ActiveGroup.
                ActiveGroup activeGroup = dataSnapshot.getValue(ActiveGroup.class);

                // If there was no data at the location we added the listener, then
                // ActiveGroup will be NULL
                if (activeGroup != null) {
                    // If there was data, take the TextViews & set appropriate values
                    mTextViewGroupName.setText(activeGroup.getGroupName());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /**
         * Set interactive bits, such as click events and adapters
         */

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Blank for now
            }
        });

        mTextViewGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Starts an active showing the details for the selected list
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() { super.onDestroy(); }

    /**
     * Link layout elements from XML
     */
    private void initializeScreen(View rootView) {
        mListView = (ListView) rootView.findViewById(R.id.list_view_active_groups);

        // Get the TextViews in the single_active_group layout for group name
        mTextViewGroupName = (TextView) rootView.findViewById(R.id.text_view_group_name);
    }
}
