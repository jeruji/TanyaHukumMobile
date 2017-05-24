package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AddAppointmentActivityInterface;
import com.app.tanyahukum.view.RegistrationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class AddAppointmentActivityModule {
    private final AddAppointmentActivityInterface.View mView;
    private final Context mContext;
    public AddAppointmentActivityModule(AddAppointmentActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    AddAppointmentActivityInterface.View providesAddAppointmentActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesAddAppointmentActivityContext(){
        return mContext;
    }

}
