package cse110.giftexchangeapplication.ui.createAccount;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.login.LoginActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class CreateAcctActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Create New Account");
        setSupportActionBar(toolbar);


    }

    public void createNewAccount(View view) {
        EditText editText = (EditText) findViewById(R.id.editText3);
        String email = editText.getText().toString();

        EditText editText1 = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String pass = editText1.getText().toString();
        String passConfirm = editText2.getText().toString();

        if(!pass.equals(passConfirm)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hello") //
                    .setMessage("password is not the same") //
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }) //
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
            return;
        }


        //add to firebase
        Firebase ref = new Firebase("https://giftexchangeapp.firebase.io");
        ref.createUser(email, pass, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                finish();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                System.out.println("ERROR: " + firebaseError.getMessage());
            }
        });
    }

    public void goLogin(View view){
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

}


