package com.app.tanyahukum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerQuestionsDetailActivityComponent;
import com.app.tanyahukum.data.module.QuestionsActivityModule;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.presenter.QuestionDetailPresenter;
import com.app.tanyahukum.util.Config;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
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
    @BindView(R.id.attachmentLayout)
    RelativeLayout attachmentLayout;
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

    int indexAttachment = 88;
    int indexImgDownload = 30;
    float densityVar;

    ProgressDialog progressDialog ;

    @Inject
    QuestionDetailPresenter questionDetailPresenter;
    Consultations consultations;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_detail_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("Question Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        DaggerQuestionsDetailActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .questionsActivityModule((new QuestionsActivityModule(this,this)))
                .build().inject(this);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        initializeQuestionsDetailActivityProperties();

    }

    public void initializeQuestionsDetailActivityProperties(){
        densityVar = this.getResources().getDisplayMetrics().density;

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
        intent.putExtra("chronology",consultations.getChronology());
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
        intent.putExtra("chronology", consultations.getChronology());
        intent.putExtra("statusConsultation","UPDATE");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.AddConsultationActivity");
        startActivity(intent);
    }

    @Override
    public void checkConsultation(List<HistoryConsultations> questions) {
        layoutButtonMakeAppointment.setVisibility(View.VISIBLE);
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

        if(consultations.getAttachment()!=null) {
            ArrayList<String> attachmentList = consultations.getAttachment();

            for(int index=0;index<attachmentList.size();index++){

                TextView tvFilePath = new TextView(this);
                tvFilePath.setText(attachmentList.get(index).substring(0,20));
                tvFilePath.setId(indexAttachment+1);
                RelativeLayout.LayoutParams paramsAttachment = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsAttachment.topMargin = (int)(20 * densityVar);

                if(indexAttachment>88){

                    for(int indexAttachmentRule=indexAttachment;indexAttachmentRule>88;indexAttachmentRule--) {
                        if (this.findViewById(indexAttachmentRule) != null) {
                            paramsAttachment.addRule(RelativeLayout.BELOW, indexAttachmentRule);
                            break;
                        }
                    }
                }

                tvFilePath.setLayoutParams(paramsAttachment);

                attachmentLayout.addView(tvFilePath);

                ImageView imgDownload = new ImageView(this);
                imgDownload.setImageResource(R.drawable.icon_download);
                imgDownload.setId(indexImgDownload+1);
                RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams((int)(25 * densityVar), (int)(25 * densityVar));
                params.topMargin = (int)(16 * densityVar);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                if(indexImgDownload>30){
                    for(int indexImgDownloadRule=indexImgDownload;indexImgDownloadRule>30;indexImgDownloadRule--) {
                        if (this.findViewById(indexImgDownloadRule) != null) {
                            params.addRule(RelativeLayout.BELOW, indexImgDownloadRule);
                            break;
                        }
                    }
                }

                imgDownload.setLayoutParams(params);
                imgDownload.setScaleType(ImageView.ScaleType.FIT_CENTER);

                final String attachmentName = attachmentList.get(index);

                imgDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        questionDetailPresenter.downloadAttachment(attachmentName);
                    }
                });

                attachmentLayout.addView(imgDownload);

                indexAttachment += 1;
                indexImgDownload += 1;
            }
        }

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
            }
            else if(consultations.getStatus().equals("appointment")){
                layoutButtonNext.setVisibility(View.GONE);
                badgeQuestions_.setVisibility(View.GONE);
                answers_.setText("you have an appointment with consultant..!");
                answers_.setTextColor(Color.parseColor("#FF1744"));
                badgeAnswers_.setVisibility(View.GONE);
            }
            else {
                layoutButtonNext.setVisibility(View.GONE);
                badgeQuestions_.setVisibility(View.VISIBLE);
                answers_.setText("waiting answers from consultant..!");
                answers_.setTextColor(Color.parseColor("#8BC34A"));
                badgeAnswers_.setVisibility(View.GONE);
            }
        }else{
            labelUser.setText("client");
            user.setText(consultations.getClientName());
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
                answers_.setTextColor(Color.parseColor("#FF1744"));
                badgeQuestions_.setVisibility(View.VISIBLE);
            }

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
