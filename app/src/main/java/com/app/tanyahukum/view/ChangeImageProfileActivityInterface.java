package com.app.tanyahukum.view;

/**
 * Created by emerio on 4/20/17.
 */

public interface ChangeImageProfileActivityInterface {
    interface View{
        void upload();
        void uploadResult(boolean result);
        void showProgress(boolean show);
        void showImage(String url);
    }
    interface Presenter{

    }
}
