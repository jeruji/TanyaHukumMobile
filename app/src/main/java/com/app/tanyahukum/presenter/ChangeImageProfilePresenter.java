package com.app.tanyahukum.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.app.tanyahukum.view.ChangeImageProfileActivityInterface;
import com.app.tanyahukum.view.EditAccountInterface;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Inject;

/**
 * Created by emerio on 4/20/17.
 */

public class ChangeImageProfilePresenter implements ChangeImageProfileActivityInterface.Presenter {
    ChangeImageProfileActivityInterface.View view;
    FirebaseDatabase firebase;
    Context context;
    DatabaseReference userRef;
    FirebaseStorage storage;
    StorageReference storageReference ;
    //StorageReference profileFileRef = storageReference.child("marker/"+child+".jpg");
    @Inject
    public ChangeImageProfilePresenter(FirebaseDatabase firebase, ChangeImageProfileActivityInterface.View view, Context context) {
        this.firebase = firebase;
        this.view = view;
        this.context = context;
        userRef = this.firebase.getReference().child("users");
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReferenceFromUrl("gs://tanyahukum-9d16f.appspot.com");
    }
    public void submitProfileImage(File file, String child) {
        view.showProgress(true);
        StorageReference profileFileRef = storageReference.child("profile/"+child+".jpg");
        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        try {
            ExifInterface exif = new ExifInterface(file.getAbsolutePath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            // rotating bitmap
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }
        catch (Exception e) {

        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        UploadTask uploadTask = profileFileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                view.showProgress(false);
                view.uploadResult(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Log.d("sukses","sukses");
                view.showProgress(false);
                view.uploadResult(true);
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
