package com.app.tanyahukum.view;

import com.app.tanyahukum.model.User;

/**
 * Created by emerio on 4/6/17.
 */

public interface RegistrationActivityInterface {
    interface View{
        void submit();
        void clear();
        void registrationResult(String result,boolean status);
        void detailRegistrationResult(boolean status);
        void showProgressBar(String message);
        void hideProgressBar();
        void showPassword();
        void hidePassword();
        void toLoginPage();
    }
    interface Presenter{
        int checkValidation(String name,String gender,String bornDate,String email, String password,String province, String city,String address,String phone,String usertType );
        void submitRegistration(String loginType,String userId,String name,String gender,String bornDate,String email, String password,String province, String city,String address,String phone,String usertType );
        void submitUserDetail(User user);
    }
}
