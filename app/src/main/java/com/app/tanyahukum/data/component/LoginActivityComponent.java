package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.LoginActivityModule;
import com.app.tanyahukum.data.module.RegistrationActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.LoginActivity;
import com.app.tanyahukum.view.RegistrationActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

    @CustomScope
    @Component(dependencies = NetComponent.class,modules = LoginActivityModule.class)
    public interface LoginActivityComponent {
        void inject(LoginActivity loginActivity);
    }

