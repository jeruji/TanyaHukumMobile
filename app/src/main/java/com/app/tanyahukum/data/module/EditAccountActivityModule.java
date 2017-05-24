package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.EditAccountInterface;
import com.app.tanyahukum.view.MyAccountActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class EditAccountActivityModule {
    private final EditAccountInterface.View mView;
    private final Context mContext;
    public EditAccountActivityModule(EditAccountInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    EditAccountInterface.View providesEditAccountActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesMyAccountActivityContext(){
        return mContext;
    }

}
