package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import com.app.tanyahukum.view.QuestionsDetailActivityInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/8/17.
 */

@Module
public class QuestionsActivityModule {
    private final QuestionsDetailActivityInterface.View mView;
    private final Context mContext;
    public QuestionsActivityModule(QuestionsDetailActivityInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }

    @Provides
    @CustomScope
    QuestionsDetailActivityInterface.View providesQuestionsActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesQuestionsActivityContext(){
        return mContext;
    }

}
