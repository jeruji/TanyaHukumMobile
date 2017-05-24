package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.AcceptQuestionsActivityModule;
import com.app.tanyahukum.data.module.AddConsultationActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AcceptQuestionsActivity;
import com.app.tanyahukum.view.AddConsultationActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = AcceptQuestionsActivityModule.class)
public interface AcceptQuestionsActivityComponent {
    void inject(AcceptQuestionsActivity acceptQuestionsActivity);
}
