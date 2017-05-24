package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.QuestionsActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.QuestionsDetailActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = QuestionsActivityModule.class)
public interface QuestionsDetailActivityComponent {
    void inject(QuestionsDetailActivity questionsDetailActivity);
}
