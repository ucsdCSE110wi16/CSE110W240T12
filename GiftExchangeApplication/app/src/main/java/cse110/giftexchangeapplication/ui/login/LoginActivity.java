package cse110.giftexchangeapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.BaseActivity;
import cse110.giftexchangeapplication.ui.MainActivity;
import cse110.giftexchangeapplication.utils.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private Firebase ref;
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        ref = new Firebase(Constants.FIREBASE_URL);
        mEmailView = (EditText) findViewById(R.id.edit_text_email);
        mPasswordView = (EditText) findViewById(R.id.edit_text_password);

        Button mEmailSignInButton = (Button) findViewById(R.id.login_with_password);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // Not a button TODO
        TextView mCreateAccount = (TextView) findViewById(R.id.tv_sign_up);
        mCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateAcctActivity();
            }
        });

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        //Checks if email is valid
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError("This field is required!");
            cancel = true;
        }
        else if(!isEmailValid(email)) {
            mEmailView.setError("This email address is invalid");
            cancel = true;
        }
        //Checks if password is valid
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError("This field is required!");
            cancel = true;
        }
        else if(!isPasswordValid(password)) {
            mPasswordView.setError("This password is too short!");
            cancel = true;
        }
        if(!cancel){
            ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    startHomeActivity();
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    if(firebaseError.getCode() == FirebaseError.USER_DOES_NOT_EXIST) {
                        mEmailView.setError("Email does not exist.");
                    }
                    else if(firebaseError.getCode() == FirebaseError.INVALID_PASSWORD) {
                        mPasswordView.setError("Password is incorrect.");
                    }
                    else {
                        Toast toast = Toast.makeText(LoginActivity.this, "An error has occured!", Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        if( v != null) {
                            v.setGravity(Gravity.CENTER);
                            toast.show();
                        }
                    }
                }
            });
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    public void startHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void startCreateAcctActivity(){
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

}

