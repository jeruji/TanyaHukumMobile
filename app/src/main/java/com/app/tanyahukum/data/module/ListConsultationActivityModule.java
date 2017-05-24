package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.ListConsultationInterface;
import com.app.tanyahukum.view.RegistrationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class ListConsultationActivityModule {
    private final ListConsultationInterface.View mView;
    private final Context mContext;
    public ListConsultationActivityModule(ListConsultationInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    ListConsultationInterface.View providesListConsultationActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesListConsultationActivityContext(){
        return mContext;
    }

}
