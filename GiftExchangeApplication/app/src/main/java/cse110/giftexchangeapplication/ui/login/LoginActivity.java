package cse110.giftexchangeapplication.ui.login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.AuthData;
import com.firebase.client.FirebaseError;

import cse110.giftexchangeapplication.R;
import cse110.giftexchangeapplication.ui.createAccount.CreateAcctActivity;
import cse110.giftexchangeapplication.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {
    String userID;
    public final static String UID = "edu.ucsd.cse110wi16.giftexchange.UID";
    Firebase ref;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userID = null;
        ref = new Firebase("https://giftexchangeapp.firebaseio.com");
        setContentView(R.layout.activity_login);

        TextView appName = (TextView) findViewById(R.id.txtAppTitle);
        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        appName.setText("GIFT EXCHANGE APP");
        appName.setTypeface(type);

        TextView appTagLine = (TextView) findViewById(R.id.txtAppTagline);
        Typeface type2 = Typeface.createFromAsset(getAssets(), "fonts/Avenir Next.ttc");
        appTagLine.setText("Exchanging gifts the easy way");
        appTagLine.setTypeface(type);



    }

    public void goCreate(View view){
        Intent createIntent = new Intent(this, CreateAcctActivity.class);
        startActivity(createIntent);
    }

    public void goAuth(View view){
        EditText username = (EditText) findViewById(R.id.editUsername);
        EditText passwordText = (EditText) findViewById(R.id.editPassword);

        //send username and pass

        email = username.getText().toString();
        String password = passwordText.getText().toString();

        //verify with firebase

        ref.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                userID = authData.getUid();

                ref.child("Users").child(authData.getUid()).child("email").setValue(email);
                ref.child("Users").child(authData.getUid()).child("name").setValue("hardcoded name");

                startHomeActivity();

            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
                System.out.print("Wrong Info");
            }
        });


    }

    public void startHomeActivity() {
        if(userID != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(UID, userID);
            startActivity(intent);
        }
    }

}
