package edu.ucsd.cse110wi16.giftexchange;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;

public class CreateGroup extends Activity {

    public final static String MIN_PRICE = "edu.ucsd.cse110wi16.giftexchange.MIN_PRICE";
    public final static String MAX_PRICE = "edu.ucsd.cse110wi16.giftexchange.MAX_PRICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
    }

    public void createGroup(View view) {
        Intent intent = new Intent(this, viewCurrentGroups.class);
        EditText min_price = (EditText) findViewById(R.id.min_price);
        EditText max_price = (EditText) findViewById(R.id.max_price);
        String minPrice = min_price.getText().toString();
        String maxPrice = min_price.getText().toString();
        //will put other stuff once group options are fully sorted out
        //also, debating whether to set min/max price as just recommendations
        //for one to include in a giant event description.
        intent.putExtra(MIN_PRICE, minPrice);
        intent.putExtra(MAX_PRICE, maxPrice);
        //startActivity(intent);
        //in actuality, this would send you to the home page with the new group visible
        //because it had been saved to firebase (and groups on the home page load from firebase)
        //so I don't think this would actually send anything using intents, just save to firebase.
    }
}
