package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.AboutActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.AboutActivity;

import dagger.Component;

/**
 * Created by echosimanjuntak on 6/29/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = AboutActivityModule.class)
public interface AboutActivityComponent {
    void inject(AboutActivity aboutActivity);
}
