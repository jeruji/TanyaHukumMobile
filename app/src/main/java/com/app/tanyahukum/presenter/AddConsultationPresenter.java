package com.app.tanyahukum.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.fcm.FCMHelper;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import com.app.tanyahukum.view.ListConsultationInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

/**
 * Created by emerio on 4/9/17.
 */

public class AddConsultationPresenter implements AddConsultationActivityInterface.Presenter {
    FirebaseDatabase firebase;
    AddConsultationActivityInterface.View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;
    DatabaseReference userRef;
    DatabaseReference historyRef;
    FirebaseStorage storage;
    StorageReference storageReference ;
    Context context;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    boolean stop=true;
    boolean check=true;
    String status;
    Date date = Calendar.getInstance().getTime();
    DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm ");
    String today = formatter.format(date);
     String consultantId,consultantName;
    int totalAnswerValue;
    final List<String> deviceToken = new ArrayList<>();
    final List<String> consultantIdList=new ArrayList<>();
    final List<String> consultantNameList=new ArrayList<>();
    final List<Integer> totalAnswerList=new ArrayList<>();
    String fileName="";
    @Inject
    public AddConsultationPresenter(FirebaseDatabase firebase, AddConsultationActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = this.firebase.getReference("questions");
        historyRef = this.firebase.getReference("historyQuestions");
        userRef = this.firebase.getReference("users");
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-9d16f.appspot.com");
        this.context = context;

    }

    public void initializeTimerTask(final Consultations consultations) {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (stop){

                            Query statusQuery = dbRef.orderByChild("consultationId").equalTo(consultations.getConsultationId());
                            statusQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Consultations consultationsStatus=null;
                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        consultationsStatus = singleSnapshot.getValue(Consultations.class);
                                        Log.d("cons",consultationsStatus.getStatus());
                                    }
                                    String status=consultationsStatus.getStatus();
                                    if (status.equalsIgnoreCase("new")){
                                        stop=false;
                                        Query userQuery = userRef.orderByChild("usertype").equalTo("CONSULTANT");
                                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                User user=null;
                                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                    user = singleSnapshot.getValue(User.class);
                                                    for (int i = 0; i <user.getSpecialization().size() ; i++) {
                                                        String spesialization = user.getSpecialization().get(i);
                                                        Log.d("specialization",spesialization);
                                                        if (consultations.getConsultationsType().equalsIgnoreCase(spesialization)) {
                                                            if (user.getCity().equalsIgnoreCase(consultations.getClientCity())) {
                                                                List<Integer> totalAnswers = new ArrayList<Integer>();
                                                                totalAnswers.add(user.getTotalConsultation());
                                                                int totalAnswersMin = Collections.min(totalAnswers);
                                                                Log.d("total answers:", String.valueOf(totalAnswersMin));
                                                                if (totalAnswersMin == user.getTotalConsultation()) {
                                                                    Log.d("masuk status :", "true");
                                                                    deviceToken.add(user.getFirebaseToken());
                                                                    consultantIdList.add(user.getId());
                                                                    consultantNameList.add(user.getName());
                                                                    totalAnswerList.add(user.getTotalConsultation());
                                                                    Log.d("cons name:", consultantNameList.toString());
                                                                    if (deviceToken.size() > 0) {
                                                                        consultantId = consultantIdList.get(0);
                                                                        consultantName = consultantNameList.get(0);
                                                                        totalAnswerValue=totalAnswerList.get(0);
                                                                    }

                                                                }

                                                            }

                                                        } else {
                                                            Log.d("false", "false");
                                                        }

                                                    }
                                                }
                                                dbRef.child(consultations.getConsultationId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot tasksSnapshot) {
                                                        try {
                                                            if (consultantId==null){
                                                                view.showProgressDialog(false);
                                                                dbRef.child(consultations.getConsultationId()).removeValue();
                                                                view.infoDialog("Consultant is not available");
                                                            }else {
                                                                dbRef.child(consultations.getConsultationId()).child("consultantId").setValue(consultantId);
                                                                dbRef.child(consultations.getConsultationId()).child("consultantName").setValue(consultantName);
                                                                dbRef.child(consultations.getConsultationId()).child("status").setValue("Accepted");
                                                                stop = false;
                                                                view.showProgressDialog(false);
                                                                view.detailConsultationResult(true, consultations.getConsultationId());
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                if (consultantId!=null) {
                                                    userRef.child(consultantId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot tasksSnapshot) {
                                                            try {
                                                                userRef.child(consultantId).child("totalConsultation").setValue(totalAnswerValue + 1);
                                                                stop = false;
                                                                view.showProgressDialog(false);
                                                                view.detailConsultationResult(true, consultations.getConsultationId());
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }else{
                                        stop=false;
                                        Log.d("status else",consultationsStatus.getStatus());
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
            }
        };
    }
    void startTimer(Consultations consultation){
        timer = new Timer();
        initializeTimerTask(consultation);
        timer.schedule(timerTask, 10000, 10000);
    }
    void startTimerCheck(String id){
        timer = new Timer();
        checkConsultation(id);
        timer.schedule(timerTask, 1000, 1000);
    }
    void checkConsultation(final  String id){
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (stop){
        Query statusQuery = dbRef.orderByChild("consultationId").equalTo(id);
        statusQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consultations consultationsStatus=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    consultationsStatus = singleSnapshot.getValue(Consultations.class);
                    Log.d("cons",consultationsStatus.getStatus());
                }
                String status=consultationsStatus.getStatus();
                if (status.equalsIgnoreCase("Accepted")){
                    stop=false;
                    view.showProgressDialog(false);
                    view.detailConsultationResult(true,id);
                }else{
                    Log.d("status else",consultationsStatus.getStatus());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
                        }
                    }
                });
            }
        };
    }
    @Override
    public void submitConsultation(final Consultations consultations,final String status,final  String path) {
        if (status.equals("NEW")){
            view.showProgressDialog(true);
            String id=dbRef.push().getKey();
            attachmentFileUpload(path);
            String fileName="";
            if (path==null){
                fileName="";
            }else {
                 fileName = path.substring(path.lastIndexOf('/') + 1);
            }
            Log.d("file name : ",fileName);
            final String historyQuestionsId=historyRef.push().getKey();
            Query userQuery = userRef.orderByChild("usertype").equalTo("CONSULTANT");
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                     User user=null;
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user",user.getFirebaseToken());
                        for (int i = 0; i <user.getSpecialization().size() ; i++) {
                            String spesialization=user.getSpecialization().get(i);
                            if (consultations.getConsultationsType().equalsIgnoreCase(spesialization)){
                                if (user.getCity().equalsIgnoreCase(consultations.getClientCity())){
                                    Log.d("masuk status :","trus");
                                    deviceToken.add(user.getFirebaseToken());
                                }
                            }
                            else{
                                Log.d("false","false");
                            }
                        }
                    }
                    System.out.println("device token list : "+deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice(status,deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE,"acceptConsultation",Config.USER_ID,"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            DatabaseReference childRef = dbRef.child(id);
            consultations.setConsultationId(id);
            consultations.setHistoryId(historyQuestionsId);
            consultations.setAttachment(fileName);
            startTimer(consultations);
            childRef.setValue(consultations);
            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   // view.detailConsultationResult(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                  //  view.detailConsultationResult(false);
                }
            });

            DatabaseReference historyQuesChild = historyRef.child(historyQuestionsId);
            HistoryConsultations historyConsultations=new HistoryConsultations();
            historyConsultations.setHistoryQuestionsId(historyQuestionsId);
            historyConsultations.setQuestionsId(consultations.getConsultationId());
            historyConsultations.setQuestionsOld(consultations.getQuestions());
            historyConsultations.setAttachmentOld(consultations.getAttachment());
            historyConsultations.setQuestionsDate(today);
            historyConsultations.setAnswersOld("");
            historyConsultations.setAnswersDate("");
            historyQuesChild.setValue(historyConsultations);
            historyQuesChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   Log.d("update history: ","sukses");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("update history: ","failed");
                }
            });

            startTimerCheck(id);

        }
        else if (status.equals("UPDATE")){
            attachmentFileUpload(path);
            if (path==null){
                fileName="";
            }else {
                fileName = path.substring(path.lastIndexOf('/') + 1);
            }
            final String consultId=consultations.getConsultantId();
            Log.d("consultant id : ",consultId);
            Log.d("path update : ",fileName);

            final String historyQuestionsId=historyRef.push().getKey();
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(consultId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user=null;
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user",user.getFirebaseToken());
                        Log.d("user id",user.getId());
                        if (user.getId().equals(consultId)){
                            deviceToken.add(user.getFirebaseToken());
                        }
                    }

                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice(status,deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE,"detailConsultation",Config.USER_ID,consultations.getConsultantId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            dbRef.child(consultations.getConsultationId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        dbRef.child(consultations.getConsultationId()).child("historyId").setValue(historyQuestionsId);
                        dbRef.child(consultations.getConsultationId()).child("questions").setValue(consultations.getQuestions());
                        dbRef.child(consultations.getConsultationId()).child("attachment").setValue(fileName);
                        dbRef.child(consultations.getConsultationId()).child("status").setValue("sent");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.detailConsultationResult(true,consultations.getConsultationId());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.detailConsultationResult(false,consultations.getConsultationId());
                }
            });
            DatabaseReference historyQuesChild = historyRef.child(historyQuestionsId);
            HistoryConsultations historyConsultations=new HistoryConsultations();
            historyConsultations.setHistoryQuestionsId(historyQuestionsId);
            historyConsultations.setQuestionsId(consultations.getConsultationId());
            historyConsultations.setQuestionsOld(consultations.getQuestions());
            historyConsultations.setAttachmentOld(consultations.getAttachment());
            historyConsultations.setQuestionsDate(today);
            historyConsultations.setAnswersOld("");
            historyQuesChild.setValue(historyConsultations);
            historyQuesChild.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("update history: ","sukses");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("update history: ","failed");
                }
            });
        }
        else if(status.equals("PENDING")){
            dbRef.child(consultations.getConsultationId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        dbRef.child(consultations.getConsultationId()).child("status").setValue("RESEND");
                        final List<String> deviceToken = new ArrayList<>();
                        Query userQuery = userRef.orderByChild("usertype").equalTo("CONSULTANT");
                        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user=null;
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    user = singleSnapshot.getValue(User.class);
                                    Log.d("user",user.getFirebaseToken());
                                    deviceToken.add(user.getFirebaseToken());
                                }

                               /* System.out.println("device token list : "+deviceToken);
                                try {
                                    FCMHelper.getInstance().sendNotificationMultipleDevice("RESEND",deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                */
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                       // startTimer(consultations.getConsultationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.detailConsultationResult(true,consultations.getConsultationId());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.detailConsultationResult(false,consultations.getConsultationId());
                }
            });
        }
    }

    @Override
    public void answers(final String questionId,final String historyId, final String clientId, final String answers,final String attachmentFile) {
        final List<String> deviceToken = new ArrayList<>();
        Log.d("cleint id: ",clientId);
        attachmentFileUpload(attachmentFile);
        if (attachmentFile==null){
            fileName="";
        }else {
            fileName = attachmentFile.substring(attachmentFile.lastIndexOf('/') + 1);
        }
        Log.d("file name : ",fileName);
        Query userQuery = userRef.orderByChild("id").equalTo(clientId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                    if (user.getId().equals(clientId)){
                        deviceToken.add(user.getFirebaseToken());
                    }
                    System.out.println(deviceToken);
                }
                try {
                    FCMHelper.getInstance().sendNotificationMultipleDevice("UPDATE",deviceToken,questionId,"answers from "+ Config.USER_NAME,"answersed ",answers,Config.USER_TYPE,"detailConsultation",Config.USER_ID,clientId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dbRef.child(questionId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    dbRef.child(questionId).child("answers").setValue(answers);
                    dbRef.child(questionId).child("attachment").setValue(fileName);
                    dbRef.child(questionId).child("status").setValue("Answered");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.detailConsultationResult(true,questionId);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.detailConsultationResult(false,questionId);
            }
        });
        historyRef.child(historyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    historyRef.child(historyId).child("answersOld").setValue(answers);
                    historyRef.child(historyId).child("answersDate").setValue(today);
                    historyRef.child(historyId).child("attachmentOld").setValue(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.detailConsultationResult(false,questionId);
            }
        });
    }

    @Override
    public void getConsultant(String usertype, String specialization) {
        Query userQuery = userRef.orderByChild("usertype").equalTo("CONSULTANT");
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data snapshot : ", dataSnapshot.toString());
                User user=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                }
               // view.toDashboardPage(user,loginType);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void attachmentFileUpload(String path) {
        if (path==null){
             Log.d("path null","kosong");
        }else{
            Uri file = Uri.fromFile(new File(path));
            StorageReference attachRef = storageReference.child("doc/" + file.getLastPathSegment());
            UploadTask uploadTask = attachRef.putFile(file);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("failed upload ;", "fail");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("sukses upload", "sukses");

                }
            });
        }


    }


}
