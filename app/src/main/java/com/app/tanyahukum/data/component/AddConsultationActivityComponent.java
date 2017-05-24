package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.AddConsultationActivityModule;
import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AddConsultationActivity;
import com.app.tanyahukum.view.ListConsultationActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = AddConsultationActivityModule.class)
public interface AddConsultationActivityComponent {
    void inject(AddConsultationActivity addConsultationActivity);
}
