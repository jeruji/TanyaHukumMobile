package com.app.tanyahukum.view;

import com.app.tanyahukum.model.User;
import com.facebook.AccessToken;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by emerio on 4/8/17.
 */

public interface LoginActivityInterface {
    interface View{
        void submitLoginEmailWithPassword();
        void submitLoginEmail();
        void submitLoginFacebook();
        void submitResult(boolean result);
        void showProgressBar(String message);
        void registrationByGmail(GoogleSignInAccount acct,String id,boolean status);
        void registrationByFacebook(User user,String id, boolean status);
        void showNotConnected();
        void hideProgressBar();
        void toDashboardPage(User user,String loginType);
        void toRegistrationPage();
        void showPassword();
        void hidePassword();
    }
    interface Presenter{
         int checkValidation(String email,String password);
        void submitLoginByEmailPassword(String email,String password);
        void submitLoginByEmail(GoogleSignInAccount acct);
        void submitLoginByFacebook(User user,AccessToken token);
        void submitUserDetail(User user);
        boolean checkEmailExist(String email);
    }
}
