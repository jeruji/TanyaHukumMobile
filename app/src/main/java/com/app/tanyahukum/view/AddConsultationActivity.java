package com.app.tanyahukum.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAddConsultationActivityComponent;
import com.app.tanyahukum.data.module.AddConsultationActivityModule;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.presenter.AddConsultationPresenter;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.util.FileUtils;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    @BindView(R.id.edTextTitle)
    EditText title;

    @BindView(R.id.toolbar)
    Toolbar toolbarForm;

    @BindView(R.id.layoutAnswers)
    RelativeLayout answersLayout;

    @BindView(R.id.edTextanswers)
    EditText answers;

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

    @BindView(R.id.attachmentLayout)
    RelativeLayout attachmentLayout;

    @BindView(R.id.edTextChronology)
    EditText textChronology;

    String userid,clientName;
    int indexAttachment = 88;
    int indexImgDelete = 30;
    Consultations consultations;

    float densityVar;

    @Inject
    AddConsultationPresenter addConsultationPresenter;

    String statusConsultation="";
    ProgressDialog progressDialog ;
    String clientId;
    ArrayList<String> filePath;

    private static final int FILE_SELECT_CODE = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_consultation_layout);
        ButterKnife.bind(this);

        initializeConsultationActivityProperties();

        userTypeViewSettings();

        DaggerAddConsultationActivityComponent.builder()
                .netComponent(((App) getApplicationContext()).getNetComponent())
                .addConsultationActivityModule(new AddConsultationActivityModule(this, this))
                .build().inject(this);

        addConsultationPresenter.getConsultationType();

    }

    @Override
    public void constructConsultationType(ArrayList<String> consultationTypeList) {
        ArrayAdapter<String> adapterConsultationType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, consultationTypeList);
        adapterConsultationType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        consultationsType.setAdapter(adapterConsultationType);
    }

    public void initializeConsultationActivityProperties(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        progressDialog = new ProgressDialog(this);
        setSupportActionBar(toolbarForm);

        filePath = new ArrayList<>();

        densityVar = this.getResources().getDisplayMetrics().density;

        userid=Config.USER_ID;
        clientName=App.getInstance().getPrefManager().getUser().getName();
    }

    public void userTypeViewSettings(){

        if(Config.USER_TYPE.equalsIgnoreCase("CLIENT")) {
            getSupportActionBar().setTitle("Add Questions");
            answersLayout.setVisibility(View.GONE);
            Intent i=getIntent();
            statusConsultation=i.getStringExtra("statusConsultation");
            if (statusConsultation.equals("UPDATE")){
                _layoutUpdateForm.setVisibility(View.VISIBLE);
                _layoutConsultantType.setVisibility(View.GONE);
                _layoutTitle.setVisibility(View.GONE);
                questionsOld.setText(i.getStringExtra("questions"));
                answersOld.setText(i.getStringExtra("answers"));
                textChronology.setTextColor(Color.parseColor("#000000"));
                textChronology.setText(i.getStringExtra("chronology"));
                textChronology.setEnabled(false);
            }
            else if(statusConsultation.equals("PENDING")){
                questions.setText(i.getStringExtra("questions"));
                title.setText(i.getStringExtra("title"));
            }
        }
        else
            if(Config.USER_TYPE.equalsIgnoreCase("CONSULTANT")){

            getSupportActionBar().setTitle("Add Answers");
            answersLayout.setVisibility(View.VISIBLE);

            Intent i=getIntent();
            clientId=getIntent().getStringExtra("clientId");

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
            consultations.setChronology(i.getStringExtra("chronology"));

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
            textChronology.setTextColor(Color.parseColor("#000000"));
            textChronology.setEnabled(false);
            textChronology.setText(consultations.getChronology());

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @OnClick(R.id.attachment)
    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        Uri uri = data.getData();
                        String filePath = FileUtils.getPath(this, uri);
                        //attachmentPath.setText(filePath);
                        addAttachmentPath(filePath);

                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addAttachmentPath(final String filePath){

        if(isFileExists(filePath)){
            infoDialogFile("File has been added, please choose another file");
        }
        else{
            TextView tvFilePath =new TextView(this);
            tvFilePath.setText(filePath.substring(filePath.lastIndexOf('/')+1));
            tvFilePath.setId(indexAttachment+1);
            tvFilePath.setTag("tvFile_"+indexAttachment+1);
            RelativeLayout.LayoutParams paramsTv = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTv.topMargin = (int)(20 * densityVar);

            if(indexAttachment>88){

                for(int indexAttachmentRule=indexAttachment;indexAttachmentRule>88;indexAttachmentRule--) {
                    if (this.findViewById(indexAttachmentRule) != null) {
                        paramsTv.addRule(RelativeLayout.BELOW, indexAttachmentRule);
                        break;
                    }
                }
            }

            tvFilePath.setLayoutParams(paramsTv);

            attachmentLayout.addView(tvFilePath);

            this.filePath.add(filePath);

            ImageView btnDeleteAttachment = new ImageView(this);
            btnDeleteAttachment.setImageResource(R.drawable.trash);
            btnDeleteAttachment.setId(indexImgDelete+1);
            btnDeleteAttachment.setTag("delete_"+indexImgDelete+1);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(25 * densityVar), (int)(25 * densityVar));
            params.topMargin = (int)(16 * densityVar);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            if(indexImgDelete>30){
                for(int indexImgDeleteRule=indexImgDelete;indexImgDeleteRule>30;indexImgDeleteRule--) {
                    if (this.findViewById(indexImgDeleteRule) != null) {
                        params.addRule(RelativeLayout.BELOW, indexImgDeleteRule);
                        break;
                    }
                }
            }

            btnDeleteAttachment.setLayoutParams(params);
            btnDeleteAttachment.setScaleType(ImageView.ScaleType.FIT_CENTER);

            btnDeleteAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tvFileDeleted = (TextView)findViewById(v.getId()+58);
                    ImageView imgFileDeleted = (ImageView)findViewById(v.getId());

                    removeFilePath(tvFileDeleted.getText().toString());

                    attachmentLayout.removeView(tvFileDeleted);
                    attachmentLayout.removeView(imgFileDeleted);
                }
            });

            attachmentLayout.addView(btnDeleteAttachment);

            indexAttachment += 1;
            indexImgDelete += 1;
        }
    }

    public boolean isFileExists(String fileName) {

        boolean isExists = false;

        for(int index = 0; index < filePath.size(); index++) {
            if (filePath.get(index).equals(fileName)) {
                isExists = true;
            }
            else
                if(filePath.get(index).substring(filePath.get(index).lastIndexOf('/') + 1).equals(fileName.substring(fileName.lastIndexOf('/')+1))){
                    isExists = true;
                }
        }

        return isExists;
    }

    public void removeFilePath(String fileName) {

        for(int index = 0; index < filePath.size(); index++) {
            if (filePath.get(index).substring(filePath.get(index).lastIndexOf('/') + 1).equals(fileName)) {
                filePath.remove(index);
            }
        }
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
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");
                    cons.setClientCity(App.getInstance().getPrefManager().getUser().getCity());
                    cons.setChronology(textChronology.getText().toString());

                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm ");
                    Long tsLong = System.currentTimeMillis()/1000;
                    String timestamp = tsLong.toString();
                    String today = formatter.format(date);

                    cons.setConsultationsDate(today);
                    cons.setLastUpdateDate(timestamp);

                    String statusQuestions = "NEW";
                    addConsultationPresenter.submitConsultation(cons, statusQuestions,filePath);

                }
                else
                    if(statusConsultation.equals("UPDATE")){

                    Intent intent = getIntent();

                    Consultations cons = new Consultations();
                    cons.setConsultantId(intent.getStringExtra("consultantId"));
                    cons.setConsultationId(intent.getStringExtra("consultationId"));
                    cons.setAnswers(intent.getStringExtra("answers"));
                    cons.setConsultationsType(intent.getStringExtra("consultationType"));
                    cons.setTitle(intent.getStringExtra("title"));
                    cons.setQuestions(questions.getText().toString());
                    cons.setChronology(textChronology.getText().toString());
                    cons.setExpertRecomendations(intent.getStringExtra("expertRecomendation"));
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");

                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm ");
                    String today = formatter.format(date);

                    cons.setConsultationsDate(today);
                    String statusQuestions = "UPDATE";
                    addConsultationPresenter.submitConsultation(cons, statusQuestions,filePath);

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
                    cons.setChronology(textChronology.getText().toString());
                    cons.setExpertRecomendations(intent.getStringExtra("expertRecomendation"));
                    cons.setClientId(userid);
                    cons.setClientName(clientName);
                    cons.setStatus("new");
                    cons.setStatusAppointment("false");

                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm ");
                    String today = formatter.format(date);

                    cons.setConsultationsDate(today);
                    String statusQuestions = "PENDING";

                    addConsultationPresenter.submitConsultation(cons, statusQuestions,filePath);

                }
            }
            else {
                if (answers.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "please fill answers field..", Toast.LENGTH_LONG).show();
                }
                else {
                    String _answers = answers.getText().toString();
                    addConsultationPresenter.answers(consultations.getConsultationId(), consultations.getHistoryId(), clientId, _answers,filePath);
                }
            }
        }
    }

    @Override
    public void detailConsultationResult(boolean result,String consultationId) {
        if (result){

            Intent intent = new Intent();
            intent.putExtra("consultationId",consultationId);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.QuestionsDetailActivity");
            startActivity(intent);

        }
        else{

            Toast.makeText(getApplicationContext(),"failed create consultations",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showProgressDialog(boolean status) {
        if(status){
            progressDialog.setTitle("Search Consultant");
            progressDialog.setMessage("please wait we are finding consultant..");
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
            builder = new AlertDialog.Builder(AddConsultationActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(AddConsultationActivity.this);
        }
        builder.setTitle("Info")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(AddConsultationActivity.this,ListConsultationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            AddConsultationActivity.this.finish();
                            startActivity(intent);
                        }
                    }
                )
                .show();
    }

    public void infoDialogFile(String message){
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AddConsultationActivity.this, R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(AddConsultationActivity.this);
        }
        builder.setTitle("Info")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }
                )
                .show();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
