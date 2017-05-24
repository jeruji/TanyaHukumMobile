package com.app.tanyahukum.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAddConsultationActivityComponent;
import com.app.tanyahukum.data.component.DaggerListConsultationActivityComponent;
import com.app.tanyahukum.data.module.AddConsultationActivityModule;
import com.app.tanyahukum.data.module.ListConsultationActivityModule;
import com.app.tanyahukum.fcm.FCMHelper;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.presenter.AddConsultationPresenter;
import com.app.tanyahukum.presenter.ListConsultationPresenter;
import com.app.tanyahukum.util.Config;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/9/17.
 */

public class AddConsultationActivity extends AppCompatActivity implements AddConsultationActivityInterface.View {
    @BindView(R.id.input_layout_consult_type)
    TextInputLayout layoutConsultType;
    @BindView(R.id.edTextConsultType)
    EditText editTextConsultType;
    @BindView(R.id.edTextQuestions)
    EditText questions;
    @BindView(R.id.spinnerConsultationType)
    Spinner consultationsType;
    @BindView(R.id.spinnerExpertRecomendation)
    Spinner expertRecomendations;
    @BindView(R.id.edTextTitle)
    EditText title;
    @BindView(R.id.toolbar)
    Toolbar toolbarForm;
    @BindView(R.id.layoutAnswers)
    RelativeLayout answersLayout;
    @BindView(R.id.edTextanswers)
    EditText answers;
    @BindView(R.id.layoutSpinnerExpert)
    RelativeLayout spinnerLayout;
    @BindView(R.id.layoutUpdateForm)
    RelativeLayout _layoutUpdateForm;
    @BindView(R.id.layoutConsultantType)
    RelativeLayout _layoutConsultantType;
    @BindView(R.id.layoutTitle)
    RelativeLayout _layoutTitle;
    @BindView(R.id.questionsOld)
    TextView questionsOld;
    @BindView(R.id.answersOld)
    TextView answersOld;
    String userid,clientName;
    Consultations consultations;
    @Inject
    AddConsultationPresenter addConsultationPresenter;
    String statusConsultation="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.add_consultation_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarForm);
        Log.d("user type: ",Config.USER_TYPE);
        userid=Config.USER_ID;
        clientName=App.getInstance().getPrefManager().getUser().getName();
        if(Config.USER_TYPE.equalsIgnoreCase("CLIENT")) {
            getSupportActionBar().setTitle("Add Questions");
            answersLayout.setVisibility(View.GONE);
            Intent i=getIntent();
            statusConsultation=i.getStringExtra("statusConsultation");
            if (statusConsultation.equals("UPDATE")){
                _layoutUpdateForm.setVisibility(View.VISIBLE);
                _layoutConsultantType.setVisibility(View.GONE);
                _layoutTitle.setVisibility(View.GONE);
                spinnerLayout.setVisibility(View.GONE);
                questionsOld.setText(i.getStringExtra("questions"));
                answersOld.setText(i.getStringExtra("answers"));
            }
            else if(statusConsultation.equals("PENDING")){
                questions.setText(i.getStringExtra("questions"));
                title.setText(i.getStringExtra("title"));
            }
        }else if(Config.USER_TYPE.equalsIgnoreCase("CONSULTANT")){
            getSupportActionBar().setTitle("Add Answers");
            answersLayout.setVisibility(View.VISIBLE);
            spinnerLayout.setVisibility(View.GONE);
            Intent i=getIntent();
            consultations=new Consultations();
            consultations.setTitle(i.getStringExtra("title"));
            consultations.setConsultationsDate(i.getStringExtra("date"));
            consultations.setQuestions(i.getStringExtra("questions"));
            consultations.setClientId(i.getStringExtra("clientId"));
            consultations.setConsultationId(i.getStringExtra("consultationId"));
            consultations.setConsultationsType(i.getStringExtra("consultationType"));
            consultations.setConsultantId(i.getStringExtra("consultantId"));
            consultations.setStatus(i.getStringExtra("status"));
            consultations.setHistoryId(i.getStringExtra("historyId"));
            consultationsType.setEnabled(false);
            consultationsType.setVisibility(View.GONE);
            layoutConsultType.setVisibility(View.VISIBLE);
            editTextConsultType.setText(consultations.getConsultationsType());
            editTextConsultType.setEnabled(false);
            title.setEnabled(false);
            title.setTextColor(Color.parseColor("#000000"));
            editTextConsultType.setTextColor(Color.parseColor("#000000"));
            questions.setTextColor(Color.parseColor("#000000"));
            questions.setEnabled(false);
            title.setText(consultations.getTitle());
            questions.setText(consultations.getQuestions());

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerAddConsultationActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .addConsultationActivityModule(new AddConsultationActivityModule(this, this))
                .build().inject(this);
        addConsultationPresenter.getConsultant("CONSULTANT","KRIMINAL");

    }

    @Override
    @OnClick(R.id.btnAddConsultation)
    public void submit() {
         if(questions.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"please fill questions field..",Toast.LENGTH_LONG).show();
        }
        else {
            if (Config.USER_TYPE.equalsIgnoreCase("CLIENT")) {
                if (statusConsultation.equals("NEW")) {
                    Consultations cons = new Consultations();
                    cons.setConsultantId("");
                    cons.setConsultantName("");
                    cons.setConsultationId("");
                    cons.setAnswers("");
                    cons.setConsultationsType(consultationsType.getSelectedItem().toString());
                    cons.setTitle(title.getText().toString());
                    cons.setQuestions(questions.getText().toString());
                    cons.setExpertRecomendations(expertRecomendations.getSelectedItem().toString());
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");
                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm ");
                    String today = formatter.format(date);
                    System.out.println("Today : " + today);
                    cons.setConsultationsDate(today);
                    String statusQuestions = "NEW";
                    addConsultationPresenter.submitConsultation(cons, statusQuestions);
                } else if(statusConsultation.equals("UPDATE")){
                    Intent intent = getIntent();
                    Consultations cons = new Consultations();
                    cons.setConsultantId(intent.getStringExtra("consultantId"));
                    cons.setConsultationId(intent.getStringExtra("consultationId"));
                    cons.setAnswers(intent.getStringExtra("answers"));
                    cons.setConsultationsType(intent.getStringExtra("consultationType"));
                    cons.setTitle(intent.getStringExtra("title"));
                    cons.setQuestions(questions.getText().toString());
                    cons.setExpertRecomendations(intent.getStringExtra("expertRecomendation"));
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");
                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm ");
                    String today = formatter.format(date);
                    System.out.println("Today : " + today);
                    cons.setConsultationsDate(today);
                    String statusQuestions = "UPDATE";
                    addConsultationPresenter.submitConsultation(cons, statusQuestions);
                }
                else{
                    Intent intent = getIntent();
                    Consultations cons = new Consultations();
                    cons.setConsultationId(intent.getStringExtra("consultationId"));
                    cons.setConsultantId("");
                    cons.setConsultantName("");
                    cons.setAnswers("");
                    cons.setConsultationsType(intent.getStringExtra("consultationType"));
                    cons.setTitle(intent.getStringExtra("title"));
                    cons.setQuestions(questions.getText().toString());
                    cons.setExpertRecomendations(intent.getStringExtra("expertRecomendation"));
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");
                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm ");
                    String today = formatter.format(date);
                    System.out.println("Today : " + today);
                    cons.setConsultationsDate(today);
                    String statusQuestions = "PENDING";
                    Log.d("COnsId : ",intent.getStringExtra("consultationId"));
                    addConsultationPresenter.submitConsultation(cons, statusQuestions);
                }
            } else {
                String _answers = answers.getText().toString();
                addConsultationPresenter.answers(consultations.getConsultationId(), consultations.getHistoryId(),consultations.getClientId(), _answers);
            }
        }
    }

    @Override
    public void detailConsultationResult(boolean result) {
        if (result){
            Toast.makeText(getApplicationContext(),"data created",Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("userid",userid);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.ListConsultationActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"failed create consultations",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void toastShedule(String msg) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
        final String strDate = simpleDateFormat.format(calendar.getTime());
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(getApplicationContext(), msg, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
