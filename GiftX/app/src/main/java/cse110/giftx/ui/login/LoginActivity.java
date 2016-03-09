package cse110.giftx.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import cse110.giftx.R;
import cse110.giftx.ui.BaseActivity;
import cse110.giftx.ui.MainActivity;
import cse110.giftx.utils.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private ProgressDialog mAuthProgressDialog;
    private Firebase ref;
    private EditText mEditTextEmailInput;
    private EditText mEditTextPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Create Firebase references
        ref = new Firebase(Constants.FIREBASE_URL);

        // Link layout elements
        initializeScreen();

        mEditTextPasswordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getAction() == KeyEvent.ACTION_DOWN) {
                    attemptLogin();
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * Override onCreateOptionsMenu to inflate nothing
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        return true;
    }

    public void onSignInPressed(View view) {
        attemptLogin();
    }

    /*
     * Open CreateAccountActivity when the user taps on "Sign up"
     */
    public void onSignUpPressed(View view) {
        Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
        startActivity(intent);
    }

    /**
     * Initialize all the elements on the screen
     */
    public void initializeScreen() {
        mEditTextEmailInput = (EditText) findViewById(R.id.edit_text_email);
        mEditTextPasswordInput = (EditText) findViewById(R.id.edit_text_password);

        LinearLayout linearLayoutLoginActivity = (LinearLayout) findViewById(R.id.linear_layout_login_activity);
        initializeBackground(linearLayoutLoginActivity);

        // Set up a progress dialog
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle(getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage(getString(R.string.progress_dialog_authenticating_with_firebase));
        mAuthProgressDialog.setCancelable(false);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mEditTextEmailInput.setError(null);
        mEditTextPasswordInput.setError(null);

        final String email = mEditTextEmailInput.getText().toString();
        final String password = mEditTextPasswordInput.getText().toString();

        boolean cancel = areInputsValid(email, password);

        if(!cancel){
            mAuthProgressDialog.show();
            ref.authWithPassword(email, password, new MyAuthResultHandler(Constants.PASSWORD_PROVIDER));
        }
    }

    /**
     * When user taps "Done" check password is valid
     */
    boolean areInputsValid(String email, String password) {
        boolean cancel = false;

        if(email.equals("")) {
            mEditTextEmailInput.setError(getString(R.string.error_cannot_be_empty));
            cancel = true;
        }

        if(password.equals("")) {
            mEditTextPasswordInput.setError(getString(R.string.error_cannot_be_empty));
            cancel = true;
        }
        if(!isPasswordValid(password)) {
            mEditTextPasswordInput.setError(getString(R.string.error_invalid_password_not_valid));
            cancel = true;
        }

        return cancel;
    }

    /**
     * Handles the user authentication if the inputs pass the first check
     */

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {

        public MyAuthResultHandler(String provider) {
            // Required empty constructor
        }

        /**
         * On SUCCESSFUL authentication
         * @param authData
         */
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.dismiss();

            if (authData != null) {
                // Go to the main activity
                startMainActivity();
            }
        }

        /**
         * On NON SUCCESSFULL authentication
         * @param firebaseError
         *
         */
        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.dismiss();

            /**
             * Check the network connection state, & check for other
             * login errors & throw Firebase's errors
             */

            switch (firebaseError.getCode()) {
                case FirebaseError.INVALID_EMAIL:
                case FirebaseError.USER_DOES_NOT_EXIST:
                    mEditTextEmailInput.setError(getString(R.string.error_message_email_issue));
                    break;

                case FirebaseError.INVALID_PASSWORD:
                    mEditTextPasswordInput.setError(firebaseError.getMessage());
                    break;

                case FirebaseError.NETWORK_ERROR:
                    showErrorToast(getString(R.string.error_message_failed_sign_in_no_network));
                    break;

                default:
                    showErrorToast(firebaseError.toString());

            }
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Helper method used to start the MainActivity
     */
    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // Show the Toast with the error
    private void showErrorToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}

