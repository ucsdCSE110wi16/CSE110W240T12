package cse110.giftx;

import com.firebase.client.Firebase;

public class GiftX extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        Firebase.setAndroidContext(this);
    }
}
