package com.app.tanyahukum.data.module;

import android.content.Context;

import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.QuestionsDetailActivityInterface;
import com.app.tanyahukum.view.QuestionsHistoryInterface;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/18/17.
 */
@Module
public class QuestionsHistoryModule  {
    private final QuestionsHistoryInterface.View mView;
    private final Context mContext;
    public QuestionsHistoryModule(QuestionsHistoryInterface.View view, Context context){
        this.mView=view;
        this.mContext=context;
    }
    @Provides
    @CustomScope
    QuestionsHistoryInterface.View providesQuestionsHistoryActivityInterfaceView(){
        return mView;
    }
    @Provides
    @CustomScope
    Context providesQuestionsHistoryActivityContext(){
        return mContext;
    }
}
