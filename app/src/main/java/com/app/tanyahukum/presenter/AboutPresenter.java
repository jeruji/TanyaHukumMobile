package com.app.tanyahukum.presenter;

import android.content.Context;
import android.content.Intent;

import com.app.tanyahukum.view.AboutActivityInterface;

import javax.inject.Inject;

/**
 * Created by echosimanjuntak on 6/29/17.
 */

public class AboutPresenter implements AboutActivityInterface.Presenter {

    AboutActivityInterface.View view;
    Context context;

    @Inject
    public AboutPresenter(AboutActivityInterface.View view, Context context) {

        this.view = view;
        this.context = context;
    }

    @Override
    public void closeAbout() {
        Intent intent = new Intent();
        intent.setClassName(context, "com.app.tanyahukum.view.DashboardActivity");
        context.startActivity(intent);
    }
}
