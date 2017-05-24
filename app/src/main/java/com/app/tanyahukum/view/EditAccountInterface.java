package com.app.tanyahukum.view;

import com.app.tanyahukum.model.User;

/**
 * Created by emerio on 4/20/17.
 */

public interface EditAccountInterface {
    interface View{
        void showAccount(User user);
        void showProgress(boolean show);
        void update();
        void toMyAccountPage(boolean result);
    }
    interface Presenter{
        void getUserById(String userId);
        void saveUpdatedAccount(String userId,User user);
    }
}
