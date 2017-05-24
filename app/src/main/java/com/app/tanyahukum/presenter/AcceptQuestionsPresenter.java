package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.view.AcceptQuestionsActivityInterface;
import com.app.tanyahukum.view.AddConsultationActivityInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

/**
 * Created by emerio on 4/15/17.
 */

public class AcceptQuestionsPresenter implements AcceptQuestionsActivityInterface.Presenter {
    FirebaseDatabase firebase;
    AcceptQuestionsActivityInterface.View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;
    Context context;
    @Inject
    public AcceptQuestionsPresenter(FirebaseDatabase firebase, AcceptQuestionsActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        dbRef = this.firebase.getReference("questions");
        this.context = context;

    }
    @Override
    public void acceptQuestions() {

    }

    @Override
    public void updateQuestions(final String questionsId) {
        Log.d("string id : ", questionsId);
        dbRef.child(questionsId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    Log.d("test", "onDataChange: "+tasksSnapshot.toString());
                    String consultantId=App.getInstance().getPrefManager().getUserId();
                    String consultantName=App.getInstance().getPrefManager().getUser().getName();
                    dbRef.child(questionsId).child("consultantId").setValue(consultantId);
                    dbRef.child(questionsId).child("consultantName").setValue(consultantName);
                    dbRef.child(questionsId).child("status").setValue("Accepted");
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
