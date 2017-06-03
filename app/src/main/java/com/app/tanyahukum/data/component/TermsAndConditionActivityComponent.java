package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.TermsAndConditionActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.TermsAndConditionActivity;

import dagger.Component;

/**
 * Created by echosimanjuntak on 6/2/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = TermsAndConditionActivityModule.class)
public interface TermsAndConditionActivityComponent {
    void inject(TermsAndConditionActivity termsAndConditionActivity);
}
