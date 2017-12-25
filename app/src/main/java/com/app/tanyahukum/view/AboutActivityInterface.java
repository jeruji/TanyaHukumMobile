package com.app.tanyahukum.view;

/**
 * Created by echosimanjuntak on 6/29/17.
 */

public interface AboutActivityInterface {
    interface View{
        void setWebViewContent();
        void btnBackFunction();
    }

    interface Presenter{
        void closeAbout();
    }
}
