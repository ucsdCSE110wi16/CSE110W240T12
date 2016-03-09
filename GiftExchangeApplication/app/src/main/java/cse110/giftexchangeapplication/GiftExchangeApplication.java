package cse110.giftexchangeapplication;

import com.firebase.client.Firebase;

public class GiftExchangeApplication extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        Firebase.setAndroidContext(this);
    }
}
