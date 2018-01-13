package com.app.tanyahukum.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.adapter.QuestionsAdapter;
import com.app.tanyahukum.data.component.DaggerListConsultationActivityComponent;
import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.presenter.ListConsultationPresenter;
import com.app.tanyahukum.util.Config;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class ListConsultationActivity extends AppCompatActivity implements ListConsultationInterface.View, QuestionsAdapter.QuestionsAdapterCallback  {
    @Inject
    ListConsultationPresenter listConsultationPresenter;
    @Inject
    QuestionsAdapter questionsAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbarList;
    @BindView(R.id.recycler_view_questions)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_no_questions)
    TextView mTextNoQuestions;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.fab)
    FloatingActionButton _fab;
    String userid;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_consultation_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarList);
        getSupportActionBar().setTitle("Consultation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        DaggerListConsultationActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .listConsultationActivityModule(new ListConsultationActivityModule(this, this))
                .build().inject(this);

       load();
    }

    public void load(){
        userid=App.getInstance().getPrefManager().getUser().getId();
        listConsultationPresenter.getQuestionsByUser(userid);
        questionsAdapter.setCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(questionsAdapter);
        if (Config.USER_TYPE.equals("CONSULTANT")){
            Log.d("masuk konsultan","test");
            _fab.setVisibility(View.GONE);
        }
    }
    @Override
    @OnClick(R.id.fab)
    public void toConsultationForm() {
        Intent intent = new Intent();
        intent.putExtra("userid",userid);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("statusConsultation","NEW");
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void showQuestions(List<Consultations> questions) {
        mTextNoQuestions.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        questionsAdapter.setQuestions(questions);
        questionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showQuestionsProgress(boolean show) {
        if (show && questionsAdapter.getItemCount() == 0) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void showEmptyMessage() {
        mTextNoQuestions.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void deleteWarningMessage(final String consultationId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure to delete this consultation?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                listConsultationPresenter.deleteConsultationById(consultationId);
                dialog.dismiss();
                recreate();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onQuestionsClicked(Consultations consultations) {
        if (consultations.getStatus().equalsIgnoreCase("PENDING")){
            Intent intent = new Intent();
            intent.putExtra("userid",userid);
            intent.putExtra("title",consultations.getTitle());
            intent.putExtra("consultationType",consultations.getConsultationsType());
            intent.putExtra("consultationId",consultations.getConsultationId());
            intent.putExtra("consultantId",consultations.getConsultantId());
            intent.putExtra("questions",consultations.getQuestions());
            intent.putExtra("date",consultations.getConsultationsDate());
            intent.putExtra("status",consultations.getStatus());
            intent.putExtra("answers",consultations.getAnswers());
            intent.putExtra("expertRecomendation",consultations.getExpertRecomendations());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("statusConsultation","PENDING");
            intent.putExtra("title",consultations.getTitle());
            intent.putExtra("questions",consultations.getQuestions());
            intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.putExtra("title", consultations.getTitle());
            intent.putExtra("consultationType", consultations.getConsultationsType());
            intent.putExtra("consultationId", consultations.getConsultationId());
            intent.putExtra("consultantId", consultations.getConsultantId());
            intent.putExtra("consultantName", consultations.getConsultantName());
            intent.putExtra("historyId", consultations.getHistoryId());
            intent.putExtra("consultantId", consultations.getConsultantId());
            intent.putExtra("id", consultations.getConsultationId());
            intent.putExtra("clientId", consultations.getClientId());
            intent.putExtra("clientName", consultations.getClientName());
            intent.putExtra("questions", consultations.getQuestions());
            intent.putExtra("answers", consultations.getAnswers());
            intent.putExtra("date", consultations.getConsultationsDate());
            intent.putExtra("status", consultations.getStatus());
            intent.putExtra("statusAppointment", consultations.getStatusAppointment());
            intent.putExtra("expertRecomendation", consultations.getExpertRecomendations());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.QuestionsDetailActivity");
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteConsultationClicked(String consultationId) {
        deleteWarningMessage(consultationId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
