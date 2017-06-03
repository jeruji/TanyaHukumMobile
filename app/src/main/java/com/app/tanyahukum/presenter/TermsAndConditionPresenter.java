package com.app.tanyahukum.presenter;

import android.content.Context;
import android.widget.CheckBox;

import com.app.tanyahukum.view.TermsAndConditionActivityInterface;

import javax.inject.Inject;

/**
 * Created by echosimanjuntak on 6/2/17.
 */

public class TermsAndConditionPresenter implements TermsAndConditionActivityInterface.Presenter {

    TermsAndConditionActivityInterface.View view;
    Context context;

    @Inject
    public TermsAndConditionPresenter(TermsAndConditionActivityInterface.View view, Context context) {

        this.view = view;
        this.context = context;
    }

    @Override
    public boolean checkCheckedAgreeBox(CheckBox termsCheckBox) {

        if(termsCheckBox.isChecked())
        {
            return true;
        }

        return false;
    }
}
