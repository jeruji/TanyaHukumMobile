package com.app.tanyahukum;

import android.app.Application;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;

import com.app.tanyahukum.data.component.DaggerNetComponent;
import com.app.tanyahukum.data.component.NetComponent;
import com.app.tanyahukum.data.module.AppModule;
import com.app.tanyahukum.data.module.NetModule;
import com.app.tanyahukum.util.MyPreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

/**
 * Created by emerio on 4/5/17.
 */

public class App extends Application {
    private NetComponent mNetComponent;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    public AppCompatActivity activity;
    private MyPreferenceManager pref;
    private static App mInstance;
    @Override
    public void onCreate(){
        super.onCreate();
        mNetComponent = DaggerNetComponent.builder().appModule(new AppModule(this)).netModule(new NetModule("https://kepo-5fcd5.firebaseio.com"))
                .build();
        mInstance = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static synchronized App getInstance() {
        return mInstance;
    }
    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public GoogleSignInOptions getGoogleSignInOptions(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        return gso;
    }
    public GoogleApiClient getGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener){
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this.activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
                .build();
        return mGoogleApiClient;
    }
    public MyPreferenceManager getPrefManager() {
        if (pref == null) {
            pref = new MyPreferenceManager(this);
        }

        return pref;
    }
}
