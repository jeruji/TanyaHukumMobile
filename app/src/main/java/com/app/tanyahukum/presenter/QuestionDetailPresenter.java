package com.app.tanyahukum.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tanyahukum.model.HistoryConsultations;
import com.app.tanyahukum.view.ListConsultationInterface;
import com.app.tanyahukum.view.QuestionsDetailActivityInterface;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by emerio on 4/10/17.
 */

public class QuestionDetailPresenter implements QuestionsDetailActivityInterface.Presenter{
    QuestionsDetailActivityInterface.View view;
    FirebaseDatabase firebase;
    DatabaseReference dbRef;
    Context context;
    FirebaseStorage storage;
    StorageReference storageReference ;
    @Inject
    public QuestionDetailPresenter(FirebaseDatabase firebase, QuestionsDetailActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        dbRef = this.firebase.getReference("historyQuestions");
        this.context = context;
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-9d16f.appspot.com");

    }



    @Override
    public void getHistoryConsultation(String questionsId) {
        Query userQuery;
        userQuery = dbRef.orderByChild("questionsId").equalTo(questionsId);
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
                    view.checkConsultation(consultationsList);
                }else {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void getImageProfile(String userId){
        String filePath="profile/"+userId+".jpg";
        storageReference.child(filePath).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("uri",uri.toString());
                view.showImage(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
