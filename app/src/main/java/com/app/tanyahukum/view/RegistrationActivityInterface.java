package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Province;
import com.app.tanyahukum.model.Regency;
import com.app.tanyahukum.model.User;

import java.util.ArrayList;
import java.util.List;

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
        void showSpinnerProvince(List <Province> provList);
        void showSpinnerRegency(List <Regency> regencyList,String tipe);
        void  selectProvinsi();
        void  selectRegency();
    }

    interface Presenter{
        int checkValidation(String name,String gender,String bornDate,String email, String password,String province, String city,String address,String phone,String usertType );
        void submitRegistration(String loginType,String userId,String name,String gender,String bornDate,String email, String password,String province, String city,String address,String phone,String usertType );
        void submitUserDetail(User user);
        void getProvince();
        void getRegencies(Long provinceId,String tipe);
    }
}
