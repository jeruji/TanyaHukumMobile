package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.ChangeImageProfileActivityInterface;
import com.app.tanyahukum.view.EditAccountInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class ChangeImageProfileActivityModule {
    private final ChangeImageProfileActivityInterface.View mView;
    private final Context mContext;
    public ChangeImageProfileActivityModule(ChangeImageProfileActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    ChangeImageProfileActivityInterface.View providesChangeImageProfileActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesChangeImageProfileActivityContext(){
        return mContext;
    }

}
