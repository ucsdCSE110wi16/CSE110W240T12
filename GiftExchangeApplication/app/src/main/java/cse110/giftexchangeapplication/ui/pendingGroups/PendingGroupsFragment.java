package cse110.giftexchangeapplication.ui.pendingGroups;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import cse110.giftexchangeapplication.R;

public class PendingGroupsFragment extends Fragment {

    private ListView mListView;

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static PendingGroupsFragment newInstance() {
        PendingGroupsFragment fragment = new PendingGroupsFragment();
        Bundle args = new Bundle();
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
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending_groups_list, container, false);

        /**
         * Link layout elements from XML and setup the toolbar
         */
        initializeScreen(rootView);

        /**
         * Set interactive bits, such as click events/adapters
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
