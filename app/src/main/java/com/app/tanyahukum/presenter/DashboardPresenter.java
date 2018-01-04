package com.app.tanyahukum.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.model.Consultations;
import com.app.tanyahukum.view.DashboardActivityInterface;
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

import javax.inject.Inject;

/**
 * Created by emerio on 4/9/17.
 */

public class DashboardPresenter implements DashboardActivityInterface.Presenter {
    DashboardActivityInterface.View view;
    FirebaseDatabase firebase;
    Context context;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbRef;
    FirebaseStorage storage;
    StorageReference storageReference ;

    @Inject
    public DashboardPresenter(FirebaseDatabase firebase, DashboardActivityInterface.View view, Context context) {

        this.firebase = firebase;
        this.view = view;
        this.context = context;
        dbRef = this.firebase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-9d16f.appspot.com");
    }

    @Override
    public void updateFirebaseToken(final String token) {
        final String userId = firebaseAuth.getCurrentUser().getUid();
        Log.d("usrId",userId);
        Log.d("token update : ",token);
        dbRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    dbRef.child(userId).child("firebaseToken").setValue(token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void signOutUser() {
        App.getInstance().getPrefManager().clear();
        view.toLoginPage();
    }

    @Override
    public void queryNewAnswer(String userId) {
        DatabaseReference consultationRef = firebase.getReference("questions");
        Query consultationQuery = consultationRef.orderByChild("clientId").equalTo(userId);
        consultationQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Consultations consultations=null;
                boolean isNew = false;

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    consultations = singleSnapshot.getValue(Consultations.class);
                    if(!consultations.getAnswers().equals("")&&consultations.getStatus().equals("Answered")){
                        isNew = true;
                        break;
                    }
                }

                if(isNew){
                    view.appearNewNotification();
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
                Log.v("uri",uri.toString());
                view.showImage(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.v("error profile image", exception.getStackTrace().toString());
            }
        });
    }
}
