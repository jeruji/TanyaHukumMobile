package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.LoginActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class LoginActivityModule {
    private final LoginActivityInterface.View mView;
    private final Context mContext;
    public LoginActivityModule(LoginActivityInterface.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Provides
    @CustomScope
    LoginActivityInterface.View providesLoginActivityInterfaceView () {
        return mView;
    }

    @Provides
    @CustomScope
    Context providesLoginActivityContext () {
        return mContext;
    }
}
