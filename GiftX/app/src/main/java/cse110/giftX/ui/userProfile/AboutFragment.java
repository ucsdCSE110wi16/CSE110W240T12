package cse110.giftX.ui.userProfile;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "email";
    private static final String ARG_PARAM2 = "position";

    // TODO: Rename and change types of parameters
    private String email;
    private int position;
    private Firebase ref;
    private EditText mInfoDisplay;
    private ImageView profilePicture;
    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String email, int position) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, email);
        args.putInt(ARG_PARAM2, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
        }
        profilePicture = (ImageView) getActivity().findViewById(R.id.userAvatar);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Initialize UI elements
        final View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        initializeScreen(rootView);
        String encoded = Utils.encodeEmail(email);
        ref = new Firebase(Constants.FIREBASE_URL_USERS).child(encoded);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                new DownloadImageTask().execute(user.getProfileURL());

                switch (position) {
                    case 0:
                        mInfoDisplay.setText(user.getAboutMe());
                        break;
                    case 1:
                        mInfoDisplay.setText(user.getLikes());
                        break;
                    case 2:
                        mInfoDisplay.setText(user.getDislikes());
                        break;
                    default:
                        mInfoDisplay.setText(user.getAboutMe());
                        break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        if(Utils.encodeEmail(ref.getAuth().getProviderData().get("email").toString()).equals(email)) {
            mInfoDisplay.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    switch (position) {
                        case 0:
                        /*
                        newInfo.put("aboutMe", mInfoDisplay.getText().toString());
                        ref.child("aboutMe").updateChildren(newInfo);
                        */
                            ref.child("aboutMe").setValue(mInfoDisplay.getText().toString());
                            break;
                        case 1:
                            ref.child("likes").setValue(mInfoDisplay.getText().toString());
                            break;
                        case 2:
                            ref.child("dislikes").setValue(mInfoDisplay.getText().toString());
                            break;
                        default:
                            ref.child("aboutMe").setValue(mInfoDisplay.getText().toString());
                            break;
                    }
                }
            });
        } else {
            mInfoDisplay.setKeyListener(null);
        }
        return rootView;
    }

    private void initializeScreen(View rootView) {
        mInfoDisplay = (EditText) rootView.findViewById(R.id.infoDisplay);
    }

    private class DownloadImageTask extends AsyncTask<Object, Integer, Drawable> {

        protected Drawable doInBackground(Object... args) {
            Drawable d = Utils.loadImageFromWeb((String) args[0]);
            return d;
        }

        protected void onPostExecute(Drawable result) {
            if(result != null && profilePicture != null) {
                profilePicture.setImageDrawable(result);
            }
        }
    }
}