package cse110.giftexchangeapplication.ui.userProfile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cse110.giftexchangeapplication.R;

/**
 * Created by AJ on 2/29/16.
 */
public class AboutMeFragment extends Fragment {

    public AboutMeFragment() {
        // Required blank constructor
    }

    public static AboutMeFragment newInstance() {
        AboutMeFragment fragment = new AboutMeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize UI elements
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        initializeScreen(rootView);

        return null;
    }

    private void initializeScreen(View rootView) {

    }


}
