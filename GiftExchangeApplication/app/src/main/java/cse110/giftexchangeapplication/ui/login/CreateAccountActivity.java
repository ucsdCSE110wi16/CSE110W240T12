package cse110.giftexchangeapplication.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.model.User;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.ui.MainActivity;
import cse110.giftexchangeapplication.utils.Constants;
import cse110.giftexchangeapplication.utils.Utils;

public class CreateAccountActivity extends BaseActivity {

    private Firebase mFirebaseRef;
    private EditText mEditTextFirstNameCreate;
    private EditText mEditTextLastNameCreate;
    private EditText mEditTextEmailCreate;
    private EditText mEditTextPasswordCreate;
    private EditText mEditTextPasswordConfirmCreate;

    private String mFirstName, mLastName, mUserEmail, mPassword, mConFirmPassword;

    ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Create Firebase Reference
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        // Link XML layout elements & setup the progress dialog
        initializeScreen();

        //Add functionality to button.
        Button mRegisterButton = (Button) findViewById(R.id.btn_create_account_final);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void initializeScreen() {
        mEditTextFirstNameCreate = (EditText) findViewById(R.id.edit_text_first_name_create);
        mEditTextLastNameCreate = (EditText) findViewById(R.id.edit_text_user_last_name_create);
        mEditTextEmailCreate = (EditText) findViewById(R.id.edit_text_email_create);
        mEditTextPasswordCreate = (EditText) findViewById(R.id.edit_text_password_create);
        mEditTextPasswordConfirmCreate = (EditText) findViewById(R.id.edit_text_confirm_password_create);

        // Setup a progress dialog for authentication progress
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getResources().getString(R.string.progress_dialog_creating_user_with_firebase));
        mAuthProgressDialog.setCancelable(false);

    }

    /**
     * Open LoginActivity when user taps on "Sign in" TextView
     */
    public void onSignInPressed(View view) {
        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Create a new account using Firebase email/password provider
     */
    public void createNewAccount() {

        mFirstName = mEditTextFirstNameCreate.getText().toString();
        mLastName = mEditTextLastNameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString();
        mPassword = mEditTextPasswordCreate.getText().toString();
        mConFirmPassword = mEditTextPasswordConfirmCreate.getText().toString();


        //Checks validity of all text fields
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validFirstName = isFirstNameValid(mFirstName);
        boolean validLastName = isLastNameValid(mLastName);
        boolean validPassword = isPasswordValid(mPassword, mConFirmPassword);

        if(!validEmail || !validFirstName || !validLastName || !validPassword)
            return;

        // If everything is valid show the progress dialog
        mAuthProgressDialog.show();

        final AuthData authData = mFirebaseRef.getAuth();
        if (authData != null) {
            mFirebaseRef.unauth();
        }

        mFirebaseRef.createUser(mUserEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                // Dismiss progress dialog
                mAuthProgressDialog.dismiss();
                final String uid = (String) stringObjectMap.get("uid");

                mFirebaseRef.authWithPassword(mUserEmail, mPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        createUserInFirebase(authData.getUid()); //michael - added UID to parameters
                        //startMainActivity(); bug1
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {

                    }
                });
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // Error occurs, dismiss dialog
                mAuthProgressDialog.dismiss();

                // Display error
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                    mEditTextEmailCreate.setError(getString(R.string.error_email_taken));
                }
                else {
                    showErrorToast(firebaseError.getMessage());
                }
            }
        });
    }

    /**
     * Helper that creates a User & stores it in Firebase
     */

    private void createUserInFirebase(String uid) { //michael - added uid parameter here and in user pojo
        final String encodedEmail = Utils.encodeEmail(mUserEmail);
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);
        final String userID = uid;

        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If there is no user, make one
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(mFirstName, mLastName, encodedEmail, timestampJoined, userID);
                    userLocation.setValue(newUser);
                    startMainActivity();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // blank
            }
        });

    }

    // Check if email is valid
    private boolean isEmailValid(String email) {
        boolean isGoodEmail =
                (email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches());

        if(!isGoodEmail) {
            mEditTextEmailCreate.setError(String.format(getResources().getString(R.string.error_invalid_email_not_valid),
                    email));
            return false;
        }
        return isGoodEmail;
    }

    // Check if first name is valid
    private boolean isFirstNameValid(String firstName) {
        if (firstName.equals("")) {
            mEditTextFirstNameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    // Check if last name is valid
    private boolean isLastNameValid(String lastName) {
        if (lastName.equals("")) {
            mEditTextFirstNameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    // Check if password is valid
    private boolean isPasswordValid(String password, String confirmPassword) {
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        else if (!password.equals(confirmPassword)) {
            mEditTextPasswordConfirmCreate.setError(getResources().getString(R.string.error_confirm_password_not_valid));
        }
        return true;
    }
    
    // Show error toast to user
    private void showErrorToast(String message) {
        Toast.makeText(CreateAccountActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void startMainActivity(){
        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}