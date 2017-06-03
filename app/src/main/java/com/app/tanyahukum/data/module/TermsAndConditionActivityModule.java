package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.TermsAndConditionActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by echosimanjuntak on 6/2/17.
 */
@Module
public class TermsAndConditionActivityModule {

    private final TermsAndConditionActivityInterface.View mView;
    private final Context mContext;
    public TermsAndConditionActivityModule(TermsAndConditionActivityInterface.View view, Context context) {
        this.mView = view;
        this.mContext = context;
    }

    @Provides
    @CustomScope
    TermsAndConditionActivityInterface.View providesTermsAndConditionActivityInterfaceView () {
        return mView;
    }

    @Provides
    @CustomScope
    Context providesTermsAndConditionActivityContext () {
        return mContext;
    }

}
