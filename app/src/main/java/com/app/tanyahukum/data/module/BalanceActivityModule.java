package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.BalanceActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by echosimanjuntak on 6/30/17.
 */

@Module
public class BalanceActivityModule {

    private final BalanceActivityInterface.View mView;
    private final Context mContext;
    public BalanceActivityModule(BalanceActivityInterface.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Provides
    @CustomScope
    BalanceActivityInterface.View providesBalanceActivityInterfaceView () {
        return mView;
    }

    @Provides
    @CustomScope
    Context providesBalanceActivityContext () {
        return mContext;
    }

}
