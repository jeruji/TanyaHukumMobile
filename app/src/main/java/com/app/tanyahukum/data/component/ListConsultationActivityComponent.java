package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.ListConsultationActivity;
import com.app.tanyahukum.view.RegistrationActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = ListConsultationActivityModule.class)
public interface ListConsultationActivityComponent {
    void inject(ListConsultationActivity listConsultationActivity);
}
