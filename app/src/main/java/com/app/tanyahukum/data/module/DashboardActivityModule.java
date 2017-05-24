package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.DashboardActivityInterface;
import com.app.tanyahukum.view.RegistrationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class DashboardActivityModule {
    private final DashboardActivityInterface.View mView;
    private final Context mContext;
    public DashboardActivityModule(DashboardActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    DashboardActivityInterface.View providesDashboardActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesDashboardActivityContext(){
        return mContext;
    }

}
