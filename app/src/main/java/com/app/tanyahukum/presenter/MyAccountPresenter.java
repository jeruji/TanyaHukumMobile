package com.app.tanyahukum.presenter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tanyahukum.model.User;
import com.app.tanyahukum.view.MyAccountActivityInterface;
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
 * Created by emerio on 4/19/17.
 */

public class MyAccountPresenter implements MyAccountActivityInterface.Presenter {
    MyAccountActivityInterface.View view;
    FirebaseDatabase firebase;
    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference userRef;
    FirebaseStorage storage;
    StorageReference storageReference ;

    @Inject
    public MyAccountPresenter(FirebaseDatabase firebase, MyAccountActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        this.context = context;
        userRef = this.firebase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-c060c.appspot.com");
    }

    @Override
    public void getUserById(String userId) {
        view.showProgress(true);
        getImageProfile(userId);
        Query userQuery ;
            userQuery = userRef.orderByChild("id").equalTo(userId);
        Log.d("user id",userId);
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("data users : ", dataSnapshot.toString());
                User user=null;
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    user = singleSnapshot.getValue(User.class);
                }
                view.showAccount(user);
                view.showProgress(false);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public String showImageStorage(String userId) {
        return null;
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
            public void onFailure(@NonNull  Exception exception) {
                // Handle any errors
            }
        });
    }

}
