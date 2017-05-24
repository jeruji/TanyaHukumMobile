package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.ListAppointmentActivityInterface;
import com.app.tanyahukum.view.RegistrationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class ListAppointmnetActivityModule {
    private final ListAppointmentActivityInterface.View mView;
    private final Context mContext;
    public ListAppointmnetActivityModule(ListAppointmentActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    ListAppointmentActivityInterface.View providesListAppointmentActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesListAppointmentActivityContext(){
        return mContext;
    }

}
