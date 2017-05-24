package com.app.tanyahukum.presenter;

import android.content.Context;
import android.os.Handler;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    Context context;
    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    boolean stop=true;
    String status;
    Date date = Calendar.getInstance().getTime();
    DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy HH:mm ");
    String today = formatter.format(date);
    @Inject
    public AddConsultationPresenter(FirebaseDatabase firebase, AddConsultationActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = this.firebase.getReference("questions");
        historyRef = this.firebase.getReference("historyQuestions");
        userRef = this.firebase.getReference("users");
        this.context = context;

    }
    public void initializeTimerTask(final String id) {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if (stop){
                            Log.d("questions id : ",id);
                            Query query = dbRef.orderByChild("consultationId").equalTo(id);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Log.d("data schedule : ", dataSnapshot.toString());
                                    Consultations cons=null;
                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        cons = singleSnapshot.getValue(Consultations.class);
                                        Log.d("status",cons.getStatus());
                                        status=cons.getStatus();
                                    }
                                    if (status.equalsIgnoreCase("new")){
                                        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot tasksSnapshot) {
                                                try {
                                                    dbRef.child(id).child("status").setValue("PENDING");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        stop=false;
                                    }else  if (status.equalsIgnoreCase("RESEND")){
                                        dbRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot tasksSnapshot) {
                                                try {
                                                    dbRef.child(id).child("status").setValue("PENDING");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });
                                        stop=false;
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
    void startTimer(String id){
        timer = new Timer();
        initializeTimerTask(id);
        timer.schedule(timerTask, 100000, 100000);
    }
    @Override
    public void submitConsultation(final Consultations consultations,final String status) {
        if (status.equals("NEW")){
            String id=dbRef.push().getKey();
            startTimer(id);
            final String historyQuestionsId=historyRef.push().getKey();
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
                    System.out.println("device token list : "+deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice(status,deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE);
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
            childRef.setValue(consultations);
            childRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    view.detailConsultationResult(true);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.detailConsultationResult(false);
                }
            });

            DatabaseReference historyQuesChild = historyRef.child(historyQuestionsId);
            HistoryConsultations historyConsultations=new HistoryConsultations();
            historyConsultations.setHistoryQuestionsId(historyQuestionsId);
            historyConsultations.setQuestionsId(consultations.getConsultationId());
            historyConsultations.setQuestionsOld(consultations.getQuestions());
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
        }
        else if (status.equals("UPDATE")){
            final String consultId=consultations.getConsultantId();
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
                        FCMHelper.getInstance().sendNotificationMultipleDevice(status,deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE);
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
                        dbRef.child(consultations.getConsultationId()).child("status").setValue("sent");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.detailConsultationResult(true);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.detailConsultationResult(false);
                }
            });
            DatabaseReference historyQuesChild = historyRef.child(historyQuestionsId);
            HistoryConsultations historyConsultations=new HistoryConsultations();
            historyConsultations.setHistoryQuestionsId(historyQuestionsId);
            historyConsultations.setQuestionsId(consultations.getConsultationId());
            historyConsultations.setQuestionsOld(consultations.getQuestions());
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
                                System.out.println("device token list : "+deviceToken);
                                try {
                                    FCMHelper.getInstance().sendNotificationMultipleDevice("RESEND",deviceToken,consultations.getConsultationId(),"questions from "+ Config.USER_NAME,consultations.getTitle(),consultations.getQuestions(),Config.USER_TYPE);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        startTimer(consultations.getConsultationId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.detailConsultationResult(true);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    view.detailConsultationResult(false);
                }
            });
        }
    }

    @Override
    public void answers(final String questionId,final String historyId, final String clientId, final String answers) {
        final List<String> deviceToken = new ArrayList<>();
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
                }
                try {
                    FCMHelper.getInstance().sendNotificationMultipleDevice("UPDATE",deviceToken,questionId,"answers from "+ Config.USER_NAME,"answersed ",answers,Config.USER_TYPE);
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
                    dbRef.child(questionId).child("status").setValue("Answered");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.detailConsultationResult(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.detailConsultationResult(false);
            }
        });
        historyRef.child(historyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    historyRef.child(historyId).child("answersOld").setValue(answers);
                    historyRef.child(historyId).child("answersDate").setValue(today);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.detailConsultationResult(false);
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



}
