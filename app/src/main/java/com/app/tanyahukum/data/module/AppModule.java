package com.app.tanyahukum.data.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by emerio on 4/5/17.
 */
@Module
public class AppModule {
    Application application;
    public AppModule(Application mApplication){
        this.application=mApplication;
    }
    @Provides
    @Singleton
    Application providesApplication(){
        return application;
    }
}
