package com.app.tanyahukum.view;

import com.app.tanyahukum.model.User;

/**
 * Created by emerio on 4/19/17.
 */

public interface MyAccountActivityInterface {
    interface View{
        void showAccount(User user);
        void showProgress(boolean show);
        void editAccount();
        void changeImageProfile();
        void showImage(String url);
    }
    interface Presenter{
         void getUserById(String userId);
         String showImageStorage(String userId);

    }
}
