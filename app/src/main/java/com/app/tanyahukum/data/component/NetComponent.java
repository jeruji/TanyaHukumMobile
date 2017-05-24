package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.AppModule;
import com.app.tanyahukum.data.module.NetModule;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Singleton;
import dagger.Component;

/**
 * Created by emerio on 4/5/17.
 */
@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    FirebaseDatabase firebase();
}
