package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AcceptQuestionsActivityInterface;
import com.app.tanyahukum.view.AddConsultationActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class AcceptQuestionsActivityModule {
    private final AcceptQuestionsActivityInterface.View mView;
    private final Context mContext;
    public AcceptQuestionsActivityModule(AcceptQuestionsActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    AcceptQuestionsActivityInterface.View provideAcceptQuestionsnActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesAcceptQuestionsActivityContext(){
        return mContext;
    }


}
