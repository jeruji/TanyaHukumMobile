package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerBalanceActivityComponent;
import com.app.tanyahukum.data.module.BalanceActivityModule;
import com.app.tanyahukum.presenter.BalancePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BalanceActivity extends AppCompatActivity implements BalanceActivityInterface.View {

    @Inject
    BalancePresenter balancePresenter;

    @BindView(R.id.toolbar)
    Toolbar toolbarList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        ButterKnife.bind(this);

        setSupportActionBar(toolbarList);
        getSupportActionBar().setTitle("Balance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        DaggerBalanceActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .balanceActivityModule(new BalanceActivityModule(this, this))
                .build().inject(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
