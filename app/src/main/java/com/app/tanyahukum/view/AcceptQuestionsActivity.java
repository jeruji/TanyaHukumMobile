package com.app.tanyahukum.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAcceptQuestionsActivityComponent;
import com.app.tanyahukum.data.component.DaggerDashboardActivityComponent;
import com.app.tanyahukum.data.module.AcceptQuestionsActivityModule;
import com.app.tanyahukum.data.module.DashboardActivityModule;
import com.app.tanyahukum.presenter.AcceptQuestionsPresenter;
import com.app.tanyahukum.presenter.AddConsultationPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/12/17.
 */

public class AcceptQuestionsActivity  extends AppCompatActivity implements AcceptQuestionsActivityInterface.View{
    String consultationId="";
    @BindView(R.id.labelTitle)
    TextView _title;
    @BindView(R.id.questions)
    TextView _questions;
    @Inject
    AcceptQuestionsPresenter acceptQuestionsPresenter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_consultation_layout);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbarForm);
        DaggerAcceptQuestionsActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .acceptQuestionsActivityModule((new AcceptQuestionsActivityModule(this,this)))
                .build().inject(this);
        Intent i=getIntent();
        consultationId=i.getStringExtra("consultationId");
        _title.setText(i.getStringExtra("title"));
        _questions.setText(i.getStringExtra("questions"));

    }

    @Override
    @OnClick(R.id.btnAcceptQuestions)
    public void accept() {
        acceptQuestionsPresenter.updateQuestions(consultationId);

    }

    @Override
    public void toDashboard() {
        Intent intent = new Intent();
        intent.putExtra("consultationId",consultationId);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AcceptQuestionsActivity.this.finish();
        intent.setClassName(this, "com.app.tanyahukum.view.QuestionsDetailActivity");
        startActivity(intent);
    }

    @Override
    public void infoDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AcceptQuestionsActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(AcceptQuestionsActivity.this);
        }
        builder.setTitle("Info")
                .setMessage("Other Consultant was accepted questions.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AcceptQuestionsActivity.this,DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AcceptQuestionsActivity.this.finish();
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
