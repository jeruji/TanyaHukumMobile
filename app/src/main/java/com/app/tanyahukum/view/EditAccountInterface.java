package com.app.tanyahukum.view;

import com.app.tanyahukum.model.Province;
import com.app.tanyahukum.model.Regency;
import com.app.tanyahukum.model.User;

import java.util.List;

/**
 * Created by emerio on 4/20/17.
 */

public interface EditAccountInterface {
    interface View{
        void showAccount(User user);
        void showProgress(boolean show);
        void update();
        void toMyAccountPage(boolean result);
        void cancel();
        void showSpinnerProvince(List<Province> provList,String tipe);
        void showSpinnerRegency(List <Regency> regencyList, String tipe);
        void  selectProvinsi();
        void  selectRegency();
    }
    interface Presenter{
        void getUserById(String userId);
        void saveUpdatedAccount(String userId,User user);
        void getProvince(String tipe);
        void getRegencies(Long provinceId,String tipe);
    }
}
