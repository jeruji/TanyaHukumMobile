package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.EditAccountActivityModule;
import com.app.tanyahukum.data.module.MyAccountActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.EditAccountActivity;
import com.app.tanyahukum.view.MyAccountActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = EditAccountActivityModule.class)
public interface EditAccountActivityComponent {
    void inject(EditAccountActivity editAccountActivity);
}
