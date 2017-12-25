package com.app.tanyahukum.presenter;

import android.content.Context;

import com.app.tanyahukum.view.BalanceActivityInterface;

import javax.inject.Inject;

/**
 * Created by echosimanjuntak on 6/30/17.
 */

public class BalancePresenter implements BalanceActivityInterface.Presenter {

    BalanceActivityInterface.View view;
    Context context;

    @Inject
    public BalancePresenter(BalanceActivityInterface.View view, Context context) {

        this.view = view;
        this.context = context;
    }

}
