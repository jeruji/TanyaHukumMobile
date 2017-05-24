package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.fcm.FCMHelper;
import com.app.tanyahukum.model.Appointment;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.view.AddAppointmentActivityInterface;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by emerio on 4/19/17.
 */

public class AddAppointmentPresenter implements AddAppointmentActivityInterface.Presenter {
    FirebaseDatabase firebase;
    AddAppointmentActivityInterface.View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference appointmentRef;
    DatabaseReference userRef;
    DatabaseReference questionsRef;
    Context context;
    @Inject
    public AddAppointmentPresenter(FirebaseDatabase firebase, AddAppointmentActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        appointmentRef = this.firebase.getReference("appointment");
        questionsRef = this.firebase.getReference("questions");
        userRef = this.firebase.getReference("users");
        this.context = context;
    }
    public int[] getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{year, month, day};
    }

    @Override
    public void submitAppointment(final Appointment appointment, String status) {
        String id=appointmentRef.push().getKey();
        final List<String> deviceToken = new ArrayList<>();
        Query userQuery = userRef.orderByChild("id").equalTo(appointment.getConsultantId());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data snapshot : ", dataSnapshot.toString());
                User user=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                    Log.d("user",user.getFirebaseToken());
                    Log.d("user id",user.getId());
                    if (user.getId().equals(appointment.getConsultantId())){
                        deviceToken.add(user.getFirebaseToken());
                    }

                }
                System.out.println("device token next : "+deviceToken);
                try {
                    FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT",deviceToken,appointment.getAppointmentId(),"appointment from "+ Config.USER_NAME,"APPOINTMENT NEW ",appointment.getTitle(),Config.USER_TYPE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference childRef = appointmentRef.child(id);
        appointment.setAppointmentId(id);
        childRef.setValue(appointment);
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                view.detailAppointmentResult(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.detailAppointmentResult(false);
            }
        });
        questionsRef.child(appointment.getQuestionsId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                     questionsRef.child(appointment.getQuestionsId()).child("statusAppointment").setValue("true");
                    questionsRef.child(appointment.getQuestionsId()).child("status").setValue("appointment");
                     Log.d("success update : ","true");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("success update : ","false");
            }
        });
    }

    @Override
    public void proposeAppointment(final String appointmetId,final String clientId,final String consultantId, final String date) {
        if (App.getInstance().getPrefManager().getUserType().equalsIgnoreCase("CLIENT")){
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(consultantId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(consultantId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }

                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmetId, "appointment is reschedule with " + Config.USER_NAME, "Reschedule ", "Reschedule appointment", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmetId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        appointmentRef.child(appointmetId).child("status").setValue("RESCHEDULE");
                        appointmentRef.child(appointmetId).child("dateAppointment").setValue(date);
                        Log.d("success update : ", "true");
                        view.toAppointmentList(true,"APPOINTMENT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"APPOINTMENT");
                }
            });
        }else {
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(clientId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(clientId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }

                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmetId, "appointment is reschedule with " + Config.USER_NAME, "Reschedule ", "Reschedule appointment", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmetId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        appointmentRef.child(appointmetId).child("consultantName").setValue(App.getInstance().getPrefManager().getUser().getName());
                        appointmentRef.child(appointmetId).child("status").setValue("RESCHEDULE");
                        appointmentRef.child(appointmetId).child("dateAppointment").setValue(date);
                        Log.d("success update : ", "true");
                        view.toAppointmentList(true,"APPOINTMENT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"APPOINTMENT");
                }
            });
        }

    }

    @Override
    public void approveAppointment(final String appointmentId,final String clientId,final String consultantId) {
        if (App.getInstance().getPrefManager().getUser().getUsertype().equalsIgnoreCase("CLIENT")){
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(consultantId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(consultantId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }

                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmentId, "appointment is approved with " + Config.USER_NAME, "Approved ", "Approved appointment", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        appointmentRef.child(appointmentId).child("status").setValue("Accepted");
                        view.toAppointmentList(true,"APPOINTMENT");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"APPOINTMENT");
                }
            });

        }else {
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(clientId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(clientId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }

                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmentId, "appointment is approved with " + Config.USER_NAME, "Approved ", "Approved appointment", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        appointmentRef.child(appointmentId).child("consultantName").setValue(App.getInstance().getPrefManager().getUser().getName());
                        appointmentRef.child(appointmentId).child("consultantPhone").setValue(App.getInstance().getPrefManager().getUser().getPhone());
                        appointmentRef.child(appointmentId).child("consultantAddress").setValue(App.getInstance().getPrefManager().getUser().getAddress());
                        appointmentRef.child(appointmentId).child("status").setValue("Accepted");
                        view.toAppointmentList(true,"APPOINTMENT");
                        Log.d("success update : ", "true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"APPOINTMENT");
                }
            });
        }
    }

    @Override
    public void doneAppointment(final String appointmentId, final String questionsId, final String clientId, final String consultantId) {
        if (App.getInstance().getPrefManager().getUser().getUsertype().equalsIgnoreCase("CLIENT")){
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(consultantId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(consultantId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }
                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmentId, "appointment is DONE with " + Config.USER_NAME, "APPOINTMENT DONE ", "APPOINTMENT DONE", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                        appointmentRef.child(appointmentId).child("status").setValue("Done");
                        view.toAppointmentList(true,"HISTORY");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"HISTORY");
                }
            });

        }else {
            final List<String> deviceToken = new ArrayList<>();
            Query userQuery = userRef.orderByChild("id").equalTo(clientId);
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("data snapshot : ", dataSnapshot.toString());
                    User user = null;
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        user = singleSnapshot.getValue(User.class);
                        Log.d("user", user.getFirebaseToken());
                        Log.d("user id", user.getId());
                        if (user.getId().equals(clientId)) {
                            deviceToken.add(user.getFirebaseToken());
                        }

                    }
                    System.out.println("device token next : " + deviceToken);
                    try {
                        FCMHelper.getInstance().sendNotificationMultipleDevice("APPOINTMENT", deviceToken, appointmentId, "appointment is DONE with " + Config.USER_NAME, "APPOINTMENT DONE ", "APPOINTMENT DONE", Config.USER_TYPE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            appointmentRef.child(appointmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot tasksSnapshot) {
                    try {
                          appointmentRef.child(appointmentId).child("status").setValue("Done");
                        view.toAppointmentList(true,"HISTORY");
                        Log.d("success update : ", "true");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("success update : ", "false");
                    view.toAppointmentList(false,"HISTORY");
                }
            });
        }
        questionsRef.child(questionsId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    questionsRef.child(questionsId).child("status").setValue("Done");
                    Log.d("success update : ","true");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("success update : ","false");
            }
        });
    }

    @Override
    public void rateConsultant(String appointmentId, String clientId, String consultantId) {

    }
}
