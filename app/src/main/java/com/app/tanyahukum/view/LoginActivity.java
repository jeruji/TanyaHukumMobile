package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerLoginActivityComponent;
import com.app.tanyahukum.data.module.LoginActivityModule;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.LoginPresenter;
import com.app.tanyahukum.presenter.RegistrationPresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/8/17.
 */

public class LoginActivity extends AppCompatActivity implements LoginActivityInterface.View ,GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.titleProgress)
    TextView titleProgressBar;
    @BindView(R.id.edTextEmail)
    EditText email;
    @BindView(R.id.edTextPassword)
    EditText password;
    @BindView(R.id.button_facebook_sign_in)
    LoginButton facebookLogin;
    @BindView(R.id.visibility)
    ImageView _showPassword;
    @BindView(R.id.invisibility)
    ImageView _hidePassword;

    @Inject
    LoginPresenter loginPresenter;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private CallbackManager callbackManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login_layout);
        ButterKnife.bind(this);
        checkSession();
        DaggerLoginActivityComponent.builder()
       .netComponent(((App) getApplicationContext()).getNetComponent())
                .loginActivityModule(new LoginActivityModule(this, this))
                .build().inject(this);
        gso = ((App) getApplication()).getGoogleSignInOptions();
        mGoogleApiClient = ((App) getApplication()).getGoogleApiClient(LoginActivity.this, this);
        callbackManager = CallbackManager.Factory.create();
        facebookLogin.setReadPermissions("email", "public_profile");
        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());

                                User userFb=new User();
                                try {
                                    if (!loginPresenter.checkEmailExist(object.getString("email"))){
                                        userFb.setName(object.getString("name"));
                                        userFb.setEmail(object.getString("email"));
                                        loginPresenter.submitLoginByFacebook(userFb,loginResult.getAccessToken());
                                    }else{
                                        Log.d("email exist","true");
                                        loginPresenter.getUserDataByEmail(object.getString("email"));
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    @OnClick(R.id.btnLogin)
    public void submitLoginEmailWithPassword() {
        String emailStr = email.getText().toString();
        String passwordStr = password.getText().toString();
        int statusValidation=loginPresenter.checkValidation(emailStr,passwordStr);
        if(statusValidation==1) {
            email.setError("Please correct your email address");
        }
        else
        if(statusValidation==2) {
            password.setError("Please correct your password");
        }
        else
        loginPresenter.submitLoginByEmailPassword(emailStr, passwordStr);

    }
    @Override
    @OnClick(R.id.sign_in_button)
    public void submitLoginEmail() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d("masuk : ","masuk");
                GoogleSignInAccount account = result.getSignInAccount();
                loginPresenter.submitLoginByEmail(account);
            } else {
                Toast.makeText(getApplicationContext(), "gagal login gmail", Toast.LENGTH_LONG).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }
    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    public void submitLoginFacebook() {
    }
    @Override
    public void submitResult(boolean result) {
        if (!result){
            Toast.makeText(getApplicationContext(),"Please correct email and password",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
        titleProgressBar.setVisibility(View.VISIBLE);
        titleProgressBar.setText("Sedang login..");
    }

    @Override
    public void registrationByGmail(GoogleSignInAccount acct,String id, boolean status) {
        if(status) {
            User user = new User();
            user.setName(acct.getDisplayName());
            user.setEmail(acct.getEmail());
            user.setId(id);
            loginPresenter.submitUserDetail(user);
        }
        else
            Toast.makeText(this, "failed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void registrationByFacebook(User user,String id, boolean status) {
        user.setId(id);
        loginPresenter.submitUserDetail(user);
    }
    @Override
    public void showNotConnected() {

    }
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        titleProgressBar.setVisibility(View.GONE);
    }
    @Override
    public void toDashboardPage(User user,String loginType) {
        if (loginType.equals("FACEBOOK")||loginType.equals("GMAIL")){
                Intent intent = new Intent();
                intent.putExtra("loginType",loginType);
                intent.putExtra("userId",user.getId());
                intent.putExtra("name",user.getName());
                intent.putExtra("email",user.getEmail());
                intent.setClassName(this, "com.app.tanyahukum.view.RegistrationActivity");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

        }else{
            Intent intent = new Intent();
            intent.putExtra("loginType",loginType);
            intent.setClassName(this, "com.app.tanyahukum.view.DashboardActivity");
            App.getInstance().getPrefManager().storeUser(user);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }


    }

    private void checkSession(){
        if (App.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }
    }
    @Override
    @OnClick(R.id.createAccount)
    public void toRegistrationPage() {
        Intent intent = new Intent();
        intent.putExtra("loginType","");
        intent.putExtra("userId","userid");
        intent.putExtra("name","");
        intent.putExtra("email","");
        intent.setClassName(this, "com.app.tanyahukum.view.RegistrationActivity");
        startActivity(intent);
    }

    @Override
    @OnClick(R.id.visibility)
    public void showPassword() {
        password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        _showPassword.setVisibility(View.GONE);
        _hidePassword.setVisibility(View.VISIBLE);
    }

    @Override
    @OnClick(R.id.invisibility)
    public void hidePassword() {
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        _showPassword.setVisibility(View.VISIBLE);
        _hidePassword.setVisibility(View.GONE);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
