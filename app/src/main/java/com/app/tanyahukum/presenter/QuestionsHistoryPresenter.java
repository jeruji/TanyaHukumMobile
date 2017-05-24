package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.util.Config;
import com.app.tanyahukum.view.ListConsultationInterface;
import com.app.tanyahukum.view.QuestionsDetailActivityInterface;
import com.app.tanyahukum.view.QuestionsHistoryInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by emerio on 4/18/17.
 */

public class QuestionsHistoryPresenter implements QuestionsHistoryInterface.Presenter {
    FirebaseDatabase firebase;
    QuestionsHistoryInterface.View view;
    FirebaseAuth firebaseAuth;
    DatabaseReference historyRef;
    Context context;
    @Inject
    public QuestionsHistoryPresenter(FirebaseDatabase firebase, QuestionsHistoryInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        firebaseAuth = FirebaseAuth.getInstance();
        historyRef = this.firebase.getReference("historyQuestions");
        this.context = context;
    }

    @Override
    public void getHistoryQuestions(String questionsId) {
        view.showQuestionsProgress(true);
        Query userQuery;
        userQuery = historyRef.orderByChild("questionsId").equalTo(questionsId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data consult : ", dataSnapshot.toString());
                List<HistoryConsultations> consultationsList=new ArrayList<HistoryConsultations>();
                HistoryConsultations consultations=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    consultations = singleSnapshot.getValue(HistoryConsultations.class);
                    consultationsList.add(consultations);
                }
                if (consultationsList.size()>0) {
                    view.showQuestionsProgress(false);
                    view.showQuestions(consultationsList);
                }else {
                    view.showQuestionsProgress(false);
                    view.showEmptyMessage();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
