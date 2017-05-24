package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.QuestionsHistoryModule;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.QuestionsHistoryActivity;
import com.app.tanyahukum.view.RegistrationActivity;

import dagger.Component;

/**
 * Created by emerio on 4/19/17.
 */


@CustomScope
@Component(dependencies = NetComponent.class,modules = QuestionsHistoryModule.class)
public interface QuestionsHistoryComponent {
    void inject(QuestionsHistoryActivity questionsHistoryActivity);
}
