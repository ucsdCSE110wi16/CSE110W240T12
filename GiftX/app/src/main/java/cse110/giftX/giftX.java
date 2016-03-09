package cse110.giftX;

import com.firebase.client.Firebase;

public class giftX extends android.app.Application{

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Firebase
        Firebase.setAndroidContext(this);
    }
}
