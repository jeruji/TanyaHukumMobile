package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AboutActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by echosimanjuntak on 6/29/17.
 */

@Module
public class AboutActivityModule {

    private final AboutActivityInterface.View mView;
    private final Context mContext;
    public AboutActivityModule(AboutActivityInterface.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Provides
    @CustomScope
    AboutActivityInterface.View providesAboutActivityInterfaceView () {
        return mView;
    }

    @Provides
    @CustomScope
    Context providesAboutActivityContext () {
        return mContext;
    }

}
