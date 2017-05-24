package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.ChangeImageProfileActivityModule;
import com.app.tanyahukum.data.module.EditAccountActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.ChangeImageProfileActivity;
import com.app.tanyahukum.view.EditAccountActivity;

import dagger.Component;

/**
 * Created by emerio on 4/8/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = ChangeImageProfileActivityModule.class)
public interface ChangeImageProfileActivityComponent {
    void inject(ChangeImageProfileActivity changeImageProfileActivity);
}
