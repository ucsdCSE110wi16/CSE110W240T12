package edu.ucsd.cse110wi16.giftexchange;

import android.os.Bundle;
import android.app.Activity;

public class createAccount extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
