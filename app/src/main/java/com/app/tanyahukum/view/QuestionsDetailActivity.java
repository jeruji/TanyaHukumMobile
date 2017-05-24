package com.app.tanyahukum.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAcceptQuestionsActivityComponent;
import com.app.tanyahukum.data.component.DaggerAddConsultationActivityComponent;
import com.app.tanyahukum.data.component.DaggerQuestionsDetailActivityComponent;
import com.app.tanyahukum.data.module.AcceptQuestionsActivityModule;
import com.app.tanyahukum.data.module.AddConsultationActivityModule;
import com.app.tanyahukum.data.module.QuestionsActivityModule;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.presenter.AddConsultationPresenter;
import com.app.tanyahukum.presenter.QuestionDetailPresenter;
import com.app.tanyahukum.util.Config;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerio on 4/9/17.
 */

public class QuestionsDetailActivity extends AppCompatActivity implements QuestionsDetailActivityInterface.View {
    @BindView(R.id.imageUser)
    CircleImageView imageUser;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.labelUser)
    TextView labelUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar_;
    @BindView(R.id.title)
    TextView title_;
    @BindView(R.id.date)
    TextView consultationDate_;
    @BindView(R.id.questions)
    TextView questions_;
    @BindView(R.id.answersResult)
    TextView answers_;
    @BindView(R.id.consultationType)
    TextView consultationType;
    @BindView(R.id.status)
    TextView status_;
    @BindView(R.id.linearLayoutHistory)
    LinearLayout layoutButtonHistory;
    @BindView(R.id.linearLayoutAnswers)
    LinearLayout layoutButtonAnswers;
    @BindView(R.id.linearLayoutNext)
    LinearLayout layoutButtonNext;
    @BindView(R.id.linearLayoutMakeAppointmnet)
    LinearLayout layoutButtonMakeAppointment;
    @BindView(R.id.nextQuestions)
    AppCompatButton buttonNext;
    @BindView(R.id.badgeQuestions)
    TextView badgeQuestions_;
    @BindView(R.id.badgeAnswers)
    TextView badgeAnswers_;

    @Inject
    QuestionDetailPresenter questionDetailPresenter;
    Consultations consultations;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_detail_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("Detail Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerQuestionsDetailActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .questionsActivityModule((new QuestionsActivityModule(this,this)))
                .build().inject(this);
        Intent i=getIntent();
        consultations=new Consultations();
        consultations.setTitle(i.getStringExtra("title"));
        consultations.setConsultationsDate(i.getStringExtra("date"));
        consultations.setClientName(i.getStringExtra("clientName"));
        consultations.setConsultantName(i.getStringExtra("consultantName"));
        consultations.setQuestions(i.getStringExtra("questions"));
        consultations.setConsultationId(i.getStringExtra("consultationId"));
        consultations.setConsultationsType(i.getStringExtra("consultationType"));
        consultations.setConsultantId(i.getStringExtra("consultantId"));
        consultations.setStatus(i.getStringExtra("status"));
        consultations.setAnswers(i.getStringExtra("answers"));
        consultations.setConsultantId(i.getStringExtra("consultantId"));
        consultations.setExpertRecomendations(i.getStringExtra("expertRecomendation"));
        consultations.setHistoryId(i.getStringExtra("historyId"));
        consultations.setStatusAppointment(i.getStringExtra("statusAppointment"));
        //==============================//
        title_.setText(consultations.getTitle());
        consultationDate_.setText(consultations.getConsultationsDate());
        questions_.setText(consultations.getQuestions());
        answers_.setText(consultations.getAnswers());
        consultationType.setText(consultations.getConsultationsType());
        status_.setText(consultations.getStatus());
        if (Config.USER_TYPE.equals("CLIENT")){
            consultations.setClientId(Config.USER_ID);
            labelUser.setText("consultant");
            user.setText(consultations.getConsultantName());
            questionDetailPresenter.getImageProfile(consultations.getConsultantId());
            questionDetailPresenter.getHistoryConsultation(consultations.getConsultationId());
            if (!answers_.getText().toString().equals("")){
                badgeAnswers_.setVisibility(View.VISIBLE);
                badgeAnswers_.setText("Answered");
            }else{
                badgeAnswers_.setVisibility(View.GONE);
            }

            if (consultations.getStatus().equals("Answered")){
                layoutButtonNext.setVisibility(View.VISIBLE);
            }else {
                layoutButtonNext.setVisibility(View.GONE);
                badgeQuestions_.setVisibility(View.VISIBLE);
                answers_.setText("waiting answers from consultant..!");
                answers_.setTextColor(Color.parseColor("#8BC34A"));
                badgeAnswers_.setVisibility(View.GONE);
            }
        }else{
            labelUser.setText("client");
            user.setText(consultations.getClientName());
            consultations.setClientId(i.getStringExtra("clientId"));
            questionDetailPresenter.getImageProfile(consultations.getClientId());
            if (answers_.getText().toString().equals("")){
                badgeQuestions_.setVisibility(View.VISIBLE);
                badgeQuestions_.setText("New");
            }
            else{
                badgeQuestions_.setVisibility(View.GONE);
            }
            if (consultations.getStatus().equals("Answered")||consultations.getStatus().equals("appointment")){
                layoutButtonAnswers.setVisibility(View.GONE);
            }else {
                layoutButtonAnswers.setVisibility(View.VISIBLE);
                answers_.setText("please answer the question..!");
                answers_.setTextColor(Color.parseColor("#8BC34A"));
                badgeQuestions_.setVisibility(View.VISIBLE);
            }

        }
    }
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @Override
    @OnClick(R.id.answers)
    public void toAnswersPage() {
        Intent intent = new Intent();
        intent.putExtra("title",consultations.getTitle());
        intent.putExtra("consultationType",consultations.getConsultationsType());
        intent.putExtra("consultationId",consultations.getConsultationId());
        intent.putExtra("questions",consultations.getQuestions());
        intent.putExtra("date",consultations.getConsultationsDate());
        intent.putExtra("status",consultations.getStatus());
        intent.putExtra("historyId",consultations.getHistoryId());
        intent.putExtra("clientId",consultations.getClientId());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    @OnClick(R.id.history)
    public void toHistoryPage() {
        Intent intent = new Intent();
        intent.putExtra("questionsId",consultations.getConsultationId());
        intent.putExtra("clientName",consultations.getClientName());
        intent.putExtra("consultantName",consultations.getConsultantName());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.QuestionsHistoryActivity");
        startActivity(intent);
    }

    @Override
    @OnClick(R.id.nextQuestions)
    public void toNextQuestionsPage() {
        Intent intent = new Intent();
        intent.putExtra("title",consultations.getTitle());
        intent.putExtra("consultationType",consultations.getConsultationsType());
        intent.putExtra("consultationId",consultations.getConsultationId());
        intent.putExtra("consultantId",consultations.getConsultantId());
        intent.putExtra("questions",consultations.getQuestions());
        intent.putExtra("date",consultations.getConsultationsDate());
        intent.putExtra("status",consultations.getStatus());
        intent.putExtra("answers",consultations.getAnswers());
        intent.putExtra("expertRecomendation",consultations.getExpertRecomendations());
        intent.putExtra("statusConsultation","UPDATE");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void checkConsultation(List<HistoryConsultations> questions) {

        if(questions.size()==5&&consultations.getStatusAppointment().equals("false")){
            layoutButtonMakeAppointment.setVisibility(View.VISIBLE);
            layoutButtonNext.setVisibility(View.GONE);
        }else if(questions.size()==5&&consultations.getStatusAppointment().equals("true")){
            layoutButtonMakeAppointment.setVisibility(View.GONE);
            layoutButtonNext.setVisibility(View.GONE);
        }

    }

    @Override
    @OnClick(R.id.makeAppointment)
    public void makeAppointment() {
        Intent intent = new Intent();
        intent.putExtra("consultationId",consultations.getConsultationId());
        intent.putExtra("clientId",consultations.getClientId());
        intent.putExtra("consultantId",consultations.getConsultantId());
        intent.putExtra("date", "");
        intent.putExtra("detail", "");
        intent.putExtra("title", "");
        intent.putExtra("topic", "");
        intent.putExtra("status", "");
        intent.putExtra("id","");
        intent.putExtra("bookingcode","");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.AddAppointmentActivity");
        startActivity(intent);
    }

    @Override
    public void showImage(String url) {
        Picasso.with(this)
                .load(url)
                .into(imageUser);
    }

}
