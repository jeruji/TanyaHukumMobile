package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.RegistrationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class RegistrationActivityModule {
    private final RegistrationActivityInterface.View mView;
    private final Context mContext;
    public RegistrationActivityModule(RegistrationActivityInterface.View view,Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    RegistrationActivityInterface.View providesRegistrationActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesRegistrationActivityContext(){
        return mContext;
    }

}
