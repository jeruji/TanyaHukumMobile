package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.view.AcceptQuestionsActivityInterface;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

/**
 * Created by emerio on 4/15/17.
 */

public class AcceptQuestionsPresenter implements AcceptQuestionsActivityInterface.Presenter {
    FirebaseDatabase firebase;
    AcceptQuestionsActivityInterface.View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef,userRef;
    Context context;
    String consultantId=App.getInstance().getPrefManager().getUserId();
    String consultantName=App.getInstance().getPrefManager().getUser().getName();
    String totalConsultation;
    @Inject
    public AcceptQuestionsPresenter(FirebaseDatabase firebase, AcceptQuestionsActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = this.firebase.getReference("questions");
        userRef = this.firebase.getReference("users");
        this.context = context;

    }
    @Override
    public void acceptQuestions() {

    }

    @Override
    public void updateQuestions(final String questionsId) {
        Query statusQuery = dbRef.orderByChild("consultationId").equalTo(questionsId);
        statusQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Consultations consultations=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    consultations = singleSnapshot.getValue(Consultations.class);
                    Log.d("cons",consultations.getConsultantId());
                }
                 String checkConsultant=consultations.getConsultantId();
                if (!checkConsultant.equalsIgnoreCase("")){
                    view.infoDialog();
                }else{
                    //Query statusQuery = dbRef.orderByChild("consultationId").equalTo(questionsId);
                    /*
                    Query userQuery = userRef.child(consultantId);
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = null;
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                user = singleSnapshot.getValue(User.class);
                            }
                             totalConsultation=String.valueOf(user.getTotalConsultation());
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                */

                    Log.d("string id : ", questionsId);
                    dbRef.child(questionsId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot tasksSnapshot) {
                            try {

                                dbRef.child(questionsId).child("consultantId").setValue(consultantId);
                                dbRef.child(questionsId).child("consultantName").setValue(consultantName);
                                dbRef.child(questionsId).child("status").setValue("Accepted");
                                view.toDashboard();

                               /* userRef.child(consultantId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot tasksSnapshot) {
                                        try {
                                            int total=(Integer.parseInt(totalConsultation)+1);
                                            userRef.child(consultantId).child("totalConsultation").setValue(String.valueOf(total));
                                            view.toDashboard();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                */

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

    }
}
