package com.app.tanyahukum.data.component;

import com.app.tanyahukum.data.module.BalanceActivityModule;
import com.app.tanyahukum.util.CustomScope;
import com.app.tanyahukum.view.BalanceActivity;

import dagger.Component;

/**
 * Created by echosimanjuntak on 6/30/17.
 */

@CustomScope
@Component(dependencies = NetComponent.class,modules = BalanceActivityModule.class)
public interface BalanceActivityComponent {
    void inject(BalanceActivity balanceActivity);
}
