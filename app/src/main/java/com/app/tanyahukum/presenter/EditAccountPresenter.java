package com.app.tanyahukum.presenter;

import android.content.Context;
import android.util.Log;

import com.app.tanyahukum.App;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.view.EditAccountInterface;
import com.app.tanyahukum.view.MyAccountActivityInterface;
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
 * Created by emerio on 4/20/17.
 */

public class EditAccountPresenter implements EditAccountInterface.Presenter {
    EditAccountInterface.View view;
    FirebaseDatabase firebase;
    Context context;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference userRef;
    FirebaseStorage storage;
    StorageReference storageReference ;
    //StorageReference profileFileRef = storageReference.child("marker/"+child+".jpg");
    @Inject
    public EditAccountPresenter(FirebaseDatabase firebase, EditAccountInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        this.context = context;
        userRef = this.firebase.getReference().child("users");
        firebaseAuth = FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-9d16f.appspot.com");
    }

    @Override
    public void getUserById(String userId) {
        view.showProgress(true);
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
    public void saveUpdatedAccount(final String userId, final User user) {
        view.showProgress(true);
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                try {
                    userRef.child(userId).child("name").setValue(user.getName());
                    userRef.child(userId).child("email").setValue(user.getEmail());
                    userRef.child(userId).child("bornDate").setValue(user.getBornDate());
                    userRef.child(userId).child("city").setValue(user.getCity());
                    userRef.child(userId).child("province").setValue(user.getProvince());
                    userRef.child(userId).child("gender").setValue(user.getGender());
                    userRef.child(userId).child("phone").setValue(user.getPhone());
                    String usertype= App.getInstance().getPrefManager().getUserType();
                    if (usertype.equals("CONSULTANT")){
                        userRef.child(userId).child("lawFirm").setValue(user.getLawFirm());
                        userRef.child(userId).child("lawFirmAddress:").setValue(user.getLawFirmAddress());
                        userRef.child(userId).child("lawFirmCity").setValue(user.getLawFirmCity());
                        userRef.child(userId).child("lawFirmAddress").setValue(user.getLawFirmAddress());
                    }
                    view.showProgress(false);
                    view.toMyAccountPage(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                view.showProgress(false);
                view.toMyAccountPage(false);
            }
        });

    }
}
