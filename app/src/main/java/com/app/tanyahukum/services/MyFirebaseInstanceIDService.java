package com.app.tanyahukum.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.util.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenFirebase=FirebaseInstanceId.getInstance().getToken();
        App.getInstance().getPrefManager().addTokenFirebase(tokenFirebase);
        sendRegistrationToServer(tokenFirebase);
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", tokenFirebase);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
    private void sendRegistrationToServer(final String token) {
        Log.e(TAG, "sendRegistrationToServer: " + token);
        User user = App.getInstance().getPrefManager().getUser();
        if (user == null) {
            return;
        }
    }
}

