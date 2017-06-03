package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerAcceptQuestionsActivityComponent;
import com.app.tanyahukum.data.component.DaggerMyAccountActivityComponent;
import com.app.tanyahukum.data.module.AcceptQuestionsActivityModule;
import com.app.tanyahukum.data.module.MyAccountActivityModule;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.AcceptQuestionsPresenter;
import com.app.tanyahukum.presenter.MyAccountPresenter;
import com.app.tanyahukum.util.Config;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by emerio on 4/19/17.
 */

public class MyAccountActivity extends AppCompatActivity implements MyAccountActivityInterface.View{
    @BindView(R.id.toolbar)
    Toolbar toolbar_;
    @BindView(R.id.edTextName)
    TextView _name;
    @BindView(R.id.edTextEmail)
    TextView _email;
    @BindView(R.id.edTextBornDate)
    TextView _borndate;
    @BindView(R.id.edTextGender)
    TextView _gender;
    @BindView(R.id.edTextPhone)
    TextView _phone;
    @BindView(R.id.edTextProvince)
    TextView _province;
    @BindView(R.id.edTextCity)
    TextView _city;
    @BindView(R.id.edTextAddress)
    TextView _address;
    @BindView(R.id.edTextLawFirm)
    TextView _lawFirm;
    @BindView(R.id.edTextLawFirmCity)
    TextView _lawFirmCity;;
    @BindView(R.id.edTextLawFirmAddress)
    TextView _lawFirmAddress;
    @BindView(R.id.layoutLawFirm)
    RelativeLayout _layoutLawFirm;
    @BindView(R.id.imageDefault)
    ImageView imageProfile;
    @BindView(R.id.edTextUserType)
    TextView _userType;
    @BindView(R.id.imageProfile)
    CircleImageView _imageProfile;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @Inject
    MyAccountPresenter myAccountPresenter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("My Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerMyAccountActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .myAccountActivityModule((new MyAccountActivityModule(this,this)))
                .build().inject(this);
        String userId=App.getInstance().getPrefManager().getUser().getId();
        Log.d("user id in accnt",userId);
        myAccountPresenter.getUserById(userId);
    }
    @Override
    public void showAccount(User user) {
        String usertype=user.getUsertype();
        if (usertype.equals("CLIENT")) {
            Log.d("name", user.getName());
            _name.setText(user.getName());
            _email.setText(user.getEmail());
            _borndate.setText(user.getBornDate());
            _gender.setText(user.getGender());
            _phone.setText(user.getPhone());
            _province.setText(user.getProvince());
            _city.setText(user.getCity());
            _address.setText(user.getAddress());
            _userType.setText(user.getUsertype());

        }else{
            _layoutLawFirm.setVisibility(View.VISIBLE);
            _userType.setText(user.getUsertype());
            _name.setText(user.getName());
            _email.setText(user.getEmail());
            _borndate.setText(user.getBornDate());
            _gender.setText(user.getGender());
            _phone.setText(user.getPhone());
            _province.setText(user.getProvince());
            _city.setText(user.getCity());
            _address.setText(user.getAddress());
            _lawFirm.setText(user.getLawFirm());
            _lawFirmCity.setText(user.getLawFirmCity());
            _lawFirmAddress.setText(user.getLawFirmAddress());
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
    @OnClick(R.id.btnEdit)
    public void editAccount() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.EditAccountActivity");
        startActivity(intent);

    }

    @Override
    @OnClick(R.id.imageDefault)
    public void changeImageProfile() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.ChangeImageProfileActivity");
        startActivity(intent);
    }

    @OnClick(R.id.imageProfile)
    public void changeImageProfileCircle() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClassName(this, "com.app.tanyahukum.view.ChangeImageProfileActivity");
        startActivity(intent);
    }


    @Override
    public void showImage(String url) {
        imageProfile.setVisibility(View.GONE);
        _imageProfile.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(url)
                .into(_imageProfile);
    }

    @Override
    @OnClick(R.id.btnCancel)
    public void cancel() {
        super.onBackPressed();
    }
}
