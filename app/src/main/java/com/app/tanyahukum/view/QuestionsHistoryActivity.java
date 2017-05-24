package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.adapter.QuestionsAdapter;
import com.app.tanyahukum.adapter.QuestionsHistoryAdapter;
import com.app.tanyahukum.data.component.DaggerQuestionsHistoryComponent;
import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.data.module.QuestionsHistoryModule;
import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.presenter.ListConsultationPresenter;
import com.app.tanyahukum.presenter.QuestionsHistoryPresenter;
import com.app.tanyahukum.util.Config;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by emerio on 4/18/17.
 */

public class QuestionsHistoryActivity extends AppCompatActivity implements QuestionsHistoryInterface.View, QuestionsHistoryAdapter.QuestionsAdapterCallback {
    @Inject
    QuestionsHistoryPresenter questionsHistoryPresenter;
    @BindView(R.id.recycler_view_history_questions)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_no_questions)
    TextView mTextNoQuestions;
    @BindView(R.id.tvConsultantName)
    TextView consultantName;
    @BindView(R.id.tvClientName)
    TextView clientName;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @Inject
    QuestionsHistoryAdapter questionsAdapter;
    @BindView(R.id.toolbar)
    Toolbar toolbarList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_history_questions);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarList);
        getSupportActionBar().setTitle("History");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerQuestionsHistoryComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .questionsHistoryModule(new QuestionsHistoryModule(this, this))
                .build().inject(this);
        Intent i=getIntent();
        String questionsId=i.getStringExtra("questionsId");
        clientName.setText(i.getStringExtra("clientName"));
        consultantName.setText(i.getStringExtra("consultantName"));
        questionsHistoryPresenter.getHistoryQuestions(questionsId);
        questionsAdapter.setCallback(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(questionsAdapter);
    }

    @Override
    public void onQuestionsClicked(HistoryConsultations consultations) {

    }

    @Override
    public void showQuestions(List<HistoryConsultations> questions) {
        mTextNoQuestions.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        questionsAdapter.setMovies(questions);
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
}
