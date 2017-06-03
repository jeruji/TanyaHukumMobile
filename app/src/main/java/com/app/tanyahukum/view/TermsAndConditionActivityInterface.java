package com.app.tanyahukum.view;

import android.widget.CheckBox;

/**
 * Created by echosimanjuntak on 6/2/17.
 */

public interface TermsAndConditionActivityInterface {

    interface View{
        void continueToRegistration();
        void setWebViewContent();
    }

    interface Presenter{
        boolean checkCheckedAgreeBox(CheckBox termsCheckBox);
    }

}
