package com.app.tanyahukum.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tanyahukum.App;
import com.app.tanyahukum.R;
import com.app.tanyahukum.data.component.DaggerEditAccountActivityComponent;
import com.app.tanyahukum.data.component.DaggerMyAccountActivityComponent;
import com.app.tanyahukum.data.module.EditAccountActivityModule;
import com.app.tanyahukum.data.module.MyAccountActivityModule;
import com.app.tanyahukum.model.User;
import com.app.tanyahukum.presenter.EditAccountPresenter;
import com.app.tanyahukum.presenter.MyAccountPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by emerio on 4/20/17.
 */

public class EditAccountActivity extends AppCompatActivity implements EditAccountInterface.View {
    @BindView(R.id.toolbar)
    Toolbar toolbar_;
    @BindView(R.id.edTextName)
    EditText _name;
    @BindView(R.id.edTextEmail)
    EditText _email;
    @BindView(R.id.edTextBornDate)
    EditText _borndate;
    @BindView(R.id.edTextGender)
    EditText _gender;
    @BindView(R.id.edTextPhone)
    EditText _phone;
    @BindView(R.id.edTextProvince)
    EditText _province;
    @BindView(R.id.edTextCity)
    EditText _city;
    @BindView(R.id.edTextAddress)
    EditText _address;
    @BindView(R.id.edTextLawFirm)
    EditText _lawFirm;
    @BindView(R.id.edTextLawFirmCity)
    EditText _lawFirmCity;
    @BindView(R.id.edTextLawFirmAddress)
    EditText _lawFirmAddress;
    @BindView(R.id.layoutLawFirm)
    RelativeLayout _layoutLawFirm;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @Inject
    EditAccountPresenter editAccountPresenter;
    String userId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_account_layout);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar_);
        getSupportActionBar().setTitle("Edit Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DaggerEditAccountActivityComponent.builder()
                .netComponent(((App)getApplicationContext()).getNetComponent())
                .editAccountActivityModule((new EditAccountActivityModule(this,this)))
                .build().inject(this);
        userId=App.getInstance().getPrefManager().getUser().getId();
        editAccountPresenter.getUserById(userId);
    }
    @Override
    public void showAccount(User user) {
            _name.setText(user.getName());
            _email.setText(user.getEmail());
            _borndate.setText(user.getBornDate());
            _gender.setText(user.getGender());
            _phone.setText(user.getPhone());
            _province.setText(user.getProvince());
            _city.setText(user.getCity());
            _address.setText(user.getAddress());
            _layoutLawFirm.setVisibility(View.VISIBLE);
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

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgress.setVisibility(View.VISIBLE);
        } else {
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    @OnClick(R.id.btnSave)
    public void update() {
        String usertype=App.getInstance().getPrefManager().getUserType();
        User users=new User();
        users.setName(_name.getText().toString());
        users.setEmail(_email.getText().toString());
        users.setBornDate(_borndate.getText().toString());
        users.setGender(_gender.getText().toString());
        users.setPhone(_phone.getText().toString());
        users.setProvince(_province.getText().toString());
        users.setCity(_city.getText().toString());
        users.setAddress(_address.getText().toString());
        users.setLawFirm(_lawFirm.getText().toString());
        users.setLawFirmCity(_lawFirmCity.getText().toString());
        users.setLawFirmAddress(_lawFirmAddress.getText().toString());
        editAccountPresenter.saveUpdatedAccount(userId,users);
    }

    @Override
    public void toMyAccountPage(boolean result) {
        if (result){
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setClassName(this, "com.app.tanyahukum.view.MyAccountActivity");
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(),"Edit Account Failed",Toast.LENGTH_LONG).show();
        }

    }
}
