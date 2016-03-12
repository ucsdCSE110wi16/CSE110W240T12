package cse110.giftX.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
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

import cse110.giftX.R;
import cse110.giftX.model.User;
import cse110.giftX.ui.BaseActivity;
import cse110.giftX.ui.MainActivity;
import cse110.giftX.utils.Constants;
import cse110.giftX.utils.Utils;

public class CreateAccountActivity extends BaseActivity {

    //Log tag
    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();

    // Progress dialog
    ProgressDialog mAuthProgressDialog;

    // Firebase reference
    private Firebase mFirebaseRef;

    // Declare TextViews
    private EditText mEditTextUserNameCreate, mEditTextEmailCreate, mEditTextPasswordCreate, mEditTextPasswordConfirmCreate;

    private String mUserName, mUserEmail, mPassword, mConFirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Create Firebase Reference
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        // Link XML layout elements & setup the progress dialog
        initializeScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void initializeScreen() {
        mEditTextUserNameCreate = (EditText) findViewById(R.id.edit_text_username_create);
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
    public void createNewAccount(View view) {

        mUserName = mEditTextUserNameCreate.getText().toString();
        mUserEmail = mEditTextEmailCreate.getText().toString();
        mPassword = mEditTextPasswordCreate.getText().toString();
        mConFirmPassword = mEditTextPasswordConfirmCreate.getText().toString();

        //Checks validity of all text fields
        boolean validUserName = isUserNameValid(mUserName);
        boolean validEmail = isEmailValid(mUserEmail);
        boolean validPassword = isPasswordValid(mPassword, mConFirmPassword);

        // Exit if any field is not valid
        if(!validEmail || !validUserName || !validPassword)
            return;

        // If everything is valid show the progress dialog
        mAuthProgressDialog.show();

        final AuthData authData = mFirebaseRef.getAuth();
        if (authData != null) {
            mFirebaseRef.unauth();
        }

        /**
         * Create new user with given email & password
         */
        mFirebaseRef.createUser(mUserEmail, mPassword, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                // Dismiss progress dialog
                mAuthProgressDialog.dismiss();
                Log.i(LOG_TAG, getString(R.string.log_message_auth_successful));
                createUserInFirebase();

                mFirebaseRef.authWithPassword(mUserEmail, mPassword, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        startMainActivity();
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

    private void createUserInFirebase() {
        final String encodedEmail = Utils.encodeEmail(mUserEmail);
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(encodedEmail);

        /**
         * Check if there is already a user
         * ex. if they already logged in w/ Google Account
         */
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If there is no user, make one
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP, ServerValue.TIMESTAMP);

                    User newUser = new User(mUserName, encodedEmail, timestampJoined);
                    userLocation.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(LOG_TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
            }
        });

    }

    // Check if the user's full name is valid
    private boolean isUserNameValid(String userName) {
        if (userName.equals("")) {
            mEditTextUserNameCreate.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
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

    // Check if password is valid
    private boolean isPasswordValid(String password, String confirmPassword) {
        // Check if password is correct length
        if (password.length() < 6) {
            mEditTextPasswordCreate.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        // Check if both passwords match
        else if (!password.equals(confirmPassword)) {
            mEditTextPasswordConfirmCreate.setError(getResources().getString(R.string.error_confirm_password_not_valid));
            return false;
        }

        // If this point is reached, password is valid
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