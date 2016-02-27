package cse110.giftexchangeapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
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

public class CreateAccountActivity extends BaseActivity {

    private Firebase ref;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Instantiate variables to their XML objects
        ref = new Firebase(Constants.FIREBASE_URL);
        mFirstNameView = (EditText) findViewById(R.id.edit_text_first_name_create);
        mLastNameView = (EditText) findViewById(R.id.edit_text_user_last_name_create);
        mEmailView = (EditText) findViewById(R.id.edit_text_email_create);
        mPasswordView = (EditText) findViewById(R.id.edit_text_password_create);
        mPasswordConfirmView = (EditText) findViewById(R.id.edit_text_confirm_password_create);
        //Add functionality to button.
        Button mRegisterButton = (Button) findViewById(R.id.btn_create_account_final);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewAccount();
            }
        });
    }

    public void createNewAccount() {
        //Clear all error messages
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);
        boolean cancel = false;

        final String firstName = mFirstNameView.getText().toString();
        final String lastName = mLastNameView.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String passwordConfirm = mPasswordConfirmView.getText().toString();

        //Checks validity of all text fields
        if(firstName.equals("")) {
            mFirstNameView.setError("This field is required!");
            cancel = true;
        }
        if(lastName.equals("")) {
            mLastNameView.setError("This field is required!");
            cancel = true;
        }
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError("This field is required!");
            cancel = true;
        }
        else if(!isEmailValid(email)) {
            mEmailView.setError("This email address is invalid");
            cancel = true;
        }
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError("This field is required!");
            cancel = true;
        }
        else if(!isPasswordValid(password)) {
            mPasswordView.setError("This password is too short!");
            cancel = true;
        }
        else if(!password.equals(passwordConfirm)) {
            mPasswordConfirmView.setError("The passwords do not match!");
            cancel = true;
        }

        if(TextUtils.isEmpty(passwordConfirm)) {
            mPasswordConfirmView.setError("This field is required!");
            cancel = true;
        }

        if(!cancel)
        {
            AuthData authData = ref.getAuth();
            if(authData != null) {
                ref.unauth();
            }
            ref.createUser(email, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                             ref.child("Users").child(authData.getUid()).child("email").setValue(email);
                             ref.child("Users").child(authData.getUid()).child("first_name").setValue(firstName);
                             ref.child("Users").child(authData.getUid()).child("last_name").setValue(lastName);

                             startHomeActivity();
                         }
                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                        }
                    });
                }
                public void onError(FirebaseError firebaseError) {
                    if(firebaseError.getCode() == FirebaseError.EMAIL_TAKEN) {
                        Toast toast = Toast.makeText(CreateAccountActivity.this, "Email is already taken.", Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        if( v != null) {
                            v.setGravity(Gravity.CENTER);
                            toast.show();
                        }
                    }
                    else {
                        Toast toast = Toast.makeText(CreateAccountActivity.this, "An error has occured!", Toast.LENGTH_SHORT);
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
            return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 5;
    }

    public void startHomeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }
}