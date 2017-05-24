package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AddConsultationActivity;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class AddConsultationActivityModule {
    private final AddConsultationActivityInterface.View mView;
    private final Context mContext;
    public AddConsultationActivityModule(AddConsultationActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    AddConsultationActivityInterface.View providesAddConsultationActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesAddConsultationActivityContext(){
        return mContext;
    }

}
