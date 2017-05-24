package com.app.tanyahukum.view;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerChangeImageProfileActivityComponent;
import com.app.tanyahukum.data.module.ChangeImageProfileActivityModule;
import com.app.tanyahukum.data.module.EditAccountActivityModule;
import com.app.tanyahukum.presenter.ChangeImageProfilePresenter;
import com.app.tanyahukum.presenter.EditAccountPresenter;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerio on 4/20/17.
 */

public class ChangeImageProfileActivity extends AppCompatActivity implements ChangeImageProfileActivityInterface.View {
    Intent  CropIntent ;
    @BindView(R.id.toolbar)
    Toolbar toolbar_;
    @BindView(R.id.imagePath)
    TextView _imagePath;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.imageDefault)
    ImageView imageProfile;
    @BindView(R.id.imageProfile)
    CircleImageView _imageProfile;
    @Inject
    ChangeImageProfilePresenter changeImageProfilePresenter;
    File imgFile;
    Uri uri;
    String userId;
    private Bitmap bitmap;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_image_profile_layout);
        ButterKnife.bind(this);
        DaggerChangeImageProfileActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .changeImageProfileActivityModule((new ChangeImageProfileActivityModule(this,this)))
                .build().inject(this);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("Change Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        userId=App.getInstance().getPrefManager().getUser().getId();
        changeImageProfilePresenter.getImageProfile(userId);
    }

    @OnClick(R.id.btnSelect)
    public void chooseImageProfile() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgFile = getBitmapFile(data);
            _imagePath.setText(imgFile.getPath().toString());
            bitmap=ShrinkBitmap(imgFile.getPath().toString(),1000,1000);
            _imageProfile.setImageBitmap(bitmap);
        }
    }
    Bitmap ShrinkBitmap(String file, int width, int height){
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        bmpFactoryOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        int heightRatio = (int)Math.ceil(bmpFactoryOptions.outHeight/(float)height);
        int widthRatio = (int)Math.ceil(bmpFactoryOptions.outWidth/(float)width);
        if (heightRatio > 1 || widthRatio > 1)
        {
            if (heightRatio > widthRatio)
            {
                bmpFactoryOptions.inSampleSize = heightRatio;
            } else {
                bmpFactoryOptions.inSampleSize = widthRatio;
            }
        }
        bmpFactoryOptions.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
        return bitmap;
    }
    public File getBitmapFile(Intent data) {
        isStoragePermissionGranted();
        Uri selectedImage = data.getData();
        Cursor cursor = getContentResolver().query(selectedImage, new String[] { android.provider.MediaStore.Images.Media.DATA }, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String selectedImagePath = cursor.getString(idx);
        cursor.close();
        return new File(selectedImagePath);
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            return true;
        }
    }

    @Override
    @OnClick(R.id.btnChange)
    public void upload() {
        changeImageProfilePresenter.submitProfileImage(imgFile,userId);
    }

    @Override
    public void uploadResult(boolean result) {
        if (result){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.MyAccountActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"upload failed",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showImage(String url) {
        imageProfile.setVisibility(View.GONE);
        _imageProfile.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(url)
                .into(_imageProfile);
    }
}
