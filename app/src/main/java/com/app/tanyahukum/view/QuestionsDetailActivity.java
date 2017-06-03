package com.app.tanyahukum.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
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

import java.io.File;
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
    @BindView(R.id.attachment)
    TextView attachment_;
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

    ProgressDialog progressDialog ;
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
        progressDialog= new ProgressDialog(this);
        Intent startingIntent = getIntent();
        String consultationId;
        if (startingIntent != null) {
             consultationId= startingIntent.getStringExtra("consultationId"); // Retrieve the id
             questionDetailPresenter.getConsultationDetailById(consultationId);
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
        intent.putExtra("statusConsultation","UPDATE");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void checkConsultation(List<HistoryConsultations> questions) {
        if(questions.size()==5&&consultations.getStatusAppointment().equals("false")){
            infoDialog("Your consultation exceeds the limit. Please make an Appointment.");
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
        intent.putExtra("title", consultations.getTitle());
        intent.putExtra("topic", consultations.getConsultationsType());
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

    @Override
    public void showData(Consultations cons) {
        consultations= cons;
        title_.setText(consultations.getTitle());
        consultationDate_.setText(consultations.getConsultationsDate());
        questions_.setText(consultations.getQuestions());
        answers_.setText(consultations.getAnswers());
        consultationType.setText(consultations.getConsultationsType());
        status_.setText(consultations.getStatus());
        attachment_.setText(consultations.getAttachment());
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
            }
            else if(consultations.getStatus().equals("Done")){
                layoutButtonNext.setVisibility(View.GONE);
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
           // consultations.setClientId(i.getStringExtra("clientId"));
            questionDetailPresenter.getImageProfile(consultations.getClientId());
            if (answers_.getText().toString().equals("")){
                badgeQuestions_.setVisibility(View.VISIBLE);
                badgeQuestions_.setText("New");
            }
            else{
                badgeQuestions_.setVisibility(View.GONE);
            }
            if (consultations.getStatus().equals("Answered")||consultations.getStatus().equals("appointment")||consultations.getStatus().equals("Done")){
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
    @OnClick(R.id.downloadAttachment)
    public void downloadAttachment() {
        if (attachment_.getText().toString().equalsIgnoreCase("")){
             Toast.makeText(getApplicationContext(),"attachment is empty",Toast.LENGTH_LONG).show();
        }else {
            questionDetailPresenter.downloadAttachment(consultations.getAttachment());
        }
    }

    @Override
    public void openFileAttachment(String filepath) {
        File file = new File(filepath);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);
        if (type == null)
            type = "*/*";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);
        intent.setDataAndType(data, type);
        startActivity(intent);
    }

    @Override
    public void showProgressDialog(boolean status) {
        if(status){
            progressDialog.setTitle("Info");
            progressDialog.setMessage("download attachment file");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    @Override
    public void infoDialog(String message) {
            final AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(QuestionsDetailActivity.this, R.style.MyDialogTheme);
            } else {
                builder = new AlertDialog.Builder(QuestionsDetailActivity.this);
            }
            builder.setTitle("Info")
                    .setMessage(message)
                    .setPositiveButton("OK",null
                    )
                    .show();

    }
}
